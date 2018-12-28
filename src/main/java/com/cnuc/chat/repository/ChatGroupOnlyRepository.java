package com.cnuc.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cnuc.chat.entity.ChatGroupOnly;

public interface ChatGroupOnlyRepository extends JpaRepository<ChatGroupOnly, String> {
	ChatGroupOnly findClientById(String id);
}
