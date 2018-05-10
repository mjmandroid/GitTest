package com.gaoshoubang.ui.registration.view;

import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface BindingView extends BaseView {
	void afterCheckNum(CheckMobileResponse result);
}
