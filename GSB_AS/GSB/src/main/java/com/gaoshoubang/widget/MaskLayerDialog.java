package com.gaoshoubang.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;

import com.gaoshoubang.R;

/**
 * 加载dialog
 */
public class MaskLayerDialog extends Dialog {
	private Context context;

	public MaskLayerDialog(Context context, int layout) {
		super(context, R.style.CustomProgressDialog);
		setContentView(layout);
		this.context = context;

		LayoutParams lay = getWindow().getAttributes();
		setParams(lay);
		setCancelable(true);
		setCanceledOnTouchOutside(true);

	}

	private void setParams(LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels - rect.top;
		lay.width = dm.widthPixels;
	}

	/**
	 * 蒙版点击
	 *
	 * @param onClickListener
	 */
	public void setMaskOnClickListener(View.OnClickListener onClickListener) {
		View maskView = findViewById(R.id.mask_view);
		maskView.setOnClickListener(onClickListener);
	}

	/**
	 * 指定控件位置显示
	 *
	 * @param relativeView
	 */
	public void setMaskLocation(View relativeView) {
		View maskSource = findViewById(R.id.mask_source);
		int[] location = new int[2];
		relativeView.getLocationInWindow(location); // 获取在当前窗口内的绝对坐标
		// maskSource.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
		Log.i("---------------------", "location[1]:" + location[1]);
		FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) maskSource.getLayoutParams();
		params.setMargins(0, location[1], 0, 0);// 通过自定义坐标来放置你的控件
		maskSource.setLayoutParams(params);
	}

}