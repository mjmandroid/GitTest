package com.gaoshoubang.base.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.base.view.BaseView;
import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.util.LockUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.widget.MaterialRippleLayout;
import com.gaoshoubang.widget.PromptDialog;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类
 */
public abstract class BaseFragment<T extends BasePresenterImpl> extends Fragment implements BaseView {
	public T mPresenter;
	protected View contentView;
	protected LoadDialog loadDialog;

	@Override
	public void startActivity(Intent intent) {
		getActivity().startActivity(intent);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (contentView == null) {
			contentView = inflater.inflate(getLayoutId(), container, false);
		}
		if (contentView != null) {
			return contentView;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		loadDialog = new LoadDialog(getActivity());
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = getPresenter();
		if (mPresenter != null) {
			mPresenter.attachView(this);
		}
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initView();
		initEvent();
		loadData();
	}
/*
*
*
* 初始化方法
* */

	/*布局文件的ID*/
	protected abstract int getLayoutId();

	protected abstract void initView();

	protected abstract void initEvent();

	protected abstract void bindData();

	protected abstract void loadData();

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.onDestroy();
		}
	}

	/**
	 * 点击波浪动画
	 */
	public void ripple(View view) {
		MaterialRippleLayout.on(view).create();
	}

	/**
	 * 是否延迟显示加载
	 *
	 * @param isDelay
	 */
	public void showLoad(boolean isDelay) {
		if (loadDialog == null) {
			return;
		}
		if (isDelay) {
			loadDialog.showLoad();
		}
		else {
			loadDialog.show();
		}
	}

	/**
	 * 配合延迟显示
	 *
	 * @param isDelay
	 */
	public void dismissLoad(boolean isDelay) {
		if (loadDialog == null) {
			return;
		}
		if (isDelay) {
			loadDialog.dismissLoad();
		}
		else {
			loadDialog.dismiss();
		}
	}

	/**
	 * 友盟事件统计
	 *
	 * @param strEvent
	 */
	public void setUmEvent(String strEvent) {
		MobclickAgent.onEvent(getActivity(), strEvent);
	}

	/**
	 * 客服电话
	 */
	public void telephoneDialog() {
		final PromptDialog promptDialog = new PromptDialog(getActivity());
		promptDialog.setContentText("是否拨打客服热线?", null);
		promptDialog.setDefineOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000685333"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				promptDialog.dismiss();
			}
		});
		promptDialog.show();
	}

	public void enterLockActOrLogin() {
		getActivity().runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LockUtils.autoEnterLockOrLogin(getActivity());
			}
		});
	}

	@Override
	public void showProgress() {
		showLoad(false);
	}

	@Override
	public void hideProgress() {
		dismissLoad(false);
	}

	public void showErrorPage() {

	}

	@Override
	public void onRequestFail() {
		showErrorPage();
//		HttpServer.checkLoadFailReason(getContext());
		CommonUtils.checkLoadFailReason(getContext());
	}

	@Override
	public void showMsg(String msg) {
		ToastUtil.toast(getContext(), msg);
	}

	@Override
	public void showOtherErrMsg(int code, String msg) {
		ToastUtil.toast(getContext(), msg);
	}

	@Override
	public void loginInvalidate() {
		enterLockActOrLogin();
	}

	public abstract T getPresenter();
}
