package com.gaoshoubang.ui.main.view;

import com.gaoshoubang.bean.base.TotalSelfBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/26.
 */

public interface MyView extends BaseView {
	void showErrorPage(int i);

	void afterMyData(TotalSelfBean totalSelfBean);
}
