package com.gaoshoubang.bean;

public class RecommendGongGao {
	private String id;// ID
	private String desc;// 公告内容
	private String url;// 公告详情URL
	private String start_time;// 开始时间
	private String end_time;// 结束时间

	public RecommendGongGao() {
		super();
	}

	public RecommendGongGao(String id, String desc, String url, String start_time, String end_time) {
		super();
		this.id = id;
		this.desc = desc;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
