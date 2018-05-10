package com.gaoshoubang.ui.common.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.widget.FingerprintDialog;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.widget.lock.GestureContentView;
import com.gaoshoubang.widget.lock.GestureDrawline;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.fingerprint.FingerprintIdentify;
import com.gaoshoubang.fingerprint.FingerprintUtils;
import com.gaoshoubang.fingerprint.base.BaseFingerprint;
import com.gaoshoubang.bean.base.TotalSelfBean;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.service.LoginService;
import com.gaoshoubang.util.ClearUtils;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import java.util.Calendar;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 解锁页面,进入后有两种状态 @type = 0 设置状态 ,@param type = 1 校验密码状态.
 *
 * @author Cisco
 */
public class ActivityLock extends Activity implements OnClickListener, FingerprintDialog.onFingerprintRecognize {
	protected static final String TAG = "ActivityLock";

	private View lockLinear;
	private TextView day;
	private TextView month;
	private TextView week;
	private TextView nickName;
	private ImageView headImg;
	private TextView cancel;
	private GestureContentView gestureContentView;
	private FrameLayout bodyLayout;
	private TextView tips;
	private TextView forgotPwd;

	private String lockPwdTemp;
	private int errorCont = 0;

	private int type = -1;// 0设置密码 1校验密码

	private Intent intent;
	private FingerprintDialog mFingerprintDialog;
	private TextView mEnterFingerprint;
	private TextView mEnterPSW;
	private FingerprintIdentify mFingerprintIdentify;
	private boolean mIsCalledStartIdentify;
	private boolean mIsHide;
	private boolean mFingerprintSetting;
	private int mFingerprintType;
	private Intent mIntentLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lock);
//        new FullTitleBar(this, "#ffffff");
		new FullTitleBar(this, "#E44E4F");
//		setUmEvent(UmEvent.Gsy_account_gesturePwd);

		type = getIntent().getIntExtra("type", -1);
		mFingerprintType = getIntent().getIntExtra(FingerprintConst.FINGERPRINT_TYPE, -1);


		initView();
//		//初始化 指纹识别模块
		mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
			@Override
			public void onCatchException(Throwable exception) {
				Log.e(TAG, "onCatchException: " + exception.getLocalizedMessage());
			}
		});
		if (!mFingerprintIdentify.isFingerprintEnable()) {
			mEnterFingerprint.setVisibility(View.GONE);
		}
		FingerprintUtils.initFingerprintStatus(mFingerprintIdentify, GsbApplication.getUserId());
		if (mFingerprintType == -1) {
			mFingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
		}


		//初始化 指纹识别dialog
		initFingerprintDialog();


		if (type == 0) {
			settingLock();
			cancel.setVisibility(View.VISIBLE);
		}
		else {
			checkLock();
			cancel.setVisibility(View.GONE);
		}


	}

	@Override
	protected void onStart() {
		super.onStart();
//		mFingerprintSetting = UserSharedPreferenceUtils.getFingerprintSetting(GsbApplication.getUserId());

	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		LogUtils.e("ActivityLock", "onResume:");
		//指纹可用且开启指纹解锁
//		int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
//		startFingerprintIdentify();
		if (FingerprintConst.FINGERPRINT_NOT_SHOW == mFingerprintType) {
			mEnterFingerprint.setVisibility(View.GONE);
			if (type == 0) {
				mEnterPSW.setVisibility(View.GONE);
			}
			return;
		}
		if (mIsHide) {
			mFingerprintIdentify.resumeIdentify();
			mIsHide = false;
		}
		//指纹验证状态
		if (mFingerprintType == 3) {
			mFingerprintSetting = false;
			mFingerprintDialog.show();

		}//指纹设置状态
		else if (mFingerprintType == 1) {
			mFingerprintSetting = true;
			mFingerprintDialog.show();
			mEnterFingerprint.setVisibility(View.GONE);
			mEnterPSW.setVisibility(View.GONE);

		}
		else if (mFingerprintType == FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_ClOSE ||
				mFingerprintType == FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_OPEN
				|| mFingerprintType == FingerprintConst.FINGERPRINT_VERIFICATION_SUC
				) {
			mFingerprintSetting = true;
			mFingerprintDialog.show();
			mEnterFingerprint.setVisibility(View.GONE);
			mEnterPSW.setVisibility(View.GONE);
		}

	}

	/**
	 * 开启指纹解锁
	 */
	private void startFingerprintIdentify() {
		LogUtils.e("ActivityLock", "startFingerprintIdentify:");
		if (mFingerprintType == 2) {
			LogUtils.e("ActivityLock", "onResume:" + "没开启指纹或者不支持");
			mEnterFingerprint.setVisibility(View.GONE);
			if (mFingerprintDialog != null) {
				mFingerprintDialog.dismiss();
			}
			mEnterFingerprint.setVisibility(View.GONE);
			return;
		}
		if (!mFingerprintIdentify.isFingerprintEnable()) {
			Log.e(TAG, "onCreate: " + "指纹不可用");
			UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), 2);
			mEnterFingerprint.setVisibility(View.GONE);
			if (mFingerprintDialog != null) {
				mFingerprintDialog.dismiss();
			}
			mEnterFingerprint.setVisibility(View.GONE);
			return;
		}
//		mFingerprintDialog.show();
		if (mIsCalledStartIdentify) {
			mFingerprintDialog.setTvTips("再试一次");
			mFingerprintIdentify.resumeIdentify();
			LogUtils.e("ActivityLock", "resumeIdentify:");
			return;
		}
		mIsCalledStartIdentify = true;

		mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
			@Override
			public void onSucceed() {
				/**
				 * 指纹设置状态且验证成功
				 */
				if (mFingerprintSetting) {
					//验证成功,没开启手势
					mFingerprintDialog.dismiss();
					if (mFingerprintType == FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_ClOSE) {
						ToastUtil.toast(ActivityLock.this, "关闭成功！");
						UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), FingerprintConst.FINGERPRINT_HARDWARE_ENABLE);
						finish();
					}
					else if (mFingerprintType == FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_OPEN) {
						ToastUtil.toast(ActivityLock.this, "设置成功！");
						UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), FingerprintConst.FINGERPRINT_OPENING);
						finish();
					}
					else {
						UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), FingerprintConst.FINGERPRINT_VERIFICATION_SUC);
					}
				}
				else {
					mIntentLogin = new Intent(ActivityLock.this, LoginService.class);
					startService(mIntentLogin);
					mFingerprintDialog.dismiss();
					finish();
				}
			}

			@Override
			public void onNotMatch(int availableTimes) {
				LogUtils.e("ActivityLock", "onNotMatch:" + availableTimes);
				if (availableTimes <= 0) {
					if (mFingerprintSetting) {
						ToastUtil.toast(ActivityLock.this, "您的指纹识别错误次数过多，开启失败。");
						return;
					}
					ToastUtil.toast(ActivityLock.this, "您的指纹识别错误次数过多，请选择其他方式登录。");
					mFingerprintIdentify.cancelIdentify();
					return;
				}
				mFingerprintDialog.setTvTips("再试一次");
			}

			@Override
			public void onFailed() {
				LogUtils.e("ActivityLock", "onFailed:");
				if (mFingerprintSetting) {
					ToastUtil.toast(ActivityLock.this, "您的指纹识别错误次数过多，请稍后重试。");
				}
				else {
					ToastUtil.toast(ActivityLock.this, "您的指纹识别错误次数过多，请选择其他方式登录。");
				}
				if (mFingerprintDialog != null && mFingerprintDialog.isShowing()) {
//					mFingerprintDialog.setTvTips("解锁失败");
					mFingerprintDialog.dismiss();
				}
				mIsCalledStartIdentify = false;

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
		//失去焦点，取消验证
//		mFingerprintDialog.dismiss();
		mFingerprintIdentify.cancelIdentify();
		mIsHide = true;


	}

	private void initFingerprintDialog() {
		mFingerprintDialog = new FingerprintDialog(this);
		mFingerprintDialog.setOnFingerprintRecognize(this);
		mFingerprintDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				LogUtils.e("ActivityLock", "onShow:start");
				startFingerprintIdentify();//开启指纹解锁
			}
		});
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if (mIntentLogin!=null)
//		stopService(mIntentLogin);
	}

	private void initView() {
//        lockLinear = findViewById(R.id.lock_linear);
//        day = (TextView) findViewById(R.id.date_day);
//        month = (TextView) findViewById(R.id.date_month);
		week = (TextView) findViewById(R.id.date_week);
		nickName = (TextView) findViewById(R.id.lock_nickname);
		cancel = (TextView) findViewById(R.id.tv_cancel);
//        headImg = (ImageView) findViewById(R.id.iv_me_head);
		headImg = (ImageView) findViewById(R.id.iv_me_head);
		bodyLayout = (FrameLayout) findViewById(R.id.body_layout);
		tips = (TextView) findViewById(R.id.tv_tips);
		mEnterFingerprint = ((TextView) findViewById(R.id.lock_act_fingerprint));
		mEnterPSW = ((TextView) findViewById(R.id.lock_act_psw));
		mEnterFingerprint.setOnClickListener(this);
		mEnterPSW.setOnClickListener(this);
//        forgotPwd = (TextView) findViewById(R.id.tv_forgot_password);

//        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/num.ttf");
//        day.setTypeface(typeFace);

		cancel.setOnClickListener(this);
//        forgotPwd.setOnClickListener(this);

		setTextDate();

//		UserSharedPreferenceUtils mUserShared = new UserSharedPreferenceUtils(this);
		String nickNameStr = UserSharedPreferenceUtils.getNickName();

		if (TextUtils.isEmpty(nickNameStr)) {
			LogUtils.e("ActivityLock", "initView:nickNameStr====" + nickNameStr);
			if (!TextUtils.isEmpty(UserSharedPreferenceUtils.getLoginValue())) {
				requestMyself();
			}
		}
		else {
			ImageLoader.getInstance().displayImage("file://" + FilesPath.headPhoto, headImg, new ImageLoaderUtils().headerOption(500));
			nickName.setText(nickNameStr + "，欢迎您");
		}

	}

	private void setTextDate() {
		Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/num.ttf");
		Calendar mCalendar = Calendar.getInstance();
		String weekDay = "";
		switch (mCalendar.get(Calendar.DAY_OF_WEEK)) {
			case 1:
				weekDay = "星期日";
				break;
			case 2:
				weekDay = "星期一";
				break;
			case 3:
				weekDay = "星期二";
				break;
			case 4:
				weekDay = "星期三";
				break;
			case 5:
				weekDay = "星期四";
				break;
			case 6:
				weekDay = "星期五";
				break;
			case 7:
				weekDay = "星期六";
				break;
		}
		String month = mCalendar.get(Calendar.MONTH) + 1 + "月";
		String day = mCalendar.get(Calendar.DAY_OF_MONTH) + "号";
		StringBuilder sb = new StringBuilder();
		sb.append(month);
		sb.append(day);
		sb.append(",");
		sb.append(weekDay);
		String finalStr = sb.toString();
		week.setText(finalStr);
		week.setTypeface(typeFace);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		/*    case R.id.tv_forgot_password:
		        forgetPwdDialog();
                break;*/
			case R.id.tv_cancel:
				next();
				break;
			case R.id.lock_act_fingerprint:
				if (!mFingerprintIdentify.isHardwareEnable()) {
					ToastUtil.toast(this, "不支持该设备");
					return;
				}
				else if (!mFingerprintIdentify.isRegisteredFingerprint()) {
					ToastUtil.toast(this, "还没录入指纹,请先到设置页面开启");
					return;
				}
				if (!mFingerprintIdentify.isFingerprintEnable()) {
					LogUtils.e("ActivityLock", "onClick:");
				}
				mFingerprintDialog.show();
				break;
			case R.id.lock_act_psw:
				Intent intent = new Intent(this, ActivityLogin.class);
				startActivity(intent);
				finish();
				break;
		}
	}


	// 下一步的跳转
	private void next() {
		if (getIntent().getBooleanExtra("toLogin", false)) {
			intent = new Intent(ActivityLock.this, MainActivity.class);
			startActivity(intent);
		}
		else {
			setResult(RESULT_OK, intent);
		}
		finish();
	}

	// 提示语动画
	private void textAnimatorSet() {
		Animation animation = new TranslateAnimation(-10, 10, 0, 0);
		animation.setDuration(50);
		animation.setRepeatCount(10);
		animation.setRepeatMode(Animation.REVERSE);
		tips.startAnimation(animation);
	}

	// 设置手势密码
	private void settingLock() {
		tips.setText("请绘制手势密码,至少连接4个点");
		gestureContentView = new GestureContentView(this, false, "", new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
				if (inputCode.length() < 4) {
					tips.setText("至少连接4个点");
					textAnimatorSet();
					return;
				}
				if (lockPwdTemp == null) {
					lockPwdTemp = inputCode;
					tips.setText("请再次设置解锁图案");
					return;
				}
				if (!lockPwdTemp.equals(inputCode)) {
					lockPwdTemp = null;
					tips.setText("两次密码不一致,请重新设置");
					textAnimatorSet();
					return;
				}
				if (lockPwdTemp.equals(inputCode)) {
					ToastUtil.toast(ActivityLock.this, "设置成功");
					UserSharedPreferenceUtils.setGestureLock(inputCode, GsbApplication.getUserId());
					int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
					if (fingerprintType == 4) {//指纹验证成功,且手势开启
						UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), 3);
					}
					next();
				}
			}

			@Override
			public void checkedSuccess() {

			}

			@Override
			public void checkedFail() {

			}
		});
		gestureContentView.setParentView(bodyLayout);
	}

	// 校验手势密码
	private void checkLock() {
//        forgotPwd.setVisibility(View.VISIBLE);
		gestureContentView = new GestureContentView(this, true, UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()), new GestureDrawline.GestureCallBack() {
			@Override
			public void onGestureCodeInput(String inputCode) {
			}

			@Override
			public void checkedSuccess() {
				GsbApplication.isActive = true;
				errorCont = 0;
//				requestLogin();
				mIntentLogin = new Intent(ActivityLock.this, LoginService.class);
				startService(mIntentLogin);
				finish();
			}

			@Override
			public void checkedFail() {
				if (errorCont == 4) {
					ToastUtil.toast(ActivityLock.this, "手势密码错误,请重新登录");
					clearInfo();
					return;
				}
				errorCont++;
				tips.setText("手势密码错误,请重新输入");
				textAnimatorSet();
			}
		});
		gestureContentView.setParentView(bodyLayout);
	}


	// 忘记密码
	private void forgetPwdDialog() {
		final PromptDialog promptDialog = new PromptDialog(this);
		promptDialog.setContentText("重新登录设置新的手势密码？", null);
		promptDialog.setDefineOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				clearInfo();
				promptDialog.dismiss();
			}
		});
		promptDialog.show();
	}

	// 清除用户信息重新登录
	private void clearInfo() {
		UserSharedPreferenceUtils.setGestureLock(null, GsbApplication.getUserId());
		new ClearUtils().ClearUserInfo(ActivityLock.this);
		/*Intent intent = new Intent(this, ActivityLogin.class);
		startActivity(intent);
		finish();*/
	}

	private void requestMyself() {
	/*	if (GsbApplication.getUserId() == null) {
			return;
		}*/
		if (!CommonUtils.isLogin()) {
			return;
		}
//		LogUtils.e("ActivityLock", "requestMyself:" + GsbApplication.getUserId());
//		showLoad(false);
		NetworkManager.get(Urls.ACTION_MY)
				.execute(new com.gaoshoubang.net.callback.JsonCallback<TotalSelfBean>() {
					@Override
					public void onSuccess(TotalSelfBean totalSelfBean, Call call, Response response) {
						ImageLoader.getInstance().displayImage(totalSelfBean.getSelf().getFaceUrl(), headImg, new ImageLoaderUtils().headerOption(500));
						nickName.setText(totalSelfBean.getSelf().getNickname() + "，欢迎您");
					}

					@Override
					public void onError(Call call, Response response, Exception e) {

					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {

					}

					@Override
					public void onLoginMsgInvalidate() {
						//这里不需要回调登录失效.
					}
				});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (0 == type) {
				next();
				return true;
			}
			else {
				return true;
			}


			/*else {
				finish();
			}*/
		}
		return super.onKeyDown(keyCode, event);
	}


	@Override
	public void onIdentifyStart() {
		mFingerprintIdentify.resumeIdentify();
	}

	@Override
	public void onIdentifyCancel() {
		mFingerprintIdentify.cancelIdentify();
		if (mFingerprintSetting) {
			finish();
		}
	}
}
