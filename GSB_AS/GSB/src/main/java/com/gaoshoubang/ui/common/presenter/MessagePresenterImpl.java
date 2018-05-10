package com.gaoshoubang.ui.common.presenter;

import com.gaoshoubang.bean.base.TotalMessageBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.common.view.MessageView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/9.
 */

public class MessagePresenterImpl extends BasePresenterImpl<MessageView> {
	public void requestMyMessage() {
		NetworkManager.get(Urls.ACTION_MESSAGE)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<TotalMessageBean>(getView()) {

					@Override
					public void onSuccess(TotalMessageBean messages, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetMessages(messages.data);
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						super.onReceiveOtherErr(code, msg);

					}
				});
	}
}
