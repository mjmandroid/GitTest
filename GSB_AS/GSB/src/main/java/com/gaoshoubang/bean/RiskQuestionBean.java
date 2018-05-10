package com.gaoshoubang.bean;


import java.util.List;

/**
 * Created by lzx on 2017/7/4.
 */

public class RiskQuestionBean {
	/**
	 * id : 0
	 * qnid : 3
	 * type : 1
	 * question : 高搜易是深圳市唯一一家政府战略扶持的双国资背景，发改委战略扶持，社科院指导，创东方投资的互联网金融平台，您知道吗？
	 * weight : 0
	 * options : []
	 */
	private String id;
	private String qnid;
	private String type;
	private String question;
	private String weight;
	private List<RiskOptionBean> options;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQnid() {
		return qnid;
	}

	public void setQnid(String qnid) {
		this.qnid = qnid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public List<RiskOptionBean> getOptions() {
		return options;
	}

	public void setOptions(List<RiskOptionBean> options) {
		this.options = options;
	}

}
