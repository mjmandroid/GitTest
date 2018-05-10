package com.gaoshoubang.bean;

/**
 * 新手指南等信息 ( ver 3.6+ )
 *
 * @author Cisco
 */
public class RecommendIntro {
	private String id; // 消息id [冗余]
	private String title; // 主题 [冗余]
	private String url;// 跳转的URL
	private String logo_url; // 图片连接

	public RecommendIntro(String id, String title, String url, String logo_url) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.logo_url = logo_url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

}
