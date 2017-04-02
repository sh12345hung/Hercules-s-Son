package com.thaonguyen.MongoDBConnectorClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public abstract class MongoDBConnectorClient extends WebSocketClient {
	//private static final String DEFAULT_SERVER_URI = "ws://192.168.14.128:7777";
	//private static final String DEFAULT_SERVER_URI = "ws://127.0.0.1:7777";
	private static final String DEFAULT_SERVER_URI = "ws://54.250.240.202:7777";
	private String _userID;
	
	public MongoDBConnectorClient(String UserID) throws URISyntaxException {
		super(new URI(DEFAULT_SERVER_URI));
		this._userID = UserID;
	}
	
	public MongoDBConnectorClient(String UserID, URI serverUri, Draft draft) {
		super(serverUri, draft);
		this._userID = UserID;
	}

	public MongoDBConnectorClient(String UserID, URI serverURI) {
		super(serverURI);
		this._userID = UserID;
	}

	@Override
	public void onMessage(String message) {
		log(message);
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(message);
			
			boolean isNewUser;
			long count;
			List<String> contain;
			
			switch ((String)obj.get("TYPE")) {
			case "LOGIN":
				isNewUser = (boolean) obj.get("IsNewUser");
				this.Login_Callback(isNewUser);
				break;
			case "GETNEWS":
				count = (long)obj.get("Count");
				contain = (List<String>)obj.get("Contain");
				this.GetNews_Callback(count, contain);
				break;
			case "GETCOMMENT":
				log("called");
				contain = (List<String>) obj.get("COMMENT");
				this.GetComment_Callback(contain);
				break;
			default:
				break;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void Login() throws InterruptedException {
		this.connect();
		Thread.sleep(1000);
		
		String json = "{\"TYPE\" : \"LOGIN\",\"UserID\" : \"" + this._userID + "\"}";
		this.send(json);
	}
	
	public abstract void Login_Callback(boolean IsNewUser);
	
	public void Logout() {
		String json = "{\"TYPE\" : \"LOGOUT\",\"UserID\" : \"" + this._userID + "\"}";
		this.send(json);
		this.close();
	}
	
	public void GetNews(String Topic) {
		String json = "{\"TYPE\" : \"GETNEWS\",\"Topic\" : \"" + Topic + "\"}";
		this.send(json);
	}
	
	public abstract void GetNews_Callback(long count, List<String> News);
	
	public void GetComment(String NewsID) {
		String json = "{\"TYPE\" : \"GETCOMMENT\",\"NewsID\" : \"" + NewsID + "\"}";
		this.send(json);
	}
	
	public abstract void GetComment_Callback(List<String> Comment);
	
	public void AddComment(String NewsID, String Comment) {
		String json = "{\"TYPE\" : \"ADDCOMMENT\",\"UserID\" : \"" + this._userID + "\",\"NewsID\" : \"" + NewsID + "\",\"Comment\" : \"" + Comment + "\"}";
		this.send(json);
	}
	
	public void Read(String NewsID) {
		String json = "{\"TYPE\" : \"READ\"\"UserID\" : \"" + this._userID + "\",\"NewsID\" : \"" + NewsID + "\"}";
		this.send(json);
	}
	
	public static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
   }
}
