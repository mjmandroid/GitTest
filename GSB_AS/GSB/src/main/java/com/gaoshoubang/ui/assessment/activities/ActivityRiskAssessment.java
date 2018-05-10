package com.gaoshoubang.ui.assessment.activities;

import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.RiskQuestionBean;
import com.gaoshoubang.ui.assessment.presenter.RiskAssessmentPresenterImpl;
import com.gaoshoubang.ui.assessment.view.RiskAssessmentView;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.assessment.fragments.FinishAssFragment;
import com.gaoshoubang.ui.assessment.fragments.TestRiskAssFragment;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import java.util.List;

/**
 * Created by lzx on 2017/7/3.
 * 风险测评
 */

public class ActivityRiskAssessment extends BaseActivity<RiskAssessmentPresenterImpl> implements RiskAssessmentView {


	private FrameLayout mFlContainer;

	@Override
	protected RiskAssessmentPresenterImpl getPresenter() {
		return new RiskAssessmentPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_risk_assessment;
	}

	@Override
	protected void initView() {
//		mTvTips = ((TextView) findViewById(R.id.activity_risk_ass_tv_tips));
		mFlContainer = (FrameLayout) findViewById(R.id.body_container);
	}

	@Override
	protected void initEvent() {


	}

	@Override
	protected void loadData() {
		mPresenter.getQuestionList();
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void afterGetQuestionList(List<RiskQuestionBean> data) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		//处理页面跳转
		int riskAssessmentScore = UserSharedPreferenceUtils.getRiskAssessmentScore();
		if (riskAssessmentScore > 0) {
			FinishAssFragment finishAssFragment = FinishAssFragment.newInstance(riskAssessmentScore);
			finishAssFragment.setData(data);
			transaction.replace(R.id.body_container, finishAssFragment);
		}
		else {
			TestRiskAssFragment testRiskAssFragment = new TestRiskAssFragment();
			testRiskAssFragment.setData(data);
			transaction.replace(R.id.body_container, testRiskAssFragment);
		}
		transaction.commit();

	}
}
