package com.gaoshoubang.net.request;

import android.text.TextUtils;

import com.gaoshoubang.bean.RecommendGongHuo;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.net.Constants;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.adapter.CustomCall;
import com.gaoshoubang.net.callback.AbsCallback;
import com.gaoshoubang.net.convert.Converter;
import com.gaoshoubang.util.encrypt.Md5Utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public abstract class BaseRequest<R extends BaseRequest> {

	protected String url;
	protected String method;
	protected String baseUrl;
	protected Object tag;
	protected long readTimeOut;
	protected long writeTimeOut;
	protected long connectTimeout;
	protected int retryCount;
	//	protected HttpParams params = new HttpParams(); //添加的param
	protected LinkedHashMap<String, String> headers = new LinkedHashMap<>();             //添加的header
	protected LinkedHashMap<String, String> params = new LinkedHashMap<>();             //添加的header
	protected List<Interceptor> interceptors = new ArrayList<>();   //额外的拦截器
	private Request mRequest;
	private AbsCallback mCallback;
	private Converter mConverter;
	private Request.Builder mRequestBuilder;

	public BaseRequest(String url) {
		this.url = url;
		baseUrl = url;
		NetworkManager networkManager = NetworkManager.getInstance();
		/**
		 * 添加默认的请求头
		 */
		mRequestBuilder = new Request.Builder();
		mRequestBuilder.addHeader(Constants.FIRST_KEY, Constants.TOKEN)
				.addHeader(Constants.SECOND_KEY,
						Md5Utils.encrypt(Constants.KEY + Md5Utils.encrypt(Md5Utils.encrypt(Constants.KEY + Constants.TOKEN))));
		//超时重试次数
		retryCount = networkManager.getRetryCount();
	}

	@SuppressWarnings("unchecked")
	public R url(String url) {
		this.url = url;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R tag(Object tag) {
		this.tag = tag;
		return (R) this;
	}


	@SuppressWarnings("unchecked")
	public R headers(String key, String value) {
		headers.put(key, value);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R params(String key, String value) {
		if (TextUtils.isEmpty(value)||"null".equals(value)) {
			return (R) this;
		}
		params.put(key, value);
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R params(String key, int value) {
		params.put(key, value + "");
		return (R) this;
	}


	@SuppressWarnings("unchecked")
	public R readTimeOut(long readTimeOut) {
		this.readTimeOut = readTimeOut;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R writeTimeOut(long writeTimeOut) {
		this.writeTimeOut = writeTimeOut;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R connTimeOut(long connTimeOut) {
		this.connectTimeout = connTimeOut;
		return (R) this;
	}


	@SuppressWarnings("unchecked")
	public R setCallback(AbsCallback callback) {
		this.mCallback = callback;
		return (R) this;
	}

	@SuppressWarnings("unchecked")
	public R addInterceptor(Interceptor interceptor) {
		interceptors.add(interceptor);
		return (R) this;
	}


	public String getUrl() {
		return url;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public Object getTag() {
		return tag;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public Request getRequest() {
		return mRequest;
	}

	public AbsCallback getCallback() {
		return mCallback;
	}

	public Converter getConverter() {
		return mConverter;
	}

	/**
	 * 返回当前的请求方法
	 * GET,POST,HEAD,PUT,DELETE,OPTIONS
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * 根据不同的请求方式和参数，生成不同的RequestBody
	 */
	public abstract RequestBody generateRequestBody();

	/** 对请求body进行包装，用于回调上传进度 *//*
	public RequestBody wrapRequestBody(RequestBody requestBody) {
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody);
        progressRequestBody.setListener(new ProgressRequestBody.Listener() {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength, final long networkSpeed) {
                OkGo.getInstance().getDelivery().post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCallback != null) mCallback.upProgress(bytesWritten, contentLength, bytesWritten * 1.0f / contentLength, networkSpeed);
                    }
                });
            }
        });
        return progressRequestBody;
    }*/

	/**
	 * 根据不同的请求方式，将RequestBody转换成Request对象
	 */
	public abstract Request generateRequest();

	/**
	 * 根据当前的请求参数，生成对应的 Call 任务
	 */
	public okhttp3.Call generateCall(Request request) {
		mRequest = request;
		if (readTimeOut <= 0 && writeTimeOut <= 0 && connectTimeout <= 0 && interceptors.size() == 0) {
			return NetworkManager.getInstance().getOkHttpClient().newCall(request);
		}else {
			OkHttpClient.Builder newClientBuilder = NetworkManager.getInstance().getOkHttpClient().newBuilder();
			if (readTimeOut > 0) newClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
			if (writeTimeOut > 0) {
				newClientBuilder.writeTimeout(writeTimeOut, TimeUnit.MILLISECONDS);
			}
			if (connectTimeout > 0) {
				newClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
			}
			if (interceptors.size() > 0) {
				for (Interceptor interceptor : interceptors) {
					newClientBuilder.addInterceptor(interceptor);
				}
			}
			return newClientBuilder.build().newCall(request);
		}
	}

	public Request.Builder getRequestBuilder() {
		return mRequestBuilder;
	}

	/**
	 * 获取同步call对象
	 */
	public okhttp3.Call getCall() {
		//构建请求体，返回call对象
//        RequestBody requestBody = generateRequestBody();
//        mRequest = generateRequest(wrapRequestBody(requestBody));
		return generateCall(mRequest);
	}

	/**
	 * 普通调用，阻塞方法，同步请求执行
	 *
	 * @param recommendGongHuoGenericResponse
	 */
	public Response execute(DataResponse<RecommendGongHuo> recommendGongHuoGenericResponse) throws IOException {
		return getCall().execute();
	}

	/**
	 * 非阻塞方法，异步请求，但是回调在子线程中执行
	 */
	@SuppressWarnings("unchecked")
	public <T> void execute(AbsCallback<T> callback) {
		mCallback = callback;
		mConverter = callback;
		new CustomCall<T>(this).execute(callback);
	}
}