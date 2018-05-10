package com.gaoshoubang.ui.registration.view;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface BindUserView extends BaseView {
	void afterLoginSuccess(LoginBean loginBean, String psw);
}
