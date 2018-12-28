package com.cnuc.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnuc.chat.entity.ChatGroupOnly;
import com.cnuc.chat.entity.MessageLog;

public interface MessageLogRepository extends JpaRepository<MessageLog, String> {
	
}
