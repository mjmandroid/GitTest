package com.gaoshoubang.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.fingerprint.FingerprintIdentify;
import com.gaoshoubang.fingerprint.base.BaseFingerprint;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.util.encrypt.Des3;

/**
 * Created by Administrator on 2017/5/23.
 */

public class LockUtils {
	//	private static boolean isTaskRunning;
	private static long time = -1;

	private LockUtils() {
	}

	public static LockUtils getInstance() {
		return LockHolder.holder;
	}

	private static class LockHolder {
		private static LockUtils holder = new LockUtils();
	}

	/**
	 * 如果用户已登录,且开启了手势则跳到解锁页,ps.开启指纹必须开启手势.
	 */
	public static boolean enterLockAct(final Activity activity) {
		FingerprintIdentify fingerprintIdentify = new FingerprintIdentify(activity, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
			@Override
			public void onCatchException(Throwable exception) {
				LogUtils.e(activity.getLocalClassName(), "onCatchException:" + exception.getLocalizedMessage());
			}
		});
		boolean fingerprintEnable = fingerprintIdentify.isFingerprintEnable();
		Intent intent = new Intent(activity, ActivityLock.class);
		if (UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()) != null) {
			//用户开启了手势解锁
			int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
			//开启了指纹,进入指纹验证状态
			if (fingerprintEnable) {
				if (fingerprintType == 3) {
					intent.putExtra("fingerprintType", 3);
				}
				else {
					intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
				}

			}
			else {
				//不支持指纹,或者没有录入
				intent.putExtra("fingerprintType", 2);
				UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), 2);
				LogUtils.e("BaseActivity", "isEnterLockAct:" + "不支持指纹,或者没有录入");
			}
			intent.putExtra("type", 1);     //解锁类型
			activity.startActivity(intent);
//			isTaskRunning = false;

			return true;
		}
		else {
//			isTaskRunning = false;
			return false;
		}

	}

	public static void enterLoginAct(Context context) {
		Intent intent = new Intent(context, ActivityLogin.class);
		context.startActivity(intent);
//		isTaskRunning = false;
	}

	/**
	 * 用户信息失效后,自动进入锁屏页或者登录页
	 *
	 * @param activity
	 */
	public static void autoEnterLockOrLogin(Activity activity) {
		if (time != -1) {
			if (System.currentTimeMillis() - time < 30 * 1000) {
				return;
			}
		}
//		if (!isTaskRunning){
//			isTaskRunning = true;
//		已经设置解锁进入解锁页,否则进入登录页,
		if (!enterLockAct(activity)) {
			enterLoginAct(activity);
		}
		time = System.currentTimeMillis();
	}

	/**
	 * @param mobile
	 * @param passwd
	 */
	public static void saveLoginMessage(String mobile, String passwd) {
		UserSharedPreferenceUtils.setMobile(mobile);// 保存手机号码
		try {
			String encode = Des3.encode(passwd);
			UserSharedPreferenceUtils.setPSW(encode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
