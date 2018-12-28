package com.cnuc.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnuc.chat.entity.ChatGroupOnly;
import com.cnuc.chat.entity.ChatHistory;

public interface ChatHistoryRepository extends JpaRepository<ChatHistory, String> {
	
}
