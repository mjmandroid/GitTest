package com.gaoshoubang.bean;

/**
 * 签到周期
 *
 * @author Cisco
 */
public class SignInCyclesBean {
	private int day;
	private int val;
	private boolean isSign;

	public SignInCyclesBean() {
		super();
	}

	public SignInCyclesBean(int day, int val) {
		super();
		this.day = day;
		this.val = val;
	}

	public SignInCyclesBean(int day, int val, boolean isSign) {
		super();
		this.day = day;
		this.val = val;
		this.isSign = isSign;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getVal() {
		return val;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public boolean isSign() {
		return isSign;
	}

	public void setSign(boolean isSign) {
		this.isSign = isSign;
	}

}
