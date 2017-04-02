package com.thaonguyen.MongoDBConnectorClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

public class Example {
	public static void main(String[] args) {
		try {
			MongoDBConnectorClient client = new MongoDBConnectorClient("ThaiNguyen") {

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
				}

				@Override
				public void Login_Callback(boolean IsNewUser) {
					if (IsNewUser) {
						MongoDBConnectorClient.log("Loged in: New");
					}
					else {
						MongoDBConnectorClient.log("Loged in: Old");
					}
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
			};
			
			client.Login();
			MongoDBConnectorClient.log("Login OK");
			
//			client.GetNews("THEGIOI");
//			MongoDBConnectorClient.log("Get news OK");
			
//			client.GetComment("58e081e8ac0e564b41cc51b7");
//			MongoDBConnectorClient.log("Get comment OK");
			
			client.AddComment("58e081e8ac0e564b41cc51b7", "LOL");
			MongoDBConnectorClient.log("Add comment OK");
			
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
