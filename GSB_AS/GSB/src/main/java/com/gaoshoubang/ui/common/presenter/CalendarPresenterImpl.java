package com.gaoshoubang.ui.common.presenter;

import com.gaoshoubang.bean.CalenderBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.common.view.MyCalendarView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.request.BaseRequest;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class CalendarPresenterImpl extends BasePresenterImpl<MyCalendarView> {
	public void requestRecMoneyDay() {
		NetworkManager.get(Urls.ACTION_RETURNED_MONEY_CALENDER)
				.execute(new JsonCallback<DataResponse<List<CalenderBean>>>() {
					@Override
					public void onReceiveOtherErr(int code, String msg) {
						onOtherError(code, msg);
					}

					@Override
					public void onLoginMsgInvalidate() {
						loginInvalidate();
					}

					@Override
					public void onSuccess(DataResponse<List<CalenderBean>> listGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetRecMoneyDay(listGenericResponse.data);
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						if (getView() != null) {
							getView().onRequestFail();
						}
					}


					@Override
					public void onAfter(DataResponse<List<CalenderBean>> listGenericResponse, Exception e) {
						super.onAfter(listGenericResponse, e);
						hideProgress();
					}

					@Override
					public void onBefore(BaseRequest baseRequest) {
						super.onBefore(baseRequest);
						showProgress();
					}
				});
	}

}
