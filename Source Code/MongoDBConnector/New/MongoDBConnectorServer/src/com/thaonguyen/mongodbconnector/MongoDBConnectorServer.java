package com.thaonguyen.mongodbconnector;

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

import org.bson.Document;
import org.bson.types.ObjectId;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Projections;

import static com.mongodb.client.model.Filters.*;

public class MongoDBConnectorServer extends WebSocketServer {
	/*----- Constants -----*/
	private static final String DEFAULT_DATABASE_HOST_NAME = "localhost";
	private static final int DEFAULT_DATABASE_PORT = 27017;
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
			String UserID, Topic, NewsID, Comment;
			switch ((String)obj.get("TYPE")) { /* Get TYPE in JSON Object to identify type of message */
			case "LOGIN":
				UserID = (String) obj.get("UserID");
				if (UserID != null) {
					conn.send(Login(UserID));
				}
				else {
					CloseConnection(conn);
				}
				break;
			case "LOGOUT":
				UserID = (String) obj.get("UserID");
				if (UserID != null) {
					Logout(UserID);
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
		//ex.printStackTrace();
		log("error " + ex.getMessage());
	}
	
	private String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
	
	private void log(String message) {
		System.out.println(getCurrentDateTime() + ": " + message);
   }
	
	private String Login(String UserID) {
		/* Find UserID in database */
		long count = _user.count(eq("UserID", UserID)); /* Check if UserID existed in database */
		boolean state = (count == 0);
		if (state) {
			/* Insert new UserID to database */
			_user.insertOne(new Document("UserID", UserID));
		}
		
		/* Add history */
//		Document DocToInsert = new Document("Time", getCurrentDateTime());
//		Document UpdateQuery = new Document("UserID", UserID);
//		Document UpdateCommand = new Document("$push", new Document("History.Login", DocToInsert));
//		_user.updateOne(UpdateQuery, UpdateCommand);
		/* Not implement yet */
		
		//String json = "{\"TYPE\" : \"LOGIN\",\"IsNewUser\" : "+ state +"}"; /* Generate JSON string to send to client */
		Document json = new Document("TYPE", "LOGIN").append("IsNewUser", state);
		log("Loged in with " + state + " UserID " + UserID);
		return json.toJson();
	}
	
	private void Logout(String UserID) throws Exception {
		if (_user.count(eq("UserID", UserID)) == 0) {
			throw (new Exception("UserID is not available"));
		}
		
		/* Add history */
		log("Loged out with UserID " + UserID);
	}
	
	private String GetNews(String Topic) {
		/* Query to database */
		FindIterable<Document> doc = _news.find(eq("TOPIC", Topic));
		
		/* Get information */
		List<String> contain = new ArrayList<String>();
		int count = 0;
		Iterator<Document> i = doc.iterator();
		while (i.hasNext()) {
			count++;
			contain.add(((Document)i.next()).toJson());
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
			FindIterable<Document> doc = _news.find(eq("_id", new ObjectId(NewsID))).projection(Projections.include("COMMENT"));
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
	
	private void AddComments(String UserID, String NewsID, String Comment) {
		Document findQuery = new Document("_id", new ObjectId(NewsID));
		Document item = new Document("COMMENT", new Document("UserID", UserID).append("Comment", Comment));
		Document updateQuery = new Document("$push", item);
		_news.updateOne(findQuery, updateQuery);
		
		log("Add comment with UserID " + UserID + " NewsID " + NewsID + " Comment " + Comment);
	}
	
	private void Read(String UserID, String NewsID) {
		/* Not implement yet */
		log("Read with UserID " + UserID + " NewsID " + NewsID);
	}
	
	private void CloseConnection(WebSocket conn) {
		conn.close(1, "TYPE is not available.");
		log("Wrong TYPE from" + conn.getRemoteSocketAddress());
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
