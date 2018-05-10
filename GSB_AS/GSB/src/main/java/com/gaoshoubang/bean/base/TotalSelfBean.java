package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.SelfBean;

/**
 * Created by Administrator on 2017/5/8.
 */

public class TotalSelfBean extends SuperResponse {
	public SelfBean self;
	public String isShowModifyPasswd;

	public SelfBean getSelf() {
		return self;
	}

	public void setSelf(SelfBean self) {
		this.self = self;
	}

	public String getIsShowModifyPasswd() {
		return isShowModifyPasswd;
	}

	public void setIsShowModifyPasswd(String isShowModifyPasswd) {
		this.isShowModifyPasswd = isShowModifyPasswd;
	}
}
