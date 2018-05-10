package com.gaoshoubang.ui.common.activities;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.ShareBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.ShareInfoUtil;
import com.gaoshoubang.net.ShareInfoUtil.OnGetShareInfo;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.QRCodeUtil;
import com.gaoshoubang.util.ShareContent;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ActivityShare extends BaseActivity implements OnClickListener {

	private ImageView shareSJImg;
	private ImageView qrImg;
	private LinearLayout weixin;
	private LinearLayout weixinFriend;
	private LinearLayout QQ;
	private LinearLayout weibo;
	private LinearLayout phoneB;
	private LinearLayout QQzone;
	private LinearLayout qrCode;
	private LinearLayout copyUrl;

	private TextView shareInvite;
	private TextView shareMessage;

	private ShareContent shareContent;
	private ShareInfoUtil shareInfoUtil;

	private CnfListBean cnfListBean;
	private Intent intent;


	@Override
	protected BasePresenterImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_share;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		shareSJImg = (ImageView) findViewById(R.id.share_sj_img);
		qrImg = (ImageView) findViewById(R.id.share_qr_img);
		weixin = (LinearLayout) findViewById(R.id.find_weixin);
		weixinFriend = (LinearLayout) findViewById(R.id.find_weixin_q);
		QQ = (LinearLayout) findViewById(R.id.find_qq);
		weibo = (LinearLayout) findViewById(R.id.find_weibo);
		phoneB = (LinearLayout) findViewById(R.id.find_phoneb);
		QQzone = (LinearLayout) findViewById(R.id.find_qq_zone);
		qrCode = (LinearLayout) findViewById(R.id.find_qrcode);
		copyUrl = (LinearLayout) findViewById(R.id.find_copyurl);
		shareInvite = (TextView) findViewById(R.id.share_invite);
		shareMessage = (TextView) findViewById(R.id.share_message);


	}

	@Override
	protected void initEvent() {
		weixin.setOnClickListener(this);
		weixinFriend.setOnClickListener(this);
		QQ.setOnClickListener(this);
		weibo.setOnClickListener(this);
		phoneB.setOnClickListener(this);
		QQzone.setOnClickListener(this);
		qrCode.setOnClickListener(this);
		copyUrl.setOnClickListener(this);
		shareInvite.setOnClickListener(this);
		shareMessage.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		shareContent = new ShareContent(this);
		shareInfoUtil = new ShareInfoUtil(this);
		initQrCodeImg("INVITE");
		ImageLoader.getInstance().displayImage(getIntent().getStringExtra("team_labelUrl"), shareSJImg, new ImageLoaderUtils().defaultOptions());

	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.find_weixin://微信
				shareContent.share("INVITE", 0);
				return;

			case R.id.find_weixin_q:
				shareContent.share("INVITE", 1);
				return;

			case R.id.find_qq:
				shareContent.share("INVITE", 2);
				return;

			case R.id.find_weibo:
				shareContent.share("INVITE", 3);
				return;

			case R.id.find_phoneb:// 通信录
				shareContent.share("INVITE", 4);
				return;

			case R.id.find_qrcode:// 二维码
				shareContent.share("INVITE", 6);
				return;

			case R.id.find_qq_zone:// QQ空间
				shareContent.share("INVITE", 7);
				return;

			case R.id.find_copyurl:// 复制链接
				shareContent.share("INVITE", 5);
				return;

		}
		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean mCnfListBean) {
				cnfListBean = mCnfListBean;
			}

			public void onFail() {
				return;
			}
		});
		switch (v.getId()) {
			case R.id.share_invite:// 分享文章邀请
				setUmEvent(UmEvent.gsy_everyDayShare_shareBook);
				intent = new Intent(this, WebActivityShow.class);
				intent.putExtra("url", getIntent().getStringExtra("shareUrl"));
				intent.putExtra("type", "分享文章邀请");
				startActivity(intent);
				return;

			case R.id.share_message:// 短信提醒投资
				setUmEvent(UmEvent.gsy_everyDayShare_message);
				intent = new Intent(this, WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getMESS_NOTICE());
				intent.putExtra("type", "短信提醒投资");
				startActivity(intent);
				return;
		}
	}

	private String shareUrl;
	private String filePath;

	private void initQrCodeImg(String code) {
		filePath = FilesPath.qrCodePath;
		final int width = DisplayUtil.getScreenWidth(ActivityShare.this) / 12 * 11;
		shareInfoUtil.getShareCnfContent(code, new OnGetShareInfo() {
			@Override
			public void success(ShareBean shareCnf) {
				shareUrl = shareCnf.destUrl;
				if (!shareUrl.contains("rmid")) {
					shareUrl += "?rmid=" + GsbApplication.getUserId();// 加上rmid
				}
				shareUrl = shareUrl.replace(Urls.PREFIX, "www");

				new Thread(new Runnable() {
					@Override
					public void run() {
						boolean success = new QRCodeUtil().createQRImage(ActivityShare.this, shareUrl, width, width, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_qrcode),
								filePath);
						if (success) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ImageLoader.getInstance().displayImage("file://" + filePath, qrImg);
								}
							});
						}
					}
				}).start();
			}

			@Override
			public void onFail() {
				ToastUtil.toast(ActivityShare.this, "获取分享信息失败,请检查网络配置");
			}
		});
	}
}
