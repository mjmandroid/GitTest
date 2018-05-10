package com.gaoshoubang.bean;

import java.io.Serializable;

/**
 * 配置信息实体类
 */
public class CnfListBean implements Serializable {

	private String HOME_PAGE_URL;// 主页URL
	private String WD_RESULT_URL;// 提现结果通知URL
	private String RCHG_RESULT_URL;// 充值结果通知URL
	private String INV_RESULT_URL;// 投资结果通知URL
	private String INVITE_REWARD_SEND_RED_BAG_URL;// 邀请返利页面的发红包URL
	private String UMP_BIND_BANK_CARD_RESULT_URL;// 联动绑定银行卡结果通知URL
	private String TREAS_URL;// 限时夺宝H5
	private String MASTER_LIST_URL;// 高手列表H5
	private String RCHG_URL;// 充值H5
	private String WD_URL;// 取现H5
	private String COUPON_URL;// 优惠券H5
	private String TREAS_SHARE_URL;// 限时夺宝分享目标URL
	private String AUTO_BID_URL;// 高手帮自动投标
	private String GSB_INVEST_STREAM;// 高手帮投资流水
	private String GSB_INVEST_RECORD;// 投资记录
	private String FUND_CUSTODY_URL;// 高手帮资金托管账户
	private String UPDATE_PAY_PWD;// 修改支付密码
	private String GSB_BONUS_BILL;// 高手帮我的现金券
	private String GSB_DCB_INFO;// 高手帮定存宝优势
	private String GSB_XTB_INFO;// 高手帮信托宝优势
	private String GSB_JZB_INFO;// 高手帮净值宝优势
	private String INVITE_REWARD_URL;// 邀请返利URL
	private String OPEN_APP_IMG_URL;// 打开APP闪频图片URL
	private String OPEN_APP_DEST_URL;// 打开APP闪频目标URL
	private String UMP_URL;// 开通联动优势的页面URL
	private String APP_URL;// App下载地址
	private String GSB_RED_INVESTMENT;// 红包投资
	private String GSB_BORROW_URL;// 我的借款
	private String MY_MESSAGE_URL;// 我的消息
	private String MY_GRADE_URL;// 我的等级
	private String ABOUT_ME_URL;// 关于我们
	private String SHARE_FOR_COOKIE;// 分享地址
	private String REG_IMAGE_URL; // APP 注册页图片地址
	private String LOGIN_IMAGE_URL; // APP 登录页图片地址
	private String COUPON_EXPLAIN; // 红包解释页面
	private String EXCHANGE_EXPLAIN;// 高手币兑换解释
	private String GRADE_SCORE;// 高手币兑换
	private String ARTICLE_SHARE;// 分享文章邀请
	private String MESS_NOTICE;// 短信提醒投资
	private String PASSPORT_URL;// 判断当前登录用户的
	private String REGISTER_SUCCESS;// 注册成功
	private String REG_HEAD_IMG;// 注册头部图片
	private String SHOP_URL;
	private String MEDAL_RULE;// 勋章
	private String REG_LABEL_INDEX; // 注册提示--在注册
	private String REG_LABEL_SHARE; // 注册提示--在分享

	public CnfListBean() {
		super();
	}

	@Override
	public String toString() {
		return super.toString();
	}

	public String getHOME_PAGE_URL() {
		return HOME_PAGE_URL;
	}

	public void setHOME_PAGE_URL(String hOME_PAGE_URL) {
		HOME_PAGE_URL = hOME_PAGE_URL;
	}

	public String getWD_RESULT_URL() {
		return WD_RESULT_URL;
	}

	public void setWD_RESULT_URL(String wD_RESULT_URL) {
		WD_RESULT_URL = wD_RESULT_URL;
	}

	public String getRCHG_RESULT_URL() {
		return RCHG_RESULT_URL;
	}

	public void setRCHG_RESULT_URL(String rCHG_RESULT_URL) {
		RCHG_RESULT_URL = rCHG_RESULT_URL;
	}

	public String getINV_RESULT_URL() {
		return INV_RESULT_URL;
	}

	public void setINV_RESULT_URL(String iNV_RESULT_URL) {
		INV_RESULT_URL = iNV_RESULT_URL;
	}

	public String getINVITE_REWARD_SEND_RED_BAG_URL() {
		return INVITE_REWARD_SEND_RED_BAG_URL;
	}

	public void setINVITE_REWARD_SEND_RED_BAG_URL(String iNVITE_REWARD_SEND_RED_BAG_URL) {
		INVITE_REWARD_SEND_RED_BAG_URL = iNVITE_REWARD_SEND_RED_BAG_URL;
	}

	public String getUMP_BIND_BANK_CARD_RESULT_URL() {
		return UMP_BIND_BANK_CARD_RESULT_URL;
	}

	public void setUMP_BIND_BANK_CARD_RESULT_URL(String uMP_BIND_BANK_CARD_RESULT_URL) {
		UMP_BIND_BANK_CARD_RESULT_URL = uMP_BIND_BANK_CARD_RESULT_URL;
	}

	public String getTREAS_URL() {
		return TREAS_URL;
	}

	public void setTREAS_URL(String tREAS_URL) {
		TREAS_URL = tREAS_URL;
	}

	public String getMASTER_LIST_URL() {
		return MASTER_LIST_URL;
	}

	public void setMASTER_LIST_URL(String mASTER_LIST_URL) {
		MASTER_LIST_URL = mASTER_LIST_URL;
	}

	public String getRCHG_URL() {
		return RCHG_URL;
	}

	public void setRCHG_URL(String rCHG_URL) {
		RCHG_URL = rCHG_URL;
	}

	public String getWD_URL() {
		return WD_URL;
	}

	public String getSHOP_URL() {
		return SHOP_URL;
	}

	public void setSHOP_URL(String sHOP_URL) {
		SHOP_URL = sHOP_URL;
	}

	public String getPASSPORT_URL() {
		return PASSPORT_URL;
	}

	public void setPASSPORT_URL(String pASSPORT_URL) {
		PASSPORT_URL = pASSPORT_URL;
	}

	public void setWD_URL(String wD_URL) {
		WD_URL = wD_URL;
	}

	public String getCOUPON_URL() {
		return COUPON_URL;
	}

	public void setCOUPON_URL(String cOUPON_URL) {
		COUPON_URL = cOUPON_URL;
	}

	public String getTREAS_SHARE_URL() {
		return TREAS_SHARE_URL;
	}

	public void setTREAS_SHARE_URL(String tREAS_SHARE_URL) {
		TREAS_SHARE_URL = tREAS_SHARE_URL;
	}

	public String getAUTO_BID_URL() {
		return AUTO_BID_URL;
	}

	public void setAUTO_BID_URL(String aUTO_BID_URL) {
		AUTO_BID_URL = aUTO_BID_URL;
	}

	public String getGSB_INVEST_STREAM() {
		return GSB_INVEST_STREAM;
	}

	public void setGSB_INVEST_STREAM(String gSB_INVEST_STREAM) {
		GSB_INVEST_STREAM = gSB_INVEST_STREAM;
	}

	public String getGSB_INVEST_RECORD() {
		return GSB_INVEST_RECORD;
	}

	public void setGSB_INVEST_RECORD(String gSB_INVEST_RECORD) {
		GSB_INVEST_RECORD = gSB_INVEST_RECORD;
	}

	public String getFUND_CUSTODY_URL() {
		return FUND_CUSTODY_URL;
	}

	public void setFUND_CUSTODY_URL(String fUND_CUSTODY_URL) {
		FUND_CUSTODY_URL = fUND_CUSTODY_URL;
	}

	public String getUPDATE_PAY_PWD() {
		return UPDATE_PAY_PWD;
	}

	public void setUPDATE_PAY_PWD(String uPDATE_PAY_PWD) {
		UPDATE_PAY_PWD = uPDATE_PAY_PWD;
	}

	public String getGSB_BONUS_BILL() {
		return GSB_BONUS_BILL;
	}

	public void setGSB_BONUS_BILL(String gSB_BONUS_BILL) {
		GSB_BONUS_BILL = gSB_BONUS_BILL;
	}

	public String getGSB_DCB_INFO() {
		return GSB_DCB_INFO;
	}

	public void setGSB_DCB_INFO(String gSB_DCB_INFO) {
		GSB_DCB_INFO = gSB_DCB_INFO;
	}

	public String getGSB_XTB_INFO() {
		return GSB_XTB_INFO;
	}

	public void setGSB_XTB_INFO(String gSB_XTB_INFO) {
		GSB_XTB_INFO = gSB_XTB_INFO;
	}

	public String getGSB_JZB_INFO() {
		return GSB_JZB_INFO;
	}

	public void setGSB_JZB_INFO(String gSB_JZB_INFO) {
		GSB_JZB_INFO = gSB_JZB_INFO;
	}

	public String getINVITE_REWARD_URL() {
		return INVITE_REWARD_URL;
	}

	public void setINVITE_REWARD_URL(String iNVITE_REWARD_URL) {
		INVITE_REWARD_URL = iNVITE_REWARD_URL;
	}

	public String getOPEN_APP_IMG_URL() {
		return OPEN_APP_IMG_URL;
	}

	public void setOPEN_APP_IMG_URL(String oPEN_APP_IMG_URL) {
		OPEN_APP_IMG_URL = oPEN_APP_IMG_URL;
	}

	public String getOPEN_APP_DEST_URL() {
		return OPEN_APP_DEST_URL;
	}

	public void setOPEN_APP_DEST_URL(String oPEN_APP_DEST_URL) {
		OPEN_APP_DEST_URL = oPEN_APP_DEST_URL;
	}

	public String getUMP_URL() {
		return UMP_URL;
	}

	public void setUMP_URL(String uMP_URL) {
		UMP_URL = uMP_URL;
	}

	public String getAPP_URL() {
		return APP_URL;
	}

	public void setAPP_URL(String aPP_URL) {
		APP_URL = aPP_URL;
	}

	public String getGSB_RED_INVESTMENT() {
		return GSB_RED_INVESTMENT;
	}

	public void setGSB_RED_INVESTMENT(String gSB_RED_INVESTMENT) {
		GSB_RED_INVESTMENT = gSB_RED_INVESTMENT;
	}

	public String getGSB_BORROW_URL() {
		return GSB_BORROW_URL;
	}

	public void setGSB_BORROW_URL(String gSB_BORROW_URL) {
		GSB_BORROW_URL = gSB_BORROW_URL;
	}

	public String getMY_MESSAGE_URL() {
		return MY_MESSAGE_URL;
	}

	public void setMY_MESSAGE_URL(String mY_MESSAGE_URL) {
		MY_MESSAGE_URL = mY_MESSAGE_URL;
	}

	public String getMY_GRADE_URL() {
		return MY_GRADE_URL;
	}

	public void setMY_GRADE_URL(String mY_GRADE_URL) {
		MY_GRADE_URL = mY_GRADE_URL;
	}

	public String getABOUT_ME_URL() {
		return ABOUT_ME_URL;
	}

	public void setABOUT_ME_URL(String aBOUT_ME_URL) {
		ABOUT_ME_URL = aBOUT_ME_URL;
	}

	public String getSHARE_FOR_COOKIE() {
		return SHARE_FOR_COOKIE;
	}

	public void setSHARE_FOR_COOKIE(String sHARE_FOR_COOKIE) {
		SHARE_FOR_COOKIE = sHARE_FOR_COOKIE;
	}

	public String getREG_IMAGE_URL() {
		return REG_IMAGE_URL;
	}

	public void setREG_IMAGE_URL(String rEG_IMAGE_URL) {
		REG_IMAGE_URL = rEG_IMAGE_URL;
	}

	public String getLOGIN_IMAGE_URL() {
		return LOGIN_IMAGE_URL;
	}

	public void setLOGIN_IMAGE_URL(String lOGIN_IMAGE_URL) {
		LOGIN_IMAGE_URL = lOGIN_IMAGE_URL;
	}

	public String getCOUPON_EXPLAIN() {
		return COUPON_EXPLAIN;
	}

	public void setCOUPON_EXPLAIN(String cOUPON_EXPLAIN) {
		COUPON_EXPLAIN = cOUPON_EXPLAIN;
	}

	public String getEXCHANGE_EXPLAIN() {
		return EXCHANGE_EXPLAIN;
	}

	public void setEXCHANGE_EXPLAIN(String eXCHANGE_EXPLAIN) {
		EXCHANGE_EXPLAIN = eXCHANGE_EXPLAIN;
	}

	public String getGRADE_SCORE() {
		return GRADE_SCORE;
	}

	public void setGRADE_SCORE(String gRADE_SCORE) {
		GRADE_SCORE = gRADE_SCORE;
	}

	public String getARTICLE_SHARE() {
		return ARTICLE_SHARE;
	}

	public void setARTICLE_SHARE(String aRTICLE_SHARE) {
		ARTICLE_SHARE = aRTICLE_SHARE;
	}

	public String getMESS_NOTICE() {
		return MESS_NOTICE;
	}

	public void setMESS_NOTICE(String mESS_NOTICE) {
		MESS_NOTICE = mESS_NOTICE;
	}

	public String getREGISTER_SUCCESS() {
		return REGISTER_SUCCESS;
	}

	public void setREGISTER_SUCCESS(String rEGISTER_SUCCESS) {
		REGISTER_SUCCESS = rEGISTER_SUCCESS;
	}

	public String getREG_HEAD_IMG() {
		return REG_HEAD_IMG;
	}

	public void setREG_HEAD_IMG(String rEG_HEAD_IMG) {
		REG_HEAD_IMG = rEG_HEAD_IMG;
	}

	public String getMEDAL_RULE() {
		return MEDAL_RULE;
	}

	public void setMEDAL_RULE(String mEDAL_RULE) {
		MEDAL_RULE = mEDAL_RULE;
	}

	public String getREG_LABEL_INDEX() {
		return REG_LABEL_INDEX;
	}

	public void setREG_LABEL_INDEX(String rEG_LABEL_INDEX) {
		REG_LABEL_INDEX = rEG_LABEL_INDEX;
	}

	public String getREG_LABEL_SHARE() {
		return REG_LABEL_SHARE;
	}

	public void setREG_LABEL_SHARE(String rEG_LABEL_SHARE) {
		REG_LABEL_SHARE = rEG_LABEL_SHARE;
	}

}
