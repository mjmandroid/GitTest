package com.gaoshoubang.ui.registration.activities;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.ui.password.activities.ActivityFindPwd;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.ClearEditText;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.ui.registration.presenter.BindUserPresenterImpl;
import com.gaoshoubang.ui.registration.view.BindUserView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.google.gson.JsonObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * 绑定高手账户
 *
 * @author Cisco
 */
public class ActivityBindUser extends BaseActivity<BindUserPresenterImpl> implements OnClickListener, BindUserView {
	private static final String TAG = "ActivityBindUser";

	private ClearEditText editPassword;
	private ImageView passwordClear;
	private TextView findPwd;
	private TextView next;

	private Tuser tuser;
	private String mobile;
	private Intent intent;


	@Override
	protected BindUserPresenterImpl getPresenter() {
		return new BindUserPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_binding_user;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");


		editPassword = (ClearEditText) findViewById(R.id.binding_user_password);
		passwordClear = (ImageView) findViewById(R.id.binding_user_password_clear);
		editPassword.setIvClear(passwordClear);
		findPwd = (TextView) findViewById(R.id.binding_user_findPwd);
		next = (TextView) findViewById(R.id.binding_user_next);


	}

	@Override
	protected void initEvent() {
		findPwd.setOnClickListener(this);
		next.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		tuser = (Tuser) getIntent().getSerializableExtra("tuser");
		mobile = getIntent().getStringExtra("mobile");
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.binding_user_findPwd:
				intent = new Intent(this, ActivityFindPwd.class);
				startActivity(intent);
				break;

			case R.id.binding_user_next:
				if (editPassword.getText().toString().length() < 6) {
					ToastUtil.toast(this, "密码必须大于6位");
					return;
				}
				mPresenter.requestLogin(mobile, editPassword.getText().toString(), tuser);
				break;
		}

	}


	@Override
	public void afterLoginSuccess(LoginBean loginBean, String psw) {
		setUmEvent(UmEvent.Gsy_threeLogin_bangDing);
		UserSharedPreferenceUtils.setMobile(mobile);// 保存手机号码
		UserSharedPreferenceUtils.setCookie(loginBean.getCKey(), loginBean.getCVal());
		/*保存登录状态*/
		ConfigUtils.getInstance().setUid(loginBean.getUid());// 保存uid
		UserSharedPreferenceUtils.setLoginValue(loginBean.getLoginValue());
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("phone", mobile);
		jsonObject.addProperty("password", psw);
		ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
		if (GsbApplication.getUserId() != null) {
			JPushInterface.setAlias(ActivityBindUser.this, GsbApplication.getUserId(), new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.i("zhj", "jpush success");
					}
				}
			});
		}
		intent = new Intent(ActivityBindUser.this, ActivityLock.class);
		intent.putExtra("type", 0);
		intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
		intent.putExtra("toLogin", true);
		startActivity(intent);

		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 4));
	}


}
