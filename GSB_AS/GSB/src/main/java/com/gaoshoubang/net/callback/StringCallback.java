package com.gaoshoubang.net.callback;

import com.gaoshoubang.net.convert.StringConvert;

import okhttp3.Response;

/**
 * Created by lzx on 2017/6/5.
 */
/*
* 普通的String请求,不需要处理进度条等UI操作
* */

public abstract class StringCallback extends AbsCallback<String> {

	@Override
	public String convertSuccess(Response response) throws Exception {
		String s = StringConvert.create().convertSuccess(response);
		response.close();
		return s;
	}
}

