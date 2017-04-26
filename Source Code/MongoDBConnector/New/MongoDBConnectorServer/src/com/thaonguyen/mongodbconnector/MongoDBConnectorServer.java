package com.thaonguyen.mongodbconnector;

/* TimeZone
 * EST - -05:00
 * HST - -10:00
 * MST - -07:00
 * ACT - Australia/Darwin
 * AET - Australia/Sydney
 * AGT - America/Argentina/Buenos_Aires
 * ART - Africa/Cairo
 * AST - America/Anchorage
 * BET - America/Sao_Paulo
 * BST - Asia/Dhaka
 * CAT - Africa/Harare
 * CNT - America/St_Johns
 * CST - America/Chicago
 * CTT - Asia/Shanghai
 * EAT - Africa/Addis_Ababa
 * ECT - Europe/Paris
 * IET - America/Indiana/Indianapolis
 * IST - Asia/Kolkata
 * JST - Asia/Tokyo
 * MIT - Pacific/Apia
 * NET - Asia/Yerevan
 * NST - Pacific/Auckland
 * PLT - Asia/Karachi
 * PNT - America/Phoenix
 * PRT - America/Puerto_Rico
 * PST - America/Los_Angeles
 * SST - Pacific/Guadalcanal
 * VST - Asia/Ho_Chi_Minh
 */

/*----- Libraries -----*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.types.User;

import NewsCrawler.*;

import static com.mongodb.client.model.Filters.*;

public class MongoDBConnectorServer extends WebSocketServer {
	class MessageProcessThread extends Thread {
		WebSocket _conn = null;
		String _message = "";

		public MessageProcessThread(WebSocket conn, String message) {
			super();
			this._conn = conn;
			this._message = message;
		}

		@Override
		public void run() {
			try {
				/* Parse message to JSON Object */
				JSONParser parser = new JSONParser();
				JSONObject obj = (JSONObject) parser.parse(_message);
				String UserID, Topic, NewsID, Comment, Token, Keyword;
				long Count, Start;
				switch ((String) obj
						.get("TYPE")) { /*
										 * Get TYPE in JSON Object to identify
										 * type of message
										 */
				case "LOGIN":
					Token = (String) obj.get("Token");
					if (Token != null) {
						_conn.send(Login(Token));
					} else {
						CloseConnection(_conn);
					}
					break;
				case "LOGOUT":
					UserID = (String) obj.get("UserID");
					if (UserID != null) {
						Logout(_conn, UserID);
					} else {
						CloseConnection(_conn);
					}
					break;
				case "GETNEWS":
					Topic = (String) obj.get("Topic");
					if (Topic != null) {
						_conn.send(GetNews(Topic));
					} else {
						CloseConnection(_conn);
					}
					break;
				case "GETNEWS2":
					Topic = (String) obj.get("Topic");
					Count = (long) obj.get("Count");
					Start = (long) obj.get("Start");
					if (Topic != null) {
						_conn.send(GetNews(Topic, Start, Count));
					} else {
						CloseConnection(_conn);
					}
					break;
				case "SEARCH":
					Keyword = (String) obj.get("KEYWORD");
					Count = (long) obj.get("Count");
					Start = (long) obj.get("Start");
					if (Keyword != null) {
						_conn.send(Search(Keyword, Start, Count));
					} else {
						CloseConnection(_conn);
					}
					break;
				case "GETCOMMENT":
					NewsID = (String) obj.get("NewsID");
					if (NewsID != null) {
						_conn.send(GetComments(NewsID));
					} else {
						CloseConnection(_conn);
					}
					break;
				case "ADDCOMMENT":
					UserID = (String) obj.get("UserID");
					NewsID = (String) obj.get("NewsID");
					Comment = (String) obj.get("Comment");
					if ((UserID != null) && (NewsID != null) && (Comment != null)) {
						AddComments(UserID, NewsID, Comment);
					} else {
						CloseConnection(_conn);
					}
					break;
				case "READ":
					UserID = (String) obj.get("UserID");
					NewsID = (String) obj.get("NewsID");
					if ((UserID != null) && (NewsID != null)) {
						Read(UserID, NewsID);
					} else {
						CloseConnection(_conn);
					}
					break;
				case "GETTOPIC":
					_conn.send(GetTopic());
					break;
				default:
					CloseConnection(_conn);
					break;
				}
			} catch (Exception e) { /*
									 * Close connection if message has wrong
									 * format
									 */
				CloseConnection(_conn);
			}
		}
	}

	/*----- Constants -----*/
	private static final String DEFAULT_DATABASE_HOST_NAME = "localhost";
	private static final int DEFAULT_DATABASE_PORT = 27017;
	private static final String TIMEZONE = "VST"; /* Asia/Ho_Chi_Minh */
	private static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String DATABASE_NAME = "test";
	private static final String NEWS_COLLECTION_NAME = "News";
	private static final String USER_COLLECTION_NAME = "User";
	private static final int CRAWLER_PERIOD_TIME = 21600000; /* 6 hours */
	private static final int CRAWLER_DELAY_TIME = 60000; /* 1 minute */

	/*----- Variables -----*/
	private MongoClient _client = null;
	private MongoDatabase _db = null;
	private MongoCollection<Document> _news = null, _user = null;
	private static List<News> newsList = new ArrayList<News>();

	/*----- Constructors -----*/
	public MongoDBConnectorServer(int port) {
		super(new InetSocketAddress(port)); /* Listening on port */

		/* Initialize connection to database */
		_client = new MongoClient(DEFAULT_DATABASE_HOST_NAME, DEFAULT_DATABASE_PORT);
		_db = _client.getDatabase(DATABASE_NAME);
		_news = _db.getCollection(NEWS_COLLECTION_NAME);
		_user = _db.getCollection(USER_COLLECTION_NAME);
	}

	public MongoDBConnectorServer(InetSocketAddress address) {
		super(address); /* Listening */

		/* Initialize connection to database */
		_client = new MongoClient(DEFAULT_DATABASE_HOST_NAME, DEFAULT_DATABASE_PORT);
		_db = _client.getDatabase(DATABASE_NAME);
		_news = _db.getCollection(NEWS_COLLECTION_NAME);
		_user = _db.getCollection(USER_COLLECTION_NAME);
	}

	/* Methods */
	@Override
	public final void onOpen(WebSocket conn, ClientHandshake handshake) {
		log("New connection from " + conn.getRemoteSocketAddress());
	}

	@Override
	public final void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log("Connection from " + conn.getRemoteSocketAddress() + " is closed");
	}

	@Override
	public final void onMessage(WebSocket conn, String message) {
		(new MessageProcessThread(conn, message)).start();
	}

	@Override
	public final void onError(WebSocket conn, Exception ex) {
		log("error " + ex.getMessage());
	}

	public static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void log(String message) {
		System.out.println(getCurrentDateTime() + ": " + message);
	}

	private String Login(String Token) {
		/* Check access token */
		FacebookClient client = null;
		User me = null;

		try {
			client = new DefaultFacebookClient(Token, Version.LATEST);
			me = client.fetchObject("me", User.class, Parameter.with("fields", "id,name,email,birthday"));
		} catch (FacebookException e) { /* Token is not available or expired */
			Document json = new Document();
			json.put("TYPE", "LOGIN");
			json.put("AVAILABLE",
					false); /* Inform client that Token is not available */
			json.put("UserID", "");
			json.put("IsNewUser", false);
			return (json.toJson());
		}

		/* Get data */
		String UserID = me.getId();
		String UserName = me.getName();

		/* Find UserID in database */
		long count = _user.count(
				eq("UserID", UserID)); /* Check if UserID existed in database */
		boolean state = (count == 0);
		if (state) {
			/* Insert new UserID to database */
			Document user = new Document("UserID", UserID);
			user.append("Name", me.getName());
			user.append("Birthday", me.getBirthday());
			user.append("Email", me.getEmail());
			user.append("Avatar", "https://graph.facebook.com/" + UserID + "/picture?type=large");

			_user.insertOne(user);
		}

		Document json = new Document();
		json.put("TYPE", "LOGIN");
		json.put("AVAILABLE", true);
		json.put("UserID", UserID);
		json.put("IsNewUser", state);

		log(UserName + " loged in with " + (state ? "new" : "old") + " UserID " + UserID);

		return json.toJson();
	}

	private void Logout(WebSocket conn, String UserID) throws Exception {
		if (_user.count(eq("UserID", UserID)) == 0) {
			throw (new Exception("UserID is not available"));
		} else {
			conn.close(1002);
		}

		/* Add history */
		log("Loged out with UserID " + UserID);
	}

	private String GetNews(String Topic) {
		/* Query to database */
		FindIterable<Document> doc = _news.find(eq("TOPIC", Topic)).sort(new Document("TIME", -1));

		/* Get information */
		List<String> contain = new ArrayList<String>();
		int count = 0;
		Iterator<Document> i = doc.iterator();
		while (i.hasNext()) {
			count++;
			contain.add(i.next().toJson());
		}

		/* Generate JSON string */
		Document json = new Document();
		json.put("TYPE", "GETNEWS");
		json.put("Count", count);
		json.put("Contain", contain);
		json.put("TOPIC", Topic);

		log("Get news with Topic " + Topic);
		return json.toJson();
	}

	private String GetNews(String Topic, long Start, long Count) {
		/* Query to database */
		FindIterable<Document> doc = _news.find(eq("TOPIC", Topic)).sort(new Document("TIME", -1)).skip((int) Start)
				.limit((int) Count).projection(Projections.exclude("COMMENT", "FULLTITLE"));

		/* Get information */
		List<String> contain = new ArrayList<String>();
		int count = 0;
		Iterator<Document> i = doc.iterator();
		while (i.hasNext()) {
			count++;
			contain.add(i.next().toJson());
		}

		/* Generate JSON string */
		Document json = new Document();
		json.put("TYPE", "GETNEWS");
		json.put("Count", count);
		json.put("Contain", contain);
		json.put("TOPIC", Topic);

		log("Get news with Topic " + Topic);
		return json.toJson();
	}

	private String Search(String Keyword, long Start, long Count) {
		/* Query to the database */
		String pattern = ".*" + Keyword + ".*";
		FindIterable<Document> docs = _news.find(regex("FULLTITLE", pattern, "i")).skip((int) Start).limit((int) Count)
				.projection(Projections.exclude("COMMENT", "FULLTITLE"));

		/* Get information */
		List<String> contain = new ArrayList<String>();
		for (Document doc : docs) {
			contain.add(doc.toJson());
		}

		/* Generate JSON string */
		Document json = new Document();
		json.put("TYPE", "SEARCH");
		json.put("Keyword", Keyword);
		json.put("Contain", contain);
		log("Search news with keyword " + Keyword);
		return json.toJson();
	}

	private String GetComments(String NewsID) throws Exception {
		try {
			/* Query to database */
			FindIterable<Document> doc = _news.find(eq("_id", new ObjectId(NewsID)))
					.projection(Projections.include("COMMENT.Name", "COMMENT.Comment", "COMMENT.Avatar"));
			Document json = new Document();
			List<Document> tmp;
			List<String> list = new ArrayList<String>();

			/* Check if NewsID is exists */
			if (doc.first() != null) {
				try {
					tmp = (List<Document>) doc.first().get("COMMENT");
					for (Document d : tmp) {
						list.add(d.toJson());
					}
				} catch (Exception e) {

				}

				json.put("TYPE", "GETCOMMENT");
				json.put("COMMENT", list);

				log("Get comment with NewsID " + NewsID);

				return json.toJson();
			} else {
				throw (new Exception(
						"News id is not available")); /*
														 * Throw an exception to
														 * close the connection
														 * if NewsID not exists
														 */
			}
		} catch (Exception e) {
			throw (new Exception(e.getMessage()));
		}
	}

	private void AddComments(String UserID, String NewsID, String Comment) throws Exception {
		/* Find user name */
		Document user = _user.find(eq("UserID", UserID)).first();
		if (user == null) {
			throw (new Exception("User is NOT EXIST"));
		}
		String name = user.getString("Name");
		String avatar = "https://graph.facebook.com/" + UserID + "/picture?type=large";

		/* Add comment */
		Document findQuery = new Document("_id", new ObjectId(NewsID));
		Document item = new Document("COMMENT", new Document("UserID", UserID).append("Name", name)
				.append("Comment", Comment).append("Avatar", avatar));
		Document updateQuery = new Document("$push", item);
		_news.updateOne(findQuery, updateQuery);

		/* Increase count */
		item = new Document("COMMENTCOUNT", 1);
		updateQuery = new Document("$inc", item);
		_news.updateOne(findQuery, updateQuery);
		
		log("Add comment with UserID " + UserID + " NewsID " + NewsID + " Comment " + Comment);
	}

	private void Read(String UserID, String NewsID) {
		/* Not implement yet */
		log("Read with UserID " + UserID + " NewsID " + NewsID);
	}

	private String GetTopic() {
		Document json = new Document("TYPE", "GETTOPIC");

		/* Querry to database */
		DistinctIterable<String> result = _news.distinct("TOPIC", String.class);
		List<String> list = new ArrayList<String>();
		for (String str : result) {
			list.add(str);
		}

		json.put("Contain", list);
		return json.toJson();
	}

	private void CloseConnection(WebSocket conn) {
		log("Wrong format from" + conn.getRemoteSocketAddress());
		conn.close(1002, "TYPE is not available.");
	}

	public void notifyToAll(String text) {
		/* Generate notification json string */
		Document doc = new Document("TYPE", "NOTIFY");
		doc.put("Message", text);
		String json = doc.toJson();

		/* Send notification */
		Collection<WebSocket> con = connections();
		synchronized (con) {
			for (WebSocket c : con) {
				c.send(json);
			}
		}
	}

	private static void crawl_news() {
		new Thread() {
			@Override
			public void run() {
				for (News news : newsList) {
					news.Execution();
				}
			}
		}.start();
	}

	/********************* MAIN *********************/
	public static void main(String[] args) throws IOException, InterruptedException {
		String host = "localhost"; /* Default input host */
		int port = 7777; /* Default input port */

		/* Add news */
		newsList.add(new Baomoi());
		newsList.add(new VNExpress());
		newsList.add(new ZingNews());

		if (args.length > 0) { /* Get host and port from arguments */
			host = args[0];
			port = Integer.parseInt(args[1]);
		}

		/* Start server instant */
		MongoDBConnectorServer server = new MongoDBConnectorServer(new InetSocketAddress(host, port));
		server.start();
		MongoDBConnectorServer.log("Server is running on port " + server.getPort());

		/* Set timer for crawler */
		Timer crawlerTimer = new Timer();
		// crawlerTimer.schedule(new TimerTask() {
		// @Override
		// public void run() {
		// crawl_news();
		// }
		// }, CRAWLER_DELAY_TIME, CRAWLER_PERIOD_TIME);
		try {
			System.out.println(server.GetComments("58fdd9995a7080719852a8a8"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Commands */
		BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			String in = sysin.readLine();

			if (in.equals("notify")) {
				System.out.print("Enter the message: ");
				String message = sysin.readLine();
				server.notifyToAll(message);
			}

			if (in.equals("crawl")) {
				crawl_news();
			}

			if (in.equals("status")) {
				MongoDBConnectorServer.log("Number of connection: " + server.connections().size());
			}

			if (in.equals("exit")) {
				server.stop();
				MongoDBConnectorServer.log("Server is stop");
				break;
			}
		}

		crawlerTimer.cancel();
		Thread.sleep(1000);
		System.exit(0);
	}
}
