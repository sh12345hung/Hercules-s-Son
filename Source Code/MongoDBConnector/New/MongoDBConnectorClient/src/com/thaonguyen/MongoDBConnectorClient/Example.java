package com.thaonguyen.MongoDBConnectorClient;

import org.java_websocket.handshake.ServerHandshake;

public class Example {
	public static void main(String[] args) {
		try {
			MongoDBConnectorClient client = new MongoDBConnectorClient("ThaoNguyen") {

				@Override
				public void onClose(int arg0, String arg1, boolean arg2) {
					
				}

				@Override
				public void onError(Exception arg0) {
					
				}

				@Override
				public void onOpen(ServerHandshake arg0) {
					
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
				public void GetNews_Callback(int count, String News) {
					MongoDBConnectorClient.log("Count: " + count);
					MongoDBConnectorClient.log("News: " + News);
				}

				@Override
				public void GetComment_Callback(int count, String Comment) {
					
				}
			};
			client.Login();
			MongoDBConnectorClient.log("Login OK");
			
//			client.GetNews("THEGIOI");
//			MongoDBConnectorClient.log("Get news OK");
			
//			client.GetComment("abc123");
//			MongoDBConnectorClient.log("Get comment OK");
			
//			client.AddComment("abc123", "LOL");
//			MongoDBConnectorClient.log("Add comment OK");
			
//			client.Read("abc123");
//			MongoDBConnectorClient.log("Read OK");
			
			client.Logout();
			MongoDBConnectorClient.log("Logout OK");
		}
		catch (Exception e) {
			MongoDBConnectorClient.log(e.getMessage());
		}
	}
}
