package com.gaoshoubang.net.request;

import com.gaoshoubang.net.HttpUtils;
import com.gaoshoubang.net.ParamsConstants;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

/**
 * Created by lzx on 2017/6/12.
 */

public class PostRequest extends BaseRequest<PostRequest> {

	private File mFile;

	public PostRequest(String url) {
		super(url);
		method = "POST";
	}

	public PostRequest(String url, File file) {
		super(url);
		mFile = file;
		method = "POST";
	}

	@Override
	public RequestBody generateRequestBody() {
		return null;
	}

	@Override
	public Request generateRequest() {
		Request.Builder requestBuilder = getRequestBuilder();
//		RequestBody requestBody = RequestBody.create(MediaType.parse(""), file);
//		requestBody.
		RequestBody tempBody = null;
		if (mFile != null) {
			tempBody = RequestBody.create(MediaType.parse("image/png"), mFile);
		}

		MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("param", HttpUtils.paramToString(params, false, true));
		//头像上传
		if (tempBody != null) {
			requestBodyBuilder.addFormDataPart(ParamsConstants.PORTRAIT_TITLE, mFile.getName(), tempBody);
		}

		return requestBuilder.url(url).post(requestBodyBuilder.build()).tag(tag).build();
	}
}
