package com.thaonguyen.javanetworking.socketserverexample;

import java.io.IOException;

public class RunServer {

	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);
		try {
			Thread t = new GreetingServer(port);
			t.start();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
