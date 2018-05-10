package com.gaoshoubang.ui.main.presenter;

import com.gaoshoubang.bean.RecommendBean;
import com.gaoshoubang.bean.RecommendGongHuo;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.base.TotalRelWindowBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.main.view.RecommendView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/23.
 */

public class RecommendPresenterImpl extends BasePresenterImpl<RecommendView> {
	// 请求网络
	public void requestRecommend() {
		requestRecommend(true);//兼容
	}

	// 请求网络,不带进度
	public void requestRecommend(boolean isProgress) {
		NetworkManager.get(Urls.ACTION_RECOMMEND).execute(new JsonCallbackWrapper<DataResponse<RecommendBean>>(getView(), isProgress) {
			@Override
			public void onSuccess(DataResponse<RecommendBean> recommendBeanGenericResponse, Call call, Response response) {
				if (recommendBeanGenericResponse.data == null) {
					return;
				}
				if (getView() != null) {
					getView().afterGetRecommendData(recommendBeanGenericResponse.data);
				}
			}
		});
	}

	/**
	 * 活动公告
	 */
	public void requestActivityAndNotice() {
		NetworkManager.get(Urls.ACTION_HOME_ACTIVITY_NOTICE)
				.execute(new JsonCallbackWrapper<DataResponse<RecommendGongHuo>>(getView(), false) {
					         @Override
					         public void onSuccess(DataResponse<RecommendGongHuo> recommendGongHuoGenericResponse, Call call, Response response) {
						         if (recommendGongHuoGenericResponse.data == null) {
							         return;
						         }
						         if (getView() != null) {
							         getView().afterGetActivityAndNoticeData(recommendGongHuoGenericResponse.data);
						         }
					         }
				         }

				);

	}

	// 关闭公告
	public void requestActivityAndNoticeClose(String id) {
		NetworkManager.get(Urls.ACTION_HOME_ACTIVITY_CLOSE_NOTICE)
				.params(ParamsConstants.NOTICE_ID, id)
				.execute(new JsonCallbackWrapper<DataResponse<Void>>(getView(), false) {
					@Override
					public void onSuccess(DataResponse<Void> simpleResponse, Call call, Response response) {
						//doNothing
					}
				});
	}

	// 浮窗请求
	public void requestRelWindow() {
		NetworkManager.get(Urls.ACTION_POPUP_INFO)
				.execute(new JsonCallbackWrapper<TotalRelWindowBean>(getView(), false) {
					@Override
					public void onSuccess(TotalRelWindowBean listTotalRelWindowBean, Call call, Response response) {
						if (listTotalRelWindowBean.data == null || listTotalRelWindowBean.data.size() == 0) {
							return;
						}
						// "show" 数据状态:1不显示,2显示
						if (listTotalRelWindowBean.show == 2) {
							if (getView() != null) {
								getView().afterGetRelWindowData(listTotalRelWindowBean.data);
							}
						}
					}
				});

	}
}
