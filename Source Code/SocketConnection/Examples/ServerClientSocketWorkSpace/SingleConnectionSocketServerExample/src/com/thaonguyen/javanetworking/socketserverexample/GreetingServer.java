package com.thaonguyen.javanetworking.socketserverexample;

import java.io.*;
import java.net.*;

public class GreetingServer extends Thread {
	private ServerSocket serverSocket;
	
	public GreetingServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		serverSocket.setSoTimeout(10000);
	}
	
	@Override
	public void run() {
		while (true) {
			try {
				System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
				Socket server = serverSocket.accept();
				
				System.out.println("Just connected to " + server.getRemoteSocketAddress());
				DataInputStream in = new DataInputStream(server.getInputStream());
				
				System.out.println(in.readUTF());
				DataOutputStream out = new DataOutputStream(server.getOutputStream());
				out.writeUTF("Thank you for connecting to " + server.getLocalSocketAddress() + "\nGoodbye!!");
				
				server.close();
			}
			catch (SocketTimeoutException e) {
				System.out.println("Socket timed out!");
			}
			catch (IOException e) {
				e.printStackTrace();
	            break;
			}
		}
	}
}