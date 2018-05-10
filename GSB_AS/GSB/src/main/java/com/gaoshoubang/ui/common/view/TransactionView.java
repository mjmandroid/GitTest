package com.gaoshoubang.ui.common.view;

import com.gaoshoubang.bean.response.TransactionResponse;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/29.
 */

public interface TransactionView extends BaseView {
	void afterGetTransactionRecord(TransactionResponse transactionResponse);
}
