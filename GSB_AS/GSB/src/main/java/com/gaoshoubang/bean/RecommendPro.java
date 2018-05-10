package com.gaoshoubang.bean;

/**
 * 推荐产品
 *
 * @author Cisco
 */
public class RecommendPro {
	private String id;// 产品id
	private String title;// 标题
	private String desc;// 产品描述
	private String url;// 产品详情url
	private String limitStr;
	private String limit;
	private String logo_url;// 产品图url
	private String type;// 产品类型
	private String tags;
	private String rate;// 年化收益率(冗余字段)
	private String payTime;// 开售时间戳(冗余字段)
	private String beginTime;// 募集结束时间戳(冗余字段)
	private String useFund;
	private String useStr;

	public RecommendPro() {
		super();
	}

	public RecommendPro(String id, String title, String desc, String url, String limitStr, String limit, String logo_url, String type, String tags, String rate, String payTime, String beginTime,
	                    String useFund, String useStr) {
		super();
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.url = url;
		this.limitStr = limitStr;
		this.limit = limit;
		this.logo_url = logo_url;
		this.type = type;
		this.tags = tags;
		this.rate = rate;
		this.payTime = payTime;
		this.beginTime = beginTime;
		this.useFund = useFund;
		this.useStr = useStr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLogo_url() {
		return logo_url;
	}

	public void setLogo_url(String logo_url) {
		this.logo_url = logo_url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getLimitStr() {
		return limitStr;
	}

	public void setLimitStr(String limitStr) {
		this.limitStr = limitStr;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getUseFund() {
		return useFund;
	}

	public void setUseFund(String useFund) {
		this.useFund = useFund;
	}

	public String getUseStr() {
		return useStr;
	}

	public void setUseStr(String useStr) {
		this.useStr = useStr;
	}

}
