package com.thaonguyen.MongoDBConnectorClient;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public abstract class MongoDBConnectorClient extends WebSocketClient {
	private static final String DEFAULT_SERVER_URI = "ws://ec2-54-250-240-202.ap-northeast-1.compute.amazonaws.com:7777";
	private static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
	private static final String TIMEZONE = "VST"; /* Asia/Ho_Chi_Minh */
	private String _userID = "";
	private boolean logedInFlag = false;

	public MongoDBConnectorClient() throws URISyntaxException {
		super(new URI(DEFAULT_SERVER_URI));
		this.connect();
	}

	public MongoDBConnectorClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
		this.connect();
	}

	public MongoDBConnectorClient(URI serverURI) {
		super(serverURI);
		this.connect();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMessage(String message) {
		try {
			JSONParser parser = new JSONParser();
			JSONObject obj = (JSONObject) parser.parse(message);

			boolean isNewUser, TokenAvailable;
			String UserID, Topic, Keyword;
			long count;
			List<String> contain;

			switch ((String) obj.get("TYPE")) {
			case "LOGIN":
				TokenAvailable = (boolean) obj.get("AVAILABLE");
				UserID = (String) obj.get("UserID");
				isNewUser = (boolean) obj.get("IsNewUser");
				logedInFlag = true;
				this.Login_Callback(TokenAvailable, UserID, isNewUser);
				break;
			case "GETNEWS":
				Topic = (String) obj.get("TOPIC");
				count = (long) obj.get("Count");
				contain = (List<String>) obj.get("Contain");
				this.GetNews_Callback(Topic, count, contain);
				break;
			case "SEARCH":
				Keyword = (String) obj.get("Keyword");
				contain = (List<String>) obj.get("Contain");
				this.Search_Callback(Keyword, contain);
				break;
			case "GETCOMMENT":
				contain = (List<String>) obj.get("COMMENT");
				this.GetComment_Callback(contain);
				break;
			case "GETTOPIC":
				contain = (List<String>) obj.get("Contain");
				this.GetTopic_Callback(contain);
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void Login(String Token) throws InterruptedException {
		JSONObject json = new JSONObject();
		json.put("TYPE", "LOGIN");
		json.put("Token", Token);
		this.send(json.toJSONString());
	}

	public abstract void Login_Callback(boolean TokenAvailable, String UserID, boolean IsNewUser);

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

	public abstract void GetNews_Callback(String TOPIC, long count, List<String> News);

	public void Search(String Keyword, int Start, int Count) {
		JSONObject json = new JSONObject();
		json.put("TYPE", "SEARCH");
		json.put("KEYWORD", Keyword);
		json.put("Start", Start);
		json.put("Count", Count);
		this.send(json.toJSONString());
	}

	public abstract void Search_Callback(String Keyword, List<String> News);

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

	public void GetTopic() {
		JSONObject json = new JSONObject();
		json.put("TYPE", "GETTOPIC");

		this.send(json.toJSONString());
	}

	public abstract void GetTopic_Callback(List<String> Comment);

	private static String getCurrentDateTime() {
		DateFormat dateFormat = new SimpleDateFormat(DATEFORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone(TIMEZONE));
		Date date = new Date();
		return dateFormat.format(date);
	}

	public static void log(String message) {
		System.out.println(getCurrentDateTime() + ": " + message);
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
