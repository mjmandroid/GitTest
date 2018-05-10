package com.gaoshoubang.ui.common.fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AssetBean;
import com.gaoshoubang.bean.ChartItem;
import com.gaoshoubang.base.fragments.SimpleBaseFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * 总资产
 *
 * @author Cisco
 */
public class AssetAmountFragment extends SimpleBaseFragment implements OnClickListener {
	private PieChart mPieChart;
	private TextView totalAsset;// 总资产

	private LinearLayout dueInPrin_ly;
	private ImageView dueInPrinArrow;
	private LinearLayout dueInPrinDetails;
	private PieChart pieChartDueInPrin;
	private TextView ldFund;// 灵动
	private TextView dcFund;// 定存
	private TextView jzFund;// 净值
	private TextView haiyingbao;

	private TextView dueInPrin;// 待到账本息
	private TextView bal;// 闲置资金
	private TextView ungetFund;// 未领取赏金
	private TextView waitRecFund;// 待还借款总额

	private ArrayList<ChartItem> chartItem = new ArrayList<ChartItem>();
	private ArrayList<ChartItem> dueInPrinList = new ArrayList<ChartItem>();
	private RotateAnimation rotate;

	@Override
	public void onViewCreated(View view, Bundle bundle) {
		if (view != null) {
			mPieChart = (PieChart) view.findViewById(R.id.amount_piechart);
			totalAsset = (TextView) view.findViewById(R.id.amount_totalAsset);

//			dueInPrin_ly = (LinearLayout) view.findViewById(R.id.amount_dueInPrin_ly);
//			dueInPrinArrow = (ImageView) view.findViewById(R.id.asset_amount_arrow);
//			dueInPrinDetails = (LinearLayout) view.findViewById(R.id.amount_dueInPrin_details);
//			pieChartDueInPrin = (PieChart) view.findViewById(R.id.amount_dueInPrin_piechart);
//			ldFund = (TextView) view.findViewById(R.id.amount_ldFund);
//			dcFund = (TextView) view.findViewById(R.id.amount_dcFund);
//			jzFund = (TextView) view.findViewById(R.id.amount_jzFund);
//			haiyingbao = (TextView) view.findViewById(R.id.amount_haiyingbao);
//			haiyingbao = (TextView) view.findViewById(R.id.amount_haiyingbao);

			dueInPrin = (TextView) view.findViewById(R.id.amount_dueInPrin);
			bal = (TextView) view.findViewById(R.id.amount_bal);
			ungetFund = (TextView) view.findViewById(R.id.amount_ungetFund);
			waitRecFund = (TextView) view.findViewById(R.id.amount_waitRecFund);
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_asset_amount;
	}

	@Override
	protected void initView() {
//		dueInPrin_ly.setOnClickListener(this);
	}

	@Override
	protected void initEvent() {

	}

	/**
	 * 待到账本息暂时不需要点击事件
	 *
	 * @param
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			/*
			case R.id.amount_dueInPrin_ly:
				if (dueInPrinDetails.getVisibility() == View.GONE) {
					dueInPrinDetails.setVisibility(View.VISIBLE);

					rotate = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					rotate.setDuration(300);
					rotate.setFillAfter(true);
				}
				else {
					dueInPrinDetails.setVisibility(View.GONE);

					rotate = new RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					rotate.setDuration(300);
					rotate.setFillAfter(true);
				}
				dueInPrinArrow.startAnimation(rotate);
				break;
				*/

		}
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

	public void initData(AssetBean assetBean) {
		if (assetBean == null) {
			return;
		}

		DecimalFormat df = new DecimalFormat("#0.00");
		int per = 0;
		double mTotalAsset = assetBean.getTwo().getTotalAsset();
		double mDueInPrin = assetBean.getTwo().getDueInPrin();
		double mBal = assetBean.getTwo().getBal();
		double mUngetFund = assetBean.getTwo().getUngetFund();

		totalAsset.setText(df.format(mTotalAsset) + "元");
		dueInPrin.setText(df.format(mDueInPrin) + "元");
		bal.setText(df.format(mBal) + "元");
		ungetFund.setText(df.format(mUngetFund) + "元");

		waitRecFund.setText(df.format(assetBean.getTwo().getWaitRecFund()) + "元");

		chartItem.clear();
		if (mTotalAsset == 0) {
			chartItem.add(new ChartItem(100, 0xffe1e1e1));
		}
		else {
			if (mDueInPrin != 0) {
				per = (int) (mDueInPrin / mTotalAsset * 100);
				chartItem.add(new ChartItem(per == 0 ? 1 : per, 0xfff15353));// 待到账本息
			}
			if (mBal != 0) {
				per = (int) (mBal / mTotalAsset * 100);
				chartItem.add(new ChartItem(per == 0 ? 1 : per, 0xffffacac));// 闲置资金
			}
			if (mUngetFund != 0) {
				per = (int) (mUngetFund / mTotalAsset * 100);
				chartItem.add(new ChartItem(per == 0 ? 1 : per, 0xffff8a00));// 未领取赏金
			}
		}
		mPieChart.setVisibility(View.VISIBLE);
		showChart(mPieChart, getPieData(chartItem));

		// 待到账本息
		double ld = assetBean.getTwo().getLdFund();
		double jz = assetBean.getTwo().getJzFund();
		double dc = assetBean.getTwo().getDcFund();
		double hy = assetBean.getTwo().getHyFund();
		double all = ld + jz + dc + hy;

		dueInPrinList.clear();
		if (all == 0) {
			dueInPrinList.add(new ChartItem(100, 0xffe1e1e1));
		}
		else {
			if (ld != 0) {
				per = (int) (ld / all * 100);
				dueInPrinList.add(new ChartItem(per == 0 ? 1 : per, 0xff2183ca));// 灵动
			}
			if (dc != 0) {
				per = (int) (dc / all * 100);
				dueInPrinList.add(new ChartItem(per == 0 ? 1 : per, 0xff68b6f3));// 定存
			}
			if (jz != 0) {
				per = (int) (jz / all * 100);
				dueInPrinList.add(new ChartItem(per == 0 ? 1 : per, 0xff39ac6a));// 净值
			}
			if (hy != 0) {
				per = (int) (hy / all * 100);
				dueInPrinList.add(new ChartItem(per == 0 ? 1 : per, 0xff7034c9));// 海盈宝
			}
		}
//		pieChartDueInPrin.setVisibility(View.VISIBLE);
//		showChart(pieChartDueInPrin, getPieData(dueInPrinList));
//
//		ldFund.setText(Html.fromHtml("<font color='#a3a3a3'>" + "信托宝·灵动(元) " + "</font>" + df.format(ld)));
//		dcFund.setText(Html.fromHtml("<font color='#a3a3a3'>" + "信托宝·定存(元) " + "</font>" + df.format(dc)));
//		jzFund.setText(Html.fromHtml("<font color='#a3a3a3'>" + "信托宝·净值(元) " + "</font>" + df.format(jz)));
//		haiyingbao.setText(Html.fromHtml("<font color='#a3a3a3'>" + "海盈宝(元) " + "</font>" + df.format(hy)));

	}

	private void showChart(PieChart pieChart, PieData pieData) {
		pieChart.setHoleRadius(80f); // 半径
		pieChart.setDescription("");
		pieChart.setDrawSliceText(false);
		pieChart.setDrawCenterText(false); // 饼状图中间可以添加文字
		pieChart.setDrawHoleEnabled(true);
		pieChart.setRotationAngle(-90); // 初始旋转角度
		pieChart.setRotationEnabled(true); // 可以手动旋转
		pieChart.setUsePercentValues(false); // 显示成百分比
		// pieChart.setCenterText("Quarterly Revenue"); // 饼状图中间的文字
		// 设置数据
		pieChart.setData(pieData);
		pieChart.getLegend().setEnabled(false);
		pieChart.animateXY(1000, 1000); // 设置动画
	}

	private PieData getPieData(ArrayList<ChartItem> chartItemsList) {
		ArrayList<String> xValues = new ArrayList<String>();
		ArrayList<Entry> yValues = new ArrayList<Entry>();
		ArrayList<Integer> colors = new ArrayList<Integer>();
		for (int i = 0; i < chartItemsList.size(); i++) {
			xValues.add("");
			colors.add(chartItemsList.get(i).color);
			yValues.add(new Entry(chartItemsList.get(i).value, i));
		}
		// y轴的集合
		PieDataSet pieDataSet = new PieDataSet(yValues, "");
		pieDataSet.setSliceSpace(0f); // 设置个饼状图之间的距离
		pieDataSet.setColors(colors);

		// DisplayMetrics metrics = getResources().getDisplayMetrics();
		// float px = 2 * (metrics.densityDpi / 160f);
		pieDataSet.setSelectionShift(0); // 选中态多出的长度

		PieData pieData = new PieData(xValues, pieDataSet);
		pieData.setDrawValues(false);
		return pieData;
	}

}