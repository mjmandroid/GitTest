package com.gaoshoubang.widget.gallery;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class ClipViewPager extends ViewPager {
	private int pagerWidth = 0;
	private int pagerHeight = 0;

	public ClipViewPager(Context context) {
		super(context);
	}

	public ClipViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		pagerWidth = dm.widthPixels * 3 / 4;
		pagerHeight = pagerWidth * 6 / 5;
		int width = MeasureSpec.makeMeasureSpec(pagerWidth, MeasureSpec.EXACTLY);
		int height = MeasureSpec.makeMeasureSpec(pagerHeight, MeasureSpec.EXACTLY);
		super.onMeasure(width, height);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_UP) {
			View view = viewOfClickOnScreen(ev);
			if (view != null) {
				int index = indexOfChild(view);
				if (getCurrentItem() != index) {
					setCurrentItem(indexOfChild(view));
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	private View viewOfClickOnScreen(MotionEvent ev) {
		int childCount = getChildCount();
		int[] location = new int[2];
		for (int i = 0; i < childCount; i++) {
			View v = getChildAt(i);
			v.getLocationOnScreen(location);
			int minX = location[0];
			int minY = getTop();

			int maxX = location[0] + v.getWidth();
			int maxY = getBottom();

			float x = ev.getX();
			float y = ev.getY();

			if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
				return v;
			}
		}
		return null;
	}
}
