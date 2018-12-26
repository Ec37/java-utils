package com.cnuc.chat.handler;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.corundumstudio.socketio.annotation.OnDisconnect;
import com.corundumstudio.socketio.annotation.OnEvent;

@Component
public class MessageEventHandler {

	private final SocketIOServer server;


	@Autowired
	public MessageEventHandler(SocketIOServer server) {
		this.server = server;
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		System.out.println("test connect");// TODO:记录用户登陆状态

	}

	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		System.out.println("test disconnect");// TODO:记录用户短线状态

	}

	@OnEvent(value = "messageevent")
	public void onEvent(SocketIOClient client, AckRequest request, Map<String, String> map) {
		System.out.println("message ");
		String targetClientId = map.get("clientId");
		client.sendEvent("messageevent", map);
	}

}
