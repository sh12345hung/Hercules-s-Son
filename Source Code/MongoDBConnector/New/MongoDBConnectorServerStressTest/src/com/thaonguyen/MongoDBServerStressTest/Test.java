package com.thaonguyen.MongoDBServerStressTest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.java_websocket.handshake.ServerHandshake;

import com.thaonguyen.MongoDBConnectorClient.MongoDBConnectorClient;

public class Test {
	private static final int NUM_OF_USER = 1000;
	private static int count = 0;

	public static void main(String[] args) throws InterruptedException {
		List<MongoDBConnectorClient> list = new ArrayList<MongoDBConnectorClient> ();
		for (int i = 0; i < NUM_OF_USER; i++) {
			try {
				MongoDBConnectorClient client = new MongoDBConnectorClient(new URI("ws://ec2-54-250-240-202.ap-northeast-1.compute.amazonaws.com:7778")) {

					@Override
					public void GetComment_Callback(List<String> arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void GetNews_Callback(long arg0, List<String> arg1) {
						// TODO Auto-generated method stub
						System.out.println(++count + " : " + arg1.get(0));
					}

					@Override
					public void onClose(int arg0, String arg1, boolean arg2) {
						// TODO Auto-generated method stub
						System.out.println("Close");
					}

					@Override
					public void onError(Exception arg0) {
						// TODO Auto-generated method stub
						System.out.println("Error");
					}

					@Override
					public void onOpen(ServerHandshake arg0) {
						// TODO Auto-generated method stub
						System.out.println("Open");
					}

					@Override
					public void GetTopic_Callback(List<String> arg0) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void Login_Callback(boolean arg0, String arg1, boolean arg2) {
						// TODO Auto-generated method stub
						
					}
				};
				
				client.connect();
				list.add(client);
				
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Thread.sleep(10000);
		
		for (int i = 0; i < NUM_OF_USER; i++) {
			System.out.println(i);
			list.get(i).GetNews("Thế giới");
			Thread.sleep(500);
		}
	}

}
