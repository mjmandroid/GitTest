package com.gaoshoubang.bean;

import java.util.List;

/**
 * 常见问题
 *
 * @author Cisco
 */
public class Problem {
	private int id;
	private String name;
	private String logo;
	private List<Qs> qs;

	public Problem() {
		super();
	}

	public Problem(int id, String name, String logo, List<Qs> qs) {
		super();
		this.id = id;
		this.name = name;
		this.logo = logo;
		this.qs = qs;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public List<Qs> getQs() {
		return qs;
	}

	public void setQs(List<Qs> qs) {
		this.qs = qs;
	}

	public class Qs {
		private int id;
		private String title;
		private String cc;

		public Qs() {
			super();
		}

		public Qs(int id, String title, String cc) {
			super();
			this.id = id;
			this.title = title;
			this.cc = cc;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

	}

}
