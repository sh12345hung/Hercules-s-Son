package com.thaonguyen.MongoDBConnectorClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

public class Example {
	static boolean connected = false, logedin = false;
	static String userID = "";
	public static void main(String[] args) {
		//String defaultServer = "ws://ec2-54-250-240-202.ap-northeast-1.compute.amazonaws.com";
		String defaultServer = "ws://127.0.0.1";
		int defaultPort = 7777;
		
		if (args.length > 0) {
			defaultServer = args[0];
			defaultPort = Integer.parseInt(args[1]);
		}
		
		try {
			MongoDBConnectorClient client = new MongoDBConnectorClient(new URI(defaultServer + ":" + defaultPort)) {

				@Override
				public void onClose(int arg0, String arg1, boolean arg2) {
					MongoDBConnectorClient.log("Connection closed");
				}

				@Override
				public void onError(Exception arg0) {
					
				}

				@Override
				public void onOpen(ServerHandshake arg0) {
					MongoDBConnectorClient.log("Connection opened");
					connected = true;
				}

				@Override
				public void GetNews_Callback(long count, List<String> News) {
					MongoDBConnectorClient.log("Count: " + count);
					for (int i = 0; i < News.size(); i++) {
						MongoDBConnectorClient.log("News: " + News.get(i));
					}
				}

				@Override
				public void GetComment_Callback(List<String> Comment) {
					MongoDBConnectorClient.log("Comment: " + Comment);
				}

				@Override
				public void Login_Callback(String UserID, boolean IsNewUser) {
					if (IsNewUser) {
						MongoDBConnectorClient.log("Loged in: New");
					}
					else {
						MongoDBConnectorClient.log("Loged in: Old");
					}
					userID = UserID;
				}

				@Override
				public void GetTopic_Callback(List<String> Topic) {
					for (String str : Topic) {
						MongoDBConnectorClient.log(str);
					}
				}
			};
			
			client.connect();
			MongoDBConnectorClient.log("Conencted OK");
			
			while (!connected) {
				Thread.sleep(50);
			}
			
			client.Login("EAACEdEose0cBALcoZATO0kBZAS4BBUhYO1rxLTU0tE09HMbPdZCZBYqqSJWtsVslDhlERuaBviAIhn20feNvHRgvIWxgEjYSaTyVLStjkonfZArlBkdWZBjfB3WTHkY0JZBeZChByY9oA3oT2MYeCdKNA1eiEHLriCRkK35VBEvjEZCOwf4PSoVZCNJQ5whmpjlGUZD");
			MongoDBConnectorClient.log("Login OK");
			
			client.waitForLogin();
			client.set_userID(userID);
			
			client.GetTopic();
			
//			client.GetNews("Cười", 0, 100);
//			MongoDBConnectorClient.log("Get news OK");
			
//			client.GetComment("58ea7285543715147ae13773");
//			MongoDBConnectorClient.log("Get comment OK");
			
//			client.AddComment("58ea7285543715147ae13773", "Bố anh hút rất nhiều thuốc, mẹ anh chửi ổng quá trời!");
//			MongoDBConnectorClient.log("Add comment OK");
//			
//			client.GetComment("58ea7285543715147ae13773");
//			MongoDBConnectorClient.log("Get comment OK");
			
			BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
			while (true) {
				String in = sysin.readLine();
				if(in.equals("exit")) {
					break;
				}
			}
			
			client.Logout();
			MongoDBConnectorClient.log("Logout OK");
		}
		catch (Exception e) {
			MongoDBConnectorClient.log(e.getMessage());
		}
	}
}
