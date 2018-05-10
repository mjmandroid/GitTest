package com.gaoshoubang.util;

import android.content.Context;

/**
 * Created by Administrator on 2017/5/24.
 */

public class CheckInputUtils {
	/**
	 * 根据密码和验证码进行验证,并提示相应的信息
	 *
	 * @param context
	 * @param code
	 * @param psw
	 */
	public static boolean checkInputPSWAndCode(Context context, String code, String psw) {
		String regex = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}";
		if (code.length() == 0) {
			ToastUtil.toast(context, "请输入短信验证码");
			return false;
		}

		if (psw.length() == 0) {
			ToastUtil.toast(context, "请设置6-16位密码");
			return false;
		}
		if (psw.length() < 6) {
			ToastUtil.toast(context, "请设置6-16位密码");
			return false;
		}

		if (psw.indexOf(" ") != -1) {
			ToastUtil.toast(context, "密码不能包含空格，汉字");
			return false;
		}

		if (!psw.matches(regex)) {
			ToastUtil.toast(context, "密码必须含有数字,字母和符号中的两种");
			return false;
		}

		if (psw.length() != psw.getBytes().length) {
			ToastUtil.toast(context, "密码不能包含中文字符");
			return false;
		}
		return true;
	}

	/**
	 * @param context
	 * @param psw
	 *
	 * @return
	 */
	public static boolean checkInputPSW(Context context, String psw) {
		String regex = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}";

		if (psw.length() == 0) {
			ToastUtil.toast(context, "请输入6-16位密码");
			return false;
		}
		if (psw.length() < 6) {
			ToastUtil.toast(context, "请输入6-16位密码");
			return false;
		}

		if (psw.indexOf(" ") != -1) {
			ToastUtil.toast(context, "密码不能包含空格，汉字");
			return false;
		}

		if (!psw.matches(regex)) {
			ToastUtil.toast(context, "输入的密码必须含有数字,字母和符号中的两种");
			return false;
		}

		if (psw.length() != psw.getBytes().length) {
			ToastUtil.toast(context, "输入的密码不能包含中文字符");
			return false;
		}
		return true;
	}
}
