package com.gaoshoubang.widget.gallery;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

public class ScalePageTransformer implements ViewPager.PageTransformer {

	// 缩放
	public static final float MAX_SCALE = 1f;
	public static final float MIN_SCALE = 0.83f;

	// 透明度
	public static final float MAX_ALPHA = 1f;
	public static final float MIN_ALPHA = 0.7f;

	@SuppressLint("NewApi")
	@Override
	public void transformPage(View page, float position) {
		if (position < -1) {
			position = -1;
		}
		else if (position > 1) {
			position = 1;
		}
		float tempScale = position < 0 ? 1 + position : 1 - position;
		float slope = (MAX_SCALE - MIN_SCALE) / 1;
		float scaleValue = MIN_SCALE + tempScale * slope;// 一个公式
		page.setScaleX(scaleValue);
		page.setScaleY(scaleValue);

		float tempAlpha = position < 0 ? 1 + position : 1 - position;
		float alpha = (MAX_ALPHA - MIN_ALPHA) / 1;
		float alphaValue = MIN_ALPHA + tempAlpha * alpha;
		page.setAlpha(alphaValue);

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
			page.getParent().requestLayout();
		}
	}
}
