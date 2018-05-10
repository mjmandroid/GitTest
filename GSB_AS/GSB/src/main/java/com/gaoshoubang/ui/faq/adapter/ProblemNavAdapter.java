package com.gaoshoubang.ui.faq.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.Problem;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ProblemNavAdapter extends BaseAdapter {
	private Activity activity;
	private List<Problem> problem;
	private int isSelect = 0;

	private ImageLoaderUtils imageLoaderOptionsUtil;

	public ProblemNavAdapter(Activity activity, List<Problem> problem) {
		this.activity = activity;
		this.problem = problem;
		imageLoaderOptionsUtil = new ImageLoaderUtils();
	}

	public void setIsSelect(int isSelect) {
		this.isSelect = isSelect;
	}

	@Override
	public int getCount() {
		return problem.size();
	}

	@Override
	public Object getItem(int position) {
		return problem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(activity, R.layout.item_problem_nav, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();

		ImageLoader.getInstance().displayImage(problem.get(position).getLogo(), viewHolder.navImg, imageLoaderOptionsUtil.problemOptions());
		if (isSelect == position) {
			viewHolder.navTitle.setTextColor(Color.parseColor("#da4415"));
		}
		else {
			viewHolder.navTitle.setTextColor(Color.parseColor("#666666"));
		}
		viewHolder.navTitle.setText(problem.get(position).getName());
		return convertView;
	}

	class ViewHolder {
		private ImageView navImg;
		private TextView navTitle;

		public ViewHolder(View view) {
			navImg = (ImageView) view.findViewById(R.id.problem_nav_img);
			navTitle = (TextView) view.findViewById(R.id.problem_nav_title);
			view.setTag(this);
		}
	}

}
