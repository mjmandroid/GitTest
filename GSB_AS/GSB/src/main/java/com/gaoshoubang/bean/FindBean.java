package com.gaoshoubang.bean;

/**
 * 发现
 *
 * @author Cisco
 */
public class FindBean {
	private String uid;// id
	private int rank;// 等级id
	private String nickname;// 昵称
	private int value;// 高手币数
	private String faceUrl;// 头像url
	private String rankname;// 当前等级名称
	private String privname;// 上一个等级名称
	private String nextname;// 下一个等级名称
	private int cash;// 红包数
	private int oldCash;// 即将过期红包数
	private int sign;// 是否签到，1未签到 2已签到
	private String signDesc;// 今日签到可获取高手币
	private String getDPs;// 平台当前已领取赏金的人数
	private String ones;// 人脉数
	private String money;// 人脉赏金
	private String myRelUrl;// 人脉规则url
	private Team team;// 理财军团
	private Honour honour;// 勋章
	private Bbs bbs;// 高家大院
	private UpInfo upInfo;// 升级
	private FriendInfo friendInfo;// 我的人脉
	private RedInfo redInfo;// 我的红包
	private ValInfo valInfo;// 高手币兑换
	private SignInfo signInfo;// 签到
	private String shareUrl;

	public FindBean() {
		super();
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public String getRankname() {
		return rankname;
	}

	public void setRankname(String rankname) {
		this.rankname = rankname;
	}

	public String getPrivname() {
		return privname;
	}

	public void setPrivname(String privname) {
		this.privname = privname;
	}

	public String getNextname() {
		return nextname;
	}

	public void setNextname(String nextname) {
		this.nextname = nextname;
	}

	public int getCash() {
		return cash;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}

	public int getSign() {
		return sign;
	}

	public void setSign(int sign) {
		this.sign = sign;
	}

	public String getSignDesc() {
		return signDesc;
	}

	public void setSignDesc(String signDesc) {
		this.signDesc = signDesc;
	}

	public String getGetDPs() {
		return getDPs;
	}

	public void setGetDPs(String getDPs) {
		this.getDPs = getDPs;
	}

	public String getOnes() {
		return ones;
	}

	public void setOnes(String ones) {
		this.ones = ones;
	}

	public int getOldCash() {
		return oldCash;
	}

	public void setOldCash(int oldCash) {
		this.oldCash = oldCash;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getMyRelUrl() {
		return myRelUrl;
	}

	public void setMyRelUrl(String myRelUrl) {
		this.myRelUrl = myRelUrl;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Honour getHonour() {
		return honour;
	}

	public void setHonour(Honour honour) {
		this.honour = honour;
	}

	public Bbs getBbs() {
		return bbs;
	}

	public void setBbs(Bbs bbs) {
		this.bbs = bbs;
	}

	public UpInfo getUpInfo() {
		return upInfo;
	}

	public void setUpInfo(UpInfo upInfo) {
		this.upInfo = upInfo;
	}

	public FriendInfo getFriendInfo() {
		return friendInfo;
	}

	public void setFriendInfo(FriendInfo friendInfo) {
		this.friendInfo = friendInfo;
	}

	public RedInfo getRedInfo() {
		return redInfo;
	}

	public void setRedInfo(RedInfo redInfo) {
		this.redInfo = redInfo;
	}

	public ValInfo getValInfo() {
		return valInfo;
	}

	public void setValInfo(ValInfo valInfo) {
		this.valInfo = valInfo;
	}

	public SignInfo getSignInfo() {
		return signInfo;
	}

	public void setSignInfo(SignInfo signInfo) {
		this.signInfo = signInfo;
	}

	/**
	 * 理财军团
	 *
	 * @author Cisco
	 */
	public class Team {
		private int isOpen; // 是否启用“理财军团”模块，1不启用，2启用
		private String title;
		private String logo;
		private String miniLogo;
		private String url;
		private String desc;
		private String label;
		private int team; // 军团信息: 0指未开通, 1指铜牌军团, 2指银牌军团，3指金牌军团
		private String ctitle; // 当前军团名称
		private int nums; // 军团人数(有过投资的)
		private int goup; // 升级条件是否满足：1未满足，2满足
		private String cc;
		private String r_str;
		private String labelUrl;

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public String getMiniLogo() {
			return miniLogo;
		}

		public void setMiniLogo(String miniLogo) {
			this.miniLogo = miniLogo;
		}

		public String getR_str() {
			return r_str;
		}

		public void setR_str(String r_str) {
			this.r_str = r_str;
		}

		public Team() {
			super();
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getUrl() {
			return url;
		}

		public String getLabelUrl() {
			return labelUrl;
		}

		public void setLabelUrl(String labelUrl) {
			this.labelUrl = labelUrl;
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

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getTeam() {
			return team;
		}

		public void setTeam(int team) {
			this.team = team;
		}

		public String getCtitle() {
			return ctitle;
		}

		public void setCtitle(String ctitle) {
			this.ctitle = ctitle;
		}

		public int getNums() {
			return nums;
		}

		public void setNums(int nums) {
			this.nums = nums;
		}

		public int getGoup() {
			return goup;
		}

		public void setGoup(int goup) {
			this.goup = goup;
		}

		public int getIsOpen() {
			return isOpen;
		}

		public void setIsOpen(int isOpen) {
			this.isOpen = isOpen;
		}

	}

	/**
	 * 勋章馆
	 *
	 * @author Cisco
	 */
	public class Honour {
		private String title;
		private String logo;
		private String url;
		private int peoples;
		private int have_nums;
		private int wait_nums;
		private String label;
		private String cc;
		private String r_str;

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public String getR_str() {
			return r_str;
		}

		public void setR_str(String r_str) {
			this.r_str = r_str;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public int getHave_nums() {
			return have_nums;
		}

		public void setHave_nums(int have_nums) {
			this.have_nums = have_nums;
		}

		public int getWait_nums() {
			return wait_nums;
		}

		public void setWait_nums(int wait_nums) {
			this.wait_nums = wait_nums;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getPeoples() {
			return peoples;
		}

		public void setPeoples(int peoples) {
			this.peoples = peoples;
		}

	}

	/**
	 * 高家大院
	 *
	 * @author Cisco
	 */
	public class Bbs {
		private String title;
		private String logo;
		private String url;
		private String desc;
		private String label;
		private String cc;
		private String r_str;

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public String getR_str() {
			return r_str;
		}

		public void setR_str(String r_str) {
			this.r_str = r_str;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
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

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}
	}

	/**
	 * 升级
	 *
	 * @author Cisco
	 */
	public class UpInfo {
		private String title;
		private String label;
		private int rank;
		private int nextRank;
		private String desc;
		private String logo;
		private String cc;
		private String r_str;

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public String getR_str() {
			return r_str;
		}

		public void setR_str(String r_str) {
			this.r_str = r_str;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getRank() {
			return rank;
		}

		public void setRank(int rank) {
			this.rank = rank;
		}

		public int getNextRank() {
			return nextRank;
		}

		public void setNextRank(int nextRank) {
			this.nextRank = nextRank;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}

	/**
	 * 我的人脉
	 */
	public class FriendInfo {
		private String invest_logo;
		private String money_logo;
		private String title;
		private String label;
		private int ones;
		private String money;
		private String wait_money;
		private String friend_logo;
		private String cc;

		public String getInvest_logo() {
			return invest_logo;
		}

		public String getCc() {
			return cc;
		}

		public void setCc(String cc) {
			this.cc = cc;
		}

		public void setInvest_logo(String invest_logo) {
			this.invest_logo = invest_logo;
		}

		public String getMoney_logo() {
			return money_logo;
		}

		public void setMoney_logo(String money_logo) {
			this.money_logo = money_logo;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getOnes() {
			return ones;
		}

		public void setOnes(int ones) {
			this.ones = ones;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getWait_money() {
			return wait_money;
		}

		public void setWait_money(String wait_money) {
			this.wait_money = wait_money;
		}

		public String getFriend_logo() {
			return friend_logo;
		}

		public void setFriend_logo(String friend_logo) {
			this.friend_logo = friend_logo;
		}
	}

	/**
	 * 我的红包
	 */
	public class RedInfo {
		private String title;
		private String label;
		private int cash;
		private int oldCash;
		private String logo;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getCash() {
			return cash;
		}

		public void setCash(int cash) {
			this.cash = cash;
		}

		public int getOldCash() {
			return oldCash;
		}

		public void setOldCash(int oldCash) {
			this.oldCash = oldCash;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}

	/**
	 * 高手币兑换
	 */
	public class ValInfo {
		private String title;
		private String label;
		private int value;
		private String url;
		private String logo;
		private String cc;
		private String r_str;

		public String getTitle() {
			return title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
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

		public String getR_str() {
			return r_str;
		}

		public void setR_str(String r_str) {
			this.r_str = r_str;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}

	/**
	 * 签到
	 */
	public class SignInfo {
		private String title;
		private String label;
		private int sign;
		private int week;
		private int today_value;
		private int tomorrow_value;
		private String logo;

		public String getTitle() {
			return title;
		}

		public int getWeek() {
			return week;
		}

		public void setWeek(int week) {
			this.week = week;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public int getSign() {
			return sign;
		}

		public void setSign(int sign) {
			this.sign = sign;
		}

		public int getToday_value() {
			return today_value;
		}

		public void setToday_value(int today_value) {
			this.today_value = today_value;
		}

		public int getTomorrow_value() {
			return tomorrow_value;
		}

		public void setTomorrow_value(int tomorrow_value) {
			this.tomorrow_value = tomorrow_value;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}
	}
}
