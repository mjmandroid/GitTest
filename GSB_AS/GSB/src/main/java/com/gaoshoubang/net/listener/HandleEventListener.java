package com.gaoshoubang.net.listener;

/**
 * Created by lzx on 2017/7/10.
 */
public interface HandleEventListener {

	void showProgress();

	void hideProgress();

	void onCatchOtherError(int code, String msg);

	void onLoginMsgInvalidate();

	void onRequestFailed();
}
