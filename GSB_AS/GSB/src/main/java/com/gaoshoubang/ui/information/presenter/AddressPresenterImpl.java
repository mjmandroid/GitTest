package com.gaoshoubang.ui.information.presenter;

import com.gaoshoubang.bean.AddressBean;
import com.gaoshoubang.bean.CityStreetBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.information.view.AdressView;
import com.gaoshoubang.net.JsonUtil;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.callback.StringCallback;
import com.gaoshoubang.net.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AddressPresenterImpl extends BasePresenterImpl<AdressView> {

	/**
	 * 获取街道信息
	 *
	 * @param id
	 */
	public void requestMygetArea(String id) {
		NetworkManager.get(Urls.ACTION_ADDRESS_CATALOGUE)
				.params(ParamsConstants.ADDRESS_ID, id)
				.execute(new JsonCallback<DataResponse<CityStreetBean>>() {
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
					public void onSuccess(DataResponse<CityStreetBean> cityStreetBeanGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetCityStreetBean(cityStreetBeanGenericResponse.data);
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

	/**
	 * 保存地址
	 *
	 * @param bean
	 */
	public void requestMySetArea(AddressBean bean) {
		NetworkManager.get(Urls.ACTION_SET_ADDRESS)
				.params(ParamsConstants.ADDRESS_NAME, bean.name)
				.params(ParamsConstants.ADDRESS_PHONE, bean.phone)
				.params(ParamsConstants.ADDRESS_PROVINCE, bean.province)
				.params(ParamsConstants.ADDRESS_CITY, bean.city)
				.params(ParamsConstants.ADDDRESS_AREA, bean.area)
				.params(ParamsConstants.ADDRESS_TOWN, bean.town)
				.params(ParamsConstants.ADDRESS, bean.address)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						if (getView() == null) {
							return;
						}
						int stateCode = JsonUtil.getStateCode(s);
						switch (stateCode) {
							case 200:
								String state = JsonUtil.getValue(JsonUtil.getDataStr(s), "bool");
								getView().afterSetArea(state);
								break;
							case 4007001:
								getView().loginInvalidate();
								break;
							default:
								getView().showOtherErrMsg(JsonUtil.getStateCode(s), JsonUtil.getMessage(s));
								break;

						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {

					}

					@Override
					public void onBefore(BaseRequest baseRequest) {
						super.onBefore(baseRequest);
						if (getView() != null) {
							getView().showProgress();
						}
					}

					@Override
					public void onAfter(String s, Exception e) {
						super.onAfter(s, e);
						if (getView() != null) {
							getView().hideProgress();
						}
					}
				});
	}

}
