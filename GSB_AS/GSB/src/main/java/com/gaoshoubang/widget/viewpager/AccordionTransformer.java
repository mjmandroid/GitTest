package com.gaoshoubang.widget.viewpager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * 折叠
 */
public class AccordionTransformer implements ViewPager.PageTransformer {

	@Override
	public void transformPage(View view, float position) {
		view.setPivotX(position <= 0 ? view.getMeasuredWidth() : 0);
		view.setScaleX(position <= 0 ? 1f + position : 1f - position);
	}

}
