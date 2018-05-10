package com.gaoshoubang.ui.main.activities;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.ui.main.adapters.MainContentAdapter;
import com.gaoshoubang.ui.main.fragments.FindFragment;
import com.gaoshoubang.ui.main.fragments.MyFragment;
import com.gaoshoubang.ui.main.fragments.ProductFragment;
import com.gaoshoubang.ui.main.fragments.RecommendFragment;
import com.gaoshoubang.ui.main.view.HomeView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.widget.AppUpdateDialog;
import com.gaoshoubang.widget.MaterialRippleLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页
 */
public class MainActivity extends BaseActivity implements OnPageChangeListener, OnClickListener, HomeView {
    protected static final String TAG = "MainActivity";
    public ViewPager viewPager;

    private RadioButton menuComment;
    private RadioButton menuProduct;
    private RadioButton menuFind;
    private RadioButton menuMy;
    public ImageView menuMsgTag;
    // private MaskLayerUtils maskLayerUtils;
    public RecommendFragment recommendFragment;
    public ProductFragment mProductFragment;
    public FindFragment findFragment;
    public MyFragment myFragment;

    private static boolean isStar = true;
    private RelativeLayout mRlContainerMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_main);

        new FullTitleBar(this, "#00000000");

        registerBroadcastReceiver();

        // maskLayerUtils = new MaskLayerUtils(this);// 蒙版

        new AppUpdateDialog(MainActivity.this);// 检查更新
//		initView();
//		initEvent();

        // 注册页跳转过来
        if (getIntent().getBooleanExtra("toRegister", false)) {
            getIntent().putExtra("toRegister", false);
            Intent intent = new Intent(this, WebActivityShow.class);
            intent.putExtra("url", getIntent().getStringExtra("url"));
            intent.putExtra("type", WebActivityShow.TYPE_UMP);
            startActivity(intent);
        }

        // 从欢迎页跳转过来的
        if (getIntent().getBooleanExtra("toWelcome_isOpen", false)) {
            getIntent().putExtra("toWelcome_isOpen", false);
            Intent intent = new Intent(this, WebActivityShow.class);
            intent.putExtra("url", getIntent().getStringExtra("url"));
            startActivity(intent);
        }
//		if (getIntent().getBooleanExtra("toWebView_showProduct", false)) {
//			getIntent().putExtra("toWebView_showProduct", false);
//			viewPager.setCurrentItem(1);
//		}


        /**
         * 只在首页创建时调用,调用一次.
         */
  /*      //手势开关
        if (isStar) {
            isStar = false;
            //登录跳转
            if (new UserSharedPreferenceUtils(this).getGestureLock(GsbApplication.getUserId()) != null) {
                Intent intent = new Intent(this, ActivityLock.class);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        }*/

    }

    @Override
    protected BasePresenterImpl getPresenter() {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    public void initEvent() {
        menuComment.setOnClickListener(this);
        menuProduct.setOnClickListener(this);
        menuFind.setOnClickListener(this);
        mRlContainerMy.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void bindData() {

    }


    protected void onNewIntent(Intent intent) {
        // 从webview跳转过来的产品列表
        if (intent.getBooleanExtra("showProduct", false)) {
            getIntent().putExtra("showProduct", false);
            viewPager.setCurrentItem(1);
        }
    }

    public void initView() {
        /**
         * 底部四个tab导航button
         */
        menuComment = (RadioButton) findViewById(R.id.menu_comment);
        menuProduct = (RadioButton) findViewById(R.id.menu_product);
        menuFind = (RadioButton) findViewById(R.id.menu_find);
        menuMy = (RadioButton) findViewById(R.id.menu_my);

        menuMsgTag = (ImageView) findViewById(R.id.menu_my_msg_tag);
        mRlContainerMy = ((RelativeLayout) findViewById(R.id.act_main_rl_container_my));


        ripple(menuComment);
        ripple(menuProduct);
        ripple(menuFind);
        ripple(mRlContainerMy);

        /**
         * mainActivity中主要管理四个fragment之间的切换
         */
        List<Fragment> fragments = new ArrayList<>();
        recommendFragment = new RecommendFragment();
        mProductFragment = new ProductFragment();
        findFragment = new FindFragment();
        myFragment = new MyFragment();

        fragments.add(recommendFragment);
        fragments.add(mProductFragment);
        fragments.add(findFragment);
        fragments.add(myFragment);

        // ViewPager
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        viewPager.setOffscreenPageLimit(4);
        mMainContentAdapter = new MainContentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mMainContentAdapter);
        viewPager.addOnPageChangeListener(this);

    }

    public void ripple(View view) {
        MaterialRippleLayout.on(view).rippleDelayClick(false).rippleRoundedCorners(200).rippleDuration(500).create();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    /**
     * 不保存fragment的状态,防止改变字体,出现getActivity为null的情况
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            String FRAGMENTS_TAG = "android:support:fragments";
            // remove掉保存的Fragment
            outState.remove(FRAGMENTS_TAG);
        }
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName) && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }

	/*// 蒙版显示
    public void initMaskLayer(int position) {
		switch (position) {
			case 0:
				break;

			case 2:// 发现
				if (GsbApplication.getUserId() != null) {
					return;
				}
				findFragment.maskLogin.setVisibility(View.VISIBLE);
				findFragment.maskLogin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
						startActivity(intent);
					}
				});
				break;

			case 3:// 我的
				if (GsbApplication.getUserId() != null) {
					return;
				}

				myFragment.maskLogin.setVisibility(View.VISIBLE);
				myFragment.maskLogin.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(MainActivity.this, ActivityLogin.class);
						startActivity(intent);
					}
				});
				break;
		}


	}*/

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.menu_comment:// 推荐
                viewPager.setCurrentItem(0);
                break;
            case R.id.menu_product:// 理财
                viewPager.setCurrentItem(1);
                break;
            case R.id.menu_find:// 发现
                viewPager.setCurrentItem(2);
                break;
            case R.id.act_main_rl_container_my:// 我的
                viewPager.setCurrentItem(3);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        setRadioButtonState(position);
//		initMaskLayer(position);
    }

    // 按钮状态
    private void setRadioButtonState(int position) {
        menuComment.setChecked(false);
        menuProduct.setChecked(false);
        menuFind.setChecked(false);
        menuMy.setChecked(false);
        menuComment.setTextColor(Color.parseColor("#666666"));
        menuProduct.setTextColor(Color.parseColor("#666666"));
        menuFind.setTextColor(Color.parseColor("#666666"));
        menuMy.setTextColor(Color.parseColor("#666666"));
        switch (position) {
            case 0:
                menuComment.setChecked(true);
                menuComment.setTextColor(Color.parseColor("#DE4746"));
                break;
            case 1:
                menuProduct.setChecked(true);
                menuProduct.setTextColor(Color.parseColor("#DE4746"));
                break;
            case 2:
                menuFind.setChecked(true);
                menuFind.setTextColor(Color.parseColor("#DE4746"));
                break;
            case 3:
                menuMy.setChecked(true);
                menuMy.setTextColor(Color.parseColor("#DE4746"));
                break;
        }
    }

    /**
     * 已登录的帐号重新登录,验证帐号密码
     */
/*	private void login() {
        String loginInfo = ConfigUtils.getInstance().getLoginInfo();
		if (loginInfo == null) {
			return;
		}
		try {
			JSONObject jsonObject = new JSONObject(loginInfo);
			if (loginInfo.indexOf("phone") != -1) {
				mHomePresenter.requestLogin(jsonObject.getString("phone"), jsonObject.getString("password"), null);
				return;
			}
			if (loginInfo.indexOf("partnerCode") != -1) {
				Tuser tuser = new Tuser(jsonObject.getString("partnerCode"), jsonObject.getString("openid"), jsonObject.getString("unionid"));
				mHomePresenter.requestLogin(null, null, tuser);
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}*/


    private long currentTime;

    /**
     * 当应用运行与主界面中,按返回键
     * 第一次不会退出应用,提示再"按异常返回桌面"
     * 第二次会退出应用
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (System.currentTimeMillis() - currentTime < 1500) {
                    ToastUtil.cancelToast();
                    moveTaskToBack(true);
                } else {
                    ToastUtil.toast(this, "再按一次返回桌面");
                    currentTime = System.currentTimeMillis();
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     *注册广播标签
     */
    public void registerBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastUtil.ACTION_REFRESH_HOME);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    // 根据广播刷新页面
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BroadCastUtil.ACTION_REFRESH_HOME)) {
                switch (intent.getIntExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, -1)) {
                    case 0:
                        recommendFragment.mPresenter.requestRecommend(false);
                        break;
                    case 1:
                        mProductFragment.mPresenter.requestProduct(false);
                        break;

                    case 2:
                        findFragment.mPresenter.requestGradeSelf(false);
                        break;

                    case 3:
                        myFragment.mPresenter.requestMyself(false);
                        break;

                    case 4:
                        mProductFragment.mPresenter.requestProduct(false);
                        findFragment.mPresenter.requestGradeSelf(false);
                        myFragment.mPresenter.requestMyself(false);
                        recommendFragment.mPresenter.requestRecommend(false);
                        //mMainContentAdapter.notifyDataSetChanged();
                        break;
                    case 5:
                        findFragment.mPresenter.requestGradeSelf(false);
                        myFragment.mPresenter.requestMyself(false);
                        break;
                }
            }
        }
    };
    private MainContentAdapter mMainContentAdapter;

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }


}
