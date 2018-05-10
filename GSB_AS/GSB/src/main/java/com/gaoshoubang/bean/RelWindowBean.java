package com.gaoshoubang.bean;

/**
 * 浮窗
 *
 * @author Administrator
 */
public class RelWindowBean {
	private String id;
	private String title;
	private String img;
	private String url;

	public RelWindowBean() {
		super();
	}

	public RelWindowBean(String id, String title, String img, String url) {
		super();
		this.id = id;
		this.title = title;
		this.img = img;
		this.url = url;
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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
