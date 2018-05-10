package com.gaoshoubang.net.exception;

/**
 * Created by lzx on 2017/6/7.
 * 定义一个登录信息失效后的异常
 */

public class InValidateException extends Exception {
	private InValidateException() {
	}

	public static InValidateException getInstance() {
		return InvalidateHolder.holder;
	}

	private static class InvalidateHolder {
		private static InValidateException holder = new InValidateException();
	}
}
