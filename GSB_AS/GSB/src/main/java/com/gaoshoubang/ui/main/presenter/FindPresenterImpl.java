package com.gaoshoubang.ui.main.presenter;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.FindBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.response.NewsResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.main.view.FindView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/26.
 */

public class FindPresenterImpl extends BasePresenterImpl<FindView> {
	public void requestGradeSelf() {
		requestGradeSelf(true);
	}

	public void requestGradeSelf(boolean isProgress) {
		if (GsbApplication.getUserId() == null) {//没登陆时不加载数据
//			dismissLoad(true);
//			xRefreshView.stopRefresh();
			return;
		}
		NetworkManager.get(Urls.ACTION_FIND)
				.execute(new JsonCallbackWrapper<DataResponse<FindBean>>(getView(), isProgress) {
					@Override
					public void onSuccess(DataResponse<FindBean> findBeanGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetFindData(findBeanGenericResponse.data);
						}
					}
				});
	}

	// 媒体报道
	public void requestMessageMedia(int pageSize) {
		if (GsbApplication.getUserId() == null) {
//			dismissLoad(true);
//			xRefreshView.stopRefresh();
			return;
		}

		NetworkManager.get(Urls.ACTION_NEWS)
				.params(ParamsConstants.PRODUCT_P, 1)
				.params(ParamsConstants.PRODUCT_PAGE_SIZE, pageSize)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<NewsResponse>(getView()) {


					@Override
					public void onSuccess(NewsResponse newsResponse, Call call, Response response) {
//						onGetMessageMedia(newsResponse.data);
						if (getView() != null) {
							getView().afterGetMessageMedia(newsResponse.data);
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						if (getView() != null) {
							getView().showFooterRefresh();
						}
					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						super.onReceiveOtherErr(code, msg);
						if (getView() != null) {
							getView().showFooterRefresh();
						}
					}

					@Override
					public void onAfter(NewsResponse newsResponse, Exception e) {
						super.onAfter(newsResponse, e);
//						hideDialogMessageMedia();
					}
				});

	}
}
