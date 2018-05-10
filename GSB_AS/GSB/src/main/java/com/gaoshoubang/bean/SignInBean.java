package com.gaoshoubang.bean;

/**
 * 签到实体类
 */
public class SignInBean {
	private String mid;// 会员id
	private String rank;// 等级
	private String title;// 头衔
	private String value;// 积分值
	private String goup;// 是否满足升级条件
	private String sign;// 今天是否已签到
	private int week;// 签到周期天数
	private String val;// 签到获取的积分

	public SignInBean() {
		super();
	}

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getGoup() {
		return goup;
	}

	public void setGoup(String goup) {
		this.goup = goup;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}

}
