package com.gaoshoubang.bean;

import com.gaoshoubang.bean.base.SuperResponse;

/**
 * Created by Administrator on 2017/5/8.
 */

public class SettingBean extends SuperResponse {

	private int hasOpenUmp = -1; // 是否已经开通联动优势(Union Mobile Pay)。1：未开通；2：已开通；
	// private int hasOpenAutoBid = -1;// 是否已开启自动投标。1：否；2：是；
	private int hasOpenBankCard = -1;// 是否开通银行卡。1：否；2：是；
	private String unBindCardUrl;// 绑卡url；
	private String bindCardUrl;// 用户已经绑定的银行卡信息url；


	public int getHasOpenUmp() {
		return hasOpenUmp;
	}

	public void setHasOpenUmp(int hasOpenUmp) {
		this.hasOpenUmp = hasOpenUmp;
	}


	public int getHasOpenBankCard() {
		return hasOpenBankCard;
	}

	public void setHasOpenBankCard(int hasOpenBankCard) {
		this.hasOpenBankCard = hasOpenBankCard;
	}

	public String getUnBindCardUrl() {
		return unBindCardUrl;
	}

	public void setUnBindCardUrl(String unBindCardUrl) {
		this.unBindCardUrl = unBindCardUrl;
	}

	public String getBindCardUrl() {
		return bindCardUrl;
	}

	public void setBindCardUrl(String bindCardUrl) {
		this.bindCardUrl = bindCardUrl;
	}
}
