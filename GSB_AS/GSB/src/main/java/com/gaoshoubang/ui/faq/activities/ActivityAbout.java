package com.gaoshoubang.ui.faq.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.AppUpdateDialog;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.bean.LatestVerBean;
import com.gaoshoubang.ui.faq.presenter.AboutPresenterImpl;
import com.gaoshoubang.ui.faq.view.AboutView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;

/**
 * 关于我们
 */
public class ActivityAbout extends BaseActivity<AboutPresenterImpl> implements OnClickListener, AboutView {
	private LinearLayout update;
	private LinearLayout wechat;
	private LinearLayout qq;
	private LinearLayout weibo;
	private LinearLayout telephone;
	private LinearLayout email;
	private TextView updateVer;

	private ClipboardManager clipboardManager;

	private PackageInfo info;//包管理信息

	@Override
	protected AboutPresenterImpl getPresenter() {
		return new AboutPresenterImpl();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_about;
	}

	public void initView() {
		new FullTitleBar(this, "#ffffff");


		update = (LinearLayout) findViewById(R.id.about_update);
		wechat = (LinearLayout) findViewById(R.id.about_wechat);
		qq = (LinearLayout) findViewById(R.id.about_qq);
		weibo = (LinearLayout) findViewById(R.id.about_weibo);
		telephone = (LinearLayout) findViewById(R.id.about_telephone);
		email = (LinearLayout) findViewById(R.id.about_email);
		updateVer = (TextView) findViewById(R.id.about_update_ver);

		ripple(update);
		ripple(wechat);
		ripple(qq);
		ripple(weibo);
		ripple(telephone);
		ripple(email);

		clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

		try {
			info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		updateVer.setText("当前版本" + info.versionName);
	}

	@Override
	public void initEvent() {
		update.setOnClickListener(this);
		wechat.setOnClickListener(this);
		qq.setOnClickListener(this);
		weibo.setOnClickListener(this);
		telephone.setOnClickListener(this);
		email.setOnClickListener(this);
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

			case R.id.about_update:// 检查升级
				setUmEvent(UmEvent.Gsy_about_version);
				mPresenter.checkVer();
				break;

			case R.id.about_wechat:// 微信
				setUmEvent(UmEvent.Gsy_about_weixin);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "高搜易金融"));
				ToastUtil.toast(ActivityAbout.this, "“高搜易金融”已经复制到剪贴板");
				break;

			case R.id.about_qq:// QQ
				setUmEvent(UmEvent.Gsy_about_qq);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "1400479221"));
				ToastUtil.toast(ActivityAbout.this, "“1400479221”已经复制到剪贴板");
				break;

			case R.id.about_weibo:// 微博
				setUmEvent(UmEvent.Gsy_about_weibo);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "高搜易"));
				ToastUtil.toast(ActivityAbout.this, "“高搜易”已经复制到剪贴板");
				break;

			case R.id.about_telephone:// 商务电话
				setUmEvent(UmEvent.Gsy_about_phone);
				telephoneDialog();
				break;

			case R.id.about_email:// 商务邮箱
				setUmEvent(UmEvent.Gsy_about_email);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, "partner@gaosouyi.com"));
				ToastUtil.toast(ActivityAbout.this, "“partner@gaosouyi.com”已经复制到剪贴板");
				break;

		}
	}

	private void telephoneDialog() {
		final PromptDialog promptDialog = new PromptDialog(this);
		promptDialog.setContentText("是否拨打客服热线?", null);
		promptDialog.setDefineOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000685333"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				promptDialog.dismiss();
			}
		});
		promptDialog.show();
	}


	@Override
	public void afterGetLatestVer(LatestVerBean latestVerBean) {
		if (latestVerBean.getCode() > info.versionCode) {
			new AppUpdateDialog(ActivityAbout.this);//显示下载提示的dialog
		}
		else {
			ToastUtil.toast(ActivityAbout.this, "当前版本为最新版本");
		}
	}

}
