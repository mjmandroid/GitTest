package com.gaoshoubang.ui.login.view;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/8.
 */

public interface LoginView extends BaseView {
	void afterGetMobileVerifyCode();

	void afterCheckMobile(CheckMobileResponse registerBean);

	void afterLoginSuccess(LoginBean loginBean, Tuser tuser);

	void handleOtherErr(int code, String msg, Tuser tuser);

	void afterLoginInvalidate();
}
