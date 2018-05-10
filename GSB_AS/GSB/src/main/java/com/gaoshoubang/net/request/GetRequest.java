package com.gaoshoubang.net.request;


import com.gaoshoubang.net.HttpUtils;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * ================================================
 * 作    者：廖子尧
 * 版    本：1.0
 * 创建日期：2016/1/12
 * 描    述：Get请求的实现类，注意需要传入本类的泛型
 * 修订历史：
 * ================================================
 */
public class GetRequest extends BaseRequest<GetRequest> {
	private boolean isLogin;

	public GetRequest(String url) {
		this(url, false);
	}

	public GetRequest(String url, boolean isLogin) {
		super(url);
		method = "GET";
		this.isLogin = isLogin;
	}

	@Override
	public RequestBody generateRequestBody() {
		return null;
	}

	@Override
	public Request generateRequest() {
//        Request.Builder requestBuilder = HttpUtils.appendHeaders(headers);
		Request.Builder requestBuilder = getRequestBuilder();
		//get请求的参数需要需要转成json格式并加密
		url = HttpUtils.createUrlFromParams(baseUrl, params, isLogin);
		return requestBuilder.get().url(url).tag(tag).build();
	}
}