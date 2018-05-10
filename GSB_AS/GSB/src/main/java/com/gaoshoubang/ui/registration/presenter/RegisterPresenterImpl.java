package com.gaoshoubang.ui.registration.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.response.ReferralCodeResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.registration.view.RegisterView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/9.
 */

public class RegisterPresenterImpl extends BasePresenterImpl<RegisterView> {
	public void requestMobileVerifyCode(final String method, String mobile) {
		NetworkManager.get(Urls.ACTION_REG_GET_MOBILE_CODE)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.VERIFY_METHOD, null)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMobileVerifyCode(method);
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						super.onReceiveOtherErr(code, msg);
						if (getView() != null) {
							getView().changeMethod();

						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						if (getView() != null) {
							getView().changeMethod();
						}
					}
				});
	}

	// 检验手机和验证码
	public void requestCheckMobileVerifyCode(String code, String mobile) {
		NetworkManager.get(Urls.ACTION_REG_CHECK_MOBILE_CODE)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.MOBILE_VERIFY_CODE, code)
				.execute(new JsonCallback<DataResponse<Void>>() {
					@Override
					public void onReceiveOtherErr(int code, String msg) {
						onOtherError(code, msg);
					}

					@Override
					public void onLoginMsgInvalidate() {

					}

					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterCheckMobileVerifyCode();
						}
					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						onRequestFailed();
					}
				});

	}

	// 验证高手码
	public void requestDoAuthReferralCode(final String referralCode, final String mobile, final String psw) {
		NetworkManager.get(Urls.ACTION_CHECK_REFERRAL_CODE)
				.params(ParamsConstants.REFERRAL_CODE, referralCode)
				.execute(new JsonCallback<ReferralCodeResponse>() {

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						onOtherError(code, msg);
					}

					@Override
					public void onLoginMsgInvalidate() {
						loginInvalidate();
					}

					@Override
					public void onSuccess(ReferralCodeResponse referralCodeResponse, Call call, Response response) {
						if (referralCodeResponse.existRcmderCode == 2) {
							requestDoReg(mobile, null, null, psw, referralCode, null, 2);
						}
						else {
							if (getView() != null) {
								getView().showMsg("高手码不存在");
							}
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						onRequestFailed();
					}

				});
	}

	// 注册
	public void requestDoReg(final String mobile, String nickname, String gender, final String passwd, String referralCode, String imsi, final int isLogin) {
//		object.addProperty("promoterCode", GsbApplication.getGsbApplication().getChannelCode());
		NetworkManager.get(Urls.ACTION_REGISTER)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.NICKNAME, nickname)
				.params(ParamsConstants.GENDER, gender)
				.params(ParamsConstants.PASSWORD, passwd)
				.params(ParamsConstants.IMEI_CODE, GsbApplication.getGsbApplication().getIMEI())
				.params(ParamsConstants.REFERRAL_CODE, referralCode)
				.params(ParamsConstants.IMSI_CODE, imsi)
				.params(ParamsConstants.IS_LOGIN, isLogin)
				.params(ParamsConstants.PROMOTER_CODE, GsbApplication.getGsbApplication().getChannelCode())
//				.params(ParamsConstants.TUSER, Convert.toJson(tuser))
				.execute(new JsonCallbackWrapper<LoginBean>(getView()) {
					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterRegister(loginBean, mobile, passwd);
						}
					}

				});
	}
}
