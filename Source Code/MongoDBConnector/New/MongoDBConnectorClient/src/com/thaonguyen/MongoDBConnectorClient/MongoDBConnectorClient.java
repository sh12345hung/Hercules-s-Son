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
	private static final String DEFAULT_SERVER_URI = "ws://ec2-54-250-240-202.ap-northeast-1.compute.amazonaws.com:7777";
	private String _userID;
	private boolean logedInFlag = false;
	
	public MongoDBConnectorClient() throws URISyntaxException {
		super(new URI(DEFAULT_SERVER_URI));
	}
	
	public MongoDBConnectorClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

	public MongoDBConnectorClient(URI serverURI) {
		super(serverURI);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String message) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(message);
			
			boolean isNewUser;
			String UserID;
			long count;
			List<String> contain;
			
			switch ((String)obj.get("TYPE")) {
			case "LOGIN":
				UserID = (String) obj.get("UserID");
				isNewUser = (boolean) obj.get("IsNewUser");
				logedInFlag = true;
				this.Login_Callback(UserID, isNewUser);
				break;
			case "GETNEWS":
				count = (long)obj.get("Count");
				contain = (List<String>)obj.get("Contain");
				this.GetNews_Callback(count, contain);
				break;
			case "GETCOMMENT":
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
	
	public void Login(String Token) throws InterruptedException {
		JSONObject json = new JSONObject();
		json.put("TYPE", "LOGIN");
		json.put("Token", Token);
		this.send(json.toJSONString());
	}
	
	public abstract void Login_Callback(String UserID, boolean IsNewUser);
	
	public void Logout() {
		JSONObject json = new JSONObject();
		json.put("TYPE", "LOGOUT");
		json.put("UserID", this._userID);
		this.send(json.toJSONString());
	}
	
	public void GetNews(String Topic) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "GETNEWS");
		json.put("Topic", Topic);
		
		this.send(json.toJSONString());
	}
	
	public void GetNews(String Topic, int Start, int Count) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "GETNEWS2");
		json.put("Topic", Topic);
		json.put("Start", Start);
		json.put("Count", Count);
		
		this.send(json.toJSONString());
	}
	
	public abstract void GetNews_Callback(long count, List<String> News);
	
	public void GetComment(String NewsID) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "GETCOMMENT");
		json.put("NewsID", NewsID);
		
		this.send(json.toJSONString());
	}
	
	public abstract void GetComment_Callback(List<String> Comment);
	
	public void AddComment(String NewsID, String Comment) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "ADDCOMMENT");
		json.put("UserID", this._userID);
		json.put("NewsID", NewsID);
		json.put("Comment", Comment);
		
		this.send(json.toJSONString());
	}
	
	public void Read(String NewsID) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "ADDCOMMENT");
		json.put("UserID", this._userID);
		json.put("NewsID", NewsID);
		
		this.send(json.toJSONString());
	}
	
	public static void log(String message) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date) + ": " + message);
   }

	public String get_userID() {
		return _userID;
	}

	public void set_userID(String _userID) {
		this._userID = _userID;
	}
	
	public void waitForLogin() throws InterruptedException {
		while (!logedInFlag) {
			Thread.sleep(50);
		}
	}
}
