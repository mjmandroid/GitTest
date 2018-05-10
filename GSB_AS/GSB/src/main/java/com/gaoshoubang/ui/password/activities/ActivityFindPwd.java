package com.gaoshoubang.ui.password.activities;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.widget.MobileNumberEditText;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.ui.password.presenter.FindPwdPresenterImpl;
import com.gaoshoubang.ui.password.view.FindPwdView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.CommonUtils;

/**
 * 找回密码
 *
 * @author Cisco
 */
public class ActivityFindPwd extends BaseActivity<FindPwdPresenterImpl> implements FindPwdView {
	private static final String TAG = "ActivityFindPwd";
	private MobileNumberEditText findpwdMoblie;
	private ImageView findpwdMoblieClear;
	private TextView findpwdNext;


	@Override
	protected FindPwdPresenterImpl getPresenter() {
		return new FindPwdPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_findpwd;
	}

	@Override
	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		findpwdMoblie = (MobileNumberEditText) findViewById(R.id.findpwd_moblie);
		findpwdMoblieClear = (ImageView) findViewById(R.id.findpwd_moblie_clear);
		findpwdMoblie.setIvClear(findpwdMoblieClear);
		findpwdNext = (TextView) findViewById(R.id.findpwd_next);

	}

	@Override
	protected void initEvent() {
		findpwdNext.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String strPhone = findpwdMoblie.getTextForString();
				if (strPhone.length() == 0) {
					ToastUtil.toast(ActivityFindPwd.this, "请输入手机号");
					return;
				}
				if (strPhone.length() != 11) {
					ToastUtil.toast(ActivityFindPwd.this, "请填写正确的手机号码");
					return;
				}

				if (CommonUtils.isMobileNO(ActivityFindPwd.this, strPhone) != null) {
					ToastUtil.toast(ActivityFindPwd.this, "请填写正确的手机号码");
					return;
				}
				mPresenter.requestPwdMobileVerifyCode(findpwdMoblie.getTextForString());
			}
		});
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}


	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		if (statusCode == 4101004) {
			PromptDialog mPromptDialog = new PromptDialog(ActivityFindPwd.this);
			mPromptDialog.setContentText("提示", "手机号未注册，是否立即注册？");
			mPromptDialog.setDefineOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(ActivityFindPwd.this, ActivityLogin.class);
					intent.putExtra("mobile", findpwdMoblie.getTextForString());
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					finish();
				}
			});
			mPromptDialog.show();
		}
		else {
			ToastUtil.toast(ActivityFindPwd.this, msg);
		}
	}

	@Override
	public void afterPwdMobileVerifyCode() {
		ToastUtil.toast(ActivityFindPwd.this, "请注意查收短信");
		Intent intent = new Intent(ActivityFindPwd.this, ActivityFindPwd2.class);
		intent.putExtra("mobile", findpwdMoblie.getTextForString());
		startActivity(intent);
		finish();
	}
}
