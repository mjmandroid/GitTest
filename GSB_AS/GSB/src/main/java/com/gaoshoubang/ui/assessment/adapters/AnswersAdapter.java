package com.gaoshoubang.ui.assessment.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AnswerBean;
import com.gaoshoubang.bean.RiskOptionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2017/7/4.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {
	private List<RiskOptionBean> mList;
	private Context mContext;
	private OnItemClickListener mListener;

	public AnswersAdapter(Context context) {
		mContext = context;
		mList = new ArrayList<>();

	}

	public void updateData(List<RiskOptionBean> list) {
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public AnswersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(mContext)
				.inflate(R.layout.item_answer, parent,
						false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(AnswersAdapter.ViewHolder holder, final int position) {
		final RiskOptionBean bean = mList.get(position);
		//回显
		if (bean.isSelected) {
			holder.mAnswerTextView.setSelected(true);
		}
		else {
			holder.mAnswerTextView.setSelected(false);
		}
		holder.mAnswerTextView.setText(bean.getName());
		holder.mAnswerTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//处理回显
				clearState();
				bean.isSelected = true;
				if (mListener != null) {
					//保存答案
					//跳到下一题
					AnswerBean answerBean = new AnswerBean();
					answerBean.optionId = bean.getId();
					answerBean.questionId = bean.getQid();
					answerBean.value = bean.getValue();
					mListener.onItemClick(answerBean);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mList.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		private TextView mAnswerTextView;

		public ViewHolder(View itemView) {
			super(itemView);
			mAnswerTextView = ((TextView) itemView.findViewById(R.id.item_answer_textView));
		}
	}

	public void setOnItemClickListener(OnItemClickListener listener) {
		mListener = listener;
	}

	public interface OnItemClickListener {
		//选中题目时回调
		void onItemClick(AnswerBean answerBean);
	}

	public void clearState() {
		for (RiskOptionBean riskOptionBean : mList) {
			riskOptionBean.isSelected = false;
		}
	}
}
