package com.gaoshoubang.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class InputMethodManagerUtil {
	/**
	 * 判断输入法,是否弹出,若是弹出则隐藏
	 *
	 * @param activity
	 */
	public static void isInput(Activity activity) {
		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			boolean isOpen = imm.isActive();
			if (isOpen == true) {
				if (activity != null) {
					imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void show(Activity activity, View view) {

		try {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (activity != null) {
				imm.showSoftInput(view, InputMethodManager.SHOW_FORCED);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
