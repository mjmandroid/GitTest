package com.gaoshoubang.ui.common.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.MessageBean;
import com.gaoshoubang.util.DisplayUtil;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
	private Activity activity;
	private List<MessageBean> list;

	public MessageAdapter(List<MessageBean> list, Activity activity) {
		this.list = list;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(activity, R.layout.item_message, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		viewHolder.date.setText(list.get(position).getDate());
		viewHolder.title.setText(list.get(position).getTitle());
		viewHolder.content.setText(list.get(position).getCc());
		viewHolder.type.setText(list.get(position).getType_name());

		if (list.get(position).getTitle().equals("")) {
			viewHolder.title.setVisibility(View.GONE);
		}
		else {
			viewHolder.title.setVisibility(View.VISIBLE);
		}

		if (list.get(position).getUrl().equals("")) {
			viewHolder.details.setVisibility(View.GONE);
		}
		else {
			viewHolder.details.setVisibility(View.VISIBLE);
		}

		if (position == list.size() - 1) {
			convertView.setPadding(0, 0, 0, DisplayUtil.dip2px(activity, 50));
		}
		else {
			convertView.setPadding(0, 0, 0, 0);
		}
		return convertView;
	}

	class ViewHolder {
		TextView date;
		TextView title;
		TextView content;
		TextView type;
		TextView details;

		public ViewHolder(View view) {
			date = (TextView) view.findViewById(R.id.item_message_date);
			title = (TextView) view.findViewById(R.id.item_message_title);
			content = (TextView) view.findViewById(R.id.item_message_content);
			type = (TextView) view.findViewById(R.id.item_message_type);
			details = (TextView) view.findViewById(R.id.item_message_details);
			view.setTag(this);
		}
	}

}