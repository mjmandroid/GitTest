package com.gaoshoubang.bean;

public class TransactionBean {
	private String date; // 日期
	private String typeName; // 主题
	private String amount; // 金额
	private String bal;
	private int t; // 金额的类型： 1为负数值，2为正数值

	public TransactionBean() {
		super();
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBal() {
		return bal;
	}

	public void setBal(String bal) {
		this.bal = bal;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

}
