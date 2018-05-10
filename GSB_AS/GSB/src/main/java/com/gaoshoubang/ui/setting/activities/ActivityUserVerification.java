package com.gaoshoubang.ui.setting.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.ClearEditText;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ParamsConstants;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 验证帐号密码的页面
 *
 * @author Cisco
 */
public class ActivityUserVerification extends BaseActivity implements OnClickListener {
	protected static final String TAG = "ActivityUserVerification";
	private TextView textPhone;
	private ClearEditText editpwd;
	private ImageView editpwdClear;
	private TextView next;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_verification);
		new FullTitleBar(this, "#ffffff");
		initView();
	}

	@Override
	protected BasePresenterImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return 0;
	}

	protected void initView() {
		textPhone = (TextView) findViewById(R.id.verification_phone);
		editpwd = (ClearEditText) findViewById(R.id.verification_pwd);
		editpwdClear = (ImageView) findViewById(R.id.verification_pwd_clear);
		editpwd.setIvClear(editpwdClear);
		next = (TextView) findViewById(R.id.verification_define);

		next.setOnClickListener(this);

		String loginInfo = ConfigUtils.getInstance().getLoginInfo();
		try {
			JSONObject jsonObject = new JSONObject(loginInfo);
			if (loginInfo.indexOf("phone") != -1) {
				textPhone.setText(jsonObject.getString("phone"));
				return;
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.verification_define:
//				if (!CheckInputUtils.checkInputPSW(this,editpwd.getText().toString())){
//					return;
//				}
				requestLogin(textPhone.getText().toString(), editpwd.getText().toString());
				break;
		}
	}

	private void requestLogin(String mobile, String passwd) {
		NetworkManager.get(Urls.ACTION_LOGIN)
				.params(ParamsConstants.MOBILE_NUM, mobile)
				.params(ParamsConstants.PASSWORD, passwd)
				.execute(new JsonCallbackWrapper<LoginBean>(this) {
					@Override
					public void onSuccess(LoginBean loginBean, Call call, Response response) {
						UserSharedPreferenceUtils
								.setLoginValue(loginBean.getLoginValue());
						UserSharedPreferenceUtils
								.setGestureLock(null, GsbApplication.getUserId());
						int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
						if (fingerprintType == 3) {
							UserSharedPreferenceUtils.setFingerprintType(GsbApplication.getUserId(), 1);
						}
						finish();
					}
				});
	}
}
