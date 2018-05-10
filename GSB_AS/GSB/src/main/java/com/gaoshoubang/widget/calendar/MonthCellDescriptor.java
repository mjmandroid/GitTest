package com.gaoshoubang.widget.calendar;

import java.util.Date;

class MonthCellDescriptor {
	public enum RangeState {
		NONE, FIRST, MIDDLE, LAST
	}

	private final Date date;
	private final int value;
	private final boolean isCurrentMonth;
	private boolean isSelected;
	private final boolean isToday;
	private final Date todayDate;
	private final boolean isSelectable;
	private boolean isHighlighted;
	private RangeState rangeState;

	MonthCellDescriptor(Date date, boolean currentMonth, boolean selectable, boolean selected, boolean today,
	                    Date todayDate, boolean highlighted, int value, RangeState rangeState) {
		this.date = date;
		isCurrentMonth = currentMonth;
		isSelectable = selectable;
		isHighlighted = highlighted;
		isSelected = selected;
		isToday = today;
		this.todayDate = todayDate;
		this.value = value;
		this.rangeState = rangeState;
	}

	public Date getDate() {
		return date;
	}

	public boolean isCurrentMonth() {
		return isCurrentMonth;
	}

	public boolean isSelectable() {
		return isSelectable;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	boolean isHighlighted() {
		return isHighlighted;
	}

	void setHighlighted(boolean highlighted) {
		isHighlighted = highlighted;
	}

	public boolean isToday() {
		return isToday;
	}

	public Date getTodayDate() {
		return todayDate;
	}

	public RangeState getRangeState() {
		return rangeState;
	}

	public void setRangeState(RangeState rangeState) {
		this.rangeState = rangeState;
	}

	public int getValue() {
		return value;
	}

	@Override
	public String toString() {
		return "MonthCellDescriptor{" + "date=" + date + ", value=" + value + ", isCurrentMonth=" + isCurrentMonth
				+ ", isSelected=" + isSelected + ", isToday=" + isToday + ", isSelectable=" + isSelectable
				+ ", isHighlighted=" + isHighlighted + ", rangeState=" + rangeState + '}';
	}
}
