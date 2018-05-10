package com.gaoshoubang.ui.registration.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.ui.registration.presenter.BindingPresenter2;
import com.gaoshoubang.ui.registration.view.BindingView2;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.JsonUtil;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.CheckInputUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.google.gson.JsonObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 绑定页面2
 *
 * @author Cisco
 */
public class ActivityBinDing2 extends BaseActivity<BindingPresenter2> implements OnClickListener, BindingView2 {
	protected static final String TAG = "ActivityBinDing2";
	protected static final String METHOD_SMS = "SMS";
	protected static final String METHOD_VOICE = "VOICE";
	private EditText editCode;
	private TextView codeTime;
	private TextView codeAgain;
	private TextView codeVoice;
	private LinearLayout codeVoiceLinear;
	private EditText editPwd;
	private TextView pwdShow;
	private EditText editRcmder;
	private ImageView question;
	private TextView next;
	private RelativeLayout questionRl;
	private ImageView questionClose;

	private String mobile;
	private Tuser tuser;
	private boolean isShowPwd = true;
	private boolean isSendVoice = true;


	@Override
	protected BindingPresenter2 getPresenter() {
		return new BindingPresenter2();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_binding1;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		editCode = (EditText) findViewById(R.id.binding_code);
		codeTime = (TextView) findViewById(R.id.binding_code_time);
		codeAgain = (TextView) findViewById(R.id.binding_code_again);
		codeVoice = (TextView) findViewById(R.id.binding_v_code);
		codeVoiceLinear = (LinearLayout) findViewById(R.id.binding_v_code_linear);
		editPwd = (EditText) findViewById(R.id.binding_pwd);
		pwdShow = (TextView) findViewById(R.id.binding_showpsd);
		editRcmder = (EditText) findViewById(R.id.binding_rcmder);
		question = (ImageView) findViewById(R.id.binding_question);
		next = (TextView) findViewById(R.id.binding1_next);
		questionRl = (RelativeLayout) findViewById(R.id.binding_question_rl);
		questionClose = (ImageView) findViewById(R.id.binding_question_close);

	}

	@Override
	protected void initEvent() {
		codeAgain.setOnClickListener(this);
		codeVoice.setOnClickListener(this);
		pwdShow.setOnClickListener(this);
		question.setOnClickListener(this);
		next.setOnClickListener(this);
		questionRl.setOnClickListener(this);
		questionClose.setOnClickListener(this);
		editPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
	}

	@Override
	protected void loadData() {
		mobile = getIntent().getStringExtra("mobile");
		tuser = (Tuser) getIntent().getSerializableExtra("tuser");
		mPresenter.requestMobileVerifyCode(mobile, METHOD_SMS);
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.binding_code_again:// 再次发送
				mPresenter.requestMobileVerifyCode(mobile, METHOD_SMS);
				break;

			case R.id.binding_v_code:// 语音
				if (isSendVoice) {
					isSendVoice = false;
					mPresenter.requestMobileVerifyCode(mobile, METHOD_VOICE);
				}
				break;

			case R.id.binding_showpsd:// 显示密码
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

			case R.id.binding_question:// 提示
				if (questionRl.getVisibility() == View.GONE) {
					translateAnimation(questionRl, -1, 0, View.VISIBLE);
				}
				else {
					questionRl.setVisibility(View.GONE);
					translateAnimation(questionRl, 0, -1, View.GONE);
				}
				break;

			case R.id.binding1_next:
				checkInfo();
				break;

			case R.id.binding_question_close:// 关掉提示
				translateAnimation(questionRl, 0, -1, View.GONE);
				break;
		}
	}

	public CountDownTimer tc = new CountDownTimer(60000, 1000) {
		@Override
		public void onTick(long millisUntilFinished) {
			codeTime.setVisibility(View.VISIBLE);
			codeAgain.setVisibility(View.GONE);
			codeTime.setText(millisUntilFinished / 1000 + "S");
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
		//验证密码和验证码
		boolean status = CheckInputUtils.checkInputPSWAndCode(this, code, pwd);
		if (!status) {
			return;
		}
		mPresenter.requestCheckMobileVerifyCode(mobile, code);
	}


	JsonObject jsonObject;


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			toBangka();
		}
	}

	private void toBangka() {
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean cnfListBean) {
//				loadDialog.dismiss();
				Intent intent = new Intent(ActivityBinDing2.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("url", cnfListBean.getREGISTER_SUCCESS());
				intent.putExtra("type", WebActivityShow.TYPE_UMP);
				intent.putExtra("toRegister", true);
				startActivity(intent);
				finish();
			}

			public void onFail() {
				Intent intent = new Intent(ActivityBinDing2.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
	}

	@Override
	public void afterGetVertifyCode(String method) {
		if (method.equals("SMS")) {
			ToastUtil.toast(ActivityBinDing2.this, "短信验证码发送成功");
		}
		else {
			ToastUtil.toast(ActivityBinDing2.this, "语音验证码已发送,请接听电话获取");
		}
		tc.cancel();
		tc.start();
	}

	/**
	 * 验证码检验成功后
	 */
	@Override
	public void afterCheckVertifyPhoneNum() {
		if (!editRcmder.getText().toString().equals("")) {
			mPresenter.requestDoAuthRcmderCode(editRcmder.getText().toString());
		}
		else {
			mPresenter.requestDoReg(mobile, null, null, editPwd.getText().toString(), null, null, 2, tuser);
		}
	}

	@Override
	public void afterCheckRcmderCode(String result) {
		String existRcmderCode = JsonUtil.getValue(result, "existRcmderCode");
		if (existRcmderCode != null && existRcmderCode.equals("2")) {
			mPresenter.requestDoReg(mobile, null, null, editPwd.getText().toString(), editRcmder.getText().toString(), null, 2, tuser);
		}
		else {
			ToastUtil.toast(ActivityBinDing2.this, "高手码不存在");
		}
	}


	@Override
	public void afterDoReg(LoginBean loginBean, String psw) {
		setUmEvent(UmEvent.gsy_threeLogin_setPwd);
		UserSharedPreferenceUtils.setMobile(mobile);// 保存手机号码
		UserSharedPreferenceUtils.setLoginValue(loginBean.getLoginValue());
		UserSharedPreferenceUtils.setCookie(loginBean.getCKey(), loginBean.getCKey());
		String uid = loginBean.getUid();
		ConfigUtils.getInstance().setUid(uid);// 保存uid
		if (tuser == null) {
			jsonObject = new JsonObject();
			jsonObject.addProperty("phone", mobile);
			jsonObject.addProperty("password", psw);
			ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
		}
		else {
			jsonObject = new JsonObject();
			jsonObject.addProperty("partnerCode", tuser.getPartnerCode());
			jsonObject.addProperty("openid", tuser.getOpenid());
			jsonObject.addProperty("unionid", tuser.getUnionid());

			ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
		}
		if (GsbApplication.getUserId() != null) {
			JPushInterface.setAlias(ActivityBinDing2.this, GsbApplication.getUserId(), new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.i("zhj", "jpush success");
					}
				}
			});
		}
		Intent intent = new Intent(ActivityBinDing2.this, ActivityLock.class);
		intent.putExtra("type", 0);
		intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
		startActivityForResult(intent, 0);
	}

	@Override
	public void onRequestFail() {
		super.onRequestFail();
		isSendVoice = true;
	}

	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		super.showOtherErrMsg(statusCode, msg);
		isSendVoice = true;
	}
}
