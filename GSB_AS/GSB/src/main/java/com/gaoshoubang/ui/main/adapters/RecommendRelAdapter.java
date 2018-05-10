
package com.gaoshoubang.ui.main.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.RecommendRel;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页推荐
 */
public class RecommendRelAdapter extends BaseAdapter {

	private Context mContext;
	private List<RecommendRel> mList = new ArrayList<>();

	public RecommendRelAdapter(Context context) {
		mContext = context;
	}

	public void updateData(List<RecommendRel> list) {
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
			convertView = View.inflate(mContext, R.layout.item_recommend, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		ImageLoader.getInstance().displayImage(mList.get(position).getLogo_url(), viewHolder.rmImg, new ImageLoaderUtils().recommendItemOptions());
		viewHolder.rmTitle.setText(mList.get(position).getTitle());

		if (mList.get(position).getNums() != null && !mList.get(position).getNums().equals("0")) {
			viewHolder.rmNums.setText(mList.get(position).getNums());
			viewHolder.rmNums.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.rmNums.setVisibility(View.GONE);
		}

		if (position + 1 == mList.size()) {
			viewHolder.line.setVisibility(View.GONE);
		}
		else {
			viewHolder.line.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	static class ViewHolder {
		ImageView rmImg;
		TextView rmTitle;
		TextView rmNums;
		View line;

		public ViewHolder(View view) {
			rmImg = (ImageView) view.findViewById(R.id.rm_img);
			rmTitle = (TextView) view.findViewById(R.id.rm_title);
			rmNums = (TextView) view.findViewById(R.id.rm_nums);
			line = view.findViewById(R.id.rm_line);
			view.setTag(this);
		}
	}

}
