package com.gaoshoubang.net.callback;

import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.base.view.BaseView;
import com.gaoshoubang.net.request.BaseRequest;

/**
 * Created by lzx on 2017/6/5.
 *
 * @des 带进度条处理的回调, 在请求之前显示进度调, 结束后关闭进度条
 */

public abstract class ProgressCallback<T extends SuperResponse> extends JsonCallbackWrapper<T> {
	public ProgressCallback(BaseView view) {
		super(view);
	}

	@Override
	public void onBefore(BaseRequest baseRequest) {
//		super.onBefore(baseRequest);
	}

	@Override
	public void onAfter(T t, Exception e) {
//		super.onAfter(t, e);
	}

}
