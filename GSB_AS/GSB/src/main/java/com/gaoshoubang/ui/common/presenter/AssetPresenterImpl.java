package com.gaoshoubang.ui.common.presenter;

import com.gaoshoubang.bean.AssetBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.common.view.AssetView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AssetPresenterImpl extends BasePresenterImpl<AssetView> {
	public void loadData() {
		NetworkManager.get(Urls.ACTION_ASSETS)
				.execute(new JsonCallback<DataResponse<AssetBean>>() {
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
					public void onSuccess(DataResponse<AssetBean> assetBeanGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMyMoney(assetBeanGenericResponse.data);
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						if (getView() != null) {
							getView().onRequestFail();
						}
					}

					@Override
					public void onAfter(DataResponse<AssetBean> assetBeanGenericResponse, Exception e) {
						super.onAfter(assetBeanGenericResponse, e);
						if (getView() != null) {
							getView().hideProgress();
						}
					}

					@Override
					public void onBefore(BaseRequest baseRequest) {
						super.onBefore(baseRequest);
						if (getView() != null) {
							getView().showProgress();
						}
					}
				});
	}

}
