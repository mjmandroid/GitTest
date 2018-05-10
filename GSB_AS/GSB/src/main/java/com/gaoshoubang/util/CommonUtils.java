package com.gaoshoubang.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.gaoshoubang.GsbApplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 帮助类
 *
 * @author ZhuangHaojie
 */
public class CommonUtils {
	/*
	*
	* 判断是否登录
	* */
	public static boolean isLogin() {
		if (TextUtils.isEmpty(GsbApplication.getUserId())) {
			return false;
		}
		return true;
	}

	/**
	 * ListView ScrollView 冲突 设置高度
	 *
	 * @param listView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 验证手机格式
	 *
	 * @param context
	 * @param mobiles
	 *
	 * @return 有效：返回null 无效：返回String信息
	 */
	public static String isMobileNO(Context context, String mobiles) {
		String returnStr = null;
		Resources resources = context.getResources();
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、17*
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles) || mobiles.length() < 11) {
			returnStr = "手机号码必须为11位";
		}
		else if (!mobiles.matches(telRegex)) {
			return "手机号码格式错误";
		}
		return returnStr;
	}

	/**
	 * 格式化时间 YYY-MM-DD
	 *
	 * @param time
	 *
	 * @return
	 */
	public static String getDateFormatYMD(long time) {

		if ((time + "").length() == 10) {
			time = time * 1000;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date(time);
		return format.format(date);
	}

	private final static ThreadLocal<SimpleDateFormat> dateFormater = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}
	};

	private final static ThreadLocal<SimpleDateFormat> dateFormater2 = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		}
	};

	/**
	 * 以友好的方式显示时间
	 *
	 * @param time
	 *
	 * @return
	 */
	public static String friendly_time(Date time) {

		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		// 判断是否是同一天
		String curDate = dateFormater2.get().format(cal.getTime());
		String paramDate = dateFormater2.get().format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0) {
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			}
			else {
				ftime = hour + "小时前";
			}
			return ftime;
		}

		long lt = time.getTime() / 86400000;
		long ct = cal.getTimeInMillis() / 86400000;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
			if (hour == 0) {
				ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
			}
			else {
				ftime = hour + "小时前";
			}
		}
		else if (days == 1) {
			ftime = "昨天";
		}
		else if (days == 2) {
			ftime = "前天";
		}
		else if (days > 2 && days <= 10) {
			ftime = days + "天前";
		}
		else if (days > 10) {
			ftime = dateFormater2.get().format(time);
		}
		return ftime;
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param date
	 *
	 * @return
	 */
	public static String friendly_time(long date) {
		return friendly_time(new Date(date));
	}

	/**
	 * 以友好的方式显示时间
	 *
	 * @param sdate
	 *
	 * @return
	 */
	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		return friendly_time(time);
	}

	/**
	 * 将字符串转位日期类型
	 *
	 * @param sdate
	 *
	 * @return
	 */
	public static Date toDate(String sdate) {
		try {
			return dateFormater.get().parse(sdate);
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 邮箱验证
	 *
	 * @param mail
	 *
	 * @return
	 */
	public static boolean isValidEmail(String mail) {
		Pattern pattern = Pattern.compile("^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})");
		Matcher mc = pattern.matcher(mail);
		return mc.matches();
	}

	/**
	 * 手机验证
	 *
	 * @param mobile
	 *
	 * @return
	 */
	public static boolean isMobileNO(String mobile) {
		Pattern pattern = Pattern.compile("[1][34578]\\d{9}");
		Matcher mc = pattern.matcher(mobile);
		return mc.matches();
	}

	/**
	 * 判断字符串是否为Double
	 *
	 * @param str
	 *
	 * @return
	 */
	public static boolean isDouble(String str) {

		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException ex) {
		}
		return false;

	}

	/**
	 * 判断是否有网络连接
	 *
	 * @param context
	 *
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	public static void checkLoadFailReason(Context context) {
		if (null == context) {
			return;
		}
		if (!isNetworkConnected(context)) {
			ToastUtil.toast(context, "网络君出问题了,重启网络试试!");
		}
		else {
			ToastUtil.toast(context, "服务器需要休息了,请稍后再试!");
		}
	}


	public static int[] getScreenDispaly(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point();
		wm.getDefaultDisplay().getSize(point);
		int width = point.x;// 手机屏幕的宽度
		int height = point.y;// 手机屏幕的高度
		int result[] = {width, height};
		return result;
	}
}
