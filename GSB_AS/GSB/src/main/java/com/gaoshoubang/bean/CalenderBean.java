package com.gaoshoubang.bean;

import java.util.List;

/**
 * 回款日历bean
 *
 * @author Cisco
 */
public class CalenderBean {

	private int year;
	private int month;
	private List<Day> day;
	private Rel rel;

	public CalenderBean() {
		super();
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public List<Day> getDay() {
		return day;
	}

	public void setDay(List<Day> day) {
		this.day = day;
	}

	public Rel getRel() {
		return rel;
	}

	public void setRel(Rel rel) {
		this.rel = rel;
	}

	public class Day {
		private int date_day;
		private List<Details> details;

		public Day() {
			super();
		}

		public int getDate_day() {
			return date_day;
		}

		public void setDate_day(int date_day) {
			this.date_day = date_day;
		}

		public List<Details> getDetails() {
			return details;
		}

		public void setDetails(List<Details> details) {
			this.details = details;
		}
	}

	public class Details {
		private String day;// 回款当天日期
		private String name;// 回款名称
		private String amt;// 回款金额

		public Details() {
			super();
		}

		public String getDay() {
			return day;
		}

		public void setDay(String day) {
			this.day = day;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAmt() {
			return amt;
		}

		public void setAmt(String amt) {
			this.amt = amt;
		}

	}

	/**
	 * 当月回款详情
	 */
	public class Rel {
		private int rec_nums;// 已到账笔数
		private String rec_amt;// 已到账金额
		private int wait_nums;// 待到账笔数
		private String wait_amt;// 待到账金额

		public Rel() {
			super();
		}

		public int getRec_nums() {
			return rec_nums;
		}

		public void setRec_nums(int rec_nums) {
			this.rec_nums = rec_nums;
		}

		public String getRec_amt() {
			return rec_amt;
		}

		public void setRec_amt(String rec_amt) {
			this.rec_amt = rec_amt;
		}

		public int getWait_nums() {
			return wait_nums;
		}

		public void setWait_nums(int wait_nums) {
			this.wait_nums = wait_nums;
		}

		public String getWait_amt() {
			return wait_amt;
		}

		public void setWait_amt(String wait_amt) {
			this.wait_amt = wait_amt;
		}
	}
}
