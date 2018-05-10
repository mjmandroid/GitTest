package com.gaoshoubang.ui.information.view;

import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/9.
 */

public interface ModifyInfoView extends BaseView {
	void afterModifySuccess(String msg);

	void afterGetEmailCode();
}
