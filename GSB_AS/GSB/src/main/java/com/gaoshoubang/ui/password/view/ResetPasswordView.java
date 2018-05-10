package com.gaoshoubang.ui.password.view;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/12.
 */

public interface ResetPasswordView extends BaseView {
	void afterLoginSuccess(LoginBean data);
}
