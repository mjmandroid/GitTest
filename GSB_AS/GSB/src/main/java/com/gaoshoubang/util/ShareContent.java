package com.gaoshoubang.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.ShareBean;
import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.widget.QRCodeDialog;
import com.gaoshoubang.net.ShareInfoUtil;
import com.gaoshoubang.net.ShareInfoUtil.OnGetShareInfo;
import com.gaoshoubang.net.Urls;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享工具类
 *
 * @author Cisco
 */
public class ShareContent {
	protected static final String TAG = "ShareContent";
	private Activity activity;

	private String shareTitle;
	private String shareContent;
	private String shareUrl;
	private String shareImgUrl;
	private String shareUrlAddId;

	private LoadDialog loadDialog;
	private ShareInfoUtil shareInfoUtil;

	public ShareContent(Activity activity) {
		ShareSDK.initSDK(activity);
		loadDialog = new LoadDialog(activity);
		shareInfoUtil = new ShareInfoUtil(activity);
		this.activity = activity;
	}

	public ShareContent(Activity activity, String shareUrlAddId) {
		ShareSDK.initSDK(activity);
		loadDialog = new LoadDialog(activity);
		shareInfoUtil = new ShareInfoUtil(activity);
		this.shareUrlAddId = shareUrlAddId;
		this.activity = activity;
	}

	public void initShareContent(ShareBean shareCnf) {
		this.shareTitle = shareCnf.getTitle();
		this.shareContent = shareCnf.getContent();
		this.shareUrl = shareCnf.getDestUrl();
		this.shareImgUrl = shareCnf.getImgUrl();
	}

	// 自定义内容
	public void initShowShareContent(ShareBean shareCnf) {
		this.shareTitle = shareCnf.getTitle();
		this.shareContent = shareCnf.getContent();
		this.shareUrl = shareCnf.getDestUrl();
		this.shareImgUrl = shareCnf.getImgUrl();
		Log.e("ShareContent", "initShowShareContent:82==" + shareCnf.getImgUrl()+"===shareUrl==="+shareCnf.destUrl);

		showShareDialog();
	}

	/**
	 * @param mPlatform 0.微信, 1.微信朋友圈, 2.QQ, 3.微博 4.通信录 5.粘贴 6.二维码
	 */
	@SuppressLint("NewApi")
	public void sharePlatform(int mPlatform) {
		if (shareUrlAddId != null && !shareUrlAddId.equals("")) {
			shareUrl = shareUrlAddId;
		}
		if (!shareUrl.contains("rmid")) {
			shareUrl += "?rmid=" + GsbApplication.getUserId();// 加上rmid
		}
		shareUrl = shareUrl.replace(Urls.PREFIX, "m");

		ShareParams sp = new ShareParams();
		Platform platformName = null;
		switch (mPlatform) {
			case 0:// 微信
				sp.setShareType(Wechat.SHARE_WEBPAGE);
				sp.setTitle(shareTitle);
				sp.setText(shareContent);
				sp.setImageUrl(shareImgUrl);
				sp.setUrl(shareUrl);
				platformName = ShareSDK.getPlatform(Wechat.NAME);
				break;

			case 1:// 微信朋友圈
				sp.setShareType(Wechat.SHARE_WEBPAGE);
				sp.setTitle(shareTitle);
				sp.setText(shareContent);
				sp.setImageUrl(shareImgUrl);
				sp.setUrl(shareUrl);
				platformName = ShareSDK.getPlatform(WechatMoments.NAME);
				break;

			case 2:// QQ
				sp.setTitle(shareTitle);
				sp.setTitleUrl(shareUrl);
				sp.setText(shareContent);
				sp.setImageUrl(shareImgUrl);
				sp.setUrl(shareUrl);
				platformName = ShareSDK.getPlatform(QQ.NAME);
				break;

			case 3:// 微博
				sp.setText(shareContent + shareUrl);
				sp.setImageUrl(shareImgUrl);
				platformName = ShareSDK.getPlatform(SinaWeibo.NAME);
				break;

			case 4:// 通信录
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"));
				intent.putExtra("sms_body", shareContent + " " + shareUrl);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				activity.startActivity(intent);
				break;

			case 5:// 粘贴
				ClipboardManager clipboardManager = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboardManager.setPrimaryClip(ClipData.newPlainText(null, shareUrl));
				ToastUtil.toast(activity, "分享链接已经复制到剪贴板");
				break;

			case 6:// 二维码
				qeCodeShow(shareUrl);
				break;

			case 7:// QQ空间
				sp.setShareType(QZone.SHARE_WEBPAGE);
				sp.setTitle(shareTitle);
				sp.setTitleUrl(shareUrl);
				sp.setText(shareContent);
				sp.setImageUrl(shareImgUrl);
				sp.setSite("高搜易理财");
				sp.setSiteUrl(shareUrl);
				platformName = ShareSDK.getPlatform(QZone.NAME);
				break;
			default:

				break;
		}
		if (platformName != null) {
			platformName.setPlatformActionListener(platformActionListener);
			platformName.share(sp);
		}
	}

	/**
	 * 分享面板
	 */
	public void showShareDialog(final String stateCode) {
		final ShareDialogUtils.Builder builder = new ShareDialogUtils.Builder(activity);
		builder.setClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.ll_share_weixin:
						share(stateCode, 0);
						break;
					case R.id.ll_share_weixin_pyq:
						share(stateCode, 1);
						break;
					case R.id.ll_share_qq:
						share(stateCode, 2);
						break;
					case R.id.ll_share_sina:
						share(stateCode, 3);
						break;
					case R.id.ll_share_url:
						share(stateCode, 5);
						break;
					case R.id.ll_share_sms:
						share(stateCode, 6);
						break;
				}
				builder.dismiss();
			}
		});
		builder.create().show();
	}

	/**
	 * 分享面板
	 */
	private void showShareDialog() {
		final ShareDialogUtils.Builder builder = new ShareDialogUtils.Builder(activity);
		builder.setClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.ll_share_weixin:
						sharePlatform(0);
						break;
					case R.id.ll_share_weixin_pyq:
						sharePlatform(1);
						break;
					case R.id.ll_share_qq:
						sharePlatform(2);
						break;
					case R.id.ll_share_sina:
						sharePlatform(3);
						break;
					case R.id.ll_share_url:
						sharePlatform(5);
						break;
					case R.id.ll_share_sms:
						sharePlatform(6);
						break;
				}
				builder.dismiss();
			}
		});
		builder.create().show();
	}

	// 分享回调
	PlatformActionListener platformActionListener = new PlatformActionListener() {
		@Override
		public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
			ToastUtil.toast(activity, "分享成功");
		}

		@Override
		public void onError(Platform platform, int i, Throwable throwable) {
			ToastUtil.toast(activity, "分享失败");
			throwable.printStackTrace();
		}

		@Override
		public void onCancel(Platform platform, int i) {

		}
	};

	/**
	 * 分享
	 *
	 * @param code 分享类型
	 * @param mPlatform 分享平台 0.微信, 1.微信朋友圈, 2.QQ, 3.微博 4.通信录 5.粘贴
	 */
	public void share(final String code, final int mPlatform) {
		LogUtils.e("ShareContent", "share:");
		shareInfoUtil.getShareCnfContent(code, new OnGetShareInfo() {
			@Override
			public void success(ShareBean shareCnf) {
				LogUtils.e("ShareContent", "onSuc");
				initShareContent(shareCnf);
				sharePlatform(mPlatform);
				MobclickAgent.onEvent(activity, UmEvent.Gsy_find_share);
			}

			@Override
			public void onFail() {
				LogUtils.e("ShareContent", "onRequestFailed:");
			}
		});
	}

	// 二维码
	private void qeCodeShow(final String qeContent) {
		loadDialog.show();
		final QRCodeDialog qrCodeDialog = new QRCodeDialog(activity);
		final String filePath = FilesPath.qrCodePath;
		final int width = DisplayUtil.getScreenWidth(activity) / 12 * 11;
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean success = new QRCodeUtil().createQRImage(activity, qeContent, width, width, BitmapFactory.decodeResource(activity.getResources(), R.drawable.ic_launcher_qrcode), filePath);
				LogUtils.e("ShareContent", "run:" + success);
				if (success) {
					activity.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							qrCodeDialog.setQRImg(filePath);
							qrCodeDialog.show();
						}
					});
				}
				loadDialog.dismiss();
			}
		}).start();
	}
}
