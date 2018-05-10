package com.gaoshoubang.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.net.callback.StringCallback;
import com.gaoshoubang.util.CommonUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import okhttp3.*;

/**
 * 保存配置信息
 *
 * @author Cisco
 */
public class ConfigUtils {
	private static final String TAG = "ConfigUtils";
	private static ConfigUtils instance;
	private static Context context;
	private LoadDialog loadDialog;

	private ConfigUtils() {
	}

	public static ConfigUtils getInstance() {
		if (instance == null) {
			synchronized (ConfigUtils.class) {
				if (instance == null) {
					instance = new ConfigUtils();
				}
				context = GsbApplication.getGsbApplication(); //静态context,应该选用长生命周期的context
			}
		}
		return instance;
	}

	/**
	 * 写入配置信息
	 *
	 * @param cnfInfo
	 *
	 * @return
	 */
	public boolean setCnfInfo(String cnfInfo) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("AppConfigInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("appConfig", cnfInfo).commit();
	}

	/**
	 * 获取配置信息
	 *
	 * @return
	 */
	public CnfListBean getCnfInfo() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("AppConfigInfo", Context.MODE_PRIVATE);
		String appConfig = sharedPreferences.getString("appConfig", "");
		if (appConfig.equals("")) {
			return null;
		}
		CnfListBean cnfListBean = new Gson().fromJson(JsonUtil.getValue(appConfig, "cnfList"), CnfListBean.class);
		return cnfListBean;
	}

	/**
	 * 获取配置信息
	 *
	 * @return
	 */
	public void getCnfInfo(OnGetConfigInfo onGetConfigInfo) {
		CnfListBean cnfListBean;
		SharedPreferences sharedPreferences = context.getSharedPreferences("AppConfigInfo", Context.MODE_PRIVATE);
		String appConfig = sharedPreferences.getString("appConfig", "");
		if (!TextUtils.isEmpty(appConfig)) {
			try {
				cnfListBean = new Gson().fromJson(JsonUtil.getValue(appConfig, "cnfList"), CnfListBean.class);
				onGetConfigInfo.success(cnfListBean);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				onGetConfigInfo.onFail();
			}
			return;
		}
		requestAppConfig(onGetConfigInfo);
	}

	// 若没有保存到配置信息,则重新缓存
	private void requestAppConfig(final OnGetConfigInfo onGetConfigInfo) {
		NetworkManager.get(Urls.ACTION_APPCONFIG).execute(new StringCallback() {
			@Override
			public void onSuccess(String s, Call call, okhttp3.Response response) {
				onGetConfig(s, onGetConfigInfo);
			}

			@Override
			public void onError(Call call, okhttp3.Response response, Exception e) {
				Log.i(TAG, "配置信息获取失败");
				onFailed(onGetConfigInfo);
			}

		});
	}

	private void onFailed(OnGetConfigInfo onGetConfigInfo) {
//        HttpServer.checkLoadFailReason(context);
		CommonUtils.checkLoadFailReason(context);
		if (onGetConfigInfo != null) {
			onGetConfigInfo.onFail();
		}
//		loadDialog.dismiss();
	}


	private void onGetConfig(String result, OnGetConfigInfo onGetConfigInfo) {
		setCnfInfo(result);
		CnfListBean cnfListBean = null;
		try {
			cnfListBean = new Gson().fromJson(JsonUtil.getValue(result, "cnfList"), CnfListBean.class);
			if (onGetConfigInfo != null) {
				onGetConfigInfo.success(cnfListBean);
			}
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
			if (onGetConfigInfo != null) {
				onGetConfigInfo.onFail();
			}
		}
	}

	/*
	 * 保存第一次输入错误密码的时间
	 */
	public boolean setErrorPwdTime(long errorPwdTime) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putLong("setErrorPwdTime", errorPwdTime).commit();
	}

	/*
	 * 获取第一次输入错误密码的时间
	 */
	public long getErrorPwdTime() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getLong("setErrorPwdTime", 0);
	}

	/*
	 * 保存输入错误密码的次数
	 */
	public boolean setErrorPwdCount(int errorPwdCount) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putLong("setErrorPwdCount", errorPwdCount).commit();
	}

	/*
	 *  保存输入错误密码的次数
	 */
	public int getErrorPwdCount() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.getInt("setErrorPwdCount", 0);
	}


	/**
	 * 保存uid
	 *
	 * @param uid
	 *
	 * @return
	 */
	public boolean setUid(String uid) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("uid", uid).commit();
	}


	/**
	 * 获取uid
	 *
	 * @return
	 */
	public String
	getUid() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String uid = sharedPreferences.getString("uid", null);
		return uid;
	}

	/**
	 * 保存登录信息
	 *
	 * @param loginInfo
	 *
	 * @return
	 */
	public boolean setLoginInfo(String loginInfo) {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().putString("loginInfo", loginInfo).commit();
	}

	/**
	 * 获取登录信息
	 *
	 * @return
	 */
	public String getLoginInfo() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		String uid = sharedPreferences.getString("loginInfo", null);
		return uid;
	}

	/**
	 * 清除用户信息
	 */
	public boolean clearLoginInfo() {
		SharedPreferences sharedPreferences = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		return sharedPreferences.edit().clear().commit();
	}

	public interface OnGetConfigInfo {
		void success(CnfListBean cnfListBean);

		void onFail();
	}

}
