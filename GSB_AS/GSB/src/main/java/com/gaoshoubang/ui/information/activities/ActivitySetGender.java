package com.gaoshoubang.ui.information.activities;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.information.presenter.ModifyGenderImpl;
import com.gaoshoubang.ui.information.view.ModifyGenderView;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;

/**
 * 设置性别
 *
 * @author Cisco
 */
public class ActivitySetGender extends BaseActivity<ModifyGenderImpl> implements OnClickListener, ModifyGenderView {
	private static final String TAG = "ActivitySetGender";
	private String gender;
	private RelativeLayout man;
	private RelativeLayout woman;
	private ImageView manTag;
	private ImageView womanTag;


	@Override
	protected ModifyGenderImpl getPresenter() {
		return new ModifyGenderImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_set_gender;
	}

	@Override
	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		man = (RelativeLayout) findViewById(R.id.gengder_man);
		woman = (RelativeLayout) findViewById(R.id.gengder_woman);
		manTag = (ImageView) findViewById(R.id.gengder_man_s);
		womanTag = (ImageView) findViewById(R.id.gengder_woman_s);

	}

	@Override
	protected void initEvent() {
		man.setOnClickListener(this);
		woman.setOnClickListener(this);

	}

	@Override
	protected void loadData() {
		gender = getIntent().getStringExtra("gender");
		if (gender != null) {
			if (gender.equals("1")) {
				manTag.setVisibility(View.VISIBLE);
			}
			else {
				womanTag.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.gengder_man:
				gender = "1";
				mPresenter.requestDoSetGender(gender);
				break;
			case R.id.gengder_woman:
				gender = "2";
				mPresenter.requestDoSetGender(gender);
				break;
		}
	}

	@Override
	public void afterModifySuccess() {
		ToastUtil.toast(ActivitySetGender.this, "性别修改成功");
		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_MDIFYINFO));
		finish();
	}
}
