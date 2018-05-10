package com.gaoshoubang.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FullTitleBar {
	public FullTitleBar(Activity activity) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true, activity);
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor("#ffffff"));
			if (!setMiuiStatusBarDarkMode(activity, true) && !setMeizuStatusBarDarkIcon(activity, true)) {
				tintManager.setTintColor(0xFFe4e4e4);
			}
		}
	}

	public FullTitleBar(Activity activity, String color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true, activity);
			SystemBarTintManager tintManager = new SystemBarTintManager(activity);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setNavigationBarTintEnabled(true);
			tintManager.setTintColor(Color.parseColor(color));
			if (Color.parseColor(color) == Color.parseColor("#ffffff")) {
				if (!setMiuiStatusBarDarkMode(activity, true) && !setMeizuStatusBarDarkIcon(activity, true)) {
					tintManager.setTintColor(0xFFe4e4e4);
				}
			}
		}
	}

	public FullTitleBar(Activity activity, View view) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			view.setVisibility(View.VISIBLE);
			view.getLayoutParams().width = LayoutParams.MATCH_PARENT;
			view.getLayoutParams().height = DisplayUtil.getStatusBarHeight(activity);
//			if (!setMiuiStatusBarDarkMode(activity, true) && !setMeizuStatusBarDarkIcon(activity, true)) {
//				view.setBackgroundColor(0xFFe4e4e4);
//			}
		}
		else {
			view.setVisibility(View.GONE);
		}
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on, Activity activity) {
		Window win = activity.getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		}
		else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public boolean setMiuiStatusBarDarkMode(Activity activity, boolean darkmode) {
		Class<? extends Window> clazz = activity.getWindow().getClass();
		try {
			int darkModeFlag = 0;
			Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
			extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static boolean setMeizuStatusBarDarkIcon(Activity activity, boolean dark) {
		boolean result = false;
		if (activity != null) {
			try {
				WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
				Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
				Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
				darkFlag.setAccessible(true);
				meizuFlags.setAccessible(true);
				int bit = darkFlag.getInt(null);
				int value = meizuFlags.getInt(lp);
				if (dark) {
					value |= bit;
				}
				else {
					value &= ~bit;
				}
				meizuFlags.setInt(lp, value);
				activity.getWindow().setAttributes(lp);
				result = true;
			} catch (Exception e) {
			}
		}
		return result;
	}

}
