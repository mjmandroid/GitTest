package com.gaoshoubang.bean;

import java.util.List;

/**
 * 省市区
 *
 * @author Cisco
 */
public class CityStreetBean {
	private String id;
	private String name;
	private String pid;
	private List<Subs> subs;

	public CityStreetBean() {
		super();
	}

	public CityStreetBean(String id, String name, String pid, List<Subs> subs) {
		super();
		this.id = id;
		this.name = name;
		this.pid = pid;
		this.subs = subs;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public List<Subs> getSubs() {
		return subs;
	}

	public void setSubs(List<Subs> subs) {
		this.subs = subs;
	}

	public class Subs {
		private String id;
		private String name;

		public Subs() {
			super();
		}

		public Subs(String id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
