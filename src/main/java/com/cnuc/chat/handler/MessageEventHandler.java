package com.cnuc.chat.handler;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers;
import org.springframework.stereotype.Component;

import com.cnuc.chat.entity.ChatGroupOnly;
import com.cnuc.chat.entity.ChatHistory;
import com.cnuc.chat.repository.ChatGroupOnlyRepository;
import com.cnuc.chat.repository.ChatHistoryRepository;
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
	private ChatGroupOnlyRepository chatGroupOnlyRepository;
	
	@Autowired
	private ChatHistoryRepository chatHistoryRepository;


	@Autowired
	public MessageEventHandler(SocketIOServer server) {
		this.server = server;
	}

	@OnConnect
	public void onConnect(SocketIOClient client) {
		String id = client.getHandshakeData().getSingleUrlParam("id");
		ChatGroupOnly chatGroupOnly = chatGroupOnlyRepository.findClientById(id);
		if(null!=chatGroupOnly) {
			chatGroupOnly.setState(true);
			chatGroupOnly.setSession(client.getSessionId().toString());
			chatGroupOnly.setLastLoginDate(new Date());
			chatGroupOnlyRepository.save(chatGroupOnly);
		}else {
			onDisconnect(client);
		}
	}

	@OnDisconnect
	public void onDisconnect(SocketIOClient client) {
		UUID sessionId = client.getSessionId();
		ChatGroupOnly item = new ChatGroupOnly();
		item.setSession(sessionId.toString());
		
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("session", GenericPropertyMatchers.startsWith());
		Example<ChatGroupOnly> example = Example.of(item,matcher);
		List<ChatGroupOnly> chatGroupOnlys = chatGroupOnlyRepository.findAll(example);
		
		if(null!=chatGroupOnlys&&chatGroupOnlys.size()>0) {
			ChatGroupOnly entity = chatGroupOnlys.get(0);
			entity.setState(false);
			chatGroupOnlyRepository.save(entity);
		}
	}

	@OnEvent(value = "messageevent")
	public void onEvent(SocketIOClient client, AckRequest request, Map<String, String> map) {
		//插入消息到消息记录表
		UUID sessionId = client.getSessionId();
		String cgoId = client.getHandshakeData().getSingleUrlParam("id");
		ChatGroupOnly item = new ChatGroupOnly();
		item.setSession(sessionId.toString());
		
		ChatGroupOnly chatGroupOnlys = chatGroupOnlyRepository.findClientById(cgoId);
		
		if(null!=chatGroupOnlys) {
			ChatHistory entity = new ChatHistory();
			entity.setCgoId(cgoId);
			entity.setCreateDate(new Date());
			entity.setSession(sessionId.toString());
			entity.setType(map.get("type"));
			entity.setContent(map.get("content"));
			entity.setUserName(map.get("userName"));
			chatHistoryRepository.save(entity);
		}
		Collection<SocketIOClient> allClients = server.getAllClients();
		for (SocketIOClient c : allClients) {
			c.sendEvent("messageevent", map);
		}
	}

}
