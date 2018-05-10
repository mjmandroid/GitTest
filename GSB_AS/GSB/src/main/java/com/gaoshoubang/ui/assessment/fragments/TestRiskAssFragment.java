package com.gaoshoubang.ui.assessment.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AnswerBean;
import com.gaoshoubang.bean.RiskOptionBean;
import com.gaoshoubang.bean.RiskQuestionBean;
import com.gaoshoubang.ui.assessment.adapters.AnswersAdapter;
import com.gaoshoubang.widget.TabTipsView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2017/7/3.
 */

public class TestRiskAssFragment extends Fragment implements AnswersAdapter.OnItemClickListener {
	private TextView mQuestion;
	private TabTipsView mTabTipsView;
	private RecyclerView mRecyclerView;
	private AnswersAdapter mAnswersAdapter;
	private List<RiskQuestionBean> mData;
	private List<AnswerBean> mSaveAnswers;
	private View mTips;
	private ChangePageListener mListener;
	private View mPreQuestion;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_test_risk, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView(view);
		initEvent(view);
		initData();

	}

	private void initData() {
		mSaveAnswers = new ArrayList<>();
		//初始化题目数量
		mTabTipsView.setCount(mData.size());

		mAnswersAdapter.updateData(mData.get(getIndex()).getOptions());
		mQuestion.setText(mData.get(getIndex()).getQuestion());
		updateView();
	}

	private void initEvent(View view) {
		mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()) {
			@Override
			public boolean canScrollVertically() {
				//禁止滑动
				return false;
			}
		});
		mAnswersAdapter = new AnswersAdapter(getActivity());
		mRecyclerView.setAdapter(mAnswersAdapter);
		view.findViewById(R.id.pre_question).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//上一题
				//更新标签
				mTabTipsView.preIndex();
				mQuestion.setText(mData.get(getIndex()).getQuestion());
				mAnswersAdapter.updateData(mData.get(getIndex()).getOptions());
				updateView();

			}
		});
		mAnswersAdapter.setOnItemClickListener(this);
	}

	private void initView(View view) {
		mQuestion = ((TextView) view.findViewById(R.id.question));
		mRecyclerView = ((RecyclerView) view.findViewById(R.id.answer_recyclerView));
		mTabTipsView = ((TabTipsView) view.findViewById(R.id.tabTipsView));
		mTips = view.findViewById(R.id.activity_risk_ass_tv_tips);
		mPreQuestion = view.findViewById(R.id.pre_question);

	}


	public void setData(List<RiskQuestionBean> data) {
		mData = data;

	}


	/*
	* 每个选项的点击事件
	* */
	@Override
	public void onItemClick(AnswerBean answerBean) {
/*
		//拿到之前的截图
		getView().setDrawingCacheEnabled(true);
		Bitmap oldBitmap = getView().getDrawingCache();
*/


		//更新标签
		mTabTipsView.nextIndex();
		mQuestion.setText(mData.get(getIndex()).getQuestion());
//		updateIndex();
		int index = getIndex();
		if (index < mSaveAnswers.size()) {
			//更新答案
			mSaveAnswers.set(index, answerBean);
		}
		else {
			//保存答案
			mSaveAnswers.add(answerBean);
		}

		//已完成答卷
		if (finishTestRiskAssessment()) return;


		//更新题目
		mAnswersAdapter.updateData(mData.get(index).getOptions());

		//显示第一页的提示
		updateView();
	/*	//拿到之后的截图
		getView().setDrawingCacheEnabled(false);
		getView().setDrawingCacheEnabled(true);
		Bitmap newBitmap = getView().getDrawingCache();
		if (mListener != null) {
			mListener.next(oldBitmap,newBitmap);
		}*/

	}

	/*
	* 完成问卷,跳转页面
	* */
	private boolean finishTestRiskAssessment() {
		if (mSaveAnswers.size() >= mData.size()) {
			//跳转完成页
			FinishAssFragment finishAssFragment = new FinishAssFragment();
			finishAssFragment.setData(mData);
			finishAssFragment.setAnswerData(mSaveAnswers);
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.setCustomAnimations(R.anim.right_enter, R.anim.left_exit);
			transaction.replace(R.id.body_container, finishAssFragment);
			transaction.commit();
			return true;
		}
		return false;
	}

	private void updateView() {
		if (getIndex() == 0) {
			mTips.setVisibility(View.VISIBLE);
			mPreQuestion.setVisibility(View.GONE);
		}
		else {
			mTips.setVisibility(View.GONE);
			mPreQuestion.setVisibility(View.VISIBLE);
		}
//		getView().invalidate();

	}


	public int getIndex() {
		return mTabTipsView.getIndex() - 1;
	}

	public void clearState() {
		if (mData != null) {
			for (RiskQuestionBean riskQuestionBean : mData) {
				if (riskQuestionBean.getOptions() == null) continue;
				for (RiskOptionBean riskOptionBean : riskQuestionBean.getOptions()) {
					riskOptionBean.isSelected = false;
				}
			}
		}
	}

	public void setChangePageListener(ChangePageListener listener) {
		mListener = listener;
	}

	public interface ChangePageListener {
		void pre();

		void next(Bitmap oldBitmap, Bitmap newBitmap);
	}
}
