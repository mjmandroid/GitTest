package com.gaoshoubang.ui.registration.view;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/9.
 */

public interface RegisterView extends BaseView {
	void afterGetMobileVerifyCode(String method);

	void changeMethod();

	void afterCheckMobileVerifyCode();


	void afterRegister(LoginBean loginBean, String mobile, String passwd);
}
