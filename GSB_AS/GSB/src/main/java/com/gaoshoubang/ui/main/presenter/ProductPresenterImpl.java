package com.gaoshoubang.ui.main.presenter;

import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.main.view.ProductView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/26.
 */

public class ProductPresenterImpl extends BasePresenterImpl<ProductView> {
	public void requestProduct() {
		requestProduct(true);
	}

	public void requestProduct(boolean isProgress) {
		NetworkManager.get(Urls.ACTION_PRODUCT)
				.params(ParamsConstants.PRODUCT_P, 1)
				.params(ParamsConstants.PRODUCT_PAGE_SIZE, 10)
				.params(ParamsConstants.PRODUCT_STATUS, "10")
				.params(ParamsConstants.PRODUCT_TAG, 0)
				.execute(new JsonCallbackWrapper<DataResponse<List<ProductBean>>>(getView(), isProgress) {
					@Override
					public void onSuccess(DataResponse<List<ProductBean>> listGenericResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetProduct(listGenericResponse.data);
						}
					}
				});
	}
}
