package com.gaoshoubang.util;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static u.aly.av.n;

/**
 * Created by Administrator on 2017/5/23.
 */

public class DateUtils {
	private static SimpleDateFormat sf = null;

	/**
	 * 将时间戳转为字符窜
	 * 个提示
	 *
	 * @return
	 */
	public static CharSequence getDateToString(long time) {
		Date d = new Date(time * 1000);

		sf = new SimpleDateFormat("MM-dd HH:mm");
		sf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));//默认北京时间
		String format = sf.format(d);
		SpannableString spannableString = new SpannableString(format);
		spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, format.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableString;
	}

}
