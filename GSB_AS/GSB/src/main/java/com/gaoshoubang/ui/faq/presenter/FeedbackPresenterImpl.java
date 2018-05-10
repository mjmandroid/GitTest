package com.gaoshoubang.ui.faq.presenter;

import com.gaoshoubang.bean.response.FeedbackResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.faq.view.FeedbackView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class FeedbackPresenterImpl extends BasePresenterImpl<FeedbackView> {
	public void requestCustomerBack(String description) {
		NetworkManager.get(Urls.ACTION_FEEDBACK)
				.params(ParamsConstants.FEEDBACK_DES, description)
				.execute(new JsonCallbackWrapper<FeedbackResponse>(getView()) {
					@Override
					public void onSuccess(FeedbackResponse feedbackResponse, Call call, Response response) {
						if (feedbackResponse.saveProblem == 1) {
							if (getView() != null) {
								getView().afterFeedback();
							}
						}
						else {
							if (getView() != null) {
								getView().showMsg("服务器在忙,请稍后重试!");
							}
						}
					}
				});
	}

}
