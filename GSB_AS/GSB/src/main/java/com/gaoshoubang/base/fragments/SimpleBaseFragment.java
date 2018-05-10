package com.gaoshoubang.base.fragments;

import com.gaoshoubang.base.presenter.BasePresenterImpl;

/**
 * 简单的基类,不用加载数据
 * <p>
 * Created by lzx on 2017/6/26.
 */

public abstract class SimpleBaseFragment<T extends BasePresenterImpl> extends BaseFragment<T> {
	@Override
	public T getPresenter() {
		return null;
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}
}
