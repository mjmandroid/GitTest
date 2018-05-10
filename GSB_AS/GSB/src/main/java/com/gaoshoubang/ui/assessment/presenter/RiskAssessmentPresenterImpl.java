package com.gaoshoubang.ui.assessment.presenter;

import com.gaoshoubang.bean.RiskQuestionBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.assessment.view.RiskAssessmentView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/7/3.
 */

public class RiskAssessmentPresenterImpl extends BasePresenterImpl<RiskAssessmentView> {

	public RiskAssessmentPresenterImpl() {
	}

	public void getQuestionList() {
		NetworkManager.get(Urls.ACTION_RISK_QUESTION_LIST)
				.params(ParamsConstants.QUESTION_TYPE_ID, UserSharedPreferenceUtils.getRiskAssessmentID())
				.execute(new JsonCallbackWrapper<DataResponse<List<RiskQuestionBean>>>(getView()) {
					@Override
					public void onSuccess(DataResponse<List<RiskQuestionBean>> dataResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetQuestionList(dataResponse.data);
						}
					}
				});
	}


}
