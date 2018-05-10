package com.gaoshoubang.ui.information.activities;

import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.ClearEditText;
import com.gaoshoubang.widget.CommonTitleBar;
import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.ui.information.presenter.ModifyInfoImpl;
import com.gaoshoubang.ui.information.view.ModifyInfoView;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.Constants;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.CommonUtils;

import java.io.UnsupportedEncodingException;

public class ActivityModifyInfo extends BaseActivity<ModifyInfoImpl> implements View.OnClickListener, ModifyInfoView {

	private CommonTitleBar mTitle;
	private ClearEditText mInputEdit;
	private int mPageType;
	private SelfBean mBean;
	private int inputType;
	private View mEmailTips;
	private Button mNextButton;
	private boolean isCheckEmailCode;
	private String tempEmail; // 临时变量记录邮箱
	private String tempContent; //  临时变量记录初始内容

	@Override
	protected ModifyInfoImpl getPresenter() {
		return new ModifyInfoImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_modify_info;
	}

	@Override
	protected void initView() {
		mEmailTips = findViewById(R.id.ll_email_tips);
		mTitle = ((CommonTitleBar) findViewById(R.id.info_title));
		mInputEdit = ((ClearEditText) findViewById(R.id.info_edit));
		ImageView clearImage = (ImageView) findViewById(R.id.info_edit_clear);
		mInputEdit.setIvClear(clearImage);
		mNextButton = ((Button) findViewById(R.id.info_next));
		mPageType = getIntent().getIntExtra(Constants.PAGE_TYPE, -1);

		mBean = (SelfBean) getIntent().getSerializableExtra("bean");
		initContent();
		tempContent = mInputEdit.getText().toString().trim();
	}

	private void initContent() {
		if (mBean == null) {
			return;
		}
		switch (mPageType) {//页面类型
			case Constants.ACTION_TYPE_EMAIL://设置邮箱
				initInputEmailView();
				break;
			case Constants.ACTION_TYPE_NICKNAME://设置昵称
				initInputNickNameView();
				break;
			case Constants.ACTION_TYPE_QQ://设置qq
				initInputQQView();
				break;
		}
	}

	/**
	 * 初始化输入qq号的页面
	 */


	private void initInputQQView() {
		mTitle.setTitle("QQ设置");
		mInputEdit.setHint("请输入您的QQ");
		inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL;
		mInputEdit.setInputType(inputType);
		if (!TextUtils.isEmpty(mBean.getQq())) {
			mInputEdit.setText(mBean.getQq());
		}
	}

	/**
	 * 初始化输入昵称的页面
	 */
	private void initInputNickNameView() {
		mTitle.setTitle("昵称设置");
		mInputEdit.setHint("请输入您的昵称");
		if (!TextUtils.isEmpty(mBean.getNickname())) {
			mInputEdit.setText(mBean.getNickname());
		}
	}

	/**
	 * 初始化输入邮箱的页面
	 */
	private void initInputEmailView() {
		mTitle.setTitle("邮箱设置");
		mInputEdit.setHint("请输入您的邮箱");
		inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
		mInputEdit.setInputType(inputType);
		if (!TextUtils.isEmpty(mBean.getEmail())) {
			mInputEdit.setText(mBean.getEmail());
		}
	}

	@Override
	protected void initEvent() {
		mNextButton.setOnClickListener(this);
	}

	@Override
	protected void loadData() {


	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.info_next) {
			String content = mInputEdit.getText().toString().trim();
			if (TextUtils.isEmpty(content)) {
				ToastUtil.toast(this, "输入不能为空");
				return;
			}
			if (content.equals(tempContent)) {
				ToastUtil.toast(this, "资料无修改");
				return;
			}
			switch (mPageType) {
				case Constants.ACTION_TYPE_EMAIL:
					if (!isCheckEmailCode) {//请求验证码
						if (!CommonUtils.isValidEmail(content)) {//验证邮箱合法性
							ToastUtil.toast(this, "请输入正确的邮箱");
							return;
						}
						tempEmail = content;
						mPresenter.requestGetEmailVerifyCode(content);
					}
					else { //已经发送验证码
						mPresenter.requestDoSetEmail(tempEmail, content);
					}
					break;
				case Constants.ACTION_TYPE_NICKNAME:
					try {
						if (content.getBytes("GBK").length < 4 || content.getBytes("GBK").length > 24) { //验证中文
							ToastUtil.toast(this, "请输入4~24个字符,一个汉字为2个字符");
							return;
						}
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					mPresenter.requestDoSetNickname(content);
					break;
				case Constants.ACTION_TYPE_QQ:
					mPresenter.requestDoSetQq(content);
					break;
			}
		}

	}


	@Override
	public void afterModifySuccess(String msg) {
		//成功后的提示信息
		if (!TextUtils.isEmpty(msg)) {
			ToastUtil.toast(this, msg);
		}
		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_MDIFYINFO));
		finish();
	}

	@Override
	public void afterGetEmailCode() {
		mEmailTips.setVisibility(View.VISIBLE);
		mInputEdit.setHint("请输入验证码");
		mInputEdit.setText("");
		mInputEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
		mInputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
		mNextButton.setText("完成");
		isCheckEmailCode = true;
	}
}
