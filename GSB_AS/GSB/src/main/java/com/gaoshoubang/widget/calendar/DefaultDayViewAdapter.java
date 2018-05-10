package com.gaoshoubang.widget.calendar;


import android.view.ContextThemeWrapper;
import android.widget.TextView;

import com.gaoshoubang.R;

public class DefaultDayViewAdapter implements DayViewAdapter {
	@Override
	public void makeCellView(CalendarCellView parent) {
		TextView textView = new TextView(
				new ContextThemeWrapper(parent.getContext(), R.style.CalendarCell_CalendarDate));
		textView.setDuplicateParentStateEnabled(true);
		parent.addView(textView);
		parent.setDayOfMonthTextView(textView);
	}
}
