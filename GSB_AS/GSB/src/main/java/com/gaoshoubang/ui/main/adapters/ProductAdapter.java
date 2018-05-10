package com.gaoshoubang.ui.main.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.util.DateUtils;
import com.gaoshoubang.util.LogUtils;

import java.util.List;

public class ProductAdapter extends BaseAdapter {
	private Context mContext;
	private List<ProductBean> list;
	private ProductBean productBean;

	public ProductAdapter(List<ProductBean> list, Context context) {
		this.list = list;
		this.mContext = context;

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
			convertView = View.inflate(mContext, R.layout.item_product, null);
			new ViewHolder(convertView);
		}
		ViewHolder viewHolder = (ViewHolder) convertView.getTag();
		productBean = list.get(position);
		//标题字体颜色改变
		viewHolder.title.setTextColor(0xff8f8f8f);
		viewHolder.tag1.setBackgroundResource(R.drawable.shape_tag_bg_1);
		viewHolder.tag2.setBackgroundResource(R.drawable.shape_tag_bg_1);
		viewHolder.tag3.setBackgroundResource(R.drawable.shape_tag_bg_1);
		viewHolder.tag1.setTextColor(0xff4076d0);
		viewHolder.tag2.setTextColor(0xff4076d0);
		viewHolder.tag3.setTextColor(0xff4076d0);
		viewHolder.intstRate.setTextColor(0xffde4746);
		viewHolder.intstRatePer.setTextColor(0xffde4746);
		viewHolder.term.setTextColor(0xff000000);
		viewHolder.remainAmt.setTextColor(0xff000000);
		if (position + 1 >= list.size()) {
			viewHolder.mDivider.setVisibility(View.GONE);
		}
		if (productBean.getInittype() == 2) {
			viewHolder.mTermDescTv.setText("锁定期");
		}
		else {
			viewHolder.mTermDescTv.setText("产品期限");
		}

		viewHolder.title.setText(productBean.getTitle());

		if (productBean.getTerm().contains("天")) {
//			SpannableString spannableString = new SpannableString(productBean.getTerm());
//			spannableString.setSpan(new AbsoluteSizeSpan(23));

			int dayIndex = productBean.getTerm().indexOf("天");
			String strTerm = productBean.getTerm();
			String term = strTerm.substring(0, dayIndex) + "<small>" + strTerm.substring(dayIndex, dayIndex + 1) + "</small>";
			viewHolder.term.setText(Html.fromHtml(term));
		}
		else if (productBean.getTerm().contains("个月")) {
			int monthIndex = productBean.getTerm().indexOf("个");
			String strTerm = productBean.getTerm();
			String term = strTerm.substring(0, monthIndex) + "<small>" + strTerm.substring(monthIndex, monthIndex + 2) + "</small>";
			viewHolder.term.setText(Html.fromHtml(term));
		}
		else {
			viewHolder.term.setText("" + productBean.getTerm());
		}


		if (productBean.getRate().contains("+")) {
			int rateIndex = productBean.getRate().indexOf("+");
			String strRate = productBean.getRate();
			String rate = strRate.substring(0, rateIndex) + "<small>" + strRate.substring(rateIndex, strRate.length()) + "</small>";
			viewHolder.intstRate.setText(Html.fromHtml(rate));
		}
		else {
			viewHolder.intstRate.setText(productBean.getRate());
		}

		if (!"".equals(productBean.getRedTag())) {
			viewHolder.tag1.setText(productBean.getRedTag());
			viewHolder.tag1.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.tag1.setVisibility(View.GONE);
		}
		if (!"".equals(productBean.getTag())) {
			viewHolder.tag2.setText(productBean.getTag());
			viewHolder.tag2.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.tag2.setVisibility(View.GONE);
		}
		if (!"".equals(productBean.getTag2())) {
			viewHolder.tag3.setText(productBean.getTag2());
			viewHolder.tag3.setVisibility(View.VISIBLE);
		}
		else {
			viewHolder.tag3.setVisibility(View.GONE);
		}

		switch (productBean.getFlag()) {
			case 1:
				if (productBean.getType() == 116) {
					// 定存宝
					viewHolder.remainAmtType.setText("已投人数");
					viewHolder.remainAmt.setText(productBean.getBuyers() + "人");
				}
				else {
					// 信托宝 净值宝
					viewHolder.remainAmtType.setText("剩余可投");
					viewHolder.remainAmt.setText(productBean.getOverplus() + "元");
				}
				if (productBean.getPayTime() > productBean.getServerTime()) {
					LogUtils.e("ProductAdapter", "getView:预售" + productBean.getTitle());
					viewHolder.remainAmt.setText(DateUtils.getDateToString(productBean.getPayTime()));

					viewHolder.remainAmtType.setText("发售时间");
				}
				break;
			default:

				viewHolder.remainAmtType.setText("剩余可投");
				viewHolder.remainAmt.setText("0元");
				break;
		}
	/*	ObjectAnimator animator = ObjectAnimator.ofFloat(convertView, "alpha", 0f, 1f);
		animator.setDuration(1000);
		animator.setInterpolator(new LinearInterpolator());
		animator.start();*/
		return convertView;
	}


	class ViewHolder {
		View mDivider;
		TextView mTermDescTv;
		TextView title;
		TextView tag1;
		TextView tag2;
		TextView tag3;
		TextView intstRate;
		TextView intstRatePer;
		TextView term;
		TextView remainAmtType;
		TextView remainAmt;
//		ImageView soldout;

		public ViewHolder(View view) {
			title = (TextView) view.findViewById(R.id.product_title);
			tag1 = (TextView) view.findViewById(R.id.product_title_tag1);
			tag2 = (TextView) view.findViewById(R.id.product_title_tag2);
			tag3 = (TextView) view.findViewById(R.id.product_title_tag3);
			intstRate = (TextView) view.findViewById(R.id.product_intstRate);
			intstRatePer = (TextView) view.findViewById(R.id.product_intstRate_per);
			mTermDescTv = ((TextView) view.findViewById(R.id.product_term_desc_tv));
			term = (TextView) view.findViewById(R.id.product_term);
			remainAmtType = (TextView) view.findViewById(R.id.product_remainAmt_type);
			remainAmt = (TextView) view.findViewById(R.id.product_remainAmt);
			mDivider = view.findViewById(R.id.divider_line);

			Typeface typeFace = Typeface.createFromAsset(mContext.getAssets(), "fonts/PingFangBold.ttf");
			intstRate.setTypeface(typeFace);
			term.setTypeface(typeFace);
			remainAmt.setTypeface(typeFace);
			view.setTag(this);
		}
	}

}