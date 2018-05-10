package com.gaoshoubang.net.exception;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧）Github地址：https://github.com/jeasonlzy
 * 版    本：1.0
 * 创建日期：16/8/28
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class NetworkException extends Exception {

	public static NetworkException INSTANCE(String msg) {
		return new NetworkException(msg);
	}

	public NetworkException() {
		super();
	}

	public NetworkException(String detailMessage) {
		super(detailMessage);
	}

	public NetworkException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public NetworkException(Throwable throwable) {
		super(throwable);
	}
}