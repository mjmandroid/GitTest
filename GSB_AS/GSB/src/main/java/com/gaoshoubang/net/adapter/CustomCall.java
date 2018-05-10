package com.gaoshoubang.net.adapter;

import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Response;
import com.gaoshoubang.net.callback.AbsCallback;
import com.gaoshoubang.net.callback.AbsCallbackWrapper;
import com.gaoshoubang.net.exception.InValidateException;
import com.gaoshoubang.net.exception.NetworkException;
import com.gaoshoubang.net.exception.OtherErrorException;
import com.gaoshoubang.net.request.BaseRequest;
import com.gaoshoubang.util.LogUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;

import okhttp3.Request;

/**
 */
public class CustomCall<T> implements Call<T> {

	private volatile boolean canceled;
	private boolean executed;
	private BaseRequest baseRequest;
	private okhttp3.Call rawCall;
	private AbsCallback<T> mCallback;

	private int currentRetryCount;
	private long mStartTime;

	public CustomCall(BaseRequest baseRequest) {
		this.baseRequest = baseRequest;
	}

	@Override
	public void execute(AbsCallback<T> callback) {

		synchronized (this) {
			if (executed) throw new IllegalStateException("Already executed.");
			executed = true;
		}
		mCallback = callback;
		//没有回调时,使用默认的回调处理
		if (mCallback == null) mCallback = new AbsCallbackWrapper<>();

		//请求执行前UI线程调用
		mCallback.onBefore(baseRequest);

		//构建请求
//		RequestBody requestBody = baseRequest.generateRequestBody();
		final Request request = baseRequest.generateRequest();
		rawCall = baseRequest.generateCall(request);
//		LogUtils.e("CustomCall", "execute:"+rawCall.request().url());
		if (canceled) {
			rawCall.cancel();
		}
		currentRetryCount = 0;
		mStartTime = System.currentTimeMillis();
		rawCall.enqueue(new okhttp3.Callback() {
			@Override
			public void onFailure(okhttp3.Call call, IOException e) {
				if (e instanceof SocketTimeoutException && currentRetryCount < baseRequest.getRetryCount()) {
					//超时重试处理
					currentRetryCount++;
					okhttp3.Call newCall = baseRequest.generateCall(call.request());
					newCall.enqueue(this);
				}
				else {
					LogUtils.e("CustomCall", "onFailure:" + call.request().url());
					mCallback.parseError(call, e);
					//请求失败，一般为url地址错误，网络错误等,并且过滤用户主动取消的网络请求
					if (!call.isCanceled()) {
						sendFailResultCallback(call, null, e);
					}
				}
			}

			@Override
			public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
				int responseCode = response.code();
			/*

			String url = request.url().toString();
				url = url.substring(url.indexOf("Api_v5//"), url.indexOf("?param"));
				long endTime = response.receivedResponseAtMillis();
				LogUtils.e("CustomCall", +(endTime - mStartTime )+"===="+ url);*/
				//响应失败，一般为服务器内部错误，或者找不到页面等
				if (responseCode == 404 || responseCode >= 500) {
					LogUtils.e("CustomCall", "onResponse:" + call.request().url());
					sendFailResultCallback(call, response, NetworkException.INSTANCE("服务器数据异常!"));
					return;
				}

				try {
					Response<T> parseResponse = parseResponse(response);
					T data = parseResponse.body();
					//网络请求成功回调
					sendSuccessResultCallback(data, call, response);
				} catch (InValidateException e) {
					sendInvalidateCallback(e);
				} catch (OtherErrorException e) {
					sendOtherErrorCallback(e);
				} catch (Exception e) {
					//一般为服务器响应成功，但是数据解析错误
					sendFailResultCallback(call, response, e);
				}
			}
		});
	}

	private void sendFailResultCallback(final okhttp3.Call call, final okhttp3.Response response, final Exception e) {
		NetworkManager.getInstance().getDelivery().post(new Runnable() {
			@Override
			public void run() {

				mCallback.onError(call, response, e);        //请求失败回调 （UI线程）
				mCallback.onAfter(null, e);              //请求结束回调 （UI线程）

			}

		});

	}

	private void sendInvalidateCallback(final InValidateException e) {
		NetworkManager.getInstance().getDelivery().post(new Runnable() {
			@Override
			public void run() {

				mCallback.onLoginMsgInvalidate();
				mCallback.onAfter(null, e);              //请求结束回调 （UI线程）


			}
		});
	}

	private void sendOtherErrorCallback(final OtherErrorException e) {
		NetworkManager.getInstance().getDelivery().post(new Runnable() {
			@Override
			public void run() {
				mCallback.onReceiveOtherErr(e.code, e.msg);
				mCallback.onAfter(null, e);              //请求结束回调 （UI线程）
			}
		});

	}


	/**
	 * 成功回调，发送到主线程
	 */
	private void sendSuccessResultCallback(final T t, final okhttp3.Call call, final okhttp3.Response response) {
		NetworkManager.getInstance().getDelivery().post(new Runnable() {
			@Override
			public void run() {
				mCallback.onSuccess(t, call, response);      //请求成功回调 （UI线程）
				mCallback.onAfter(t, null);                  //请求结束回调 （UI线程）

			}
		});
	}

	@Override
	public Response<T> execute() throws Exception {
		synchronized (this) {
			if (executed) throw new IllegalStateException("Already executed.");
			executed = true;
		}
		okhttp3.Call call = baseRequest.getCall();
		if (canceled) {
			call.cancel();
		}
		return parseResponse(call.execute());
	}

	private Response<T> parseResponse(okhttp3.Response rawResponse) throws Exception {
		//noinspection unchecked
		T body = (T) baseRequest.getConverter().convertSuccess(rawResponse);
		return Response.success(body, rawResponse);
	}

	@Override
	public boolean isExecuted() {
		return executed;
	}

	@Override
	public void cancel() {
		canceled = true;
		if (rawCall != null) {
			rawCall.cancel();
		}
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public Call<T> clone() {
		return new CustomCall<>(baseRequest);
	}

	@Override
	public BaseRequest getBaseRequest() {
		return baseRequest;
	}
}