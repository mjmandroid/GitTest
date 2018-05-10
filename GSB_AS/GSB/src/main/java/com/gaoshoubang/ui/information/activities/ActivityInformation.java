package com.gaoshoubang.ui.information.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.bean.ShareBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.ui.information.presenter.InformationPresenterImpl;
import com.gaoshoubang.ui.information.view.InformationView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.ShareInfoUtil;
import com.gaoshoubang.net.ShareInfoUtil.OnGetShareInfo;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.Constants;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.QRCodeUtil;
import com.gaoshoubang.util.ShareContent;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.permission.PermissionError;
import com.gaoshoubang.util.permission.PermissionSuccess;
import com.gaoshoubang.util.permission.PermissionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.photopicker.PhotoPickerActivity;

import java.io.File;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 修改资料
 */
public class ActivityInformation extends BaseActivity<InformationPresenterImpl> implements OnClickListener, InformationView {
	private static final String TAG = "ActivityInformation";
	private LinearLayout lyHeadImg;
	private LinearLayout lyNickname;
	private LinearLayout lyQrCode;
	private LinearLayout lyGender;
	private LinearLayout lyGrade;
	private LinearLayout lyGradeName;
	private LinearLayout lyQQ;
	private LinearLayout lyMail;
	private LinearLayout lyAddress;
	private ImageView headImg;
	private TextView nickname;
	private ImageView qrCodeImg;
	private TextView gender;
	private TextView grade;
	private TextView gradeName;
	private TextView qq;
	private TextView mail;
	private TextView address;

	private Intent intent;
	private SelfBean selfBean;
	private SelfBean.Addr selfBeanAddr;

	private ShareContent shareContent;
	private ShareInfoUtil shareInfoUtil;


	@Override
	protected InformationPresenterImpl getPresenter() {
		return new InformationPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_information;
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 4));
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		lyHeadImg = (LinearLayout) findViewById(R.id.ly_modify_head_img);
		lyNickname = (LinearLayout) findViewById(R.id.ly_modify_nickname);
		lyQrCode = (LinearLayout) findViewById(R.id.ly_modify_qrcode);
		lyGender = (LinearLayout) findViewById(R.id.ly_modify_gender);
		lyGrade = (LinearLayout) findViewById(R.id.ly_modify_grade);
		lyGradeName = (LinearLayout) findViewById(R.id.ly_modify_grade_name);
		lyQQ = (LinearLayout) findViewById(R.id.ly_modify_qq);
		lyMail = (LinearLayout) findViewById(R.id.ly_modify_mail);
		lyAddress = (LinearLayout) findViewById(R.id.ly_modify_address);
		headImg = (ImageView) findViewById(R.id.modify_head_img);
		nickname = (TextView) findViewById(R.id.modify_nickname);
		qrCodeImg = (ImageView) findViewById(R.id.modify_qrcode);
		grade = (TextView) findViewById(R.id.modify_grade);
		gradeName = (TextView) findViewById(R.id.modify_grade_name);
		gender = (TextView) findViewById(R.id.modify_gender);
		qq = (TextView) findViewById(R.id.modify_qq);
		mail = (TextView) findViewById(R.id.modify_mail);
		address = (TextView) findViewById(R.id.modify_address);

		ripple(lyHeadImg);
		ripple(lyNickname);
		ripple(lyQrCode);
		ripple(lyGender);
		ripple(lyGrade);
		ripple(lyGradeName);
		ripple(lyQQ);
		ripple(lyMail);
		ripple(lyAddress);


	}

	@Override
	protected void initEvent() {
		lyHeadImg.setOnClickListener(this);
		lyNickname.setOnClickListener(this);
		lyQrCode.setOnClickListener(this);
		lyGender.setOnClickListener(this);
		lyGrade.setOnClickListener(this);
		lyGradeName.setOnClickListener(this);
		lyQQ.setOnClickListener(this);
		lyMail.setOnClickListener(this);
		lyAddress.setOnClickListener(this);
		registerBroadcastReceiver(BroadCastUtil.ACTION_REFRESH_MDIFYINFO, mBroadcastReceiver);
	}

	@Override
	protected void loadData() {
		mPresenter.requestMyself();
		shareContent = new ShareContent(this);
		shareInfoUtil = new ShareInfoUtil(this);

	}

	@Override
	protected void bindData() {

	}

	private void initData() {
		ImageLoader.getInstance().displayImage(selfBean.getFaceUrl(), headImg, new ImageLoaderUtils().headerOption(500));
		nickname.setText(selfBean.getNickname());
		if ("".equals(selfBean.getGender())) {
			gender.setText("未设置");
		}
		else {
			if ("1".equals(selfBean.getGender())) {
				gender.setText("男");
			}
			else {
				gender.setText("女");
			}
		}

		initQrCodeImg("INVITE");

		if ("".equals(selfBean.getQq())) {
			qq.setText("未设置");
		}
		else {
			qq.setText(selfBean.getQq());
		}

		if ("".equals(selfBean.getEmail())) {
			mail.setText("未设置");
		}
		else {
			mail.setText(selfBean.getEmail());
		}

		grade.setText("V" + selfBean.getLevel());
		gradeName.setText(selfBean.getTitle());

		if (selfBean.getAddr().getProvince_name() != null) {
			selfBeanAddr = selfBean.getAddr();
			address.setText(selfBeanAddr.getProvince_name());
		}
		else {
			address.setText("未设置");
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ly_modify_head_img:// 头像
				if (!PermissionUtil.checkCamera(this)) {
					PermissionUtil.with(this).permissions(PermissionUtil.PERMISSIONS_GROUP_CAMERA).request();
				}
				else {
					changePortrait();
				}
				break;

			case R.id.ly_modify_nickname:// 昵称
				setUmEvent(UmEvent.Gsy_changeData_name);
				intent = new Intent(this, ActivityModifyInfo.class);
//				intent.putExtra("code", ActivitySetInfo.NAME_CODE);
//				intent.putExtra("info", selfBean.getNickname());
				intent.putExtra(Constants.PAGE_TYPE, Constants.ACTION_TYPE_NICKNAME);
				intent.putExtra("bean", selfBean);
				startActivity(intent);
				break;

			case R.id.ly_modify_qrcode:// 二维码
				setUmEvent(UmEvent.Gsy_changeData_erWeiMa);
				shareContent.share("INVITE", 6);
				break;

			case R.id.ly_modify_gender:// 性别
				setUmEvent(UmEvent.Gsy_changeData_six);
				intent = new Intent(this, ActivitySetGender.class);
				intent.putExtra("gender", selfBean.getGender());
				intent.putExtra("bean", selfBean);
				startActivity(intent);
				break;

			case R.id.ly_modify_grade:// 等级
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						intent = new Intent(ActivityInformation.this, WebActivityShow.class);
						intent.putExtra("url", mCnfListBean.getMY_GRADE_URL());
						intent.putExtra("type", WebActivityShow.TYPE_LEVEL);
						startActivity(intent);
					}

					public void onFail() {
						return;
					}
				});

				break;

			case R.id.ly_modify_grade_name:// 等级名称
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						intent = new Intent(ActivityInformation.this, WebActivityShow.class);
						intent.putExtra("url", mCnfListBean.getMY_GRADE_URL());
						intent.putExtra("type", WebActivityShow.TYPE_LEVEL);
						startActivity(intent);
					}

					public void onFail() {
						return;
					}
				});
				break;

			case R.id.ly_modify_qq:// qq
				setUmEvent(UmEvent.Gsy_changeData_qq);
				intent = new Intent(this, ActivityModifyInfo.class);
				intent.putExtra(Constants.PAGE_TYPE, Constants.ACTION_TYPE_QQ);
				intent.putExtra("bean", selfBean);
//				intent.putExtra("code", ActivitySetInfo.QQ_CODE);
//				intent.putExtra("info", selfBean.getQq());
				startActivity(intent);
				break;

			case R.id.ly_modify_mail:// 邮箱
				setUmEvent(UmEvent.Gsy_changeData_email);
				intent = new Intent(this, ActivityModifyInfo.class);
//				intent.putExtra("code", ActivitySetInfo.MAIL_CODE);
//				intent.putExtra("info", selfBean.getEmail());
				intent.putExtra(Constants.PAGE_TYPE, Constants.ACTION_TYPE_EMAIL);
				intent.putExtra("bean", selfBean);
				startActivity(intent);
				break;

			case R.id.ly_modify_address:// 地址
				setUmEvent(UmEvent.Gsy_changeData_address);
				intent = new Intent(this, ActivityAddress.class);

				if (selfBeanAddr != null) {
					intent.putExtra("selfBeanAddr", selfBeanAddr);
				}
				startActivity(intent);
				break;
		}
	}

	private void changePortrait() {
		setUmEvent(UmEvent.Gsy_changeData_head);
		intent = new Intent(this, PhotoPickerActivity.class);
		intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, true);
		intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_SINGLE);
		intent.putExtra("cameraPath", FilesPath.cameraPath);
		startActivityForResult(intent, 0);
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (RESULT_OK == resultCode) {
			switch (requestCode) {
				case 0:// 头像选择
					ArrayList<String> result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
					Uri data1 = data.getData();
					LogUtils.e("ActivityInformation", "onActivityResult:" + data1);
					Log.i(TAG, "result.get(0):" + result.get(0));
					String uriImg = "file://" + result.get(0);
					startPhotoZoom(uriImg);

					break;
				case 1:// 截图返回
					if (data != null) {
//						requestDoSetFace(new File(FilesPath.headPhoto));
						NetworkManager.post(Urls.ACTION_MODIFY_PORTRAIT, new File(FilesPath.headPhoto)).execute(new JsonCallbackWrapper<DataResponse<Void>>(this) {
							@Override
							public void onSuccess(DataResponse<Void> superResponse, Call call, Response response) {
								ToastUtil.toast(ActivityInformation.this, "头像修改成功");
								ImageLoader.getInstance().clearDiskCache();
								ImageLoader.getInstance().clearMemoryCache();
								ImageLoader.getInstance().displayImage("file://" + FilesPath.headPhoto, headImg, new ImageLoaderUtils().headerOption(500));
							}
						});
					}

					break;
			}
		}
	}

	// 调用剪裁
	public void startPhotoZoom(String imgUri) {
		intent = new Intent(this, ActivityClipPicture.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("imgUri", imgUri);
		startActivityForResult(intent, 1);
	}


	private String shareUrl;
	private String filePath;

	private void initQrCodeImg(String code) {
		filePath = FilesPath.qrCodePath;
		final int width = DisplayUtil.getScreenWidth(this) / 12 * 11;
		shareInfoUtil.getShareCnfContent(code, new OnGetShareInfo() {
			@Override
			public void success(ShareBean shareCnf) {
				shareUrl = shareCnf.getDestUrl();
				if (!shareUrl.contains("rmid")) {
					shareUrl += "?rmid=" + GsbApplication.getUserId();// 加上rmid
				}
				shareUrl = shareUrl.replace(Urls.PREFIX, "www");

				new Thread(new Runnable() {
					@Override
					public void run() {
						boolean success = new QRCodeUtil().createQRImage(ActivityInformation.this, shareUrl, width, width, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_qrcode),
								filePath);
						if (success) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									ImageLoader.getInstance().displayImage("file://" + filePath, qrCodeImg);
								}
							});
						}
					}
				}).start();
			}

			@Override
			public void onFail() {
			}
		});
	}

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(BroadCastUtil.ACTION_REFRESH_MDIFYINFO)) {
				mPresenter.requestMyself();
			}
		}
	};

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@PermissionSuccess
	public void onRequestPermissionSuccess() {
		changePortrait();
	}

	@PermissionError
	public void onRequestPermissionError() {
		ToastUtil.toast(this, "无法打开摄像机,请在权限管理中给应用授权");
	}

	@Override
	public void afterGetMyInfo(SelfBean self) {
		selfBean = self;
		initData();
	}
}
