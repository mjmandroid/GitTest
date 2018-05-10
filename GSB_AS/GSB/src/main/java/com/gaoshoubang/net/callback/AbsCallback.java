package com.gaoshoubang.net.callback;


import com.gaoshoubang.net.convert.Converter;
import com.gaoshoubang.net.request.BaseRequest;
import com.gaoshoubang.util.LogUtils;

import okhttp3.Call;
import okhttp3.Response;


public abstract class AbsCallback<T> implements Converter<T> {

	/**
	 * 请求网络开始前，UI线程
	 */
//    可以处理显示进度
	public void onBefore(BaseRequest baseRequest) {

	}

	/**
	 * 对返回数据进行操作的回调， UI线程
	 */
	public abstract void onSuccess(T t, Call call, Response response);


	/**
	 * 请求失败，响应错误，数据解析错误等，都会回调该方法， UI线程
	 */
	public abstract void onError(Call call, Response response, Exception e);

	/**
	 * 网络失败结束之前的回调
	 */
	public void parseError(Call call, Exception e) {

	}

	/**
	 * 请求网络结束后，UI线程
	 */
	public void onAfter(T t, Exception e) {
		if (e != null) e.printStackTrace();
	}

	public void onReceiveOtherErr(int code, String msg) {
		LogUtils.e("JsonCallback", "错误代码：" + code + "，错误信息：" + msg);
	}

	public void onLoginMsgInvalidate() {

	}

   /* *//** 目前没用到
	 * Post执行上传过程中的进度回调，get请求不回调，UI线程
	 *
	 * @param currentSize  当前上传的字节数
	 * @param totalSize    总共需要上传的字节数
	 * @param progress     当前上传的进度
	 * @param networkSpeed 当前上传的速度 字节/秒
	 *//*
	public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
    }

    *//**
	 * 执行下载过程中的进度回调，UI线程
	 *
	 * @param currentSize  当前下载的字节数
	 * @param totalSize    总共需要下载的字节数
	 * @param progress     当前下载的进度
	 * @param networkSpeed 当前下载的速度 字节/秒
	 *//*
	public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
    }*/
}