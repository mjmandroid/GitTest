package com.gaoshoubang.net;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.HttpParams;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.util.encrypt.Des3;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by lzx on 2017/6/5.
 */

public class HttpUtils {
	/**
	 * @param url hostUrl
	 * <p>
	 * <p>
	 * 将传递进来的参数拼接成 url
	 * 返回编码后GET请求的url,
	 */
	public static String createUrlFromParams(String url, Map<String, String> params, boolean isLogin) {
//		JsonObject tempJson = new JsonObject();
//		JsonObject defaultParam;
		String paramsStr = paramToString(params, isLogin, false);
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		sb.append("?param=");
		sb.append(paramsStr);
		return sb.toString();
	}

	/**
	 * 遍历map集合,将参数转为编码后的字符串
	 * @param params
	 * @param isLogin
	 * @param isPostMethod
	 * @return
	 */
	public static String paramToString(Map<String, String> params, boolean isLogin, boolean isPostMethod) {
		JsonObject tempJson = new JsonObject();
		JsonObject defaultParam;
		if (isLogin) {
			defaultParam = addLoginDefaultParam(tempJson);
		}
		else {
			defaultParam = addDefaultParam(tempJson);//添加默认参数
		}
		if (params != null && params.size() > 0) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				defaultParam.addProperty(entry.getKey(), entry.getValue());
			}
		}
		LogUtils.e("HttpUtils", "paramToString:" + defaultParam.toString());
		return encodeParam(defaultParam.toString(), isPostMethod);
	}

	/**
	 * @param param 需要加密的参数字符串
	 *
	 * @return 返回加密后的参数字符串, 默认使用utf-8编码,编码成url格式字符串
	 */

	private static String encodeParam(String param, boolean isPostMethod) {
		String finalStr = null;
		try {
			String encryptStr = Des3.encode(param);
			if (!isPostMethod) {
				finalStr = URLEncoder.encode(encryptStr, "utf-8");
			}
			else {
				finalStr = encryptStr;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalStr;
	}

	/**
	 * 获取请求必须参数
	 *
	 * @return
	 */
	private static JsonObject addDefaultParam(JsonObject object) {
		object.addProperty("appId", Urls.APPID);
		object.addProperty("loginValue",
				UserSharedPreferenceUtils
						.getLoginValue());
		object.addProperty("verNum",
				GsbApplication.getGsbApplication().getPackageInfo().versionName);
		object.addProperty("time", getTimeAdd30());
		if (GsbApplication.getUserId() != null) {
			object.addProperty("uid", GsbApplication.getUserId());
		}
		return object;
	}

	private static JsonObject addLoginDefaultParam(JsonObject object) {
		object.addProperty("appId", Urls.APPID);
		object.addProperty("verNum",
				GsbApplication.getGsbApplication().getPackageInfo().versionName);
		object.addProperty("time", getTimeAdd30());
		return object;
	}

	/**
	 * 获取当前系统时间 +30秒
	 *
	 * @return
	 */
	public static String getTimeAdd30() {
		String time = String.valueOf(System.currentTimeMillis() + 30000L);
		return time;
	}
}
