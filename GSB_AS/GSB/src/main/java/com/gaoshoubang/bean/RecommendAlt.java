package com.gaoshoubang.bean;

/**
 * 通知消息
 *
 * @author Cisco
 */
public class RecommendAlt {
	private String id;// 消息id
	private String title;// 主题
	private String url;// 消息详情url
	private String start_time;// 开始时间戳
	private String end_time;// 结束时间戳

	public RecommendAlt() {
		super();
	}

	public RecommendAlt(String id, String title, String url, String start_time, String end_time) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
		this.start_time = start_time;
		this.end_time = end_time;
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

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

}
