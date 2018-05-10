package com.gaoshoubang.ui.registration.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.registration.view.BindingView2;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.net.callback.StringCallback;
import com.gaoshoubang.net.convert.Convert;
import com.gaoshoubang.net.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class BindingPresenter2 extends BasePresenterImpl<BindingView2> {
	public void requestMobileVerifyCode(String mobile, final String method) {
		NetworkManager.get(Urls.ACTION_REG_GET_MOBILE_CODE)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.VERIFY_METHOD, null)
				.execute(new JsonCallback<DataResponse<Void>>() {
					@Override
					public void onReceiveOtherErr(int code, String msg) {
						onOtherError(code, msg);
					}

					@Override
					public void onLoginMsgInvalidate() {
						if (getView() != null) {
							getView().loginInvalidate();
						}
					}

					@Override
					public void onSuccess(DataResponse<Void> voidDataResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetVertifyCode(method);
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						if (getView() != null) {
							getView().onRequestFail();
						}
					}


					@Override
					public void onBefore(BaseRequest baseRequest) {
						super.onBefore(baseRequest);
						if (getView() != null) {
							getView().showProgress();
						}
					}

					@Override
					public void onAfter(DataResponse<Void> voidDataResponse, Exception e) {
						super.onAfter(voidDataResponse, e);
						if (getView() != null) {
							getView().hideProgress();
						}
					}
				});
	}

	public void requestCheckMobileVerifyCode(String mobile, String code) {

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
						if (getView() != null) {
							getView().loginInvalidate();
						}
					}

					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterCheckVertifyPhoneNum();
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						if (getView() != null) {
							getView().onRequestFail();

						}
					}



				});
	}

	public void requestDoAuthRcmderCode(String referral) {

		NetworkManager.get(Urls.ACTION_CHECK_REFERRAL_CODE)
				.params(ParamsConstants.REFERRAL_CODE, referral)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						if (getView() != null) {
							getView().afterCheckRcmderCode(s);
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						if (getView() != null) {
							getView().onRequestFail();
						}
					}

				});
	}

	public void requestDoReg(String mobile, String nickname, String gender, final String passwd, String referral, String imsi, int isLogin, Tuser tuser) {
		NetworkManager.get(Urls.ACTION_REGISTER)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.NICKNAME, nickname)
				.params(ParamsConstants.GENDER, gender)
				.params(ParamsConstants.PASSWORD, passwd)
				.params(ParamsConstants.REFERRAL_CODE, referral)
				.params(ParamsConstants.IMEI_CODE, GsbApplication.getGsbApplication().getIMEI())
				.params(ParamsConstants.PROMOTER_CODE, GsbApplication.getGsbApplication().getChannelCode())
				.params(ParamsConstants.IMSI_CODE, imsi)
				.params(ParamsConstants.IS_LOGIN, isLogin)
				.params(ParamsConstants.TUSER, Convert.toJson(tuser))
				.execute(new JsonCallbackWrapper<LoginBean>(getView()) {
					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterDoReg(loginBean, passwd);
						}
					}
				});
	}


	@Override
	public void onOtherError(int stateCode, String msg) {
		if (getView() != null) {
			getView().showOtherErrMsg(stateCode, msg);
		}
	}
}
