package com.gaoshoubang.bean;

import com.gaoshoubang.bean.base.SuperResponse;

import java.util.List;

public class ProfitBean extends SuperResponse {
	private String name; // 产品名称
	private String term; // 期限，单位（月）
	private String url; // 产品详情URL
	private String rate_b; // 起步年化收益率
	private String rate_e; // 最高年化收益率
	private String desc; // 产品描述
	private String money; // 起投金额
	private String recMoney; // 剩余额度
	private List<Items> items;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRate_b() {
		return rate_b;
	}

	public void setRate_b(String rate_b) {
		this.rate_b = rate_b;
	}

	public String getRate_e() {
		return rate_e;
	}

	public void setRate_e(String rate_e) {
		this.rate_e = rate_e;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getRecMoney() {
		return recMoney;
	}

	public void setRecMoney(String recMoney) {
		this.recMoney = recMoney;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public class Items {
		private String ico;
		private String title;
		private String note;

		public String getIco() {
			return ico;
		}

		public void setIco(String ico) {
			this.ico = ico;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getNote() {
			return note;
		}

		public void setNote(String note) {
			this.note = note;
		}

	}
}
