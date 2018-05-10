package com.gaoshoubang.ui.faq.view;

import com.gaoshoubang.bean.Problem;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface CommonProView extends BaseView {
	void afterGetHelp(List<Problem> problems);
}
