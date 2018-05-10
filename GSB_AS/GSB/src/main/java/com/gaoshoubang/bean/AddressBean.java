package com.gaoshoubang.bean;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AddressBean {
	public String name;
	public String phone;
	public String province;
	public String city;
	public String area;
	public String town;
	public String address;

	public AddressBean(String name, String phone, String province, String city, String area, String town, String address) {
		this.name = name;
		this.phone = phone;
		this.province = province;
		this.city = city;
		this.area = area;
		this.town = town;
		this.address = address;
	}
}
