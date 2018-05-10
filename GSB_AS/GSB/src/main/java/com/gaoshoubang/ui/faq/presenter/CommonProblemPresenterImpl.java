package com.gaoshoubang.ui.faq.presenter;

import com.gaoshoubang.bean.Problem;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.faq.view.CommonProView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class CommonProblemPresenterImpl extends BasePresenterImpl<CommonProView> {
	public void requestHelpShow() {
		NetworkManager.get(Urls.ACTION_COMMON_PROBLEM)
				.execute(new JsonCallbackWrapper<DataResponse<List<Problem>>>(getView()) {
					@Override
					public void onSuccess(DataResponse<List<Problem>> listGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetHelp(listGenericResponse.data);
						}
					}
				});


	}


}
