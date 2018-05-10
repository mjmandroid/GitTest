package com.gaoshoubang.bean;

import java.util.List;

/**
 * 省市区
 *
 * @author Cisco
 */
public class CityBean {
	private String id;
	private String name;
	private String pid;

	private List<CitySubs> subs;

	public CityBean() {
		super();
	}

	public CityBean(String id, String name, String pid, List<CitySubs> subs) {
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

	public List<CitySubs> getSubs() {
		return subs;
	}

	public void setSubs(List<CitySubs> subs) {
		this.subs = subs;
	}

	/**
	 * 市
	 */
	public class CitySubs {
		private String id;
		private String name;
		private String pid;
		private List<DistrictSubs> subs;

		public CitySubs() {
			super();
		}

		public CitySubs(String id, String name, String pid, List<DistrictSubs> subs) {
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

		public List<DistrictSubs> getSubs() {
			return subs;
		}

		public void setSubs(List<DistrictSubs> subs) {
			this.subs = subs;
		}

	}

	/**
	 * 区
	 */
	public class DistrictSubs {
		private String id;
		private String name;
		private String pid;

		public DistrictSubs() {
			super();
		}

		public DistrictSubs(String id, String name, String pid) {
			super();
			this.id = id;
			this.name = name;
			this.pid = pid;
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

	}
}
