package com.gaoshoubang.net;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/2.
 */

public abstract class Callback implements okhttp3.Callback {
	public void onStart() {

	}

	public void onFinish() {

	}

	public void onResponse(Call call, Response response) throws IOException {

	}

	public abstract void onResponse(Call call, String result) throws IOException;
}
