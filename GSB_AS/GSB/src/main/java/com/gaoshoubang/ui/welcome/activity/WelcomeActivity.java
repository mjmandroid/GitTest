package com.gaoshoubang.ui.welcome.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.widget.CountDownView;
import com.gaoshoubang.widget.circleindicator.CircleIndicator;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.ui.main.adapters.MainContentAdapter;
import com.gaoshoubang.ui.welcome.fragments.GuideFragment1;
import com.gaoshoubang.ui.welcome.fragments.GuideFragment2;
import com.gaoshoubang.ui.welcome.fragments.GuideFragment3;
import com.gaoshoubang.ui.welcome.fragments.GuideFragment4;
import com.gaoshoubang.ui.welcome.fragments.GuideFragment5;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.util.permission.PermissionError;
import com.gaoshoubang.util.permission.PermissionSuccess;
import com.gaoshoubang.util.permission.PermissionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import static com.gaoshoubang.util.permission.PermissionUtil.with;

/**
 * 欢迎页
 */
public class WelcomeActivity extends FragmentActivity {
	private ImageView welcomeImg;
	//欢迎页倒计时
	private int mSeconds = 3;
	private String adImgPath = "";
	private String adImgPathOpenUrl = "";
	private Intent intent;
	private TextView mRemainSeconds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

//		long oldTime = System.currentTimeMillis()
		boolean checkPhoneState = PermissionUtil.check(this, PermissionUtil.PERMISSIONS_GROUP_STORAGE
				, new String[]{Manifest.permission.READ_PHONE_STATE});
		if (!checkPhoneState) {
			PermissionUtil.with(this).permissions(PermissionUtil.PERMISSIONS_GROUP_STORAGE
					, new String[]{Manifest.permission.READ_PHONE_STATE}).request();
		}


		// 判断是否第一次使用
		if (UserSharedPreferenceUtils.getFirst()) {
			initGudie();//引导页启动活动的五张图片切换
		}
		else {
			initWelcome();//加载时启动页，启动跳转到主页
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	// 加载启动页
	private void initWelcome() {
		setContentView(R.layout.activity_welcome);
		CountDownView countDownView = (CountDownView) findViewById(R.id.countDownView);
		countDownView.setListener(new CountDownView.CountDownListener() {
			@Override
			public void onFinish() {
				handler.sendEmptyMessage(0);
			}
		});
		countDownView.start();
		welcomeImg = (ImageView) findViewById(R.id.welcome_img);
	/*	mRemainSeconds = ((TextView) findViewById(R.id.remain_seconds));
		findViewById(R.id.enter_home).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				handler.sendEmptyMessage(0);
			}
		});
//		welcomeNext = (TextView) findViewById(R.id.welcome_next);
		mRemainSeconds.setText(mSeconds + "s");
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (--mSeconds > 0) {
					mRemainSeconds.setText(mSeconds + "s");
					handler.postDelayed(this, 1000);
				}else {
					handler.sendEmptyMessage(0);
				}
			}
		}, 1000);*/

		new Handler().postDelayed(new Runnable() {
			public void run() {
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean cnfListBean) {
						if (cnfListBean == null) {
							return;
						}
						adImgPath = cnfListBean.getOPEN_APP_IMG_URL();
						adImgPathOpenUrl = cnfListBean.getOPEN_APP_DEST_URL();
						ImageLoader.getInstance().displayImage(adImgPath, welcomeImg, new ImageLoaderUtils().adOption(), new ImageLoadingListener() {
							@Override
							public void onLoadingStarted(String s, View view) {

							}

							@Override
							public void onLoadingFailed(String s, View view, FailReason failReason) {
//								handler.sendEmptyMessageDelayed(0, 1000);
							}

							@Override
							public void onLoadingComplete(String s, View view, Bitmap bitmap) {
								translateAnimation(welcomeImg, 1, 0);
//								handler.sendEmptyMessageDelayed(0, mSeconds);

							}

							@Override
							public void onLoadingCancelled(String s, View view) {
							}
						});
						/**
						 * welcomeActivity引导页
						 * 跳转到mainActivity主页
						 */
						welcomeImg.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								handler.removeMessages(0);
								intent = new Intent(WelcomeActivity.this, MainActivity.class);
								intent.putExtra("toWelcome_isOpen", true);
								intent.putExtra("url", adImgPathOpenUrl);
								startActivity(intent);
							}
						});
					}

					public void onFail() {
//						handler.sendEmptyMessageDelayed(0, 1000);
					}
				});
			}
		}, 800);
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					intent = new Intent(WelcomeActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
					break;
			}
		}
	};

	/**
	 * 移动动画
	 */
	private void translateAnimation(View view, int toX, int forX) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, toX, Animation.RELATIVE_TO_SELF, forX, Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0);
		AccelerateInterpolator acce = new AccelerateInterpolator();// 加速效果
		translateAnimation.setDuration(300);
		translateAnimation.setInterpolator(acce);
		animationSet.addAnimation(translateAnimation);
		view.startAnimation(animationSet);
		view.setVisibility(View.VISIBLE);
	}

	// 以上为启动页

	// 以下为首次引导页
	private ViewPager viewPager;
	private CircleIndicator circleIndicator;
	private List<Fragment> fragments = new ArrayList<Fragment>();

	/**
	 * 引导页5张图片切换
	 */
	private void initGudie() {
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); // 设置全屏;
		setContentView(R.layout.activity_guide);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		circleIndicator = (CircleIndicator) findViewById(R.id.indicator);

		fragments.add(new GuideFragment1());
		fragments.add(new GuideFragment2());
		fragments.add(new GuideFragment3());
		fragments.add(new GuideFragment4());
		fragments.add(new GuideFragment5());

		viewPager.setAdapter(new MainContentAdapter(getSupportFragmentManager(), fragments));
		circleIndicator.setViewPager(viewPager);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				if (position == 4) {
					circleIndicator.setVisibility(View.GONE);
				}
				else {
					circleIndicator.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}
	// 以上为首次引导页

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		PermissionUtil.onRequestPermissionsResult(this, requestCode, permissions);
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@PermissionSuccess
	public void onRequestPermissionSuccess() {

	}

	@PermissionError
	public void onRequestPermissionError() {
		ToastUtil.toast(this, "授权失败，高搜易理财需要您接受授权才可以使用。");
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacksAndMessages(null);
	}
}
