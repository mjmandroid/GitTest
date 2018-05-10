package com.gaoshoubang.net.callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/5.
 */

public class AbsCallbackWrapper<T> extends AbsCallback<T> {
	@Override
	public T convertSuccess(Response value) throws Exception {
		value.close();
		return (T) value;
	}

	@Override
	public void onSuccess(T t, Call call, Response response) {
	}

	@Override
	public void onError(Call call, Response response, Exception e) {

	}
}

