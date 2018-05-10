package com.gaoshoubang.ui.password.activities;

import android.os.CountDownTimer;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.password.presenter.FindPwdPresenter2Impl;
import com.gaoshoubang.ui.password.view.FindPwdView2;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;

/**
 * 密码设置
 *
 * @author Cisco
 */
public class ActivityFindPwd2 extends BaseActivity<FindPwdPresenter2Impl> implements OnClickListener, FindPwdView2 {
	private static final String TAG = "ActivityFindPwd2";

	private EditText editCode;
	private TextView codeTime;
	private TextView codeAgain;
	private TextView codeVoice;
	private EditText editPwd;
	private TextView pwdShow;
	private TextView next;

	private String mobile;
	private boolean isSendVoice = true;
	private boolean isShowPwd = true;

	@Override
	protected FindPwdPresenter2Impl getPresenter() {
		return new FindPwdPresenter2Impl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_findpwd1;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		editCode = (EditText) findViewById(R.id.findpwd_code);
		codeTime = (TextView) findViewById(R.id.findpwd_code_time);
		codeAgain = (TextView) findViewById(R.id.findpwd_again);
		codeVoice = (TextView) findViewById(R.id.findpwd_v_code);
		editPwd = (EditText) findViewById(R.id.findpwd_pwd);
		pwdShow = (TextView) findViewById(R.id.findpwd_showpsd);
		next = (TextView) findViewById(R.id.findpwd_next);

		tc.start();
	}

	@Override
	protected void initEvent() {
		codeAgain.setOnClickListener(this);
		pwdShow.setOnClickListener(this);
		codeVoice.setOnClickListener(this);
		next.setOnClickListener(this);
		editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
	}

	@Override
	protected void loadData() {
		mobile = getIntent().getStringExtra("mobile");
	}

	@Override
	protected void bindData() {

	}

	public CountDownTimer tc = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			codeTime.setVisibility(View.VISIBLE);
			codeAgain.setVisibility(View.GONE);
			codeTime.setText(millisUntilFinished / 1000 + "S");
		}

		@Override
		public void onFinish() {
			codeTime.setVisibility(View.GONE);
			codeAgain.setVisibility(View.VISIBLE);
			isSendVoice = true;
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.findpwd_again:// 再发发送
//			requestPwdMobileVerifyCode("SMS");
				mPresenter.requestPwdMobileVerifyCode(mobile, "SMS");
				break;

			case R.id.findpwd_v_code:// 语音
				if (isSendVoice) {
					isSendVoice = false;
//				requestPwdMobileVerifyCode("VOICE");
					mPresenter.requestPwdMobileVerifyCode(mobile, "VOICE");
				}
				break;

			case R.id.findpwd_showpsd:// 显示密码
				if (!isShowPwd) {
					pwdShow.setText("隐藏");
					editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else {
					pwdShow.setText("显示");
					editPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				isShowPwd = !isShowPwd;
				editPwd.postInvalidate();
				CharSequence charSequence = editPwd.getText();
				if (charSequence instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence;
					Selection.setSelection(spanText, charSequence.length());
				}
				break;

			case R.id.findpwd_next:
				checkInfo();
				break;
		}
	}

	private void checkInfo() {
		String code = editCode.getText().toString();
		String pwd = editPwd.getText().toString();

		if (code.length() == 0) {
			ToastUtil.toast(this, "验证码不能为空");
			return;
		}
		if (pwd.length() == 0) {
			ToastUtil.toast(this, "密码不能为空");
			return;
		}
		if (pwd.length() < 6) {
			ToastUtil.toast(this, "密码必须大于6位");
			return;
		}

		if (pwd.length() != pwd.getBytes().length) {
			ToastUtil.toast(this, "密码不能包含中文字符");
			return;
		}
		mPresenter.requestDoResetPasswd(mobile, code, pwd);
	}


	@Override
	public void afterGetMobileVerifyCode(String method) {
		if (method.equals("SMS")) {
			ToastUtil.toast(ActivityFindPwd2.this, "短信验证码发送成功");
		}
		else {
			ToastUtil.toast(ActivityFindPwd2.this, "语音验证码已发送,请接听电话获取");
		}
		tc.cancel();
		tc.start();
	}

	@Override
	public void afterResetPSW() {
		ToastUtil.toast(ActivityFindPwd2.this, "密码设置成功");
		finish();
	}

	@Override
	public void receiveErrGetVerifyCode() {
		isSendVoice = true;
	}
}
