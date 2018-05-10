package com.gaoshoubang.ui.login.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.ui.registration.activities.ActivityBinDing;
import com.gaoshoubang.ui.password.activities.ActivityFindPwd;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.ui.registration.activities.ActivityRegister;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.widget.MobileNumberEditText;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.ui.login.presenter.LoginPresenterImpl;
import com.gaoshoubang.ui.login.view.LoginView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.JsonUtil;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.ClearUtils;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.InputMethodManagerUtil;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.util.CommonUtils;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class ActivityLogin extends BaseActivity<LoginPresenterImpl> implements OnClickListener, LoginView {
	private static final String TAG = "ActivityLogin";

	private TextView titleText;
	private ImageView titleBack;
	private ImageView loginAdImg;
	private MobileNumberEditText phone;
	private TextView phoneSee;
	private ImageView phoneClear;
	private Button loginBtn;
	private CheckBox agreement;
	private TextView agreementTxt;
	private TextView mRegisterRiskRevealTxt;

	private View pwdView;
	private ImageView pwdHeadImg;
	private MobileNumberEditText pwdPhone;
	private EditText pwdEdit;
	private CheckBox pwdEditSee;
	private TextView pwdForget;
	private Button pwdBtn;

	private ImageView wechat;
	private ImageView sina;
	private ImageView qq;

	private String strPhone;
	private String strPwd;

	private Tuser mTuser;
	private Platform mPlatform;
	private String partnerCode;
	private JsonObject jsonObject;

	private Intent intent;
	private TranslateAnimation mShowAction;
	private String mPlatformName;

	@Override
	protected LoginPresenterImpl getPresenter() {
		return new LoginPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}


	protected void initView() {
		// 从欢迎页跳转过来的  ??
		if (getIntent().getBooleanExtra("toWelcome_isOpen", false)) {
			getIntent().putExtra("toWelcome_isOpen", false);
			Intent intent = new Intent(this, WebActivityShow.class);
			intent.putExtra("url", getIntent().getStringExtra("url"));
			startActivity(intent);
		}


		new FullTitleBar(this, "#ffffff");
		titleText = (TextView) findViewById(R.id.login_title);
		titleBack = (ImageView) findViewById(R.id.login_back);
		loginAdImg = (ImageView) findViewById(R.id.login_ad_img);
		phone = (MobileNumberEditText) findViewById(R.id.login_phone);
		phoneClear = (ImageView) findViewById(R.id.login_phone_clear);
		phoneSee = (TextView) findViewById(R.id.login_phone_see);
		phone.setIvClear(phoneClear);
		loginBtn = (Button) findViewById(R.id.login_next);
		agreement = (CheckBox) findViewById(R.id.register_agreement_check);
		agreementTxt = (TextView) findViewById(R.id.register_agreement_txt);
		mRegisterRiskRevealTxt = (TextView) findViewById(R.id.register_risk_reveal_txt);
		pwdView = findViewById(R.id.login_pwd_view);
		pwdHeadImg = (ImageView) findViewById(R.id.login_pwd_headimg);
		pwdPhone = (MobileNumberEditText) findViewById(R.id.login_pwd_phone);
		pwdEdit = (EditText) findViewById(R.id.login_pwd);
		pwdEditSee = (CheckBox) findViewById(R.id.login_pwd_see);
		pwdForget = (TextView) findViewById(R.id.login_pwd_forget);
		pwdBtn = (Button) findViewById(R.id.login_pwd_btn);

		wechat = (ImageView) findViewById(R.id.login_wechat);
		sina = (ImageView) findViewById(R.id.login_sina);
		qq = (ImageView) findViewById(R.id.login_qq);

	}

	@Override
	protected void initEvent() {
		loginAdImg.setOnClickListener(this);
		titleBack.setOnClickListener(this);
		phone.setOnClickListener(this);
		phoneSee.setOnClickListener(this);
		loginBtn.setOnClickListener(this);
		agreement.setOnClickListener(this);
		agreementTxt.setOnClickListener(this);
		mRegisterRiskRevealTxt.setOnClickListener(this);
		pwdBtn.setOnClickListener(this);
		pwdForget.setOnClickListener(this);
		pwdEditSee.setOnClickListener(this);
		wechat.setOnClickListener(this);
		sina.setOnClickListener(this);
		qq.setOnClickListener(this);


		// 手机号码输入监听
		phone.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					phoneSee.setText(phone.getText().toString());
					showView(phoneSee);
				}
				else {
					phoneSee.setVisibility(View.GONE);
				}

				if (s.length() == 13) {
					loginBtn.setBackgroundResource(R.drawable.shape_login_btn_sel);
				}
				else {
					loginBtn.setBackgroundResource(R.drawable.shape_login_btn_nor);
				}
			}
		});

		phone.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					setUmEvent(UmEvent.Gsy_regist_phoneLittlePhone);
				}
			}
		});
	}

	@Override
	protected void loadData() {
		ShareSDK.initSDK(this);
		// 进入登录页清除用户信息
		ConfigUtils.getInstance().clearLoginInfo();

		String phoneStr = UserSharedPreferenceUtils.getMobile();
		String phoneIntent = getIntent().getStringExtra("mobile");
		if (phoneIntent != null && !phoneIntent.equals("")) {
			phoneStr = phoneIntent;
		}
		if (!phoneStr.equals("")) {
			phone.setText(phoneStr);
			phone.setSelection(phone.getText().toString().length());

			phoneSee.setText(phone.getText().toString());
			phoneSee.setVisibility(View.VISIBLE);
			loginBtn.setBackgroundResource(R.drawable.shape_login_btn_sel);
		}

		if (getIntent().getBooleanExtra("toWebView_Login", false)) {
			titleBack.setVisibility(View.VISIBLE);
		}

		initAdImg();
	}

	@Override
	protected void bindData() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
		}
	}

	private void showView(View view) {
		if (view.getVisibility() == View.GONE) {
			mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(200);
			view.setAnimation(mShowAction);
			view.setVisibility(View.VISIBLE);
		}
	}

	// 显示登录输入密码的view
	private void showPwdView(View view) {
		if (view.getVisibility() == View.GONE) {
			mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(250);
			view.setAnimation(mShowAction);
			view.setVisibility(View.VISIBLE);
			addPwdTextListener();
		}
		else {
			mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
			mShowAction.setDuration(250);
			view.setAnimation(mShowAction);
			view.setVisibility(View.GONE);
		}
		mShowAction.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				InputMethodManagerUtil.isInput(ActivityLogin.this);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
		});
	}

	private void addPwdTextListener() {
		pwdEdit.setText("");
		pwdEditSee.setChecked(false);
		pwdEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() >= 6) {
					pwdBtn.setBackgroundResource(R.drawable.shape_login_btn_sel);
					pwdBtn.setEnabled(true);
				}
				else {
					pwdBtn.setBackgroundResource(R.drawable.shape_login_btn_nor);
					pwdBtn.setEnabled(false);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.login_ad_img:
				setUmEvent(UmEvent.Gsy_regist_phoneTitleImage);
				break;

			case R.id.login_phone:
				setUmEvent(UmEvent.Gsy_regist_phoneLittlePhone);
				break;

			case R.id.login_phone_see:
				setUmEvent(UmEvent.Gsy_regist_phoneBigPhone);
				if (!phone.isFocused()) {
					phone.requestFocus();
				}
//				InputMethodManagerUtil.show(this, phone);
				break;

			case R.id.login_pwd_forget:// 忘记密码
				setUmEvent(UmEvent.Gsy_login_forgPwd);
				intent = new Intent(this, ActivityFindPwd.class);
				startActivity(intent);
				break;

			case R.id.login_back:// 返回
				if (pwdView.getVisibility() == View.VISIBLE) {
					showPwdView(pwdView);
					titleText.setText("手机号");
//					InputMethodManagerUtil.isInput(ActivityLogin.this);
					if (getIntent().getBooleanExtra("toWebView_Login", false)) {
						titleBack.setVisibility(View.VISIBLE);
					}
					return;
				}
				finish();
				break;

			case R.id.login_next:// 下一步
				setUmEvent(UmEvent.Gsy_regist_next);
				checkLoginInfo();
				break;

			case R.id.login_pwd_btn:// 登录
				setUmEvent(UmEvent.Gsy_login_login);
				strPwd = pwdEdit.getText().toString();
				mPresenter.requestLogin(strPhone, strPwd, null);
				break;

			case R.id.register_agreement_txt:// 注册协议
				setUmEvent(UmEvent.Gsy_regist_gsyXieYi);
				intent = new Intent(this, WebActivityShow.class);
				intent.putExtra("url", "file:///android_asset/agreement.html");
				intent.putExtra("type", "高搜易注册协议");
				startActivity(intent);
				break;
			case R.id.register_risk_reveal_txt://风险揭示书
				setUmEvent(UmEvent.Gsy_regist_gsyJieShiShu);
				intent = new Intent(this,WebActivityShow.class);
				intent.putExtra("url", "http://test-gsb2.gaosouyi.com/Evaluation/risknote");
				intent.putExtra("type", "风险揭示书");
				startActivity(intent);
				break;
			case R.id.login_pwd_see:// 登录显示密码
				setUmEvent(UmEvent.Gsy_login_showPwd);
				if (pwdEditSee.isChecked()) {
					pwdEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else {
					pwdEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				pwdEdit.setSelection(pwdEdit.length());
				break;

			case R.id.login_wechat:
				setUmEvent(UmEvent.Gsy_login_wxThreeLogin);
				Platform wechat = ShareSDK.getPlatform(Wechat.NAME);
				if (wechat != null) {
					authorize(wechat);
				}
				partnerCode = "WECHAT";
				break;

			case R.id.login_sina:
				setUmEvent(UmEvent.Gsy_login_wbThreeLogin);
				Platform sina = ShareSDK.getPlatform(SinaWeibo.NAME);
				if (sina != null) {
					authorize(sina);
				}
				partnerCode = "WEIBO";
				break;

			case R.id.login_qq:
				setUmEvent(UmEvent.Gsy_login_qqThreeLogin);
				Platform qq = ShareSDK.getPlatform(QQ.NAME);
				if (qq != null) {
					authorize(qq);
				}
				partnerCode = "QQ";
				break;
		}
	}

	// 检查手机号码输入信息
	private void checkLoginInfo() {
		strPhone = phone.getTextForString().trim();
		if (strPhone.length() == 0) {
			ToastUtil.toast(this, "请输入手机号");
			return;
		}
		if (strPhone.length() != 11) {
			ToastUtil.toast(this, "请填写正确的手机号码");
			return;
		}
		if (CommonUtils.isMobileNO(this, strPhone) != null) {
			ToastUtil.toast(this, "请填写正确的手机号码");
			return;
		}
		if (!agreement.isChecked()) {
			ToastUtil.toast(this, "请阅读注册协议,勾选我同意");
			return;
		}
		mPresenter.requestDoCheckMobile(strPhone);
	}


	public void handleOtherErr(int code, String msg, Tuser tuser) {
		switch (code) {
			case 4006001:// 第三方登录未绑定手机
				intent = new Intent(ActivityLogin.this, ActivityBinDing.class);
				intent.putExtra("tuser", tuser);
				startActivity(intent);
				break;

			// case 4106003:
			case 4008001:
				// 锁定
				int loginNum = UserSharedPreferenceUtils.getLoginNum();
				if (UserSharedPreferenceUtils.getLoginNum() == 0) {
					ToastUtil.toast(ActivityLogin.this, msg);
					return;
				}
				else {
					ToastUtil.toast(ActivityLogin.this, "密码错误,您还有" + UserSharedPreferenceUtils.getLoginNum() + "机会");
					UserSharedPreferenceUtils.setLoginNum(--loginNum);
				}
				break;
			default:
				ToastUtil.toast(ActivityLogin.this, JsonUtil.getCodeInfo(code));
				break;
		}
	}

	@Override
	public void afterLoginInvalidate() {
		new ClearUtils().ClearUserInfo(this);
		UserSharedPreferenceUtils.deleteLoginValue();
	}

	// 第三方登录
	private void authorize(Platform plat) {
		plat.removeAccount();
		plat.setPlatformActionListener(platformActionListener);
		// 关闭SSO授权
		//plat.SSOSetting(true);
		plat.showUser(null);
	}

	PlatformActionListener platformActionListener = new PlatformActionListener() {

		@Override
		public void onError(Platform platform, int action, Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
			if (action == Platform.ACTION_USER_INFOR) {
				Message msg = new Message();
				msg.what = 0;
				msg.obj = new Object[]{platform.getName(), res};
				handler.sendMessage(msg);
				UserSharedPreferenceUtils.setIsThirdPartLogin(true, GsbApplication.getUserId());
				jsonObject = new JsonObject();
				jsonObject.addProperty("partnerCode", mTuser.getPartnerCode());
				jsonObject.addProperty("openid", mTuser.getOpenid());
				jsonObject.addProperty("unionid", mTuser.getUnionid());
				jsonObject.addProperty("platformName", mPlatformName);
				ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
			}
		}

		@Override
		public void onCancel(Platform platform, int action) {

		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:// 第三方登录
					Object[] objs = (Object[]) msg.obj;
					mPlatformName = (String) objs[0];
					mPlatform = ShareSDK.getPlatform(mPlatformName);
					PlatformDb platDB = ShareSDK.getPlatform(mPlatformName).getDb();// 获取数平台数据DB
					int gender;
					String faceUrl;
					// 性别
					if (mPlatform.getDb().getUserGender() != null) {
						if ("m".equals(mPlatform.getDb().getUserGender())) {
							gender = 1;
						}
						else {
							gender = 2;
						}
					}
					else {
						gender = 0;
					}
					// 头像
					if (partnerCode.equals("QQ")) {
						faceUrl = platDB.getUserIcon().substring(0, platDB.getUserIcon().length() - 2) + "100";
					}
					else {
						faceUrl = platDB.getUserIcon();
					}
					mTuser = new Tuser(partnerCode, mPlatform.getDb().getUserId(), mPlatform.getDb().get("unionid"), platDB.getUserName(), gender, faceUrl);
					mPresenter.requestLogin("", "", mTuser);
					break;
			}
		}
	};


	// 运营图
	private void initAdImg() {
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean cnfListBean) {
				//// TODO: 2018/1/22
				if (cnfListBean != null) {
					ImageLoader.getInstance().displayImage(cnfListBean.getREG_HEAD_IMG(), loginAdImg, new ImageLoaderUtils().recommendOptions(DisplayUtil.dip2px(ActivityLogin.this, 4)));
					return;
				}

			}

			public void onFail() {

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				if (pwdView.getVisibility() == View.VISIBLE) {
					showPwdView(pwdView);
					titleText.setText("手机号");
//					InputMethodManagerUtil.isInput(ActivityLogin.this);
					if (getIntent().getBooleanExtra("toWebView_Login", false)) {
						titleBack.setVisibility(View.VISIBLE);
					}
					return true;
				}
				break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void afterGetMobileVerifyCode() {
		setUmEvent(UmEvent.Gsy_regist_getCode);
		ToastUtil.toast(ActivityLogin.this, "请注意查收短信");
		intent = new Intent(ActivityLogin.this, ActivityRegister.class);
		intent.putExtra("mobile", strPhone);
		startActivity(intent);
	}

	@Override
	public void afterCheckMobile(CheckMobileResponse registerBean) {
		if (2 == registerBean.hasRegMobile) {
			showPwdView(pwdView);  //显示登录输入密码的view
			titleBack.setVisibility(View.VISIBLE);
			titleText.setText("登录");


//			InputMethodManagerUtil.isInput(ActivityLogin.this);
			ImageLoader.getInstance().displayImage(registerBean.faceUrl, pwdHeadImg, new ImageLoaderUtils().headerOption(500));
			pwdPhone.setText(strPhone);
			UserSharedPreferenceUtils.saveBitmap(registerBean.faceUrl);
			UserSharedPreferenceUtils.setNickName(registerBean.nickName);
			return;
		}
		else {
			new ClearUtils().ClearUserInfo(ActivityLogin.this);
			// 未注册跳到注册页面 发送验证码
			mPresenter.requestMobileVerifyCode(strPhone);
		}
	}

	@Override
	public void afterLoginSuccess(LoginBean loginBean, Tuser tuser) {
		String loginValue = loginBean.getLoginValue();
		UserSharedPreferenceUtils.setLoginValue(loginValue);

		UserSharedPreferenceUtils.setCookie(loginBean.getCKey(), loginBean.getCVal());
		String uid = loginBean.getUid();
		if (uid == null || uid.equals("")) {
			return;
		}
		ConfigUtils.getInstance().setUid(uid);// 保存uid

		if (tuser == null) {
			jsonObject = new JsonObject();
			jsonObject.addProperty("phone", strPhone);
			jsonObject.addProperty("password", strPwd);
			ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
			UserSharedPreferenceUtils.setIsThirdPartLogin(false, GsbApplication.getUserId());
		}
		else {
			UserSharedPreferenceUtils.setIsThirdPartLogin(true, GsbApplication.getUserId());
			jsonObject = new JsonObject();
			jsonObject.addProperty("partnerCode", mTuser.getPartnerCode());
			jsonObject.addProperty("openid", mTuser.getOpenid());
			jsonObject.addProperty("unionid", mTuser.getUnionid());
			jsonObject.addProperty("platformName", mPlatformName);
			ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
		}
		if (GsbApplication.getUserId() != null) {
			JPushInterface.setAlias(ActivityLogin.this, GsbApplication.getUserId(), new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.i("zhj", "jpush success");
					}
				}
			});
		}

		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 4));

		// 从网页跳转过来的登录
		if (getIntent().getBooleanExtra("toWebView_Login", false)) {
			setResult(RESULT_OK);
			finish();
			return;
		}

		// 若没设置手势,先跳转手势
		if (UserSharedPreferenceUtils.getGestureLock(uid) == null) {
			intent = new Intent(ActivityLogin.this, ActivityLock.class);
			intent.putExtra("type", 0);
			intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
			intent.putExtra("toLogin", true);
			startActivity(intent);
		}
		else {
			intent = new Intent(ActivityLogin.this, MainActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
