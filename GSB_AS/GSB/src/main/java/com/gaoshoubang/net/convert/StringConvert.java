package com.gaoshoubang.net.convert;

import okhttp3.Response;

/**
 * Created by lzx on 2017/6/5.
 */


public class StringConvert implements Converter<String> {

	public static StringConvert create() {
		return ConvertHolder.convert;
	}

	private static class ConvertHolder {
		private static StringConvert convert = new StringConvert();
	}

	@Override
	public String convertSuccess(Response value) throws Exception {
		return value.body().string();
	}
}

