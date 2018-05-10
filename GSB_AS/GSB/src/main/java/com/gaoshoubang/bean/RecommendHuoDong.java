package com.gaoshoubang.bean;

public class RecommendHuoDong {
	private String id;
	private String more;// 参与次数标识: 1只能参与单次，2可以参与多次
	private String url;// 活动地址
	private String start_time;// 开始时间
	private String end_time;// 结束时间

	public RecommendHuoDong() {
		super();
	}

	public RecommendHuoDong(String id, String more, String url, String start_time, String end_time) {
		super();
		this.id = id;
		this.more = more;
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

	public String getMore() {
		return more;
	}

	public void setMore(String more) {
		this.more = more;
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
