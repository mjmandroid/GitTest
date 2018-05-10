package com.gaoshoubang.ui.registration.presenter;

import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.registration.view.BindingView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class BindingPresenterImpl extends BasePresenterImpl<BindingView> {
	public void requestDoCheckMobile(String mobile) {
		NetworkManager.
				get(Urls.ACTION_CHECK_IS_REG)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.execute(new JsonCallbackWrapper<CheckMobileResponse>(getView()) {
					@Override
					public void onSuccess(CheckMobileResponse checkMobileResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterCheckNum(checkMobileResponse);
						}
					}
				});
	}
}
