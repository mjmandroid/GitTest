package com.gaoshoubang.bean;

/**
 * Created by lzx on 2017/6/12.
 */

public class ShareBean {
	public String id;
	public String code;
	public String title;
	public String content;
	public String imgUrl;
	public String destUrl;

	public ShareBean(String title, String content, String imgUrl, String destUrl) {
		this.title = title;
		this.content = content;
		this.imgUrl = imgUrl;
		this.destUrl = destUrl;
	}

	public String getId() {
		return id;
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public String getDestUrl() {
		return destUrl;
	}
}
