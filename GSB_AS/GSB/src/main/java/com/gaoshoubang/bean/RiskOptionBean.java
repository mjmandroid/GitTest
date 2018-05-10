package com.gaoshoubang.bean;

/**
 * Created by lzx on 2017/7/4.
 */

public class RiskOptionBean {

	/**
	 * id : 14
	 * qnid : 2
	 * qid : 7
	 * name : 用户体验不好
	 * value : 3
	 * weight : 0
	 */

	private String id;
	private String qnid;
	private String qid;
	private String name;
	private String value;
	private String weight;
	public boolean isSelected;

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

	public String getQid() {
		return qid;
	}

	public void setQid(String qid) {
		this.qid = qid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}
}
