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
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

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

import static com.mongodb.client.model.Filters.*;

public class MongoDBConnectorServer extends WebSocketServer {
	/*----- Constants -----*/
	private static final String DEFAULT_DATABASE_HOST_NAME = "localhost";
	private static final int DEFAULT_DATABASE_PORT = 27017;
	private static final String TIMEZONE = "VST"; /* Asia/Ho_Chi_Minh */
	private static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String DATABASE_NAME = "test";
	private static final String NEWS_COLLECTION_NAME = "News";
	private static final String USER_COLLECTION_NAME = "User";
	
	/*----- Variables -----*/
	private MongoClient     _client = null;
	private MongoDatabase   _db = null;
	private MongoCollection<Document> _news = null, _user = null;
	
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
		try {
			/* Parse message to JSON Object */
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(message);
			String UserID, Topic, NewsID, Comment, Token;
			long Count, Start;
			switch ((String)obj.get("TYPE")) { /* Get TYPE in JSON Object to identify type of message */
			case "LOGIN":
				Token = (String) obj.get("Token");
				if (Token != null) {
					conn.send(Login(Token));
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "LOGOUT":
				UserID = (String) obj.get("UserID");
				if (UserID != null) {
					Logout(conn, UserID);
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "GETNEWS":
				Topic = (String) obj.get("Topic");
				if (Topic != null) {
					conn.send(GetNews(Topic));
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "GETNEWS2":
				Topic = (String) obj.get("Topic");
				Count = (long) obj.get("Count");
				Start = (long) obj.get("Start");
				if (Topic != null) {
					conn.send(GetNews(Topic, Start, Count));
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "GETCOMMENT":
				NewsID = (String) obj.get("NewsID");
				if (NewsID != null) {
					conn.send(GetComments(NewsID));
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "ADDCOMMENT":
				UserID = (String) obj.get("UserID");
				NewsID = (String) obj.get("NewsID");
				Comment = (String) obj.get("Comment");
				if ((UserID != null) && (NewsID != null) && (Comment != null)) {
					AddComments(UserID, NewsID, Comment);
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "READ":
				UserID = (String) obj.get("UserID");
				NewsID = (String) obj.get("NewsID");
				if ((UserID != null) && (NewsID != null)) {
					Read(UserID, NewsID);
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "GETTOPIC":
				conn.send(GetTopic());
				break;
			default:
				CloseConnection(conn);
				break;	
			}
		}
		catch (Exception e) { /* Close connection if message has wrong format */
			CloseConnection(conn);
		}
	}
	
	@Override
	public final void onError(WebSocket conn, Exception ex) {
		log("error " + ex.getMessage());
	}
	
	private String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	private void log(String message) {
		System.out.println(getCurrentDateTime() + ": " + message);
   }
	
	private String Login(String Token) {
		/* Check access token */
		FacebookClient client = null;
		User me = null;
		try {
			client = new DefaultFacebookClient(Token, Version.LATEST);
			me = client.fetchObject("me", User.class, Parameter.with("fields", "id,name,email,birthday"));
		}
		catch (FacebookException e) { /* Token is not available or expired */
			Document json = new Document();
			json.put("TYPE", "LOGIN");
			json.put("AVAILABLE", false); /* Inform client that Token is not available */
			json.put("UserID", "");
			json.put("IsNewUser", false);
			return (json.toJson());
		}
		
		/* Get data */
		String UserID = me.getId();
		String UserName = me.getName();
		
		/* Find UserID in database */
		long count = _user.count(eq("UserID", UserID)); /* Check if UserID existed in database */
		boolean state = (count == 0);
		if (state) {
			/* Insert new UserID to database */
			Document user = new Document("UserID", UserID);
			user.append("Name", me.getName());
			user.append("Birthday", me.getBirthday());
			user.append("Email", me.getEmail());
			
			_user.insertOne(user);
		}
		
		Document json = new Document();
		json.put("TYPE", "LOGIN");
		json.put("AVAILABLE", true);
		json.put("UserID", UserID);
		json.put("IsNewUser", state);
		
		log(UserName + " loged in with " + (state?"new":"old") + " UserID " + UserID);
		
		return json.toJson();
	}
	
	private void Logout(WebSocket conn, String UserID) throws Exception {
		if (_user.count(eq("UserID", UserID)) == 0) {
			throw (new Exception("UserID is not available"));
		}
		else {
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
		
		log("Get news with Topic " + Topic);
		return json.toJson();
	}
	
	private String GetNews(String Topic, long Start, long Count) {
		/* Query to database */
		FindIterable<Document> doc = _news.find(eq("TOPIC", Topic)).sort(new Document("TIME", -1)).skip((int)Start).limit((int)Count).projection(Projections.exclude("COMMENT"));
		
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
		
		log("Get news with Topic " + Topic);
		return json.toJson();
	}
	
	private String GetComments(String NewsID) throws Exception {
		try {
			/* Query to database */
			FindIterable<Document> doc = _news.find(eq("_id", new ObjectId(NewsID))).projection(Projections.include("COMMENT.Name", "COMMENT.Comment"));
			Document json;
			
			/* Check if NewsID is exists */
			if (doc.first() != null) {
				json = doc.first();
				json.put("TYPE", "GETCOMMENT");
				
				log("Get comment with NewsID " + NewsID);
				
				return json.toJson();
			}
			else {
				throw (new Exception("News id is not available")); /* Throw an exception to close the connection if NewsID not exists */
			}
		}
		catch (Exception e) {
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
		
		/* Add comment */
		Document findQuery = new Document("_id", new ObjectId(NewsID));
		Document item = new Document("COMMENT", new Document("UserID", UserID).append("Name", name).append("Comment", Comment));
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
		for (String str:result) {
			list.add(str);
		}
		
		json.put("Contain", list);
		return json.toJson();
	}
	
	private void CloseConnection(WebSocket conn) {
		log("Wrong format from" + conn.getRemoteSocketAddress());
		conn.close(1002, "TYPE is not available.");
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String host = "localhost";
		int port = 7777;
		
		if (args.length > 0) {
			host = args[0];
			port = Integer.parseInt(args[1]);
		}
		
		MongoDBConnectorServer server = new MongoDBConnectorServer(new InetSocketAddress(host, port));
		server.start();
		
		server.log("Server is running on port " + server.getPort());
		
		BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
		while (true) {
			String in = sysin.readLine();
			if(in.equals("exit")) {
				server.stop();
				server.log("Server is stop");
				break;
			}
		}
	}
}
