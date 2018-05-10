package com.gaoshoubang.ui.registration.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.ui.registration.presenter.RegisterPresenterImpl;
import com.gaoshoubang.ui.registration.view.RegisterView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.util.CheckInputUtils;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.LockUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 注册
 *
 * @author Cisco
 */
public class ActivityRegister extends BaseActivity<RegisterPresenterImpl> implements OnClickListener, RegisterView {
	protected static final String TAG = "ActivityRegister2";
	private ImageView regAdImg;
	private TextView regMobile;
	private EditText editCode;
	private TextView codeTime;
	private TextView codeAgain;
	private TextView codeVoice;
	private LinearLayout codeVoiceLinear;
	private EditText editPwd;
	private CheckBox pwdShow;
	private EditText editRcmder;
	private ImageView question;
	private Button next;

	private RelativeLayout questionRl;
	private ImageView questionClose;

	private String mobile;
	private boolean isSendVoice = true;


	@Override
	protected RegisterPresenterImpl getPresenter() {
		return new RegisterPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_register;
	}

	protected void initView() {
		regAdImg = (ImageView) findViewById(R.id.register_ad_img);
		regMobile = (TextView) findViewById(R.id.reg_mobile);
		editCode = (EditText) findViewById(R.id.reg_code);
		codeTime = (TextView) findViewById(R.id.reg_code_time);
		codeAgain = (TextView) findViewById(R.id.reg_code_again);
		codeVoice = (TextView) findViewById(R.id.reg_v_code);
		codeVoiceLinear = (LinearLayout) findViewById(R.id.reg_v_linear);
		editPwd = (EditText) findViewById(R.id.reg_pwd);
		pwdShow = (CheckBox) findViewById(R.id.reg_showpsd);
		editRcmder = (EditText) findViewById(R.id.reg_rcmder);
		question = (ImageView) findViewById(R.id.reg_question);
		next = (Button) findViewById(R.id.reg_next);
		questionRl = (RelativeLayout) findViewById(R.id.reg_question_rl);
		questionClose = (ImageView) findViewById(R.id.reg_question_close);
		initAdImg();
	}

	@Override
	protected void initEvent() {
		editCode.setOnFocusChangeListener(mOnFocusChangeListener);
		editPwd.setOnFocusChangeListener(mOnFocusChangeListener);
		editRcmder.setOnFocusChangeListener(mOnFocusChangeListener);

		codeAgain.setOnClickListener(this);
		codeVoice.setOnClickListener(this);
		pwdShow.setOnClickListener(this);
		question.setOnClickListener(this);
		next.setOnClickListener(this);
		questionRl.setOnClickListener(this);
		questionClose.setOnClickListener(this);

		editCode.addTextChangedListener(mTextWatcher);
		editPwd.addTextChangedListener(mTextWatcher);

	}

	@Override
	protected void loadData() {
		mobile = getIntent().getStringExtra("mobile");
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean mCnfListBean) {
				next.setText(mCnfListBean.getREG_LABEL_INDEX());
			}

			public void onFail() {
				return;
			}
		});

		regMobile.setText("短信验证码已发送至" + mobile.substring(0, 3) + " " + mobile.substring(3, 7) + " " + mobile.substring(7, 11));

		tc.start();
		setUmEvent(UmEvent.Gsy_regist_regist);
	}

	@Override
	protected void bindData() {

	}

	// 获取焦点 添加友盟统计
	OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (!hasFocus) {
				return;
			}
			switch (v.getId()) {
				case R.id.reg_code:
					setUmEvent(UmEvent.Gsy_regist_codetfd);
					break;

				case R.id.reg_pwd:
					setUmEvent(UmEvent.Gsy_regist_pwdTfd);
					break;

				case R.id.reg_rcmder:
					setUmEvent(UmEvent.Gsy_regist_rmid);
					break;
			}
		}
	};

	TextWatcher mTextWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (editCode.length() != 0 && editPwd.length() >= 6) {
				next.setBackgroundResource(R.drawable.shape_login_btn_sel);
			}
			else {
				next.setBackgroundResource(R.drawable.shape_login_btn_nor);
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.reg_code_again:// 再次发送
				setUmEvent(UmEvent.Gsy_regist_getCode);
				mPresenter.requestMobileVerifyCode("SMS", mobile);
				break;

			case R.id.reg_v_code:// 语音
				if (isSendVoice) {
					isSendVoice = false;
					mPresenter.requestMobileVerifyCode("VOICE", mobile);
					setUmEvent(UmEvent.Gsy_regist_getVoiceCode);
				}
				break;

			case R.id.reg_showpsd:// 显示密码
				setUmEvent(UmEvent.Gsy_regist_showPwd);
				if (pwdShow.isChecked()) {
					editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else {
					editPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				editPwd.setSelection(editPwd.length());
				break;

			case R.id.reg_question:// 提示
				if (questionRl.getVisibility() == View.GONE) {
					translateAnimation(questionRl, -1, 0, View.VISIBLE);
				}
				else {
					questionRl.setVisibility(View.GONE);
					translateAnimation(questionRl, 0, -1, View.GONE);
				}
				break;

			case R.id.reg_next:
				setUmEvent(UmEvent.Gsy_regist_registSuccess);
				checkInfo();
				break;

			case R.id.reg_question_close:// 关掉提示
				translateAnimation(questionRl, 0, -1, View.GONE);
				break;
		}
	}

	public CountDownTimer tc = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			codeTime.setVisibility(View.VISIBLE);
			codeAgain.setVisibility(View.GONE);
			codeTime.setText(millisUntilFinished / 1000 + "s");
			if (millisUntilFinished / 1000 == 30) {
				codeVoiceLinear.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onFinish() {
			codeTime.setVisibility(View.GONE);
			codeAgain.setVisibility(View.VISIBLE);
			isSendVoice = true;
		}
	};

	public void translateAnimation(final View view, int toY, int forY, final int visibility) {
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, toY,
				Animation.RELATIVE_TO_PARENT, forY);
		AccelerateInterpolator acce = new AccelerateInterpolator();
		translateAnimation.setDuration(500);
		translateAnimation.setInterpolator(acce);
		view.startAnimation(translateAnimation);
		view.setVisibility(visibility);
	}

	private void checkInfo() {
		String code = editCode.getText().toString();
		String pwd = editPwd.getText().toString();
		//检查密码和验证码的合法性,进行提示
		boolean status = CheckInputUtils.checkInputPSWAndCode(this, code, pwd);
		if (!status) {
			return;
		}

		if (editRcmder.getText().toString().length() > 0 && editRcmder.getText().toString().length() < 6) {
			ToastUtil.toast(this, "高手码不存在，请重新输入");
			return;
		}
		mPresenter.requestCheckMobileVerifyCode(code, mobile);
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			bindCard();
		}
	}

	/**
	 * 注册成功跳转
	 */
	private void bindCard() {
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean cnfListBean) {
//				loadDialog.dismiss();
				Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("url", cnfListBean.getREGISTER_SUCCESS());
				intent.putExtra("type", WebActivityShow.TYPE_UMP);
				intent.putExtra("toRegister", true);
				startActivity(intent);
				finish();
			}

			public void onFail() {
				Intent intent = new Intent(ActivityRegister.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
	}

	// 运营图
	private void initAdImg() {
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean cnfListBean) {
				ImageLoader.getInstance().displayImage(cnfListBean.getREG_HEAD_IMG(), regAdImg, new ImageLoaderUtils().defaultOptions(DisplayUtil.dip2px(ActivityRegister.this, 5)));
			}

			public void onFail() {
			}
		});
	}

	@Override
	public void afterGetMobileVerifyCode(String method) {
		if (method.equals("SMS")) {
			setUmEvent(UmEvent.Gsy_regist_getCode);
			ToastUtil.toast(ActivityRegister.this, "短信验证码发送成功");
		}
		else {
			setUmEvent(UmEvent.Gsy_regist_voiceCode);
			ToastUtil.toast(ActivityRegister.this, "语音验证码已发送,请接听电话获取");
		}
		tc.cancel();
		tc.start();
	}

	@Override
	public void changeMethod() {
		isSendVoice = true;
	}

	@Override
	public void afterCheckMobileVerifyCode() {
		if (!TextUtils.isEmpty(editRcmder.getText().toString())) {
			mPresenter.requestDoAuthReferralCode(editRcmder.getText().toString(), mobile, editPwd.getText().toString());
		}
		else {
			LogUtils.e("ActivityRegister", "afterCheckMobileVerifyCode:" + System.currentTimeMillis());
			mPresenter.requestDoReg(mobile, null, null, editPwd.getText().toString(), null, null, 2);
		}
	}


	@Override
	public void afterRegister(LoginBean loginBean, String mobile, String passwd) {
		setUmEvent(UmEvent.Gsy_regist_regist);
		UserSharedPreferenceUtils.setMobile(mobile);// 保存手机号码
		UserSharedPreferenceUtils.setCookie(loginBean.getCKey(), loginBean.getCVal());
		LockUtils.saveLoginMessage(mobile, passwd);
		UserSharedPreferenceUtils.setLoginValue(loginBean.getLoginValue());
		String uid = loginBean.getUid();
		ConfigUtils.getInstance().setUid(uid);// 保存uid
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phone", mobile);
		jsonObject.addProperty("password", passwd);
		ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
		if (GsbApplication.getUserId() != null) {
			JPushInterface.setAlias(ActivityRegister.this, GsbApplication.getUserId(), new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.i("zhj", "jpush success");
					}
				}
			});
		}
		Intent intent = new Intent(ActivityRegister.this, ActivityLock.class);
		intent.putExtra("type", 0);
		intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
//	        intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, 1);
		startActivityForResult(intent, 0);
		LogUtils.e("ActivityRegister", "afterCheckMobileVerifyCode:" + System.currentTimeMillis());
	}
}
