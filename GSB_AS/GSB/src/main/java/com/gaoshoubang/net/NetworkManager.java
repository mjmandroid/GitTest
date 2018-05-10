package com.gaoshoubang.net;

import android.os.Handler;
import android.os.Looper;

import com.gaoshoubang.net.request.GetRequest;
import com.gaoshoubang.net.request.PostRequest;
import com.gaoshoubang.util.LogUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.*;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/5/2.
 */

public class NetworkManager {
	/*默认超时时间,30s*/
	public static final long DEFAULT_MILLISECONDS = 30_000L;
	private OkHttpClient.Builder okHttpClientBuilder;
	private OkHttpClient mOkHttpClient;
	private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
	private Handler mDelivery;                                  //用于在主线程执行的调度器
	/**
	 * 主线程业务调度
	 */
	private int mRetryCount;

	private NetworkManager() {
		LogUtils.e("NetworkManager", "NetworkManager:cons");
		okHttpClientBuilder = new OkHttpClient.Builder();
		okHttpClientBuilder.connectTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
		okHttpClientBuilder.readTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
		okHttpClientBuilder.writeTimeout(DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
		okHttpClientBuilder.addInterceptor(new Interceptor() {
			@Override
			public Response intercept(Chain chain) throws IOException {

				LogUtils.e("NetworkManager", "intercept:" + chain.request().url().toString());
				return chain.proceed(chain.request());
			}
		});
		mDelivery = new Handler(Looper.getMainLooper());
	}

	public static NetworkManager getInstance() {
		return ManagerHolder.holder;
	}


	private static class ManagerHolder {
		private static NetworkManager holder = new NetworkManager();
	}


	/**
	 * 对外暴露 OkHttpClient,方便自定义
	 */
	public OkHttpClient.Builder getOkHttpClientBuilder() {
		return okHttpClientBuilder;
	}


	public Handler getDelivery() {
		return mDelivery;
	}

	public OkHttpClient getOkHttpClient() {
		if (mOkHttpClient == null) mOkHttpClient = okHttpClientBuilder.build();
		return mOkHttpClient;
	}

	public static PostRequest post(String url, File file) {
		return new PostRequest(url, file);
	}

	public static PostRequest post(String url) {
		return new PostRequest(url);
	}


	/**
	 * get请求
	 */
	public static GetRequest get(String url) {
		return new GetRequest(url);
	}

	/*
	* 登录发的请求, 登录要求的通用参数不一样
	* */
	public static GetRequest get2Login(String url) {
		return new GetRequest(url, true);
	}


	public int getRetryCount() {
		return mRetryCount;
	}

	public void setRetryCount(int retryCount) {
		mRetryCount = retryCount;
	}
}
