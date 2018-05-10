package com.gaoshoubang.ui.assessment.view;

import com.gaoshoubang.bean.RiskQuestionBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by lzx on 2017/7/3.
 */

public interface RiskAssessmentView extends BaseView {
	void afterGetQuestionList(List<RiskQuestionBean> data);
}
