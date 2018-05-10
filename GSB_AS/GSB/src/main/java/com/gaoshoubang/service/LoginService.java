package com.gaoshoubang.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.convert.Convert;
import com.gaoshoubang.util.encrypt.Des3;
import com.gaoshoubang.net.JsonUtil;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/19.
 */

public class LoginService extends Service {

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		boolean isThirdPartLogin = UserSharedPreferenceUtils.getIsThirdPartLogin(GsbApplication.getUserId());
		if (!isThirdPartLogin) {
			if (!TextUtils.isEmpty(UserSharedPreferenceUtils.getPSW())) {
				requestLoginByPSw();
			}
		}
		else {
			ShareSDK.initSDK(this);
			requestThirdPartLogin();
		}
		return super.onStartCommand(intent, Service.START_FLAG_REDELIVERY, startId);
	}

	private void requestThirdPartLogin() {
		String loginInfo = ConfigUtils.getInstance().getLoginInfo();
		String partnerCode = JsonUtil.getValue(loginInfo, "partnerCode");
		String openID = JsonUtil.getValue(loginInfo, "openid");
		String unionID = JsonUtil.getValue(loginInfo, "unionid");
		String platformName = JsonUtil.getValue(loginInfo, "platformName");
		LogUtils.e("LoginService", "onStartCommand:" + loginInfo);
		if (!TextUtils.isEmpty(partnerCode) && !TextUtils.isEmpty(openID) && !TextUtils.isEmpty(unionID)
				&& !TextUtils.isEmpty(platformName)
				) {
			Platform mPlatform = ShareSDK.getPlatform(platformName);
			PlatformDb platDB = ShareSDK.getPlatform(platformName).getDb();// 获取数平台数据DB
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
			Tuser tUser = new Tuser(partnerCode, mPlatform.getDb().getUserId(), mPlatform.getDb().get("unionid"), platDB.getUserName(), gender, faceUrl);
			LogUtils.e("LoginService", "onStartCommand:" + "解锁第三方登录");
			doLogin("", "", tUser);
		}
	}

	private void requestLoginByPSw() {
		String mobile = UserSharedPreferenceUtils.getMobile();
		String encryptPSW = UserSharedPreferenceUtils.getPSW();
		String password = null;
		try {
			password = Des3.decode(encryptPSW);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LogUtils.e("LoginService", "requestLoginByPSw:" + password);

		if (TextUtils.isEmpty(mobile) || TextUtils.isEmpty(password)) {
			return;
		}
/*		final String saveMobile = mobile;
		final String savePSW = password;*/
		doLogin(mobile, password, null);
	}

	private void doLogin(String mobile, String password, Tuser tuser) {
//		new ClearUtils().ClearUserInfo(this);
//		UserSharedPreferenceUtils.deleteLoginValue();
		NetworkManager.get2Login(Urls.ACTION_LOGIN)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.PASSWORD, password)
				.params(ParamsConstants.TUSER, Convert.toJson(tuser))
				.execute(new JsonCallback<LoginBean>() {
					@Override
					public void onReceiveOtherErr(int code, String msg) {
					}

					@Override
					public void onLoginMsgInvalidate() {
					}

					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						String uid = loginBean.getUid();
						ConfigUtils.getInstance().setUid(uid);// 保存uid

						UserSharedPreferenceUtils.setLoginValue(loginBean.getLoginValue());

						UserSharedPreferenceUtils.setCookie(loginBean.getCKey(), loginBean.getCVal());
						Intent intent = new Intent(BroadCastUtil.ACTION_REFRESH_HOME);
						intent.putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 5);
						sendBroadcast(intent);

						LogUtils.e("LoginService", "onSuccess:解锁后登录成功");
						stopSelf();
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						stopSelf();
					}

					@Override
					public void onAfter(LoginBean loginBean, Exception e) {
						super.onAfter(loginBean, e);
						stopSelf();
					}
				});
	}
}
