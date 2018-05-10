package com.gaoshoubang.bean;

import com.google.gson.annotations.SerializedName;

public class ProductBean {
	private int id;// 产品id
	private int type;// 产品类型: 105信托宝 116定存宝 117净值宝
	private String title;// 标题
	private String term;// 产品期限
	private String url;// 产品详情url
	private String rate;// 年化收益率
	private String desc;// 产品描述
	private int payTime;// 开售时间戳
	private int beginTime;// 结束时间戳
	private String redTag;// APP专享标
	private String tag;// 标签1 新手标
	private String tag2;// 标签2 首次限投
	private String overplus;// 剩余额度
	private int buyers;// 购买人数
	private int flag;// 产品状态：1:在售/预热,3:售罄,4:计息中,5:已回款
	private int serverTime;// 服务器当前时间
	private int firstMid; // 首投: 0指代未有首投，大于0的值指代首投人的mid
	private int bestMid; // 首富: 0指代未有首富，大于0的值指代首富的mid
	private int schedule; // 进度值： 30指代 30%
	@SerializedName("activityId")
	private int activityId; //活动ID ,1 新手标

	public int getActivityId() {
		return activityId;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getInittype() {
		return inittype;
	}

	public void setInittype(int inittype) {
		this.inittype = inittype;
	}

	private int inittype;     //1固收,2灵活

	public ProductBean() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getPayTime() {
		return payTime;
	}

	public void setPayTime(int payTime) {
		this.payTime = payTime;
	}

	public int getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(int beginTime) {
		this.beginTime = beginTime;
	}

	public String getRedTag() {
		return redTag;
	}

	public void setRedTag(String redTag) {
		this.redTag = redTag;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag2() {
		return tag2;
	}

	public void setTag2(String tag2) {
		this.tag2 = tag2;
	}

	public String getOverplus() {
		return overplus;
	}

	public void setOverplus(String overplus) {
		this.overplus = overplus;
	}

	public int getBuyers() {
		return buyers;
	}

	public void setBuyers(int buyers) {
		this.buyers = buyers;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public int getServerTime() {
		return serverTime;
	}

	public void setServerTime(int serverTime) {
		this.serverTime = serverTime;
	}

	public int getFirstMid() {
		return firstMid;
	}

	public void setFirstMid(int firstMid) {
		this.firstMid = firstMid;
	}

	public int getBestMid() {
		return bestMid;
	}

	public void setBestMid(int bestMid) {
		this.bestMid = bestMid;
	}

	public int getSchedule() {
		return schedule;
	}

	public void setSchedule(int schedule) {
		this.schedule = schedule;
	}

}
