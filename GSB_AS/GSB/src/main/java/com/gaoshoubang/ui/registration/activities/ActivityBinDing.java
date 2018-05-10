package com.gaoshoubang.ui.registration.activities;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.widget.MobileNumberEditText;
import com.gaoshoubang.bean.Tuser;
import com.gaoshoubang.bean.response.CheckMobileResponse;
import com.gaoshoubang.ui.registration.presenter.BindingPresenterImpl;
import com.gaoshoubang.ui.registration.view.BindingView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.CommonUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 绑定页面1
 *
 * @author Cisco
 */
public class ActivityBinDing extends BaseActivity<BindingPresenterImpl> implements OnClickListener, BindingView {
	private static final String TAG = "ActivityBinDing";

	private ImageView headimg;
	private TextView nickname;
	private MobileNumberEditText editMobile;
	private ImageView mobileClear;
	private TextView next;

	private Tuser tuser;

	private Intent intent;


	@Override
	protected BindingPresenterImpl getPresenter() {
		return new BindingPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_binding;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		headimg = (ImageView) findViewById(R.id.binding_headimg);
		nickname = (TextView) findViewById(R.id.binding_nickname);
		editMobile = (MobileNumberEditText) findViewById(R.id.binding_mobile);
		mobileClear = (ImageView) findViewById(R.id.binding_mobile_clear);
		editMobile.setIvClear(mobileClear);
		next = (TextView) findViewById(R.id.binding_next);

		next.setOnClickListener(this);

		tuser = (Tuser) getIntent().getSerializableExtra("tuser");
		if (tuser == null) {
			finish();
		}
		ImageLoader.getInstance().displayImage(tuser.getFaceUrl(), headimg, new ImageLoaderUtils().headerOption(500));
		nickname.setText(tuser.getNickname());

		// 手机号码输入监听
		editMobile.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 13) {
					next.setBackgroundResource(R.drawable.shape_login_btn_sel);
				}
				else {
					next.setBackgroundResource(R.drawable.shape_login_btn_nor);
				}
			}
		});
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
			case R.id.binding_next:
				if (editMobile.getTextForString().length() < 11 || CommonUtils.isMobileNO(this, editMobile.getTextForString()) != null) {
					ToastUtil.toast(this, "请输入正确的手机号码");
					return;
				}
//			requestDoCheckMobile(editMobile.getTextForString());
				mPresenter.requestDoCheckMobile(editMobile.getTextForString());
				break;
		}
	}

	@Override
	public void afterCheckNum(CheckMobileResponse result) {
		// 手机号是否已注册。1：未注册 2：已注册
//		String hasRegMobile = result.hasRegMobile;
		LogUtils.e("ActivityBinDing", "afterCheckNum:" + result.hasRegMobile);
		if (result.hasRegMobile == 2) {
			ToastUtil.toast(ActivityBinDing.this, "该手机号已经注册");
			intent = new Intent(ActivityBinDing.this, ActivityBindUser.class);
		}
		else {
			intent = new Intent(ActivityBinDing.this, ActivityBinDing2.class);
		}
		intent.putExtra("tuser", tuser);
		intent.putExtra("mobile", editMobile.getTextForString());
		startActivity(intent);
	}
}
