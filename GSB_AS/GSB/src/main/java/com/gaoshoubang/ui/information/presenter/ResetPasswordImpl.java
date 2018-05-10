package com.gaoshoubang.ui.information.presenter;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.password.view.ResetPasswordView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/12.
 */

public class ResetPasswordImpl extends BasePresenterImpl<ResetPasswordView> {
	public void requestDoSetPasswd(String oldPwdStr, String newPwdStr,int hasPass) {
		NetworkManager.get(Urls.ACTION_MODIFY_PSW)
				.params(ParamsConstants.OLD_PASSWORD, oldPwdStr)
				.params(ParamsConstants.NEW_PASSWORD, newPwdStr)
				.params(ParamsConstants.HAS_PASS,hasPass)
				.execute(new JsonCallbackWrapper<LoginBean>(getView()) {
					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterLoginSuccess(loginBean);
						}
					}
				});
	}
}
