package com.gaoshoubang.ui.password.presenter;

import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.ui.password.view.FindPwdView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/8.
 */

public class FindPwdPresenterImpl extends BasePresenterImpl<FindPwdView> {
	public void requestPwdMobileVerifyCode(String mobile) {
		NetworkManager.get(Urls.ACTION_PSW_MOBILE_VERIFY_CODE)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.VERIFY_METHOD, "SMS")
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> dataResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterPwdMobileVerifyCode();
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						if (getView() != null) {
							getView().showOtherErrMsg(code, msg);
						}
					}
				});
	}

}
