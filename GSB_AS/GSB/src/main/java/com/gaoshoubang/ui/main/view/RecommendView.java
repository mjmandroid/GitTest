package com.gaoshoubang.ui.main.view;

import com.gaoshoubang.bean.RecommendBean;
import com.gaoshoubang.bean.RecommendGongHuo;
import com.gaoshoubang.bean.RelWindowBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by lzx on 2017/6/23.
 */

public interface RecommendView extends BaseView {
	void afterGetRecommendData(RecommendBean recommendBean);

	void afterGetActivityAndNoticeData(RecommendGongHuo data);

	void afterGetRelWindowData(List<RelWindowBean> data);
}
