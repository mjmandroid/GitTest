package com.gaoshoubang.ui.registration.view;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface BindingView2 extends BaseView {
	void afterGetVertifyCode(String method);

	void afterCheckVertifyPhoneNum();

	void afterCheckRcmderCode(String result);

	void afterDoReg(LoginBean loginBean, String psw);
}
