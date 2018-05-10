package com.gaoshoubang.bean;

/**
 * 资产总览
 *
 * @author Cisco
 */
public class AssetBean {
	private One one;
	private Two two;

	public AssetBean() {
		super();
	}

	public AssetBean(One one, Two two) {
		super();
		this.one = one;
		this.two = two;
	}

	public One getOne() {
		return one;
	}

	public void setOne(One one) {
		this.one = one;
	}

	public Two getTwo() {
		return two;
	}

	public void setTwo(Two two) {
		this.two = two;
	}

	public static class One {
		private double accumInIntst; // 累计收益（元）
		private double recInPrin; // 已到账收益
		private double dueInIntst; // 待到账收益
		private double okFund; // 投资收益
		private double friendFund; // 人脉赏金
		private double redFund; // 红包收益

		public One() {
			super();
		}

		public One(double accumInIntst, double recInPrin, double dueInIntst, double okFund, double friendFund, double redFund) {
			super();
			this.accumInIntst = accumInIntst;
			this.recInPrin = recInPrin;
			this.dueInIntst = dueInIntst;
			this.okFund = okFund;
			this.friendFund = friendFund;
			this.redFund = redFund;
		}

		public double getAccumInIntst() {
			return accumInIntst;
		}

		public void setAccumInIntst(double accumInIntst) {
			this.accumInIntst = accumInIntst;
		}

		public double getRecInPrin() {
			return recInPrin;
		}

		public void setRecInPrin(double recInPrin) {
			this.recInPrin = recInPrin;
		}

		public double getDueInIntst() {
			return dueInIntst;
		}

		public void setDueInIntst(double dueInIntst) {
			this.dueInIntst = dueInIntst;
		}

		public double getOkFund() {
			return okFund;
		}

		public void setOkFund(double okFund) {
			this.okFund = okFund;
		}

		public double getFriendFund() {
			return friendFund;
		}

		public void setFriendFund(double friendFund) {
			this.friendFund = friendFund;
		}

		public double getRedFund() {
			return redFund;
		}

		public void setRedFund(double redFund) {
			this.redFund = redFund;
		}

	}

	public static class Two {
		private double totalAsset; // 总资产（元）
		private double bal; // 闲置资金
		private double dueInPrin; // 待到账本息
		private double ungetFund; // 未领取赏金
		private double waitRecFund;// 待还借款总额
		private double ldFund; // 信托宝·灵动（元）
		private double dcFund; // 信托宝·定存
		private double jzFund; // 信托宝·净值
		private double hyFund;// 海盈宝

		public Two() {
			super();
		}

		public Two(double totalAsset, double bal, double dueInPrin, double ungetFund, double waitRecFund, double ldFund, double dcFund, double jzFund, double hyFund) {
			super();
			this.totalAsset = totalAsset;
			this.bal = bal;
			this.dueInPrin = dueInPrin;
			this.ungetFund = ungetFund;
			this.waitRecFund = waitRecFund;
			this.ldFund = ldFund;
			this.dcFund = dcFund;
			this.jzFund = jzFund;
			this.hyFund = hyFund;
		}

		public double getTotalAsset() {
			return totalAsset;
		}

		public void setTotalAsset(double totalAsset) {
			this.totalAsset = totalAsset;
		}

		public double getBal() {
			return bal;
		}

		public void setBal(double bal) {
			this.bal = bal;
		}

		public double getDueInPrin() {
			return dueInPrin;
		}

		public void setDueInPrin(double dueInPrin) {
			this.dueInPrin = dueInPrin;
		}

		public double getUngetFund() {
			return ungetFund;
		}

		public void setUngetFund(double ungetFund) {
			this.ungetFund = ungetFund;
		}

		public double getWaitRecFund() {
			return waitRecFund;
		}

		public void setWaitRecFund(double waitRecFund) {
			this.waitRecFund = waitRecFund;
		}

		public double getLdFund() {
			return ldFund;
		}

		public void setLdFund(double ldFund) {
			this.ldFund = ldFund;
		}

		public double getDcFund() {
			return dcFund;
		}

		public void setDcFund(double dcFund) {
			this.dcFund = dcFund;
		}

		public double getJzFund() {
			return jzFund;
		}

		public void setJzFund(double jzFund) {
			this.jzFund = jzFund;
		}

		public double getHyFund() {
			return hyFund;
		}

		public void setHyFund(double hyFund) {
			this.hyFund = hyFund;
		}

	}
}
