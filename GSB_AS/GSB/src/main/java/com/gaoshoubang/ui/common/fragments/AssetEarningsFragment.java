package com.gaoshoubang.ui.common.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
 * 累计收益
 *
 * @author Cisco
 */
public class AssetEarningsFragment extends SimpleBaseFragment {
	private PieChart mPieChart;
	private TextView accumInIntst;
	private TextView invest;
	private TextView people;
	private TextView hongbao;
	private TextView recInPrin;
	private TextView dueInIntst;

	private ArrayList<ChartItem> chartItemsList = new ArrayList<ChartItem>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_asset_earnings;
	}

	@Override
	protected void initView() {
		if (contentView != null) {
			mPieChart = (PieChart) contentView.findViewById(R.id.earnings_chart);
			accumInIntst = (TextView) contentView.findViewById(R.id.earnings_accuminintst);
			invest = (TextView) contentView.findViewById(R.id.earnings_invest);
			people = (TextView) contentView.findViewById(R.id.earnings_people);
			hongbao = (TextView) contentView.findViewById(R.id.earnings_hongbao);
			recInPrin = (TextView) contentView.findViewById(R.id.earnings_recInPrin);
			dueInIntst = (TextView) contentView.findViewById(R.id.earnings_dueInIntst);
		}
	}

	@Override
	protected void initEvent() {

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

		double ok = assetBean.getOne().getOkFund();
		double red = assetBean.getOne().getRedFund();
		double friend = assetBean.getOne().getFriendFund();
		double all = ok + red + friend;
		int per = 0;

		chartItemsList.clear();
		if (all == 0) {
			chartItemsList.add(new ChartItem(100, 0xffe1e1e1));
		}
		else {
			if (ok != 0) {
				per = (int) (ok / all * 100);
				chartItemsList.add(new ChartItem(per == 0 ? 1 : per, 0xff2183ca));// 投资收益
			}
			if (friend != 0) {
				per = (int) (friend / all * 100);
				chartItemsList.add(new ChartItem(per == 0 ? 1 : per, 0xff68b6f3));// 人脉收益
			}
			if (red != 0) {
				per = (int) (red / all * 100);
				chartItemsList.add(new ChartItem(per == 0 ? 1 : per, 0xff9691ff));// 红包收益
			}
		}
		mPieChart.setVisibility(View.VISIBLE);
		showChart(mPieChart, getPieData(chartItemsList));

		DecimalFormat df = new DecimalFormat("#0.00");
		accumInIntst.setText(df.format(assetBean.getOne().getAccumInIntst()) + "元");
		invest.setText(df.format(ok) + "元");
		people.setText(df.format(friend) + "元");
		hongbao.setText(df.format(red) + "元");

		recInPrin.setText(df.format(assetBean.getOne().getRecInPrin()) + "元");
		dueInIntst.setText(df.format(assetBean.getOne().getDueInIntst()) + "元");
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