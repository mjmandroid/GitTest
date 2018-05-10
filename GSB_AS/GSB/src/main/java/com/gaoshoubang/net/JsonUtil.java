package com.gaoshoubang.net;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
	public static final String STATE = "state";
	public static final String MSG = "msg";
	public static final String DATA = "data";

	/**
	 * 获取错误码对应提示
	 *
	 * @param code
	 *
	 * @return
	 */
	public static String getCodeInfo(int code) {
		switch (code) {
			case 200:
				return "请求成功";
			case -200:
				return "请求失败";
			case 1:
				return "没有数据";
			case -400:
				return "无网络";
			case 4001001:
				return "App ID不能为空";
			case 4001002:
				return "App ID不合法";
			case 4002001:
				return "版本号不能为空";
			case 4002002:
				return "版本号不合法";
			case 4003001:
				return "请求提交时间不能为空";
			case 4003002:
				return "请求提交时间不合法";
			case 4003003:
				return "请求提交时间已过期";
			case 4004001:
				return "用户ID不能为空";
			case 4004002:
				return "用户ID不合法";
			case 4005001:
				return "被关注者ID不能为空";
			case 4005002:
				return "被关注者ID不合法";
			case 4006001:
				return "合作方用户未绑定";
			case 4007001:
				return "登录信息失效,请退出后重新登录";
			case 4101001:
				return "手机号码不能为空";
			case 4101002:
				return "手机号码格式错误";
			case 4101003:
				return "该手机号已注册，请登录";
			case 4101004:
				return "手机号码未注册";
			case 4103001:
				return "手机验证码不能为空";
			case 4103002:
				return "手机验证码不合法";
			case 4103003:
				return "手机验证码错误";
			case 4103004:
				return "手机验证码已过期";
			case 4104001:
				return "邮箱验证码不能为空";
			case 4104002:
				return "邮箱验证码不合法";
			case 4104003:
				return "邮箱验证码错误";
			case 4104004:
				return "邮箱验证码已过期";
			case 4105001:
				return "昵称不能为空";
			case 4105002:
				return "昵称不合法";
			case 4105003:
				return "昵称已存在";
			case 4106001:
				return "密码不能为空";
			case 4106002:
				return "密码不合法";
			case 4106003:
				return "密码错误";
			case 4107001:
				return "问题内容不能为空";
			case 4107002:
				return "问题内容不合法";
			case 4108001:
				return "栏目ID不能为空";
			case 4108002:
				return "栏目ID不合法";
			case 4109001:
				return "QQ不能为空";
			case 4109002:
				return "QQ不合法";
			case 4110001:
				return "性别不能为空";
			case 4110002:
				return "性别不合法";
			case 4111001:
				return "问题ID不能为空";
			case 4111002:
				return "问题ID不合法";
			case 4112001:
				return "新登录密码不能为空";
			case 4112002:
				return "新登录密码不合法";
			case 4113001:
				return "上传的头像不能为空";
			case 4113002:
				return "上传的头像不合法";
			case 4114001:
				return "收藏ID不能为空";
			case 4114002:
				return "收藏ID不合法";
			case 4115001:
				return "关键字不能为空";
			case 4115002:
				return "关键字不合法";
			case 4116001:
				return "关键字ID不能为空";
			case 4116002:
				return "关键字ID不合法";
			case 4117001:
				return "回答内容不能为空";
			case 4117002:
				return "回答内容不合法";
			case 4118001:
				return "开始时间不能为空";
			case 4118002:
				return "开始时间不合法";
			case 4118003:
				return "开始时间不能早于1个月";
			case 4119001:
				return "结束时间不能为空";
			case 4119002:
				return "结束时间不合法";
			case 4119003:
				return "结束时间不能小于开始时间";
			case 4120001:
				return "推荐码不能为空";
			case 4120002:
				return "推荐码不合法";
			case 4120003:
				return "推荐码不存在";
			case 5001001:
				return "数据中心接口调用失败";
			case 5101001:
				return "短信发送失败";
			case 5102001:
				return "提问失败";
			case 5103001:
				return "邮件发送失败";
			case 5104001:
				return "上传文件失败";
			case 5105001:
				return "回答失败";
			case -4004:
				return "请到高搜易官网进行升级";
			default:
				return "状态码state:" + code;
		}
	}

	/**
	 * 在指定的json里查找key对应的值
	 *
	 * @param json
	 * @param key
	 *
	 * @return
	 */
	public static String getValue(String json, String key) {
		if (json == null || key == null) {
			return null;
		}
		String value = "";
		try {
			JSONObject jsonObject = new JSONObject(json);
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获得状态码
	 *
	 * @param result
	 *
	 * @return
	 */
	public static int getStateCode(String result) {
		int stateCode = -1;
		try {
			JSONObject jsonObject = new JSONObject(result);
			stateCode = jsonObject.getInt(STATE);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return stateCode;
	}

	/**
	 * 获得对应的信息
	 *
	 * @param result
	 *
	 * @return
	 */
	public static String getMessage(String result) {
		String message = "";
		try {
			JSONObject jsonObject = new JSONObject(result);
			message = jsonObject.getString(MSG);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return message;
	}

	/**
	 * 获得data的内容
	 *
	 * @param result
	 *
	 * @return
	 */
	public static String getDataStr(String result) {
		String datastr = "";
		try {
			JSONObject jsonObject = new JSONObject(result);
			datastr = jsonObject.getString(DATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return datastr;
	}

	/**
	 * 把jsonObject转换成指定对象
	 * <p>
	 * 对象数组
	 *
	 * @param clazz 对象
	 *
	 * @return
	 */
	public static <T> List<T> getObjectList(String data, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		if (data.equals("")) {
			return list;
		}
		try {
			Gson gson = new Gson();
			JSONArray jsonArray = new JSONArray(data);
			for (int i = 0; i < jsonArray.length(); i++) {
				list.add(gson.fromJson(jsonArray.getString(i), clazz));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

}
