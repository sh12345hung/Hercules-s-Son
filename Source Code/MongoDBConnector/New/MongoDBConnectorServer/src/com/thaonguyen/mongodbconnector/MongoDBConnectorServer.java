package com.thaonguyen.mongodbconnector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnectorServer extends WebSocketServer {
	private static final String NEWS_COLLECTION_NAME = "News";
	private static final String USER_COLLECTION_NAME = "User";
	private static final int NUM_OF_CHAR_IN_SHORTDESC = 30;
	
	private MongoClientURI  _uri = null;
	private MongoClient     _client = null;
	private MongoDatabase   _db = null;
	private MongoCollection<Document> _news = null, _user = null;
	
	/* Constructors */
	public MongoDBConnectorServer(int port) {
		super(new InetSocketAddress(port));
	}

	public MongoDBConnectorServer(InetSocketAddress address) {
		super(address);	
	}

	public static void main(String[] args) throws IOException, InterruptedException {
		String host = "localhost";
		int port = 9999;
		
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

	/* Methods */
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		log("New connection from " + conn.getRemoteSocketAddress());
	}
	
	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		log("Connection from " + conn.getRemoteSocketAddress() + " is closed");
	}
	
	@Override
	public void onMessage(WebSocket conn, String message) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(message);
			String UserID, Topic, NewsID, Comment;
			switch ((String)obj.get("TYPE")) {
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
					GetComments(NewsID);
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
		catch (Exception e) {
			//e.printStackTrace();
			CloseConnection(conn);
		}
	}
	
	@Override
	public void onError(WebSocket conn, Exception ex) {
		ex.printStackTrace();
		log("error " + ex.getMessage());
	}
	
	private void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
   }
	
	private String Login(String UserID) {
		String json = "{\"TYPE\" : \"LOGIN\",\"IsNewUser\" : false}";
		
		/* Find UserID in database */
		
		log("Loged in with UserID " + UserID);
		return json;
	}
	
	private void Logout(String UserID) {
		log("Loged out with UserID " + UserID);
	}
	
	private String GetNews(String Topic) {
		String json = "";
		
		log("Get news with Topic " + Topic);
		return json;
	}
	
	private void GetComments(String NewsID) {
		log("Get comment with NewsID " + NewsID);
	}
	
	private void AddComments(String UserID, String NewsID, String Comment) {
		log("Add comment with UserID " + UserID + " NewsID " + NewsID + " Comment " + Comment);
	}
	
	private void Read(String UserID, String NewsID) {
		log("Read with UserID " + UserID + " NewsID " + NewsID);
	}
	
	private void CloseConnection(WebSocket conn) {
		conn.close(1, "TYPE is not available.");
		log("Wrong TYPE from" + conn.getRemoteSocketAddress());
	}
}
