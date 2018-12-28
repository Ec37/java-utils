package com.cnuc.chat.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GeneratorType;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name="t_chat_history")
public class ChatHistory {
	
	@Id
    @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid2")
	private String id;
	@Column(name="cgo_id",nullable=false,length=64)
	private String cgoId;
	@Column(name="session",nullable=true,length=255)
	private String session;
	@Column(name="content",nullable=true,length=1000)
	private String content;
	@Column(name="type",nullable=true,length=255)
	private String type;
	@Column(name="createDate",nullable=false)
	private Date createDate;
	@Column(name="user_name",nullable=false,length=255)
	private String userName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCgoId() {
		return cgoId;
	}
	public void setCgoId(String cgoId) {
		this.cgoId = cgoId;
	}
	public String getSession() {
		return session;
	}
	public void setSession(String session) {
		this.session = session;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
