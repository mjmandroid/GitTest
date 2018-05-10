package com.gaoshoubang.bean;

/**
 * 版本信息
 */
public class LatestVerBean {

	private String id;
	private String appId;
	private String verNum;// 版本号
	private int code;// 版本编号
	private String name;// 版本名
	private String desc;// 版本描述
	private String appUrl; // app下载地址,可能为空
	private String appSize;// App文件的大小(单位:字节)
	private int updateType;// 更新选项： 1可选更新；2必需更新
	private long createTime;

	public LatestVerBean() {
		super();
	}

	public LatestVerBean(String id, String appId, String verNum, int code, String name, String desc, int updateType, long createTime) {
		super();
		this.id = id;
		this.appId = appId;
		this.verNum = verNum;
		this.code = code;
		this.name = name;
		this.desc = desc;
		this.updateType = updateType;
		this.createTime = createTime;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getVerNum() {
		return verNum;
	}

	public void setVerNum(String verNum) {
		this.verNum = verNum;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getAppSize() {
		return appSize;
	}

	public void setAppSize(String appSize) {
		this.appSize = appSize;
	}

	@Override
	public String toString() {
		return "LatestVerBean [id=" + id + ", appId=" + appId + ", verNum=" + verNum + ", code=" + code + ", name=" + name + ", desc=" + desc + ", updateType=" + updateType + ", createTime="
				+ createTime + "]";
	}

}
