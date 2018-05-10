package com.gaoshoubang.bean;

import java.io.Serializable;

/**
 * 第三方登录请求实体类
 */
public class Tuser implements Serializable {
	private static final long serialVersionUID = 1L;
	private String partnerCode;// 合作方编码。WECHAT：微信；QQ：QQ；WEIBO：新浪微博
	private String openid;// 合作方登录账号开放ID
	private String unionid;// 合作方登录账号联合ID
	private String nickname;// 昵称，为空值时，从合作方用户信息里取相应的nickname
	private int gender;// 性别。1：男；2：女；3：保密
	private String faceUrl;// 头像Url

	public Tuser(String partnerCode, String openid, String unionid) {
		super();
		this.partnerCode = partnerCode;
		this.openid = openid;
		this.unionid = unionid;
	}

	public Tuser(String partnerCode, String openid, String unionid, String nickname, int gender, String faceUrl) {
		super();
		this.partnerCode = partnerCode;
		this.openid = openid;
		this.unionid = unionid;
		this.nickname = nickname;
		this.gender = gender;
		this.faceUrl = faceUrl;
	}

	public String getPartnerCode() {
		return partnerCode;
	}

	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
