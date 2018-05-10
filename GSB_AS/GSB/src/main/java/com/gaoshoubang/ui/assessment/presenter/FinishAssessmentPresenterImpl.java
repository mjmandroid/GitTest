package com.gaoshoubang.ui.assessment.presenter;

import com.gaoshoubang.bean.AnswerBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.assessment.view.FinishAssessmentView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.convert.Convert;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/7/4.
 */

public class FinishAssessmentPresenterImpl extends BasePresenterImpl<FinishAssessmentView> {
	public void postAnswers(List<AnswerBean> saveAnswers) {
		String answers = Convert.toJson(saveAnswers);
		LogUtils.e("FinishAssessmentPresenterImpl", "postAnswers:" + answers);
		NetworkManager.post(Urls.ACTION_RISK_SAVE_ANSWER)
				.params(ParamsConstants.QUESTION_TYPE_ID, UserSharedPreferenceUtils.getRiskAssessmentID())
				.params(ParamsConstants.ANSWER_NAME, answers)
				.execute(new JsonCallback<DataResponse<Integer>>() {


					@Override
					public void onReceiveOtherErr(int code, String msg) {
						onOtherError(code, msg);
					}

					@Override
					public void onLoginMsgInvalidate() {
						loginInvalidate();
					}

					@Override
					public void onSuccess(DataResponse<Integer> result, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetResult(result.data);
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
}
