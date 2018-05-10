package com.gaoshoubang.net.callback;

import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.base.view.BaseView;
import com.gaoshoubang.net.listener.HandleEventListener;
import com.gaoshoubang.net.request.BaseRequest;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import okhttp3.Call;
import okhttp3.Response;

/*
*
* @des json 解析回调的包装类 ,封装 请求前, 请求 后的一些UI处理
* */
public abstract class JsonCallbackWrapper<T extends SuperResponse> extends JsonCallback<T> {
	private BaseView mView;
	private boolean mIsProgress = true;
	private HandleEventListener mListener;

	public JsonCallbackWrapper(BaseView view) {
		mView = view;
	}

	public JsonCallbackWrapper(HandleEventListener listener) {
		mListener = listener;
	}


	public JsonCallbackWrapper(BaseView view, boolean isProgress) {
		mView = view;
		mIsProgress = isProgress;
	}

	@Override
	public void onBefore(BaseRequest baseRequest) {
		super.onBefore(baseRequest);

		if (mView != null && mIsProgress) {
			mView.showProgress();
		}
		if (mListener != null) {
			mListener.showProgress();
		}

	}

	@Override
	public void onAfter(T t, Exception e) {
		super.onAfter(t, e);
		if (mView != null && mIsProgress) {
			mView.hideProgress();
		}
		if (mListener != null) {
			mListener.hideProgress();
		}
	}

	/*
	* 其他状态码的处理回调,不包含登录失效的情况
	* @param code 状态码
	* @param msg  错误信息
	* */
	public void onReceiveOtherErr(int code, String msg) {
		LogUtils.e("JsonCallback", "错误代码：" + code + "，错误信息：" + msg);
		if (mView != null) {
			mView.showOtherErrMsg(code, msg);
		}
		if (mListener != null) {
			mListener.onCatchOtherError(code, msg);
		}
	}

	public void onLoginMsgInvalidate() {
		//登录失效后,删除loginValue
		UserSharedPreferenceUtils.deleteLoginValue();
		if (mView != null) {
			mView.loginInvalidate();
		}
		if (mListener != null) {
			mListener.onLoginMsgInvalidate();
		}

	}

	@Override
	public void onError(Call call, Response response, Exception e) {
		if (mView != null) {
			mView.onRequestFail();
		}
		if (mListener != null) {
			mListener.onRequestFailed();
		}
	}
}