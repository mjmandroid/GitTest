package com.gaoshoubang.ui.assessment.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AnswerBean;
import com.gaoshoubang.bean.RiskQuestionBean;
import com.gaoshoubang.ui.assessment.presenter.FinishAssessmentPresenterImpl;
import com.gaoshoubang.ui.assessment.view.FinishAssessmentView;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.RiskAssessmentUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import java.util.List;

/**
 * Created by lzx on 2017/7/3.
 */

public class FinishAssFragment extends BaseFragment<FinishAssessmentPresenterImpl> implements View.OnClickListener, FinishAssessmentView {

	private TextView mShowRiskNote;
	private List<AnswerBean> mSaveAnswers;
	private TextView mTvResultType;
	private TextView mTvResultDesc;
	private Button mBtnToInvest;
	private Button mBtnReDo;
	private List<RiskQuestionBean> mData;

	public static FinishAssFragment newInstance(int value) {
		FinishAssFragment fragment = new FinishAssFragment();
		Bundle args = new Bundle();
		args.putInt("value", value);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_finish_risk;
	}

	@Override
	protected void initView() {
		mShowRiskNote = ((TextView) contentView.findViewById(R.id.finish_risk_tv_show_risk_note));
		mTvResultType = ((TextView) contentView.findViewById(R.id.finish_risk_tv_result));
		mTvResultDesc = ((TextView) contentView.findViewById(R.id.finish_risk_tv_result_desc));
		mBtnToInvest = ((Button) contentView.findViewById(R.id.finish_risk_btn_to_invest));
		mBtnReDo = ((Button) contentView.findViewById(R.id.finish_risk_btn_redo));
	}

	@Override
	protected void initEvent() {
		mShowRiskNote.setOnClickListener(this);
		mBtnToInvest.setOnClickListener(this);
		mBtnReDo.setOnClickListener(this);
	}

	@Override
	protected void bindData() {

	}

	@Override
	protected void loadData() {
		if (getArguments() != null) {
			int value = getArguments().getInt("value");
			updateViewByValue(value);
			return;
		}
		mPresenter.postAnswers(mSaveAnswers);
	}

	@Override
	public FinishAssessmentPresenterImpl getPresenter() {
		return new FinishAssessmentPresenterImpl();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.finish_risk_tv_show_risk_note:
				Intent intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", "file:///android_asset/riskNote.html");
				intent.putExtra("type", "风险测评书");
				startActivity(intent);
				break;
			case R.id.finish_risk_btn_to_invest:
				Intent intentProduct = new Intent(getActivity(), MainActivity.class);
				intentProduct.putExtra("showProduct", true);
				startActivity(intentProduct);
				getActivity().finish();
				break;
			case R.id.finish_risk_btn_redo:
				TestRiskAssFragment testRiskAssFragment = new TestRiskAssFragment();
				testRiskAssFragment.setData(mData);
				testRiskAssFragment.clearState();
				FragmentTransaction transaction = getFragmentManager().beginTransaction();
				transaction.replace(R.id.body_container, testRiskAssFragment);
				transaction.commit();
				break;
		}
	}


	public void setAnswerData(List<AnswerBean> saveAnswers) {
		mSaveAnswers = saveAnswers;
	}

	@Override
	public void afterGetResult(Integer data) {
		// 保存分数,用于回显
		UserSharedPreferenceUtils.setRiskAssessmentScore(data);
		LogUtils.e("FinishAssFragment", "afterGetResult:" + data);
		updateViewByValue(data);

	}

	/*根据分数更新页面*/
	private void updateViewByValue(Integer data) {
		String assessmentType = RiskAssessmentUtils.getAssessmentType(data);
		mTvResultType.setText(assessmentType);
		mTvResultDesc.setText(getString(R.string.finish_risk_desc_prefix) + RiskAssessmentUtils.getAssessmentDesc(assessmentType, getActivity()));
	}

	public void setData(List<RiskQuestionBean> data) {
		mData = data;
	}
}
