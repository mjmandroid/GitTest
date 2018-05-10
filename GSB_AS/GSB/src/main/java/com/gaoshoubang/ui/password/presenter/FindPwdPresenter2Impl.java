package com.gaoshoubang.ui.password.presenter;

import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.ui.password.view.FindPwdView2;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/8.
 */

public class FindPwdPresenter2Impl extends BasePresenterImpl<FindPwdView2> {
	public void requestDoResetPasswd(String mobile, String mobileVerifyCode, String passwd) {
		NetworkManager.get(Urls.ACTION_RESET_PSW)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.MOBILE_VERIFY_CODE, mobileVerifyCode)
				.params(ParamsConstants.PASSWORD, passwd)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<DataResponse<Void>>(getView()) {


					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterResetPSW();
						}
					}

				});
	}

	public void requestPwdMobileVerifyCode(String mobile, final String method) {
		NetworkManager.get(Urls.ACTION_PSW_MOBILE_VERIFY_CODE)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.VERIFY_METHOD, method)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMobileVerifyCode(method);
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						getView().receiveErrGetVerifyCode();
					}

				});

	}

}
