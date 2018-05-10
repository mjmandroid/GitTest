package com.gaoshoubang.receiver;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.ui.common.activities.WebActivityShow;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送处理类
 */
public class JpushReceiver extends BroadcastReceiver {
	/**
	 * 新公告
	 */
	public static final String NEW_GONGGAO = "NEW_GONGGAO";

	/**
	 * 新活动
	 */
	public static final String NEW_ACTIVITY = "NEW_ACTIVITY";
	/**
	 * 新奖券
	 */
	public static final String NEW_CASH = "NEW_CASH";

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "onReceive - " + intent.getAction() + ", extras: ");

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			Log.d(TAG, "JPush用户注册成功");

		}
		else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接受到推送下来的自定义消息");

		}
		else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
			Log.d(TAG, "接受到推送下来的通知");

		}
		else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
			Log.d(TAG, "用户点击打开了通知");
			goToActivity(context, bundle);

		}
		else {
			Log.d(TAG, "Unhandled intent - " + intent.getAction());
		}
	}

	private void goToActivity(Context context, Bundle bundle) {
		String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
		Log.i("----------------", "extras:" + extras);
		String type = "";
		String title = "消息";
		String url = "";
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(extras);
			if (extras.indexOf("type") != -1) {
				url = jsonObject.getString("url");
				type = jsonObject.getString("type");
				title = jsonObject.getString("title");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Log.i("-----------------", "type:" + type + "  title:" + title + "  url:" + url);
		Intent cashIntent = new Intent();
		if (type.equals(NEW_CASH) || type.equals(NEW_GONGGAO) || type.equals(NEW_ACTIVITY) && !url.equals("")) {
			cashIntent.setClass(context, WebActivityShow.class);
			cashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			cashIntent.putExtra("url", url);
			cashIntent.putExtra("type", title);
		}
		else {
			cashIntent.setClass(context, MainActivity.class);
		}
		context.startActivity(cashIntent);
	}

	// 检测APP是否在运行
	private boolean isLauncherRunnig(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals("com.gaoshoubang") && info.baseActivity.getPackageName().equals("com.gaoshoubang")) {
				return true;
			}
		}
		return false;
	}
}