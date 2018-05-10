package com.gaoshoubang.bean;

/**
 * 推荐位信息
 *
 * @author Cisco
 */
public class RecommendRel {
	private String id;
	private String title;// 消息主题
	private String desc;// 消息描述
	private String nums;
	private String url;// 消息url
	private String logo_url;// 消息图片url

	public RecommendRel() {
		super();
	}

	public RecommendRel(String id, String title, String desc, String nums, String url, String logo_url) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.nums = nums;
		this.url = url;
		this.logo_url = logo_url;
	}

	public String getNums() {
		return nums;
	}

	public void setNums(String nums) {
		this.nums = nums;
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

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

}
