package com.gaoshoubang.ui.information.view;

import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/9.
 */

public interface InformationView extends BaseView {

	void afterGetMyInfo(SelfBean self);
}
