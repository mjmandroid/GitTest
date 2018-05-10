package com.gaoshoubang.ui.common.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.response.NewsResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.ui.common.view.NewsView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/9.
 */

public class NewsPresenterImpl extends BasePresenterImpl<NewsView> {
	public void requestMessageMedia(final int p) {
		if (GsbApplication.getUserId() == null) {
			hideProgress();
			return;
		}
		NetworkManager.get(Urls.ACTION_NEWS)
				.params(ParamsConstants.PRODUCT_P, p)
				.params(ParamsConstants.PRODUCT_PAGE_SIZE, 15)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<NewsResponse>(getView()) {
					@Override
					public void onSuccess(NewsResponse listNewsResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetNews(p, listNewsResponse);
						}
					}
				});
	}
}
