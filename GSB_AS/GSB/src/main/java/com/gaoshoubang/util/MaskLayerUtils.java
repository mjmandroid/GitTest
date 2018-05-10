package com.gaoshoubang.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gaoshoubang.R;

/**
 * 蒙版
 *
 * @author Administrator
 */
public class MaskLayerUtils {
	private Activity activity;
	private WindowManager windowManager;
	private View view;

	public MaskLayerUtils(Activity activity) {
		this.activity = activity;
	}

	public void showMaskLayer(int viewSource) {
		view = LayoutInflater.from(activity).inflate(viewSource, null);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.TYPE_APPLICATION_PANEL, PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(view, lp);
	}

	/**
	 * 显示蒙版
	 *
	 * @param viewSource
	 */
	public void showMaskLayer(int viewSource, OnClickListener onClickListener) {
		view = LayoutInflater.from(activity).inflate(viewSource, null);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.TYPE_APPLICATION_PANEL, PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(view, lp);
		View maskView = view.findViewById(R.id.mask_view);
		maskView.setOnClickListener(onClickListener);
	}

	public void showMaskLayer(int viewSource, View relativeView, int viewHeight, OnClickListener onClickListener) {
		view = LayoutInflater.from(activity).inflate(viewSource, null);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.TYPE_APPLICATION_PANEL, PixelFormat.TRANSLUCENT);
		windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
		windowManager.addView(view, lp);
		View maskView = view.findViewById(R.id.mask_view);
		View maskSource = view.findViewById(R.id.mask_source);

		int[] location = new int[2];
		relativeView.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
		// maskSource.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
		Log.i("---------------------", "location[1]:" + location[1]);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) maskSource.getLayoutParams();
		if (viewHeight != 0) {
			params.setMargins(0, location[1] - viewHeight, 0, 0);// 通过自定义坐标来放置你的控件
		}
		else {
			params.setMargins(0, location[1], 0, 0);
		}
		maskSource.setLayoutParams(params);

		maskView.setOnClickListener(onClickListener);
	}

	public View getMaskView() {
		return view;
	}

	public void removeView() {
		windowManager.removeView(view);
	}

}
