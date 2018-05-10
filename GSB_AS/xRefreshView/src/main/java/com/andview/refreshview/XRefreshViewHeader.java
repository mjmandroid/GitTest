package com.andview.refreshview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.xrefreshview.R;

public class XRefreshViewHeader extends LinearLayout implements IHeaderCallBack {
	private ViewGroup mContent;
	private ImageView headerIcon;
	private TextView mHintTextView;

	private String str = "双国资背景 发改委战略扶持";
	private boolean isStr = true;

	private RotateAnimation mRotateAnimation;

	public XRefreshViewHeader(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public XRefreshViewHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context context) {
		mContent = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.xrefreshview_header, this);

//		mLoadingView = ((LoadingView) findViewById(R.id.loading_view));
		//去掉完成刷新后的页面
//		finishView = (LinearLayout) findViewById(R.id.header_finish);
		headerIcon = (ImageView) findViewById(R.id.xrefreshview_header_icon);
		mHintTextView = (TextView) findViewById(R.id.xrefreshview_header_hint_textview);


//		mWaveDrawable = new WaveDrawable(context, R.drawable.header_icon);
		headerIcon.setImageDrawable(getResources().getDrawable(R.drawable.header_icon));
		mRotateAnimation = new RotateAnimation(0, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateAnimation.setInterpolator(new LinearInterpolator());//线性插值器,解决旋转停顿问题
		mRotateAnimation.setRepeatCount(Animation.INFINITE);
//		mRotateAnimation.setRepeatMode(Animation.RESTART);
		mRotateAnimation.setDuration(350);
		mRotateAnimation.setFillAfter(true);
		headerIcon.setAnimation(mRotateAnimation);
//		headerIcon.setImageDrawable(mWaveDrawable);
//		mWaveDrawable.setIndeterminate(true);
	}

	/**
	 * hide footer when disable pull load more
	 */
	public void hide() {
		setVisibility(View.GONE);
	}

	public void show() {
		setVisibility(View.VISIBLE);
	}

	@Override
	public void onStateNormal() {
		//默认状态,显示文字,隐藏图标
//		finishView.setVisibility(View.GONE);
//		mLoadingView.setVisibility(View.GONE);
		mHintTextView.setVisibility(View.VISIBLE);
		headerIcon.setVisibility(View.GONE);
//		mWaveDrawable.start();

		if (isStr) {
			str = "双国资背景 发改委战略扶持";
			isStr = false;
		}
		else {
			str = "风控体系完善 历史本息兑付率100%";
			isStr = true;
		}
		mHintTextView.setText(str);
	}

	@Override
	public void onStateReady() {
		/*headerIcon.setVisibility(View.GONE);
//		mLoadingView.start();
//		mLoadingView.setVisibility(View.VISIBLE);
		mHintTextView.setVisibility(View.VISIBLE);
//		mHintTextView.setText(str);*/
	}

	@Override
	public void onStateRefreshing() {
		/*
		*
		* 正在刷新,图标旋转,文字隐藏
		* */
//		mLoadingView.setVisibility(View.VISIBLE);
//		mLoadingView.start();
		headerIcon.setVisibility(View.VISIBLE);
		mHintTextView.setVisibility(View.GONE);
		headerIcon.startAnimation(mRotateAnimation);
		if (isStr) {
			str = "双国资背景 发改委战略扶持";
			isStr = false;
		}
		else {
			str = "风控体系完善 历史本息兑付率100%";
			isStr = true;
		}
		mHintTextView.setText(str);
	}

	@Override
	public void onStateFinish() {
		/*
		* 刷新完成显示文字,隐藏图标
		* */
//		finishView.setVisibility(View.VISIBLE);
		mRotateAnimation.cancel();
		headerIcon.clearAnimation();
		headerIcon.setVisibility(View.GONE);
		mHintTextView.setVisibility(View.VISIBLE);
//		mWaveDrawable.stop();
//		mLoadingView.stop();
		mHintTextView.setText(str);
		if (isStr) {
			str = "双国资背景 发改委战略扶持";
			isStr = false;
		}
		else {
			str = "风控体系完善 历史本息兑付率100%";
			isStr = true;
		}
	}

	@Override
	public void onHeaderMove(double offset, int offsetY, int deltaY) {

	}

	@Override
	public int getHeaderHeight() {
		return getMeasuredHeight();
	}

	public void setRefreshTime(long lastRefreshTime) {
		// 获取当前时间
		// Calendar mCalendar = Calendar.getInstance();
		// long refreshTime = mCalendar.getTimeInMillis();
		// long howLong = refreshTime - lastRefreshTime;
		// int minutes = (int) (howLong / 1000 / 60);
		// String refreshTimeText = null;
		// if (minutes < 1) {
		// refreshTimeText = "刚刚刷新";
		// } else if (minutes < 60) {
		// refreshTimeText = "%1s分钟之前刷新";
		// refreshTimeText = Utils.format(refreshTimeText, minutes);
		// } else if (minutes < 60 * 24) {
		// refreshTimeText = "%1s小时之前刷新";
		// refreshTimeText = Utils.format(refreshTimeText, minutes / 60);
		// } else {
		// refreshTimeText = "%1s天之前刷新";
		// refreshTimeText = Utils.format(refreshTimeText, minutes / 60 / 24);
		// }
		// mHeaderTimeTextView.setText(refreshTimeText);
	}
}
