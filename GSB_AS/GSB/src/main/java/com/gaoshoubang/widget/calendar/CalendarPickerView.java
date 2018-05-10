package com.gaoshoubang.widget.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoshoubang.R;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

/**
 * Created by Done on 2016/4/20.
 */
public class CalendarPickerView extends ListView {

	public enum SelectionMode {
		/**
		 * Only one date will be selectable. If there is already a selected date
		 * and you select a new one, the old date will be unselected.
		 */
		SINGLE,
		/**
		 * Multiple dates will be selectable. Selecting an already-selected date
		 * will un-select it.
		 */
		MULTIPLE,
		/**
		 * Allows you to select a date range. Previous selections are cleared
		 * when you either:
		 * <ul>
		 * <li>Have a range selected and select another date (even if it's in
		 * the current range).</li>
		 * <li>Have one date selected and then select an earlier date.</li>
		 * </ul>
		 */
		RANGE
	}

	private final MonthAdapter adapter;
	private final List<List<List<MonthCellDescriptor>>> cells = new ArrayList<List<List<MonthCellDescriptor>>>();
	final MonthView.Listener listener = new CellClickedListener();
	final List<MonthDescriptor> months = new ArrayList<MonthDescriptor>();
	final List<MonthCellDescriptor> selectedCells = new ArrayList<MonthCellDescriptor>();
	final List<MonthCellDescriptor> highlightedCells = new ArrayList<MonthCellDescriptor>();
	final List<Calendar> selectedCals = new ArrayList<Calendar>();
	final List<Calendar> highlightedCals = new ArrayList<Calendar>();
	private Locale locale;
	private DateFormat monthNameFormat;
	private DateFormat fullDateFormat;
	private Calendar minCal;
	private Calendar maxCal;
	private Calendar monthCounter;
	private boolean displayOnly;
	SelectionMode selectionMode;
	Calendar today;
	private int dividerColor;
	private int dayBackgroundResId;
	private int dayTextColorResId;
	private Typeface dateTypeface;

	private OnDateSelectedListener dateListener;
	private DateSelectableFilter dateConfiguredListener;
	private OnInvalidDateSelectedListener invalidDateListener = new DefaultOnInvalidDateSelectedListener();
	private CellClickInterceptor cellClickInterceptor;
	private List<CalendarCellDecorator> decorators;
	private DayViewAdapter dayViewAdapter = new DefaultDayViewAdapter();

	private List<String> listDate;
	private OnGetScrollDate onGetScrollDate;

	public void setDecorators(List<CalendarCellDecorator> decorators) {
		this.decorators = decorators;
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	public List<CalendarCellDecorator> getDecorators() {
		return decorators;
	}

	public CalendarPickerView(Context context, AttributeSet attrs) {
		super(context, attrs);

		Resources res = context.getResources();
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarPickerView);
		final int bg = a.getColor(R.styleable.CalendarPickerView_android_background, res.getColor(R.color.calendar_bg));
		dividerColor = a.getColor(R.styleable.CalendarPickerView_tsquare_dividerColor, res.getColor(R.color.calendar_divider));
		dayBackgroundResId = a.getResourceId(R.styleable.CalendarPickerView_tsquare_dayBackground, R.drawable.calendar_bg_selector);
		dayTextColorResId = a.getResourceId(R.styleable.CalendarPickerView_tsquare_dayTextColor, R.drawable.calendar_text_selector);
		a.recycle();

		adapter = new MonthAdapter();
		setDivider(null);
		setDividerHeight(0);
		setBackgroundColor(bg);
		setCacheColorHint(bg);
		locale = Locale.getDefault();
		today = Calendar.getInstance(locale);
		minCal = Calendar.getInstance(locale);
		maxCal = Calendar.getInstance(locale);
		monthCounter = Calendar.getInstance(locale);
		monthNameFormat = new SimpleDateFormat("yyyy年MM月", locale);
		fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

		if (isInEditMode()) {
			Calendar nextYear = Calendar.getInstance(locale);
			nextYear.add(Calendar.YEAR, 1);

			init(new Date(), nextYear.getTime()) //
					.withSelectedDate(new Date());
		}
		setFastScrollEnabled(false);
		setOnScrollListener();
		TextView textView = new TextView(context);
		textView.setPadding(0, 50, 0, 50);
		addFooterView(textView);
	}

	/**
	 * Both date parameters must be non-null and their {@link Date#getTime()}
	 * must not return 0. Time of day will be ignored. For instance, if you pass
	 * in {@code minDate} as 11/16/2012 5:15pm and {@code maxDate} as 11/16/2013
	 * 4:30am, 11/16/2012 will be the first selectable date and 11/15/2013 will
	 * be the last selectable date ({@code maxDate} is exclusive).
	 * <p/>
	 * This will implicitly set the {@link SelectionMode} to
	 * {@link SelectionMode#SINGLE}. If you want a different selection mode, use
	 * {@link FluentInitializer#inMode(SelectionMode)} on the
	 * {@link FluentInitializer} this method returns.
	 * <p/>
	 * The calendar will be constructed using the given locale. This means that
	 * all names (months, days) will be in the language of the locale and the
	 * weeks start with the day specified by the locale.
	 *
	 * @param minDate Earliest selectable date, inclusive. Must be earlier than
	 * {@code maxDate}.
	 * @param maxDate Latest selectable date, exclusive. Must be later than
	 * {@code minDate}.
	 */
	public FluentInitializer init(Date minDate, Date maxDate, Locale locale) {
		if (minDate == null || maxDate == null) {
			throw new IllegalArgumentException("minDate and maxDate must be non-null.  " + dbg(minDate, maxDate));
		}
		if (minDate.after(maxDate)) {
			throw new IllegalArgumentException("minDate must be before maxDate.  " + dbg(minDate, maxDate));
		}
		if (locale == null) {
			throw new IllegalArgumentException("Locale is null.");
		}

		this.locale = locale;
		today = Calendar.getInstance(locale);
		minCal = Calendar.getInstance(locale);
		maxCal = Calendar.getInstance(locale);
		monthCounter = Calendar.getInstance(locale);
		monthNameFormat = new SimpleDateFormat("yyyy年MM月", locale);
		for (MonthDescriptor month : months) {
			month.setLabel(monthNameFormat.format(month.getDate()));
		}
		fullDateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, locale);

		this.selectionMode = SelectionMode.SINGLE;
		// Clear out any previously-selected dates/cells.
		selectedCals.clear();
		selectedCells.clear();
		highlightedCals.clear();
		highlightedCells.clear();

		// Clear previous state.
		cells.clear();
		months.clear();
		minCal.setTime(minDate);
		maxCal.setTime(maxDate);
		setMidnight(minCal);
		setMidnight(maxCal);
		displayOnly = false;

		// maxDate is exclusive: bump back to the previous day so if maxDate is
		// the first of a month,
		// we don't accidentally include that month in the view.
		maxCal.add(MINUTE, -1);

		// Now iterate between minCal and maxCal and build up our list of months
		// to show.
		listDate = new ArrayList<String>();
		monthCounter.setTime(minCal.getTime());
		final int maxMonth = maxCal.get(MONTH);
		final int maxYear = maxCal.get(YEAR);
		while ((monthCounter.get(MONTH) <= maxMonth // Up to, including the
				// month.
				|| monthCounter.get(YEAR) < maxYear) // Up to the year.
				&& monthCounter.get(YEAR) < maxYear + 1) { // But not > next yr.
			Date date = monthCounter.getTime();
			MonthDescriptor month = new MonthDescriptor(monthCounter.get(MONTH), monthCounter.get(YEAR), date, monthNameFormat.format(date));
			cells.add(getMonthCells(month, monthCounter));
			months.add(month);
			monthCounter.add(MONTH, 1);

			listDate.add(month.getLabel().toString());
		}

		validateAndUpdate();
		return new FluentInitializer();
	}

	/**
	 * Both date parameters must be non-null and their {@link Date#getTime()}
	 * must not return 0. Time of day will be ignored. For instance, if you pass
	 * in {@code minDate} as 11/16/2012 5:15pm and {@code maxDate} as 11/16/2013
	 * 4:30am, 11/16/2012 will be the first selectable date and 11/15/2013 will
	 * be the last selectable date ({@code maxDate} is exclusive).
	 * <p/>
	 * This will implicitly set the {@link SelectionMode} to
	 * {@link SelectionMode#SINGLE}. If you want a different selection mode, use
	 * {@link FluentInitializer#inMode(SelectionMode)} on the
	 * {@link FluentInitializer} this method returns.
	 * <p/>
	 * The calendar will be constructed using the default locale as returned by
	 * {@link Locale#getDefault()}. If you wish the calendar to be constructed
	 * using a different locale, use {@link #init(Date, Date, Locale)}.
	 *
	 * @param minDate Earliest selectable date, inclusive. Must be earlier than
	 * {@code maxDate}.
	 * @param maxDate Latest selectable date, exclusive. Must be later than
	 * {@code minDate}.
	 */
	public FluentInitializer init(Date minDate, Date maxDate) {
		return init(minDate, maxDate, Locale.getDefault());
	}

	public class FluentInitializer {
		/**
		 * Override the {@link SelectionMode} from the default (
		 * {@link SelectionMode#SINGLE}).
		 */
		public FluentInitializer inMode(SelectionMode mode) {
			selectionMode = mode;
			validateAndUpdate();
			return this;
		}

		public FluentInitializer withSelectedDate(Date selectedDates) {
			return withSelectedDates(Collections.singletonList(selectedDates));
		}

		public FluentInitializer withSelectedDates(Collection<Date> selectedDates) {
			if (selectionMode == SelectionMode.SINGLE && selectedDates.size() > 1) {
				throw new IllegalArgumentException("SINGLE mode can't be used with multiple selectedDates");
			}
			if (selectionMode == SelectionMode.RANGE && selectedDates.size() > 2) {
				throw new IllegalArgumentException("RANGE mode only allows two selectedDates.  You tried to pass " + selectedDates.size());
			}
			if (selectedDates != null) {
				for (Date date : selectedDates) {
					selectDate(date);
				}
			}
			scrollToSelectedDates();

			validateAndUpdate();
			return this;
		}

		public FluentInitializer withHighlightedDates(Collection<Date> dates) {
			highlightDates(dates);
			return this;
		}

		public FluentInitializer withHighlightedDate(Date date) {
			return withHighlightedDates(Collections.singletonList(date));
		}

		@SuppressLint("SimpleDateFormat")
		public FluentInitializer setShortWeekdays(String[] newShortWeekdays) {
			DateFormatSymbols symbols = new DateFormatSymbols(locale);
			symbols.setShortWeekdays(newShortWeekdays);
			return this;
		}

		public FluentInitializer displayOnly() {
			displayOnly = true;
			return this;
		}
	}

	private void validateAndUpdate() {
		if (getAdapter() == null) {
			setAdapter(adapter);
		}
		adapter.notifyDataSetChanged();
	}

	public void scrollToSelectedMonth(final int selectedIndex) {
		scrollToSelectedMonth(selectedIndex, false);
	}

	private void scrollToSelectedMonth(final int selectedIndex, final boolean smoothScroll) {
		post(new Runnable() {
			@Override
			public void run() {
				if (smoothScroll) {
					smoothScrollToPosition(selectedIndex);
				}
				else {
					setSelection(selectedIndex);
				}
			}
		});
	}

	private void scrollToSelectedDates() {
		Integer selectedIndex = null;
		Integer todayIndex = null;
		Calendar today = Calendar.getInstance(locale);
		for (int c = 0; c < months.size(); c++) {
			MonthDescriptor month = months.get(c);
			if (selectedIndex == null) {
				for (Calendar selectedCal : selectedCals) {
					if (sameMonth(selectedCal, month)) {
						selectedIndex = c;
						break;
					}
				}
				if (selectedIndex == null && todayIndex == null && sameMonth(today, month)) {
					todayIndex = c;
				}
			}
		}
		if (selectedIndex != null) {
			scrollToSelectedMonth(selectedIndex);
		}
		else if (todayIndex != null) {
			scrollToSelectedMonth(todayIndex);
		}
	}

	public boolean scrollToDate(Date date) {
		Integer selectedIndex = null;

		Calendar cal = Calendar.getInstance(locale);
		cal.setTime(date);
		for (int c = 0; c < months.size(); c++) {
			MonthDescriptor month = months.get(c);
			if (sameMonth(cal, month)) {
				selectedIndex = c;
				break;
			}
		}
		if (selectedIndex != null) {
			scrollToSelectedMonth(selectedIndex);
			return true;
		}
		return false;
	}

	public void fixDialogDimens() {
		getLayoutParams().height = getMeasuredHeight();
		getLayoutParams().width = getMeasuredWidth();
		post(new Runnable() {
			@Override
			public void run() {
				scrollToSelectedDates();
			}
		});
	}

	/**
	 * Sets the typeface to be used within the date grid.
	 */
	public void setDateTypeface(Typeface dateTypeface) {
		this.dateTypeface = dateTypeface;
		validateAndUpdate();
	}

	/**
	 * This method should only be called if the calendar is contained in a
	 * dialog, and it should only be called when the screen has been rotated and
	 * the dialog should be re-measured.
	 */
	public void unfixDialogDimens() {
		getLayoutParams().height = LayoutParams.MATCH_PARENT;
		getLayoutParams().width = LayoutParams.MATCH_PARENT;
		requestLayout();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (months.isEmpty()) {
			throw new IllegalStateException("Must have at least one month to display.  Did you forget to call init()?");
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public Date getSelectedDate() {
		return (selectedCals.size() > 0 ? selectedCals.get(0).getTime() : null);
	}

	public List<Date> getSelectedDates() {
		List<Date> selectedDates = new ArrayList<Date>();
		for (MonthCellDescriptor cal : selectedCells) {
			selectedDates.add(cal.getDate());
		}
		Collections.sort(selectedDates);
		return selectedDates;
	}

	/**
	 * Returns a string summarizing what the client sent us for init() params.
	 */
	private static String dbg(Date minDate, Date maxDate) {
		return "minDate: " + minDate + "\nmaxDate: " + maxDate;
	}

	/**
	 * Clears out the hours/minutes/seconds/millis of a Calendar.
	 */
	static void setMidnight(Calendar cal) {
		cal.set(HOUR_OF_DAY, 0);
		cal.set(MINUTE, 0);
		cal.set(SECOND, 0);
		cal.set(MILLISECOND, 0);
	}

	private class CellClickedListener implements MonthView.Listener {
		@Override
		public void handleClick(MonthCellDescriptor cell) {
			Date clickedDate = cell.getDate();
			if (cellClickInterceptor != null && cellClickInterceptor.onCellClicked(clickedDate)) {
				return;
			}
			if (!betweenDates(clickedDate, minCal, maxCal) || !isDateSelectable(clickedDate)) {
				if (invalidDateListener != null) {
					invalidDateListener.onInvalidDateSelected(clickedDate);
				}
			}
			else {
				// boolean wasSelected = doSelectDate(clickedDate, cell);
				if (dateListener != null) {
					// if (wasSelected) {
					dateListener.onDateSelected(clickedDate);
					// } else {
					// dateListener.onDateUnselected(clickedDate);
					// }
				}
			}
		}
	}

	public boolean selectDate(Date date) {
		return selectDate(date, false);
	}

	public boolean selectDate(Date date, boolean smoothScroll) {
		validateDate(date);

		MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
		if (monthCellWithMonthIndex == null || !isDateSelectable(date)) {
			return false;
		}
		boolean wasSelected = doSelectDate(date, monthCellWithMonthIndex.cell);
		if (wasSelected) {
			scrollToSelectedMonth(monthCellWithMonthIndex.monthIndex, smoothScroll);
		}
		return wasSelected;
	}

	private void validateDate(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("Selected date must be non-null.");
		}
		if (date.before(minCal.getTime()) || date.after(maxCal.getTime())) {
			throw new IllegalArgumentException(
					String.format("SelectedDate must be between minDate and maxDate." + "%nminDate: %s%nmaxDate: %s%nselectedDate: %s", minCal.getTime(), maxCal.getTime(), date));
		}
	}

	private boolean doSelectDate(Date date, MonthCellDescriptor cell) {
		Calendar newlySelectedCal = Calendar.getInstance(locale);
		newlySelectedCal.setTime(date);
		setMidnight(newlySelectedCal);

		for (MonthCellDescriptor selectedCell : selectedCells) {
			selectedCell.setRangeState(MonthCellDescriptor.RangeState.NONE);
		}

		switch (selectionMode) {
			case RANGE:
				if (selectedCals.size() > 1) {
					clearOldSelections();
				}
				else if (selectedCals.size() == 1 && newlySelectedCal.before(selectedCals.get(0))) {
					clearOldSelections();
				}
				break;

			case MULTIPLE:
				date = applyMultiSelect(date, newlySelectedCal);
				break;

			case SINGLE:
				clearOldSelections();
				break;
			default:
				throw new IllegalStateException("Unknown selectionMode " + selectionMode);
		}

		if (date != null) {
			if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
				selectedCells.add(cell);
				cell.setSelected(true);
			}
			selectedCals.add(newlySelectedCal);

			if (selectionMode == SelectionMode.RANGE && selectedCells.size() > 1) {
				// Select all days in between start and end.
				Date start = selectedCells.get(0).getDate();
				Date end = selectedCells.get(1).getDate();
				selectedCells.get(0).setRangeState(MonthCellDescriptor.RangeState.FIRST);
				selectedCells.get(1).setRangeState(MonthCellDescriptor.RangeState.LAST);

				for (List<List<MonthCellDescriptor>> month : cells) {
					for (List<MonthCellDescriptor> week : month) {
						for (MonthCellDescriptor singleCell : week) {
							if (singleCell.getDate().after(start) && singleCell.getDate().before(end) && singleCell.isSelectable()) {
								singleCell.setSelected(true);
								singleCell.setRangeState(MonthCellDescriptor.RangeState.MIDDLE);
								selectedCells.add(singleCell);
							}
						}
					}
				}
			}
		}

		// Update the adapter.
		validateAndUpdate();
		return date != null;
	}

	private void clearOldSelections() {
		for (MonthCellDescriptor selectedCell : selectedCells) {
			// De-select the currently-selected cell.
			selectedCell.setSelected(false);

			if (dateListener != null) {
				Date selectedDate = selectedCell.getDate();

				if (selectionMode == SelectionMode.RANGE) {
					// int index = selectedCells.indexOf(selectedCell);
					// if (index == 0 || index == selectedCells.size() - 1) {
					// dateListener.onDateUnselected(selectedDate);
					// }
					// } else {
					//// dateListener.onDateUnselected(selectedDate);
				}
			}
		}
		selectedCells.clear();
		selectedCals.clear();
	}

	private Date applyMultiSelect(Date date, Calendar selectedCal) {
		for (MonthCellDescriptor selectedCell : selectedCells) {
			if (selectedCell.getDate().equals(date)) {
				// De-select the currently-selected cell.
				selectedCell.setSelected(false);
				selectedCells.remove(selectedCell);
				date = null;
				break;
			}
		}
		for (Calendar cal : selectedCals) {
			if (sameDate(cal, selectedCal)) {
				selectedCals.remove(cal);
				break;
			}
		}
		return date;
	}

	public void highlightDates(Collection<Date> dates) {
		for (Date date : dates) {
			validateDate(date);

			MonthCellWithMonthIndex monthCellWithMonthIndex = getMonthCellWithIndexByDate(date);
			if (monthCellWithMonthIndex != null) {
				Calendar newlyHighlightedCal = Calendar.getInstance();
				newlyHighlightedCal.setTime(date);
				MonthCellDescriptor cell = monthCellWithMonthIndex.cell;

				highlightedCells.add(cell);
				highlightedCals.add(newlyHighlightedCal);
				cell.setHighlighted(true);
			}
		}

		validateAndUpdate();
	}

	public void clearHighlightedDates() {
		for (MonthCellDescriptor cal : highlightedCells) {
			cal.setHighlighted(false);
		}
		highlightedCells.clear();
		highlightedCals.clear();

		validateAndUpdate();
	}

	private static class MonthCellWithMonthIndex {
		public MonthCellDescriptor cell;
		public int monthIndex;

		public MonthCellWithMonthIndex(MonthCellDescriptor cell, int monthIndex) {
			this.cell = cell;
			this.monthIndex = monthIndex;
		}
	}

	private MonthCellWithMonthIndex getMonthCellWithIndexByDate(Date date) {
		int index = 0;
		Calendar searchCal = Calendar.getInstance(locale);
		searchCal.setTime(date);
		Calendar actCal = Calendar.getInstance(locale);

		for (List<List<MonthCellDescriptor>> monthCells : cells) {
			for (List<MonthCellDescriptor> weekCells : monthCells) {
				for (MonthCellDescriptor actCell : weekCells) {
					actCal.setTime(actCell.getDate());
					if (sameDate(actCal, searchCal) && actCell.isSelectable()) {
						return new MonthCellWithMonthIndex(actCell, index);
					}
				}
			}
			index++;
		}
		return null;
	}

	private class MonthAdapter extends BaseAdapter {
		private final LayoutInflater inflater;

		private MonthAdapter() {
			inflater = LayoutInflater.from(getContext());
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		@Override
		public int getCount() {
			return months.size();
		}

		@Override
		public Object getItem(int position) {
			return months.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MonthView monthView = (MonthView) convertView;
			if (monthView == null || !monthView.getTag(R.id.day_view_adapter_class).equals(dayViewAdapter.getClass())) {
				monthView = MonthView.create(parent, inflater, listener, today, dividerColor, dayBackgroundResId, dayTextColorResId, decorators, locale, dayViewAdapter);
				monthView.setTag(R.id.day_view_adapter_class, dayViewAdapter.getClass());
			}
			else {
				monthView.setDecorators(decorators);
			}
			monthView.init(months.get(position), cells.get(position), displayOnly, dateTypeface);
			return monthView;
		}
	}

	List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor month, Calendar startCal) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setTime(startCal.getTime());
		List<List<MonthCellDescriptor>> cells = new ArrayList<List<MonthCellDescriptor>>();
		cal.set(DAY_OF_MONTH, 1);
		int firstDayOfWeek = cal.get(DAY_OF_WEEK);
		int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
		if (offset > 0) {
			offset -= 7;
		}
		cal.add(Calendar.DATE, offset);

		Calendar minSelectedCal = minDate(selectedCals);
		Calendar maxSelectedCal = maxDate(selectedCals);
		Date toDayDate = null;
		while ((cal.get(MONTH) < month.getMonth() + 1 || cal.get(YEAR) < month.getYear()) //
				&& cal.get(YEAR) <= month.getYear()) {
			List<MonthCellDescriptor> weekCells = new ArrayList<MonthCellDescriptor>();
			cells.add(weekCells);
			for (int c = 0; c < 7; c++) {
				Date date = cal.getTime();
				boolean isCurrentMonth = cal.get(MONTH) == month.getMonth();
				boolean isSelected = isCurrentMonth && containsDate(selectedCals, cal);
				boolean isSelectable = isCurrentMonth && betweenDates(cal, minCal, maxCal) && isDateSelectable(date);
				boolean isToday = sameDate(cal, today);

				toDayDate = today.getTime();

				boolean isHighlighted = containsDate(highlightedCals, cal);
				int value = cal.get(DAY_OF_MONTH);

				MonthCellDescriptor.RangeState rangeState = MonthCellDescriptor.RangeState.NONE;
				if (selectedCals.size() > 1) {
					if (sameDate(minSelectedCal, cal)) {
						rangeState = MonthCellDescriptor.RangeState.FIRST;
					}
					else if (sameDate(maxDate(selectedCals), cal)) {
						rangeState = MonthCellDescriptor.RangeState.LAST;
					}
					else if (betweenDates(cal, minSelectedCal, maxSelectedCal)) {
						rangeState = MonthCellDescriptor.RangeState.MIDDLE;
					}
				}

				weekCells.add(new MonthCellDescriptor(date, isCurrentMonth, isSelectable, isSelected, isToday, toDayDate, isHighlighted, value, rangeState));
				cal.add(DATE, 1);
			}
		}
		return cells;
	}

	private boolean containsDate(List<Calendar> selectedCals, Date date) {
		Calendar cal = Calendar.getInstance(locale);
		cal.setTime(date);
		return containsDate(selectedCals, cal);
	}

	private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
		for (Calendar selectedCal : selectedCals) {
			if (sameDate(cal, selectedCal)) {
				return true;
			}
		}
		return false;
	}

	private static Calendar minDate(List<Calendar> selectedCals) {
		if (selectedCals == null || selectedCals.size() == 0) {
			return null;
		}
		Collections.sort(selectedCals);
		return selectedCals.get(0);
	}

	private static Calendar maxDate(List<Calendar> selectedCals) {
		if (selectedCals == null || selectedCals.size() == 0) {
			return null;
		}
		Collections.sort(selectedCals);
		return selectedCals.get(selectedCals.size() - 1);
	}

	private static boolean sameDate(Calendar cal, Calendar selectedDate) {
		return cal.get(MONTH) == selectedDate.get(MONTH) && cal.get(YEAR) == selectedDate.get(YEAR) && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
	}

	private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
		final Date date = cal.getTime();
		return betweenDates(date, minCal, maxCal);
	}

	static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
		final Date min = minCal.getTime();
		return (date.equals(min) || date.after(min)) // >= minCal
				&& date.before(maxCal.getTime()); // && < maxCal
	}

	private static boolean sameMonth(Calendar cal, MonthDescriptor month) {
		return (cal.get(MONTH) == month.getMonth() && cal.get(YEAR) == month.getYear());
	}

	private boolean isDateSelectable(Date date) {
		return dateConfiguredListener == null || dateConfiguredListener.isDateSelectable(date);
	}

	public void setOnDateSelectedListener(OnDateSelectedListener listener) {
		dateListener = listener;
	}

	public void setOnInvalidDateSelectedListener(OnInvalidDateSelectedListener listener) {
		invalidDateListener = listener;
	}

	public void setDateSelectableFilter(DateSelectableFilter listener) {
		dateConfiguredListener = listener;
	}

	public void setCustomDayView(DayViewAdapter dayViewAdapter) {
		this.dayViewAdapter = dayViewAdapter;
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}
	}

	public void setCellClickInterceptor(CellClickInterceptor listener) {
		cellClickInterceptor = listener;
	}

	public interface OnDateSelectedListener {
		void onDateSelected(Date date);
	}

	public interface OnInvalidDateSelectedListener {
		void onInvalidDateSelected(Date date);
	}

	public interface DateSelectableFilter {
		boolean isDateSelectable(Date date);
	}

	public interface CellClickInterceptor {
		boolean onCellClicked(Date date);
	}

	private class DefaultOnInvalidDateSelectedListener implements OnInvalidDateSelectedListener {
		@Override
		public void onInvalidDateSelected(Date date) {
			String errMessage = "Date must be between %1$s and %2$s." + fullDateFormat.format(minCal.getTime()) + fullDateFormat.format(maxCal.getTime());
			Toast.makeText(getContext(), errMessage, Toast.LENGTH_SHORT).show();
		}
	}
	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// switch (ev.getAction()) {
	// case MotionEvent.ACTION_MOVE:
	// break;
	//
	// case MotionEvent.ACTION_UP:
	// resetScroll();
	// break;
	// }
	// return super.onTouchEvent(ev);
	// }

	@SuppressLint("NewApi")
	private void resetScroll() {
		int y = scrollY % itemHeight;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (y > itemHeight / 2) {
				scrollListBy(itemHeight - y + 1);
			}
			else {
				scrollListBy(0 - y + 1);
			}
		}
		if (onGetScrollDate != null && !strDate.equals(listDate.get(mItemIndex))) {
			strDate = listDate.get(mItemIndex);
			onGetScrollDate.getDate(listDate.get(mItemIndex));
		}
	}

	private String strDate = "";
	private int itemHeight = -1;
	private int scrollY;
	private int mItemIndex = -1;
	private HashMap<Integer, TextView> mItemTextViews;

	public void setOnScrollListener() {
		setOnScrollListener(new OnScrollYListener(this) {
			@Override
			protected void onScrollY(int scrolledY, int itemIndex, Map<Integer, Integer> mItemHeights, HashMap<Integer, TextView> itemTextViews) {
				scrollY = scrolledY;
				mItemIndex = itemIndex;
				itemHeight = mItemHeights.get(itemIndex);
				mItemTextViews = itemTextViews;

				for (int i = 0; i < 20; i++) {
					if (i != mItemIndex && mItemTextViews.get(i) != null) {
						mItemTextViews.get(i).setTextColor(Color.parseColor("#a4a4a4"));
					}
					if (mItemTextViews.get(mItemIndex) != null) {
						mItemTextViews.get(mItemIndex).setTextColor(Color.parseColor("#000000"));
					}
				}

				if (onGetScrollDate != null && !strDate.equals(listDate.get(itemIndex))) {
					strDate = listDate.get(itemIndex);
					onGetScrollDate.getDate(listDate.get(itemIndex));
				}
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					resetScroll();
					MonthView monthView = (MonthView) view.getChildAt(0);
					if (monthView.getChildAt(0) != null) {
						TextView textView = (TextView) monthView.getChildAt(0);
						textView.setTextColor(Color.parseColor("#000000"));
					}

				}
			}

		});
	}

	public void setOnGetScrollDate(OnGetScrollDate onGetScrollDate) {
		this.onGetScrollDate = onGetScrollDate;
	}

	public interface OnGetScrollDate {
		void getDate(String date);
	}
}
