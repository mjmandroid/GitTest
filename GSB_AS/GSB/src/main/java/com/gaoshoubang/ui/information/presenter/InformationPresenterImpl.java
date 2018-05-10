package com.gaoshoubang.ui.information.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.base.TotalSelfBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.information.view.InformationView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/9.
 */

public class InformationPresenterImpl extends BasePresenterImpl<InformationView> {
	public void requestMyself() {
		if (GsbApplication.getUserId() == null) {
			return;
		}
		NetworkManager.get(Urls.ACTION_MY)
				.execute(new JsonCallbackWrapper<TotalSelfBean>(getView()) {
					@Override
					public void onSuccess(TotalSelfBean totalSelfBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMyInfo(totalSelfBean.getSelf());
						}
					}
				});

	}
}
