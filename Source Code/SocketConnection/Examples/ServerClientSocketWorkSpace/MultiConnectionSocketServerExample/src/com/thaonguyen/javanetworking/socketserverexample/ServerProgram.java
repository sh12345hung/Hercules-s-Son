package com.thaonguyen.javanetworking.socketserverexample;

import java.net.*;
import java.io.*;

public class ServerProgram {
	public static void main(String[] args) throws IOException {
		ServerSocket listener = null;
		
		System.out.println("Server is waiting to accept user...");
		int clientNumber = 0;
		
		try {
			listener = new ServerSocket(7777);
		}
		catch (IOException e) {
			System.out.println(e);
			System.exit(1);
		}
		
		try {
			while (true) {
				Socket socketOfServer = listener.accept();
				new ServerThread(socketOfServer, clientNumber++).start();
			}
		}
		finally {
			listener.close();
		}
	}
}