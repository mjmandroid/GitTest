package com.gaoshoubang.base.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.gaoshoubang.base.view.BaseView;

import java.lang.ref.WeakReference;


/**
 * Created by lzx on 2017/5/3.
 */
public abstract class BasePresenterImpl<T extends BaseView> implements BasePresenter {
	protected WeakReference<T> mView; //使用弱引用

	//    public Context mContext;
	@Override
	public void onCreate() {

	}


	@Override
	public void onDestroy() {
		detachView();
	}

	public void detachView() {
		if (mView != null) {
			mView.clear();
			mView = null;
			Log.i("BasePresenterImpl", "已经GC...");
		}
	}

	public void attachView(@NonNull T view) {
		mView = new WeakReference<>(view);
		//context
//        mContext = (Context) getView();
	}

	/**
	 * 获取view的方法
	 *
	 * @return 当前关联的view
	 */
	public T getView() {
		if (mView != null) {
			return mView.get();
		}
		return null;
	}

	public void hideProgress() {
		if (getView() != null) {
			getView().hideProgress();
		}
	}

	public void showProgress() {
		if (getView() != null) {
			getView().showProgress();
		}
	}

	public void loginInvalidate() {
		if (getView() != null) {
			getView().loginInvalidate();
		}
	}

	public void onRequestFailed() {
		if (getView() != null) {
			getView().onRequestFail();
		}
	}

	public void onOtherError(int stateCode, String msg) {
		if (getView() != null) {
			getView().showOtherErrMsg(stateCode, msg);
		}
	}


}
