package com.gaoshoubang.ui.faq.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.response.LastVerBeanResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.faq.view.AboutView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/4.
 */

public class AboutPresenterImpl extends BasePresenterImpl<AboutView> {

	/**
	 * 检查更新
	 */
	public void checkVer() {
		NetworkManager.get(Urls.ACTION_GET_LATEST_VERSION)
				.params(ParamsConstants.CHANEL_CODE, GsbApplication.getGsbApplication().getChannelCode())
				.execute(new JsonCallbackWrapper<LastVerBeanResponse>(getView()) {

					@Override
					public void onSuccess(LastVerBeanResponse latestVerBeanTotalLastVerBean, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetLatestVer(latestVerBeanTotalLastVerBean.ver);
						}
					}
				});
	}


}
