package com.gaoshoubang.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

public class CancelScrollGridView extends GridView {

	public CancelScrollGridView(Context context) {
		super(context);
	}

	public CancelScrollGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CancelScrollGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE) {
			return true;
		}
		return super.dispatchTouchEvent(ev);
	}
}
