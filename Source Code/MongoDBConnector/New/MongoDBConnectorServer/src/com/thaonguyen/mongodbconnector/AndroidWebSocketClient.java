package com.thaonguyen.mongodbconnector;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

public class AndroidWebSocketClient extends WebSocketClient {
	public AndroidWebSocketClient(URI serverURI) {
		super(serverURI);
	}
	
	public AndroidWebSocketClient(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}
	
	@Override
	public void onOpen(ServerHandshake handshakedata) {
	}

	@Override
	public void onMessage(String message) {
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
	}

	@Override
	public void onError(Exception ex) {
		ex.printStackTrace();
	}
}
