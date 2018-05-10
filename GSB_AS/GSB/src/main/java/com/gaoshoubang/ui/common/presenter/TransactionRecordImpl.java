package com.gaoshoubang.ui.common.presenter;

import com.gaoshoubang.bean.response.TransactionResponse;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.ui.common.view.TransactionView;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.request.BaseRequest;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by lzx on 2017/6/29.
 */

public class TransactionRecordImpl extends BasePresenterImpl<TransactionView> {
	public void requestMyPayList(final int type, final int p) {
		NetworkManager.get(Urls.ACTION_TRANSACTION_RECORD)
				.params(ParamsConstants.TYPE, type)
				.params(ParamsConstants.PRODUCT_P, p)
				.params(ParamsConstants.PRODUCT_PAGE_SIZE, 15)
				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<TransactionResponse>(getView()) {
					@Override
					public void onSuccess(TransactionResponse transactionResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetTransactionRecord(transactionResponse);
						}
					}
				});
	}

	public void requestMyPayList(final int type, final int p, boolean isShowLoading) {
		NetworkManager.get(Urls.ACTION_TRANSACTION_RECORD)
				.params(ParamsConstants.TYPE, type)
				.params(ParamsConstants.PRODUCT_P, p)
				.params(ParamsConstants.PRODUCT_PAGE_SIZE, 15)

				.execute(new com.gaoshoubang.net.callback.JsonCallbackWrapper<TransactionResponse>(getView()) {
					@Override
					public void onSuccess(TransactionResponse transactionResponse, Call call, Response response) {
						if (getView() != null) {
							getView().afterGetTransactionRecord(transactionResponse);
						}
					}

					@Override
					public void onBefore(BaseRequest baseRequest) {

					}

					@Override
					public void onAfter(TransactionResponse transactionResponse, Exception e) {

					}
				});
	}
}
