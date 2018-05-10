package com.gaoshoubang.ui.registration.presenter;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.registration.view.BindUserView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.net.convert.Convert;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class BindUserPresenterImpl extends BasePresenterImpl<BindUserView> {
	public void requestLogin(String mobile, final String passwd, Tuser tuser) {
		NetworkManager.get(Urls.ACTION_LOGIN)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.PASSWORD, passwd)
				.params(ParamsConstants.TUSER, Convert.toJson(tuser))
				.execute(new JsonCallbackWrapper<LoginBean>(getView()) {
					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterLoginSuccess(loginBean, passwd);
						}
					}
				});
	}
}
