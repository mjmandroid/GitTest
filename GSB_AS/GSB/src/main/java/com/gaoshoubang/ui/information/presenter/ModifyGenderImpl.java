package com.gaoshoubang.ui.information.presenter;

import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.information.view.ModifyGenderView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/12.
 */

public class ModifyGenderImpl extends BasePresenterImpl<ModifyGenderView> {
	public void requestDoSetGender(String gender) {
		NetworkManager.get(Urls.ACTION_MODIFY_GENDER)
				.params(ParamsConstants.GENDER, gender)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView()) {
					@Override
					public void onLoginMsgInvalidate() {
						loginInvalidate();
					}

					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterModifySuccess();
						}
					}
				});
	}
}
