package com.gaoshoubang.ui.information.presenter;

import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.response.EmailResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.information.view.ModifyInfoView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/9.
 */

public class ModifyInfoImpl extends BasePresenterImpl<ModifyInfoView> {
	public void requestDoSetEmail(String email, String verifyCode) {
		NetworkManager.get(Urls.ACTION_MODIFY_EMAIL)
				.params(ParamsConstants.EMAIL_ADDRESS, email)
				.params(ParamsConstants.EMAIL_VERIFY_CODE, verifyCode)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterModifySuccess("邮箱设置成功");
						}
					}
				});
	}

	// 设置昵称
	public void requestDoSetNickname(String nickname) {
		NetworkManager.get(Urls.ACTION_MODIFY_NICKNAME)
				.params(ParamsConstants.NICKNAME, nickname)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> voidDataResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterModifySuccess("修改昵称成功");
						}
					}
				});
	}

	// 设置QQ
	public void requestDoSetQq(String qq) {
		NetworkManager.get(Urls.ACTION_MODIFY_QQ)
				.params(ParamsConstants.QQ, qq)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onSuccess(DataResponse<Void> dataResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterModifySuccess("修改QQ成功");
						}
					}
				});
	}


	// 获取邮箱验证码
	public void requestGetEmailVerifyCode(String email) {
		NetworkManager.get(Urls.ACTION_GET_EMAIL_CODE)
				.params(ParamsConstants.EMAIL_ADDRESS, email)
				.execute(new JsonCallbackWrapper<EmailResponse>(getView()) {
					@Override
					public void onSuccess(EmailResponse emailResponse, Call call, Response response) {
						if (emailResponse.used == 1) {
							if (getView() != null) {
								getView().showMsg("该邮箱已被使用");
							}
						}
						else {
							if (getView() != null) {
								getView().afterGetEmailCode();
							}
						}
					}
				});
	}
}
