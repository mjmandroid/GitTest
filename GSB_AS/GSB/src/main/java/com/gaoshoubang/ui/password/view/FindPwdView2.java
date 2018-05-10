package com.gaoshoubang.ui.password.view;

import com.gaoshoubang.base.view.BaseView;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface FindPwdView2 extends BaseView {
	void afterGetMobileVerifyCode(String method);

	void afterResetPSW();

	void receiveErrGetVerifyCode();
}
