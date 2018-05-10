package com.gaoshoubang.net.exception;

/**
 * Created by lzx on 2017/6/7.
 * 服务器返回的其他错误异常,
 */

public class OtherErrorException extends Exception {
	public int code; //返回的状态码
	public String msg;//返回的错误信息

	public OtherErrorException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
}
