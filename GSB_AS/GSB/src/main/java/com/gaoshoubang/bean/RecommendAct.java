package com.gaoshoubang.bean;

import java.io.Serializable;

/**
 * 运营活动信息
 *
 * @author Cisco
 */
public class RecommendAct {
	private String id;// id序列
	private String title;// 活动主题
	private String logo_title;// 图片标题
	private String logo_url;// 图片url
	private String state;// 状态: 1即将开始 2进行中
	private String url;// 活动链接
	private String start_time;// 开始时间戳
	private String end_time;// 结束时间戳
	private Share share;

	public RecommendAct() {
		super();
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

	public String getLogo_title() {
		return logo_title;
	}

	public void setLogo_title(String logo_title) {
		this.logo_title = logo_title;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public Share getShare() {
		return share;
	}

	public void setShare(Share share) {
		this.share = share;
	}

	public class Share implements Serializable {
		private static final long serialVersionUID = 1L;
		private String title;
		private String image;
		private String content;

		public Share() {
			super();
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

	}
}
