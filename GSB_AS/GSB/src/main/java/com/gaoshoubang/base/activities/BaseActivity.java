package com.gaoshoubang.base.activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.widget.MaterialRippleLayout;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.base.view.BaseView;
import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.util.LockUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

/**
 * 基类
 */

public abstract class BaseActivity<T extends BasePresenterImpl> extends FragmentActivity implements BaseView {
	protected LoadDialog loadDialog;
	public long time;
	public T mPresenter;
	public long mLastUnlockTime = -1;

	private OnShowErrorViewListener onShowErrorViewListener;
	private View mErrorView;
	private boolean isErrorViewShow = false;
	private ViewStub mErrorViewStub;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/*重新管理activity的生命周期*/
		setContentView(getLayoutId());
		mPresenter = getPresenter();
		if (mPresenter != null) {
			mPresenter.attachView(this);
		}
		initView();
		initEvent();
		loadData();
		loadDialog = new LoadDialog(this);
	}


	protected abstract T getPresenter();


	/*布局文件的ID*/
	protected abstract int getLayoutId();

	/*初始化控件*/
	protected abstract void initView();

	/*初始化事件*/
	protected abstract void initEvent();

	/*加载数据*/
	protected abstract void loadData();

	/*绑定数据*/
	protected abstract void bindData();

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
	 * 注册广播
	 *
	 * @param actionName
	 * @param mBroadcastReceiver
	 */
	public void registerBroadcastReceiver(String actionName, BroadcastReceiver mBroadcastReceiver) {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(actionName);
		registerReceiver(mBroadcastReceiver, intentFilter);
	}

	/**
	 * 友盟事件统计
	 *
	 * @param strEvent
	 */
	public void setUmEvent(String strEvent) {
		MobclickAgent.onEvent(this, strEvent);
	}

	/**
	 * 统计代码
	 */
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (!GsbApplication.isActive) {
			GsbApplication.isActive = true;
//			LogUtils.e("BaseActivity", "onResume:" + 11);
			if (UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()) != null) {
				LogUtils.e("BaseActivity", "onResume:" + 22);
				if ((time + (30 * 1000)) < System.currentTimeMillis()) {
//					LogUtils.e("BaseActivity", "onResume:" + 33);
//					LockUtils.enterLockAct(this);
					LockUtils.autoEnterLockOrLogin(this);
//					isEnterLockAct();
				}
				//登录过后,每次进入app需要解锁
			}
		}
	}

	/**
	 * 统计代码
	 */
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!isAppOnForeground()) {
			GsbApplication.isActive = false;
			LogUtils.e("BaseActivity", "onStop:");
			time = System.currentTimeMillis();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mPresenter != null) {
			mPresenter.onDestroy();
		}
	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public boolean isAppOnForeground() {
		ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = getApplicationContext().getPackageName();

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 显示网络错误的view
	 */
	public void showErrorPage() {
		isErrorViewShow = false;
		if (mErrorViewStub == null) {
			mErrorViewStub = (ViewStub) findViewById(R.id.load_error_view);
			mErrorView = mErrorViewStub.inflate();
		}
//        mErrorView = findViewById(R.id.load_error_view);
		if (mErrorView == null) {
			return;
		}
		TextView refresh = (TextView) mErrorView.findViewById(R.id.error_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (onShowErrorViewListener != null) {
					isErrorViewShow = true;
					onShowErrorViewListener.refresh();
					mErrorView.setVisibility(View.GONE);
				}
			}
		});
		mErrorView.setVisibility(View.VISIBLE);
		if (onShowErrorViewListener != null) {
			onShowErrorViewListener.show();
		}
		View telView = mErrorView.findViewById(R.id.error_tel);
		telView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				telephoneDialog();
			}
		});
	}

	private void telephoneDialog() {
		final PromptDialog promptDialog = new PromptDialog(this);
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

	/**
	 * 隐藏网络错误的view
	 */
	public void hideErrorPage() {
		if (isErrorViewShow == false) {
			return;
		}
		if (onShowErrorViewListener != null) {
			onShowErrorViewListener.hide();
			mErrorView.setVisibility(View.GONE);
		}
	}

	/**
	 * 显示加载错误的界面
	 *
	 * @param onShowErrorViewListener
	 */
	public void setShowErrorView(OnShowErrorViewListener onShowErrorViewListener) {
		this.onShowErrorViewListener = onShowErrorViewListener;
	}

	@Override
	public void showProgress() {
		showLoad(false);
	}

	@Override
	public void hideProgress() {
		dismissLoad(false);
	}


	/**
	 * * Called when the request could not be executed due to cancellation, a connectivity problem or
	 * timeout. Because networks can fail during an exchange, it is possible that the remote server
	 * accepted the request before the failure.
	 * <p>
	 * <p>
	 * 发起请求失败
	 */
	@Override
	public void onRequestFail() {
		CommonUtils.checkLoadFailReason(this);
	}

	@Override
	public void showMsg(String msg) {
		ToastUtil.toast(this, msg);
	}


	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		ToastUtil.toast(this, msg);
	}

	@Override
	public void loginInvalidate() {
		/**
		 * 登录信息失效后进入解锁或者
		 */
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				LockUtils.autoEnterLockOrLogin(BaseActivity.this);
			}
		});
	}

	public abstract class OnShowErrorViewListener {

		public abstract void show();

		public abstract void refresh();

		public abstract void hide();
	}
}
