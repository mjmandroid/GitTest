package com.gaoshoubang.ui.login.presenter;

import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.ui.login.view.LoginView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.net.convert.Convert;
import com.gaoshoubang.util.LockUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/8.
 */

public class LoginPresenterImpl extends BasePresenterImpl<LoginView> {
	public void requestMobileVerifyCode(String strPhone) {
		NetworkManager.get(Urls.ACTION_REG_GET_MOBILE_CODE)
				.params(ParamsConstants.MOBILE_NUM, strPhone)
				.params(ParamsConstants.VERIFY_METHOD, null)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMobileVerifyCode();
						}
					}
				});
	}

	// 验证手机是否已注册
	public void requestDoCheckMobile(String mobile) {
		NetworkManager.get2Login(Urls.ACTION_CHECK_IS_REG)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.execute(new JsonCallbackWrapper<CheckMobileResponse>(getView()) {
					@Override
					public void onLoginMsgInvalidate() {
						if (getView() != null) {
							getView().afterLoginInvalidate();
						}
					}

					@Override
					public void onSuccess(CheckMobileResponse registerBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterCheckMobile(registerBean);
						}
					}
				});
	}

	// 执行登录
	public void requestLogin(final String mobile, final String password, final Tuser tuser) {
		com.gaoshoubang.util.LogUtils.e("LoginPresenterImpl", "requestLogin:"+Convert.toJson(tuser));
		NetworkManager.get2Login(Urls.ACTION_LOGIN)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.PASSWORD, password)
				.params(ParamsConstants.TUSER, Convert.toJson(tuser))
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<LoginBean>(getView()) {
					@Override
					public void onLoginMsgInvalidate() {
						if (getView() != null) {
							getView().afterLoginInvalidate();
						}
					}

					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						LockUtils.saveLoginMessage(loginBean.getPhone(), password);//保存密码
						if (getView() != null) {
							getView().afterLoginSuccess(loginBean, tuser);
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						if (getView() != null) {
							getView().handleOtherErr(code, msg, tuser);
						}
					}
				});
	}

}
