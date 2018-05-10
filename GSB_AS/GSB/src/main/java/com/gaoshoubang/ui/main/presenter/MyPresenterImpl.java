package com.gaoshoubang.ui.main.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.base.TotalSelfBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.main.view.MyView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/26.
 */

public class MyPresenterImpl extends BasePresenterImpl<MyView> {
	public void requestMyself() {
		requestMyself(true);
	}

	public void requestMyself(boolean isProgress) {
		if (GsbApplication.getUserId() == null) {//没登陆时不加载数据
//			dismissLoad(true);
//			xRefreshView.stopRefresh();
			return;
		}
		NetworkManager.get(Urls.ACTION_MY).execute(new JsonCallbackWrapper<TotalSelfBean>(getView(), isProgress) {
			@Override
			public void onSuccess(TotalSelfBean totalSelfBean, Call call, Response response) {
				if (getView() != null) {
					getView().afterMyData(totalSelfBean);
				}
			}

			@Override
			public void onError(Call call, Response response, Exception e) {
				if (getView() != null) {
					getView().showErrorPage(0);
				}
			}

			@Override
			public void onReceiveOtherErr(int code, String msg) {
				super.onReceiveOtherErr(code, msg);
				if (getView() != null) {
					getView().showErrorPage(1);
				}
			}
		});
	}
}
