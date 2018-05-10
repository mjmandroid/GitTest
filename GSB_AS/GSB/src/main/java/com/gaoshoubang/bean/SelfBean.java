package com.gaoshoubang.bean;

import java.io.Serializable;

/**
 * 个人资料实体类
 */
public class SelfBean implements Serializable {
	private String uid;// 用户ID
	private String isShowModifyPasswd;// 判断密码是否过期
	private String username;// 用户名
	private String nickname;// 用户昵称
	private String faceUrl;// 头像地址
	private String gender;// 性别
	private String qq;// QQ号码
	private String email;// 邮箱
	private String phone;// 手机号码
	private int level;// 等级
	private String title;// 头衔
	private int score;// 积分
	private int cash;// 现金券数量
	private int oldCash;
	private String problen;
	private String collection;
	private String isgsm;// 0 未填高手码 1已填高手码
	private double bal;// 可用余额
	private double accumInIntst;// 累计收益
	private double totalAsset;// 总资产
	private int newMsgCnt;// 我的新消息数
	private String frozen;// 冻结金额
	private String inVote;// 在投标数
	private int borrowCount;
	private String contacts;// 我的人脉数
	private String powerTotal;// 我的高手币
	private String beta;// 击败数
	private int hasOpenAutoBid;// 是否已开启自动投标。1：否；2：是；
	private int hasOpenUmp;// 是否已经开通联动优势(Union Mobile Pay)。1：未开通；2：已开通；
	private String hasOpenImg; // 1：未开通帐户是展示的图标；
	private int hasPayUmp; // 是否有过投资: 1没有； 2已投资过
	private String unPayImg; // 没有投资过时展示的图标；
	private int newMessage;// 有多少条新消息
	private String trueName;// 真实姓名
	private int hasUserInfo;//修改状态

	private Addr addr;
	public int riskScore;  //风险测评分数
	public int riskId;      //风险测评ID
	public int newInitNums; //新手投资剩余次数
	public String balanceImg;//图片地址
	public int balanceUrl; //1 产品页,2跳地址

	public int getHasUserInfo() {
		return hasUserInfo;
	}

	public void setHasUserInfo(int hasUserInfo) {
		this.hasUserInfo = hasUserInfo;
	}
	public SelfBean() {
		super();
	}

	public String getUid() {
		return uid;
	}


	public void setUid(String isShowModifyPasswd) {
		this.isShowModifyPasswd = isShowModifyPasswd;
	}

	public String getIsShowModifyPasswd() {
		return isShowModifyPasswd;
	}


	public void setIsShowModifyPasswd(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getHasOpenImg() {
		return hasOpenImg;
	}

	public void setHasOpenImg(String hasOpenImg) {
		this.hasOpenImg = hasOpenImg;
	}

	public int getHasPayUmp() {
		return hasPayUmp;
	}

	public void setHasPayUmp(int hasPayUmp) {
		this.hasPayUmp = hasPayUmp;
	}

	public String getUnPayImg() {
		return unPayImg;
	}

	public void setUnPayImg(String unPayImg) {
		this.unPayImg = unPayImg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getFaceUrl() {
		return faceUrl;
	}

	public void setFaceUrl(String faceUrl) {
		this.faceUrl = faceUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
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

	public String getProblen() {
		return problen;
	}

	public void setProblen(String problen) {
		this.problen = problen;
	}

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getIsgsm() {
		return isgsm;
	}

	public void setIsgsm(String isgsm) {
		this.isgsm = isgsm;
	}

	public double getBal() {
		return bal;
	}

	public void setBal(double bal) {
		this.bal = bal;
	}

	public double getAccumInIntst() {
		return accumInIntst;
	}

	public void setAccumInIntst(double accumInIntst) {
		this.accumInIntst = accumInIntst;
	}

	public double getTotalAsset() {
		return totalAsset;
	}

	public void setTotalAsset(double totalAsset) {
		this.totalAsset = totalAsset;
	}

	public int getNewMsgCnt() {
		return newMsgCnt;
	}

	public void setNewMsgCnt(int newMsgCnt) {
		this.newMsgCnt = newMsgCnt;
	}

	public String getFrozen() {
		return frozen;
	}

	public void setFrozen(String frozen) {
		this.frozen = frozen;
	}

	public String getInVote() {
		return inVote;
	}

	public void setInVote(String inVote) {
		this.inVote = inVote;
	}

	public int getBorrowCount() {
		return borrowCount;
	}

	public void setBorrowCount(int borrowCount) {
		this.borrowCount = borrowCount;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getBeta() {
		return beta;
	}

	public void setBeta(String beta) {
		this.beta = beta;
	}

	public int getHasOpenAutoBid() {
		return hasOpenAutoBid;
	}

	public void setHasOpenAutoBid(int hasOpenAutoBid) {
		this.hasOpenAutoBid = hasOpenAutoBid;
	}

	public int getHasOpenUmp() {
		return hasOpenUmp;
	}

	public void setHasOpenUmp(int hasOpenUmp) {
		this.hasOpenUmp = hasOpenUmp;
	}

	public int getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(int newMessage) {
		this.newMessage = newMessage;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getPowerTotal() {
		return powerTotal;
	}

	public void setPowerTotal(String powerTotal) {
		this.powerTotal = powerTotal;
	}

	public Addr getAddr() {
		return addr;
	}

	public void setAddr(Addr addr) {
		this.addr = addr;
	}

	public class Addr implements Serializable {
		private static final long serialVersionUID = 1L;
		private String name; // 收件人姓名
		private String phone; // 收件人手机号码
		private String province; // 省级/自治区/直辖市 id
		private String city; // 地级/市级 id
		private String area; // 县/区 id
		private String town; // 乡/镇/街道办 id
		private String province_name; // 省级/自治区/直辖市 名称
		private String city_name; // 地级/市级 名称
		private String area_name; // 县/区 名称
		private String town_name; // 乡/镇/街道办 名称
		private String address;// 详细地址

		public Addr() {
			super();
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getTown() {
			return town;
		}

		public void setTown(String town) {
			this.town = town;
		}

		public String getProvince_name() {
			return province_name;
		}

		public void setProvince_name(String province_name) {
			this.province_name = province_name;
		}

		public String getCity_name() {
			return city_name;
		}

		public void setCity_name(String city_name) {
			this.city_name = city_name;
		}

		public String getArea_name() {
			return area_name;
		}

		public void setArea_name(String area_name) {
			this.area_name = area_name;
		}

		public String getTown_name() {
			return town_name;
		}

		public void setTown_name(String town_name) {
			this.town_name = town_name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

	}
}
