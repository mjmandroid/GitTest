
package com.gaoshoubang.ui.main.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现页-媒体报道
 */
public class FindNewsAdapter extends BaseAdapter {

	private Context mContext;
	private List<NewsBean> mList;
	private int padding;

	public FindNewsAdapter(Context context) {
		mContext = context;
		mList = new ArrayList<>();
		padding = DisplayUtil.dip2px(context, 1);
	}

	public void updateData(List<NewsBean> list) {
		if (list == null) {
			return;
		}
		mList.clear();
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList.size();
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
			convertView = View.inflate(mContext, R.layout.item_find_news, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		ImageLoader.getInstance().displayImage(mList.get(position).getLogo_url(), viewHolder.img, new ImageLoaderUtils().findNewsOptions());
		viewHolder.title.setText(mList.get(position).getTitle());
		viewHolder.date.setText(mList.get(position).getTime());
		if (position == mList.size() - 1) {
			viewHolder.line.setVisibility(View.GONE);
		}
		else {
			viewHolder.line.setVisibility(View.VISIBLE);
		}

		if (position == mList.size() - 1) {
			viewHolder.itemBg.setBackgroundResource(R.drawable.shape_find_news_bg2);
			viewHolder.itemBg.setPadding(padding, 0, padding, 3 * padding);
		}
		else {
			viewHolder.itemBg.setBackgroundResource(R.drawable.shape_find_news_bg);
			viewHolder.itemBg.setPadding(padding, 0, padding, 0);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title;
		TextView date;
		View line;
		LinearLayout itemBg;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.item_find_img);
			title = (TextView) view.findViewById(R.id.item_find_title);
			date = (TextView) view.findViewById(R.id.item_find_date);
			line = view.findViewById(R.id.item_find_line);
			itemBg = (LinearLayout) view.findViewById(R.id.find_item_news_bg);
			view.setTag(this);
		}
	}

}
