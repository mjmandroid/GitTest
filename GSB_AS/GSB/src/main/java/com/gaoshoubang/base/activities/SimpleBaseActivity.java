package com.gaoshoubang.base.activities;

import com.gaoshoubang.base.presenter.BasePresenterImpl;

/**
 * 简单的基类,不用上来加载数据,可以根据需求来继承
 * Created by lzx on 2017/6/26.
 */

public abstract class SimpleBaseActivity<T extends BasePresenterImpl> extends BaseActivity<T> {
	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}
}
