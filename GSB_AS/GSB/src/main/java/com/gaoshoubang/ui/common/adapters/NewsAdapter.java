
package com.gaoshoubang.ui.common.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 媒体报道
 */
public class NewsAdapter extends BaseAdapter {

	private Activity activity;
	private List<NewsBean> list;

	public NewsAdapter(List<NewsBean> list, Activity activity) {
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
			convertView = View.inflate(activity, R.layout.item_news, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		ImageLoader.getInstance().displayImage(list.get(position).getLogo_url(), viewHolder.img, new ImageLoaderUtils().findNewsOptions());
		viewHolder.title.setText(list.get(position).getTitle());
		viewHolder.content.setText(list.get(position).getCc());
		viewHolder.date.setText(list.get(position).getTime());
		if (position == list.size() - 1) {
			viewHolder.line.setVisibility(View.GONE);
		}
		else {
			viewHolder.line.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title;
		TextView content;
		TextView date;
		View line;

		public ViewHolder(View view) {
			img = (ImageView) view.findViewById(R.id.item_news_img);
			title = (TextView) view.findViewById(R.id.item_news_title);
			content = (TextView) view.findViewById(R.id.item_news_content);
			date = (TextView) view.findViewById(R.id.item_news_date);
			line = view.findViewById(R.id.item_news_line);
			view.setTag(this);
		}
	}

}
