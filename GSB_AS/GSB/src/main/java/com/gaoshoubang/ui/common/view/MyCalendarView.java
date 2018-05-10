package com.gaoshoubang.ui.common.view;

import com.gaoshoubang.bean.CalenderBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface MyCalendarView extends BaseView {
	void afterGetRecMoneyDay(List<CalenderBean> calenderBean);
}
