package com.gaoshoubang.bean;

public class MessageBean {
	private String id; // 序列
	private String type_name; // 消息类型
	private String title; // 标题
	private String cc; // 内容
	private String date; // 消息时间
	private String url; // 消息url

	public MessageBean() {
		super();
	}

	public MessageBean(String id, String type_name, String title, String cc, String date, String url) {
		super();
		this.id = id;
		this.type_name = type_name;
		this.title = title;
		this.cc = cc;
		this.date = date;
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType_name() {
		return type_name;
	}

	public void setType_name(String type_name) {
		this.type_name = type_name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
