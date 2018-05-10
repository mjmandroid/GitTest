package com.gaoshoubang.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.view.View;

import com.gaoshoubang.GsbApplication;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserSharedPreferenceUtils {


	public static GsbApplication sApplication = GsbApplication.getGsbApplication();

	public static void setApplication(GsbApplication application) {
		sApplication = application;
	}

	/*风险测评相关参数======start=======*/
	public static boolean setRiskAssessmentScore(int riskAssessmentScore) {
		SharedPreferences sp = getUIDSp(sApplication.getUserId());
		return sp.edit().putInt("RiskScore", riskAssessmentScore).commit();
	}

	public static int getRiskAssessmentScore() {
		SharedPreferences sp = getUIDSp(sApplication.getUserId());
		return sp.getInt("RiskScore", -1);
	}

	public static boolean setRiskAssessmentID(int riskAssessmentID) {
		SharedPreferences sp = getUIDSp(sApplication.getUserId());
		return sp.edit().putInt("RiskId", riskAssessmentID).commit();
	}

	public static int getRiskAssessmentID() {
		SharedPreferences sp = getUIDSp(sApplication.getUserId());
		return sp.getInt("RiskId", 3);
	}
	/*风险测评相关参数======end=======*/

	/**
	 * 写入第一次使用
	 *
	 * @param firstUser
	 *
	 * @return
	 */
	public boolean setFirst(boolean firstUser) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("FirstUser", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putBoolean("thefirstuser", firstUser).commit();
	}

	/**
	 * 获取第一次使用
	 *
	 * @return
	 */
	public static boolean getFirst() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("FirstUser", Context.MODE_PRIVATE);
		boolean first = sharedPreferences.getBoolean("thefirstuser", true);
		return first;
	}

	public static boolean setHideMoneyState(boolean state) {
		SharedPreferences sp = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.edit().putBoolean("HideMoneyState", state).commit();

	}

	public static boolean getHideMoneyState() {
		SharedPreferences sp = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean("HideMoneyState", false);

	}

	/**
	 * 保存手机登录账号
	 *
	 * @param mobile
	 *
	 * @return
	 */
	public static boolean setMobile(String mobile) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("Mobile", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("Mobile", mobile).commit();
	}

	/**
	 * 保存手机登录账号
	 *
	 * @param mobile
	 *
	 * @return
	 */
	public static boolean setPSW(String mobile) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("Mobile", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("param", mobile).commit();
	}

	public static String getPSW() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("Mobile", Context.MODE_PRIVATE);
		return sharedPreferences.getString("param", "");
	}

	/**
	 * 获取输入密码次数
	 *
	 * @return
	 */
	public static int getLoginNum() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("num", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("num", 6);
	}

	/**
	 * 保存手机登录账号
	 *
	 * @param num
	 *
	 * @return
	 */
	public static boolean setLoginNum(int num) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("num", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putInt("num", num).commit();
	}

	/**
	 * 获取手机登录账号
	 *
	 * @return
	 */
	public static String getMobile() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("Mobile", Context.MODE_PRIVATE);
		return sharedPreferences.getString("Mobile", "");
	}

	/**
	 * 保存昵称
	 *
	 * @param nickName
	 *
	 * @return
	 */
	public static boolean setNickName(String nickName) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("nickName", nickName).commit();
	}

	/**
	 * 获取昵称
	 *
	 * @return
	 */
	public static String getNickName() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getString("nickName", "");
	}

	/**
	 * 写入loginValue
	 *
	 * @param loginValue
	 */
	public static void setLoginValue(String loginValue) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("loginValue", Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("loginValue", loginValue).commit();
	}

	/**
	 * 获取loginValue
	 *
	 * @param
	 *
	 * @return
	 */
	public static String getLoginValue() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("loginValue", Context.MODE_PRIVATE);
		return sharedPreferences.getString("loginValue", "");
	}

	/**
	 * 清除loginValue
	 */
	public static void deleteLoginValue() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("loginValue", Context.MODE_PRIVATE);
		sharedPreferences.edit().clear();
	}

	/**
	 * 写入cookie
	 *
	 * @param c_key
	 * @param c_val
	 */
	public static void setCookie(String c_key, String c_val) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		sharedPreferences.edit().putString("c_key", c_key).commit();
		sharedPreferences.edit().putString("c_val", c_val).commit();
	}

	/**
	 * 获取cookie
	 *
	 * @param keyOrVal
	 *
	 * @return
	 */
	public static String getCookie(String keyOrVal) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getString(keyOrVal, null);
	}

	/**
	 * 保存手势密码
	 *
	 * @param lockPwd
	 *
	 * @return
	 */
	public static boolean setGestureLock(String lockPwd, String uid) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.edit().putString("lockPwd", lockPwd).commit();
	}

	private static SharedPreferences getUIDSp(String uid) {
		return sApplication.getSharedPreferences(uid, Context.MODE_PRIVATE);
	}

	public static boolean setIsThirdPartLogin(boolean state, String uid) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.edit().putBoolean("isThirdPartLogin", state).commit();
	}

	public static boolean getIsThirdPartLogin(String uid) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.getBoolean("isThirdPartLogin", false);
	}


	/**
	 * 获得手势密码
	 *
	 * @return
	 */
	public static String getGestureLock(String uid) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.getString("lockPwd", null);
	}


	public static boolean getFingerprintGuideSetting() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("fingerprintSetting", false);
	}


	public static boolean setFingerprintGuideSetting(boolean flag) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putBoolean("fingerprintSetting", flag).commit();
	}

	public static boolean getFingerprintGuideToggle() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean("fingerprintToggle", false);
	}


	public static boolean setFingerprintGuideToggle(boolean flag) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putBoolean("fingerprintToggle", flag).commit();
	}

	/**
	 * 获得解锁类型
	 *
	 * @return type 1 有指纹未开启,2 没有指纹,3有指纹已开启,4,指纹验证成功,还没开启手势
	 */
	public static int getFingerprintType(String uid) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.getInt("fingerprint", -1);
	}

	/**
	 * 设置解锁类型
	 * type 1 有指纹未开启,2 没有指纹,3有指纹已开启,4,指纹验证成功,还没开启手势
	 *
	 * @param type 1 有指纹未开启,2 没有指纹,3有指纹已开启,4,指纹验证成功,还没开启手势
	 *
	 * @return
	 */
	public static boolean setFingerprintType(String uid, int type) {
		SharedPreferences sharedPreferences = getUIDSp(uid);
		return sharedPreferences.edit().putInt("fingerprint", type).commit();
	}

	/**
	 * 蒙层点击
	 *
	 * @param isMaskLayer
	 *
	 * @return
	 */
	public static boolean setMaskLayer(boolean isMaskLayer, String maskLayerKey) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("MaskLayer", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putBoolean(maskLayerKey, isMaskLayer).commit();
	}

	/**
	 * 获取蒙层状态
	 *
	 * @return
	 */
	public static boolean getMaskLayer(String maskLayerKey) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("MaskLayer", Context.MODE_PRIVATE);
		boolean maskLayer = sharedPreferences.getBoolean(maskLayerKey, true);
		return maskLayer;
	}

	/**
	 * 写入浮窗显示日期
	 *
	 * @param showDate
	 *
	 * @return
	 */
	public static boolean setPopShowDate(String showDate) {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("popuWindowShowdate", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("popuWindowShowdate", showDate).commit();
	}

	/**
	 * 获取显示日期
	 *
	 * @return
	 */
	public static String getPopShowDate() {
		SharedPreferences sharedPreferences = sApplication.getSharedPreferences("popuWindowShowdate", Context.MODE_PRIVATE);
		return sharedPreferences.getString("popuWindowShowdate", "");
	}

	/**
	 * 保存头像图片
	 */
	public static void saveBitmap(String url) {
		ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String arg0, View arg1) {

			}

			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {

			}

			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap bm) {
				try {
					FileOutputStream out = new FileOutputStream(FilesPath.headPhoto);
					bm.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onLoadingCancelled(String arg0, View arg1) {

			}
		});
	}
}
