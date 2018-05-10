package com.gaoshoubang.ui.common.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.calendar.CalendarCellDecorator;
import com.gaoshoubang.widget.calendar.CalendarPickerView;
import com.gaoshoubang.widget.calendar.DefaultDayViewAdapter;
import com.gaoshoubang.bean.CalenderBean;
import com.gaoshoubang.ui.common.presenter.CalendarPresenterImpl;
import com.gaoshoubang.ui.common.view.MyCalendarView;
import com.gaoshoubang.util.FullTitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 回款日历
 *
 * @author Cisco
 */

@SuppressLint("SimpleDateFormat")
public class ActivityCalendar extends BaseActivity<CalendarPresenterImpl> implements MyCalendarView {
	private static final String TAG = "ActivityCalendar";

	private CalendarPickerView calendar;
	private ListView calendarList;
	private TextView footNogetnum;
	private TextView footGetnum;
	private TextView footNogetmoney;
	private TextView footGetmoney;

	private List<CalenderBean> mCalendarList;
	private CalenderBean.Rel relBean;
	private List<CalenderBean.Details> detailsBean;

	private Calendar todayCalendar;
	private Calendar lastCalendar;
	private CalendarAdapter adapter;

	private HashMap<String, CalenderBean.Rel> rel;
	private HashMap<String, List<CalenderBean.Details>> details;
	private ArrayList<Date> dates;


	@Override
	protected CalendarPresenterImpl getPresenter() {
		return new CalendarPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_calendar;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		calendarList = (ListView) findViewById(R.id.calendar_list);
		mCalendarList = new ArrayList<CalenderBean>();
		detailsBean = new ArrayList<CalenderBean.Details>();
		adapter = new CalendarAdapter(detailsBean, this);


		View footView = LayoutInflater.from(ActivityCalendar.this).inflate(R.layout.activity_calendar_footer, null, false);
		footNogetnum = (TextView) footView.findViewById(R.id.calendar_footer_nogetnum);
		footNogetmoney = (TextView) footView.findViewById(R.id.calendar_footer_nogetmoney);
		footGetnum = (TextView) footView.findViewById(R.id.calendar_footer_getnum);
		footGetmoney = (TextView) footView.findViewById(R.id.calendar_footer_getmoney);
		calendarList.addFooterView(footView);

		initCalendar();
	}

	@Override
	protected void initEvent() {
		calendarList.setAdapter(adapter);
	}

	@Override
	protected void loadData() {
		mPresenter.requestRecMoneyDay();
	}

	@Override
	protected void bindData() {

	}

	// 初始化日历
	private void initCalendar() {
		int y, m;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date todayDate = new Date();
		Date lastDate = new Date();

		todayCalendar = Calendar.getInstance();
		y = todayCalendar.get(Calendar.YEAR);
		m = todayCalendar.get(Calendar.MONTH) + 1;

		try {
			todayDate = format.parse(y + "-" + m + "-" + 1);
			lastDate = format.parse(y + "-" + m + "-" + 31);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		lastCalendar = Calendar.getInstance();
		lastCalendar.setTime(lastDate);
		lastCalendar.add(Calendar.MONTH, 20);
		lastCalendar.set(lastCalendar.get(Calendar.YEAR), lastCalendar.get(Calendar.MONTH) - 1, 1);
		int last = lastCalendar.getActualMaximum(Calendar.DATE);
		try {
			lastDate = format.parse(lastCalendar.get(Calendar.YEAR) + "-" + lastCalendar.get(Calendar.MONTH) + "-" + last);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		todayCalendar.setTime(todayDate);
		lastCalendar.setTime(lastDate);

		calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
		calendar.setCustomDayView(new DefaultDayViewAdapter());
		calendar.setDecorators(Collections.<CalendarCellDecorator>emptyList());
		calendar.init(todayCalendar.getTime(), lastCalendar.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE);
	}

	// 将数据装入hsahMap
	private void initJson() {
		final SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy年M月");
		List<CalenderBean.Details> mDetails = new ArrayList<CalenderBean.Details>();
		ArrayList<String> mDate = new ArrayList<String>();
		rel = new HashMap<String, CalenderBean.Rel>();// 存储当月的回款详情
		details = new HashMap<String, List<CalenderBean.Details>>();// 存储当天的回款详情
		try {
			for (int i = 0; i < mCalendarList.size(); i++) {
				if (mCalendarList.get(i).getDay().size() > 0) {
					for (int j = 0; j < mCalendarList.get(i).getDay().size(); j++) {
						// 获取年月日
						String jDate = mCalendarList.get(i).getYear() + "年" + mCalendarList.get(i).getMonth() + "月" + mCalendarList.get(i).getDay().get(j).getDate_day() + "日";
						mDate.add(dateFormat1.format(dateFormat1.parse(jDate)));
						mDetails = mCalendarList.get(i).getDay().get(j).getDetails();
						for (int k = 0; k < mDetails.size(); k++) {
							mDetails.get(k).setDay(jDate);
						}
						details.put(dateFormat1.format(dateFormat1.parse(jDate)), mDetails);

					}
				}
				// 当月回款详情
				rel.put(dateFormat2.format(dateFormat2.parse(mCalendarList.get(i).getYear() + "年" + mCalendarList.get(i).getMonth() + "月")), mCalendarList.get(i).getRel());
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		// 日历上显示的日期
		dates = new ArrayList<Date>();
		Date dateTime;
		for (int i = 0; i < mDate.size(); i++) {
			try {
				dateTime = dateFormat1.parse(mDate.get(i));
				dates.add(dateTime);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// 滚动
		calendar.setOnGetScrollDate(new CalendarPickerView.OnGetScrollDate() {

			@Override
			public void getDate(String date) {
				initData(date);
				detailsBean.clear();
				adapter.notifyDataSetChanged();
			}
		});
		// 点击日期
		calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {

			@Override
			public void onDateSelected(Date date) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
				if (details.get(dateFormat.format(date)) != null) {
					detailsBean.clear();
					detailsBean.addAll(details.get(dateFormat.format(date)));
					adapter.notifyDataSetChanged();
				}
			}
		});
	}

	// 底部数据
	private void initData(String date) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年M月");
		try {
			String formatDate = dateFormat.format(dateFormat.parse(date));
			if (rel.get(formatDate) != null) {
				relBean = rel.get(formatDate);
				footNogetnum.setText(formatDate.substring(5) + "待回款（" + relBean.getWait_nums() + "笔）");
				footGetnum.setText(formatDate.substring(5) + "已回款（" + relBean.getRec_nums() + "笔）");
				footNogetmoney.setText(relBean.getWait_amt() + "元");
				footGetmoney.setText(relBean.getRec_amt() + "元");
			}
			else {
				footNogetnum.setText(formatDate.substring(5) + "待回款" + "（0笔）");
				footGetnum.setText(formatDate.substring(5) + "已回款" + "（0笔）");
				footNogetmoney.setText("0.00元");
				footGetmoney.setText("0.00元");
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}


	/**
	 * 回款日历
	 * url：/User/recMoneyDay
	 *
	 * @param calenderBean
	 */
	@Override
	public void afterGetRecMoneyDay(List<CalenderBean> calenderBean) {
		mCalendarList.addAll(calenderBean);
		initJson();
		// 加载显示有回款数据的日期
		calendar.init(todayCalendar.getTime(), lastCalendar.getTime()).inMode(CalendarPickerView.SelectionMode.MULTIPLE).withSelectedDates(dates);
	}


	/**
	 * 日历adapter适配器
	 */
	class CalendarAdapter extends BaseAdapter {
		private Activity activity;
		private List<CalenderBean.Details> list;

		public CalendarAdapter(List<CalenderBean.Details> list, Activity activity) {
			this.activity = activity;
			this.list = list;

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.item_calendar, null);
				new ViewHolder(convertView);
			}
			ViewHolder viewHolder = (ViewHolder) convertView.getTag();
			viewHolder.calendarProname.setText(list.get(position).getName() + "-回款");
			viewHolder.calendarDate.setText(list.get(position).getDay());
			viewHolder.calendarTotalmoney.setText(list.get(position).getAmt() + "元");
			return convertView;
		}

		class ViewHolder {
			TextView calendarProname;
			TextView calendarDate;
			TextView calendarTotalmoney;

			public ViewHolder(View view) {
				calendarProname = (TextView) view.findViewById(R.id.item_calendar_proname);
				calendarDate = (TextView) view.findViewById(R.id.item_calendar_date);
				calendarTotalmoney = (TextView) view.findViewById(R.id.item_calendar_totalmoney);
				view.setTag(this);
			}
		}
	}
}
