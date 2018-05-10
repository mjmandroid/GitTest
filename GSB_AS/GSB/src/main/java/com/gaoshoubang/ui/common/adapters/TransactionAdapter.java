package com.gaoshoubang.ui.common.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.TransactionBean;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends BaseAdapter {
	private Context mContext;
	private List<TransactionBean> mList;

	public TransactionAdapter(Context context) {
		mList = new ArrayList<>();
		mContext = context;
	}

	public void updateData(List<TransactionBean> list) {
		mList.clear();//加载更多,不需要清空
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_transaction_record, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		viewHolder.date.setText(mList.get(position).getDate());
		viewHolder.title.setText(mList.get(position).getTypeName());
		viewHolder.money.setText(mList.get(position).getAmount());
		viewHolder.freeMoney.setText(mList.get(position).getBal());

		// 1 负2正
		if (mList.get(position).getT() == 2) {
			viewHolder.money.setTextColor(0xff4cd964);
		}
		else {
			viewHolder.money.setTextColor(0xfff15353);
		}

		if (position == 0) {
			viewHolder.topView.setVisibility(View.INVISIBLE);
		}
		else {
			viewHolder.topView.setVisibility(View.VISIBLE);
		}

		if (position == mList.size() - 1) {
			viewHolder.bottomView.setVisibility(View.INVISIBLE);
		}
		else {
			viewHolder.bottomView.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView date;
		TextView title;
		TextView money;
		TextView freeMoney;
		View topView;
		View bottomView;

		public ViewHolder(View view) {
			date = (TextView) view.findViewById(R.id.record_date);
			title = (TextView) view.findViewById(R.id.record_title);
			money = (TextView) view.findViewById(R.id.record_money);
			freeMoney = (TextView) view.findViewById(R.id.record_free_money);
			topView = view.findViewById(R.id.record_line_top);
			bottomView = view.findViewById(R.id.record_line_bottom);
			view.setTag(this);
		}
	}

}