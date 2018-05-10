package com.gaoshoubang.ui.password.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.LoginBean;
import com.gaoshoubang.ui.information.presenter.ResetPasswordImpl;
import com.gaoshoubang.ui.password.view.ResetPasswordView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设置密码
 *
 * @author Cisco
 */
public class ActivitySetPasswd extends BaseActivity<ResetPasswordImpl> implements OnClickListener, ResetPasswordView {
	private static final String TAG = "ActivitySetPasswd";
	private EditText oldPwd;
	private EditText newPwd;
	private TextView showPwD;
	private EditText affirmPwd;
	private View next;
	private String regx = "(?!^\\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}";

	private boolean isShowPwd = true;


	@Override
	protected ResetPasswordImpl getPresenter() {
		return new ResetPasswordImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_set_passwd;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		oldPwd = (EditText) findViewById(R.id.set_password_old_pwd);
		newPwd = (EditText) findViewById(R.id.set_password_new_pwd);
		showPwD = (TextView) findViewById(R.id.set_password_showpwd);
		affirmPwd = (EditText) findViewById(R.id.set_password_affirm_pwd);
		next = findViewById(R.id.set_password_next);
	}

	@Override
	protected void initEvent() {
		showPwD.setOnClickListener(this);
		next.setOnClickListener(this);
		newPwd.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (newPwd.getText().toString().trim().length() > 20){
					newPwd.setText(newPwd.getText().toString().trim().substring(0,20));
					Toast.makeText(getApplicationContext(),"密码过长",Toast.LENGTH_SHORT).show();
				}
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
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.set_password_showpwd:
				if (!isShowPwd) {
					showPwD.setText("隐藏");
					newPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}
				else {
					showPwD.setText("显示");
					newPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}
				isShowPwd = !isShowPwd;
				newPwd.postInvalidate();
				// 切换后将EditText光标置于末尾
				CharSequence charSequence = newPwd.getText();
				if (charSequence instanceof Spannable) {
					Spannable spanText = (Spannable) charSequence;
					Selection.setSelection(spanText, charSequence.length());
				}
				break;
			case R.id.set_password_next:
//				checkPwd();
				checkPwdNew();
				break;
		}

	}

	private void checkPwdNew() {
		if (oldPwd.getText().toString().length() == 0) {
			ToastUtil.toast(this, "请输入当前密码");
			return;
		}
		if (newPwd.getText().toString().length() == 0) {
			ToastUtil.toast(this, "请输入新密码");
			return;
		}
		if (affirmPwd.getText().toString().length() == 0) {
			ToastUtil.toast(this, "请确认新密码");
			return;
		}
		if (oldPwd.getText().toString().length() < 6 || newPwd.getText().toString().length() < 6) {
			ToastUtil.toast(this, "密码不能小于6位");
			return;
		}
		if (newPwd.getText().toString().length() <6|| affirmPwd.getText().toString().length()<6){
			ToastUtil.toast(this, "密码不能小于6位");
			return;
		}
		if (!newPwd.getText().toString().equals(affirmPwd.getText().toString())){
			ToastUtil.toast(this, "新密码与确认密码不一致");
			return;
		}
		if (!newPwd.getText().toString().trim().matches(regx)) {
			ToastUtil.toast(this, "密码必须含有数字,字母和符号中的两种");
			return;
		}
		if (newPwd.getText().toString().indexOf(" ") != -1) {
			ToastUtil.toast(this, "密码不能包含空格");
			return;
		}
		if (newPwd.getText().toString().length() != newPwd.getText().toString().getBytes().length) {
			ToastUtil.toast(this, "密码不能包含中文字符");
			return;
		}
		if (!affirmPwd.getText().toString().trim().matches(regx)){
			ToastUtil.toast(this, "密码必须含有数字,字母和符号中的两种");
			return;
		}

		if (affirmPwd.getText().toString().indexOf(" ") != -1) {
			ToastUtil.toast(this, "密码不能包含空格");
			return;
		}
		if (affirmPwd.getText().toString().length() != affirmPwd.getText().toString().getBytes().length) {
			ToastUtil.toast(this, "密码不能包含中文字符");
        return;
    }
		mPresenter.requestDoSetPasswd(oldPwd.getText().toString(), affirmPwd.getText().toString(),1);
	}
	private void checkPwd() {
		if (oldPwd.getText().toString().length() == 0) {
			ToastUtil.toast(this, "请输入当前密码");
			return;
		}

		if (newPwd.getText().toString().length() == 0) {
			ToastUtil.toast(this, "请输入新密码");
			return;
		}

		if (oldPwd.getText().toString().length() < 6 || newPwd.getText().toString().length() < 6) {
			ToastUtil.toast(this, "密码不能小于6位");
			return;
		}

		if (!newPwd.getText().toString().trim().matches(regx)) {
			ToastUtil.toast(this, "密码必须含有数字,字母和符号中的两种");
			return;
		}

		if (newPwd.getText().toString().indexOf(" ") != -1) {
			ToastUtil.toast(this, "密码不能包含空格");
			return;
		}

		if (newPwd.getText().toString().length() != newPwd.getText().toString().getBytes().length) {
			ToastUtil.toast(this, "密码不能包含中文字符");
			return;
		}
//		mPresenter.requestDoSetPasswd(oldPwd.getText().toString(), newPwd.getText().toString());
	}

	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		if (statusCode == 4008001) {
			// 锁定
			int loginNum = UserSharedPreferenceUtils.getLoginNum();
			if (UserSharedPreferenceUtils.getLoginNum() == 0) {
				ToastUtil.toast(ActivitySetPasswd.this, msg);
				return;
			}
			else {
				ToastUtil.toast(ActivitySetPasswd.this, "原密码错误,您还有" + UserSharedPreferenceUtils.getLoginNum() + "机会!");
				UserSharedPreferenceUtils.setLoginNum(--loginNum);
			}
		}
		else {
			ToastUtil.toast(ActivitySetPasswd.this, msg);
		}
	}

	public void afterLoginSuccess(LoginBean loginBean) {
		ToastUtil.toast(ActivitySetPasswd.this, "修改成功");
		String data = ConfigUtils.getInstance().getLoginInfo();
		if (data != null) {
			try {
				JSONObject jsonObject = new JSONObject(data);
				if (data.indexOf("phone") != -1) {
					jsonObject.put("password", newPwd.getText().toString());
					ConfigUtils.getInstance().setLoginInfo(jsonObject.toString());
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		SharedPreferences sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean("modifyPassword",true).apply();
		setResult(1);
		finish();

	}
}
