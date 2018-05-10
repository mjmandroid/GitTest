package com.gaoshoubang.ui.common.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gaoshoubang.C;
import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.ShareBean;
import com.gaoshoubang.ui.assessment.activities.ActivityRiskAssessment;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.widget.WebPDFShowDialog;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ShareContent;
import com.gaoshoubang.util.UrlUtils;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.util.encrypt.Base64;
import com.gaoshoubang.util.encrypt.Des3;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用的webview
 *
 * @author Cisco
 */
@SuppressLint("SetJavaScriptEnabled")
public class WebActivityShow extends BaseActivity implements OnClickListener {
	private static final String TAG = "ShowWebActivity";
	public static final String TYPE_LEVEL = "升级";
	public static final String TYPE_MESSAGE = "消息";
	public static final String TYPE_ABOU_WE = "关于高搜易";
	public static final String TYPE_DETAIL = "详情";
	public static final String TYPE_REGISTER_AGREEMENT = "注册协议";
	public static final String TYPE_RECHARGE = "充值";
	public static final String TYPE_WD = "提现";
	public static final String TYPE_STREAM = "交易记录 ";
	public static final String TYPE_RECORD = "我的投资";
	public static final String TYPE_REWARD = "我的人脉";
	public static final String TYPE_REWARDMONEY = "人脉赏金";
	public static final String TYPE_YAOQINGYOUJIANG = "邀请有奖";
	public static final String TYPE_BORROW = "我的借款";
	public static final String TYPE_BONUS_BILL = "我的红包";
	public static final String TYPE_JIANGLI_GUIZE = "奖励规则";
	public static final String TYPE_AUTOBID = "自动投标";
	public static final String TYPE_UMP = "资金托管";
	public static final String TYPE_MYCARD = "我的银行卡";
	public static final String TYPE_ADDCARD = "绑定银行卡";
	public static final String TYPE_UPDATE_PAY_PWD = "修改支付密码";

	private View mWebRelative;
	private ProgressBar mProgressBar;
	private WebView mWebView;
	private TextView mWebBack;
	private TextView mWebClose;
	private TextView mWebTitle;
	private ImageView mWebShare;
	private FrameLayout mWebOther;
	private TextView mWebOtherText;
	private ImageView webLoading;

	private String tempTitle;
	public String loadUrl;
	private ShareContent shareContent;

	private String typeName;

	public String loadreveal;
	private String typereveal;
	private boolean isReload = false;// 是否要刷新,消息
	private boolean isLogin = false;// 跳转过登录后,清除历史记录

	private String cKey;
	private String cVal;
	private CookieManager cookieManager;

	private Map<String, String> cookie = new HashMap<String, String>();// 分享

	private AnimationDrawable mAnimation;

   /* @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_show);
        new FullTitleBar(this, "#ffffff");
        initView();
        initSettingWebView();

        cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        cKey = UserSharedPreferenceUtils.getCookie("c_key");
        cVal = UserSharedPreferenceUtils.getCookie("c_val");

        loadUrl = getIntent().getStringExtra("url");
        typeName = getIntent().getStringExtra("type");

        isToInvest();// 投资页面 右上角显示回款日历
        isToMyHongBao();// 我的红包页面 右上角显示问号


        webLoad(loadUrl);
    }*/

	@Override
	protected BasePresenterImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_web_show;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {

		}
	}

	public void initView() {
		new FullTitleBar(this, "#ffffff");
		mWebRelative = findViewById(R.id.web_rl);
		mProgressBar = (ProgressBar) findViewById(R.id.web_progressbar);
		mWebView = (WebView) findViewById(R.id.webview);
		mWebClose = (TextView) findViewById(R.id.web_close);
		mWebBack = (TextView) findViewById(R.id.web_back);
		mWebTitle = (TextView) findViewById(R.id.web_title);
		mWebShare = (ImageView) findViewById(R.id.web_share);
		mWebOther = (FrameLayout) findViewById(R.id.web_other_frame);
		mWebOtherText = (TextView) findViewById(R.id.web_other_text);
		webLoading = (ImageView) findViewById(R.id.web_loading);

		mAnimation = (AnimationDrawable) webLoading.getBackground();
		webLoading.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
		showErrorView();
		initSettingWebView();
	}

	@Override
	protected void initEvent() {
		mWebBack.setOnClickListener(this);
		mWebClose.setOnClickListener(this);
		mWebShare.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();

		cKey = UserSharedPreferenceUtils.getCookie("c_key");
		cVal = UserSharedPreferenceUtils.getCookie("c_val");

		loadUrl = getIntent().getStringExtra("url");
		typeName = getIntent().getStringExtra("type");

		loadreveal = getIntent().getStringExtra("url");
		typereveal = getIntent().getStringExtra("type");
		//风险揭示书
		isToInvest();// 投资页面 右上角显示回款日历
		isToMyHongBao();// 我的红包页面 右上角显示问号

		if (TextUtils.isEmpty(loadUrl)){
			webLoad(loadUrl);
		}else {
			webLoad(loadreveal);
		}
	}

	@Override
	protected void bindData() {

	}

	@SuppressLint("NewApi")
	private void initSettingWebView() {
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSettings.setDisplayZoomControls(false);
		webSettings.setAllowFileAccess(true); // 允许访问文件
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setAllowFileAccess(true);

		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		// webSettings.setDomStorageEnabled(true);
		// webSettings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
		// String appCacheDir = this.getApplicationContext().getDir("cache",
		// Context.MODE_PRIVATE).getPath();
		// webSettings.setAppCachePath(appCacheDir);
		// webSettings.setAppCacheEnabled(true);
		// webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);

		if (Build.VERSION.SDK_INT >= 21) {
			webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
		}
		mWebView.setWebChromeClient(mWebChromeClient);
		mWebView.setWebViewClient(mWebViewClient);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 4));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (RESULT_OK != resultCode) {
			return;
		}
		switch (requestCode) {
			case 0:// 跳转登录返回
				isLogin = true;
				webLoad(loadUrl);
				break;
		}
	}

	public void webLoad(String url) {
		url += "?appId=" + C.Config.APPID;
//        Log.e(TAG, "url:  " + url+"uid"+GsbApplication.getUserId());
		if (GsbApplication.getUserId() != null) {
			try {
				url += "&uid=" + Base64.change(Des3.encode(GsbApplication.getUserId()));
				// 充值链接要添加参数
				if (getIntent().getStringExtra("extra") != null) {
					url += getIntent().getStringExtra("extra");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		loadUrl = url;
		synCookies(url);
		mWebView.loadUrl(url);
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void synCookies(String url) {
		cookieManager.setAcceptCookie(true);

		if (GsbApplication.getUserId() == null && cKey != null) {
			cookieManager.removeAllCookie();
		}
		String cookies = "";
		// 登录状态传cookie
		if (GsbApplication.getUserId() != null && cKey != null && !cKey.equals("")) {
			cookies = cKey + "=" + cVal + ";path=/" + ";domain=.gaosouyi.com";
			cookieManager.setCookie(url, cookies);
		}
		if (UserSharedPreferenceUtils.getLoginValue() != null) {
			cookies = "gsb_loginValue=" +
					UserSharedPreferenceUtils.getLoginValue() + ";path=/" + ";domain=.gaosouyi.com";
			cookieManager.setCookie(url, cookies);
		}
		LogUtils.e("WebActivityShow", "synCookies:" + UserSharedPreferenceUtils.getLoginValue() + "===" + GsbApplication.getUserId());
		cookies = "prod_fr=" + GsbApplication.getGsbApplication().getChannelCode() + ";path=/" + ";domain=.gaosouyi.com";
		cookieManager.setCookie(url, cookies);
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
			cookieManager.flush();
		}
		else {
			CookieSyncManager.createInstance(this.getApplicationContext());
			CookieSyncManager.getInstance().sync();
		}

	}

	/**
	 * 诸如获取页面的title、
	 * 响应js中的alert
	 * 获取页面的加载进度等
	 */
	private WebChromeClient mWebChromeClient = new WebChromeClient() {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {//进度方法
			if (newProgress > 0) {
				mProgressBar.setVisibility(View.VISIBLE);
				mProgressBar.setProgress(newProgress);
				if (newProgress == 100) {
					mProgressBar.setVisibility(View.GONE);
				}
			}

			if (newProgress == 100) {
				mWebView.setVisibility(View.VISIBLE);
				webLoading.setVisibility(View.GONE);
				hideErrorPage();
			}
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			tempTitle = title;
			if (title.contains("appId") || title.equals("") || title.contains("http") || (typeName != null && !typeName.equals(""))) {
				mWebTitle.setText(typeName);
				return;
			}
			LogUtils.e("WebActivityShow", "onReceivedTitle:" + title);
			mWebTitle.setText(title);
		}

	};

	private WebViewClient mWebViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, final String url) {

			if (isPdfURL(url)) {
				return true;
			}

			if (isLoginURL(url)) {
				return true;
			}

			if (isTelURL(url)) {
				return true;
			}

			if (isShareURL(url)) {
				return true;
			}

			if (isSendSms(url)) {
				return true;
			}

			if (isGsbConversion(url)) {
				return true;
			}

			if (isMedal(url)) {
				return true;
			}

			if (url.indexOf(Urls.BACK_URL) != -1 || url.indexOf(Urls.AUTO_FILTER) != -1) {
				finish();
				return true;
			}

			if (ConfigUtils.getInstance().getCnfInfo() != null) {
				if (url.indexOf(ConfigUtils.getInstance().getCnfInfo().getINVITE_REWARD_SEND_RED_BAG_URL()) != -1) {
					showShare();
					return true;
				}
			}

			if (ConfigUtils.getInstance().getCnfInfo() != null) {
				if (url.indexOf(ConfigUtils.getInstance().getCnfInfo().getGSB_RED_INVESTMENT()) != -1) {
					Intent intent = new Intent(WebActivityShow.this, MainActivity.class);
					intent.putExtra("showProduct", true);
					startActivity(intent);
					finish();
					return true;
				}
			}
			if (url.indexOf(Urls.PRODUCT_TRANFER_INTERCEPTION) != -1 || url.indexOf("Xintuobao/index") != -1) {
				Intent intent = new Intent(WebActivityShow.this, MainActivity.class);
				intent.putExtra("showProduct", true);
				startActivity(intent);
				finish();
				return true;
			}
			if (url.indexOf(Urls.RISK_ASSESSMENT_INTERCEPTION) != -1) {
				Intent intent = new Intent(WebActivityShow.this, ActivityRiskAssessment.class);
				startActivity(intent);
				finish();
				return true;
			}

			return super.shouldOverrideUrlLoading(view, url);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			isShowShareBtn(loadUrl);// 分享按钮
			reloadMyMessage(url);
			if (isLoginURL(url)) {
				return;
			}
			if (isLogin) {
				mWebView.clearHistory();
				isLogin = false;
			}
		}

		@Override
		public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
			Log.e(TAG, "errorCode:" + errorCode);
			Log.e(TAG, "description:" + description);
			Log.e(TAG, "failingUrl:" + failingUrl);
			showErrorPage();
		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
			isShowCloseBtn();
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			handler.proceed();// 接受证书
		}
	};

	/**
	 * PDF文件链接拦截
	 */
	private boolean isPdfURL(String pdfUrl) {
//		Log.i("---------------------------", "pdfUrl:" + pdfUrl);
		if (pdfUrl.indexOf(".pdf") != -1) {
			new WebPDFShowDialog(WebActivityShow.this, pdfUrl).show();
			return true;
		}
		return false;
	}

	/**
	 * 登录链接拦截
	 */
	private boolean isLoginURL(String loginUrl) {
		if (loginUrl.indexOf(Urls.LOGIN_FILTER) != -1) {
			loadUrl = mWebView.getUrl();
			if (loadUrl.contains("#" + Urls.LOGIN_FILTER)) {
				loadUrl = loadUrl.replace("#" + Urls.LOGIN_FILTER, "");
			}
			Intent intent = new Intent(WebActivityShow.this, ActivityLogin.class);
			intent.putExtra("toWebView_Login", true);
			intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivityForResult(intent, 0);
			return true;
		}
		return false;
	}

	/**
	 * 客服热线拦截
	 */
	private boolean isTelURL(final String telUrl) {
		if (telUrl.indexOf("tel:") != -1) {
			final PromptDialog promptDialog = new PromptDialog(this);
			promptDialog.setContentText("是否拨打客服热线?", null);
			promptDialog.setDefineOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(telUrl));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startActivity(intent);
					promptDialog.dismiss();
				}
			});
			promptDialog.show();
			return true;
		}
		return false;
	}

	/**
	 * 拦截发短信
	 */
	private boolean isSendSms(String sendSms) {
		if (sendSms.indexOf("Index/SendSms") != -1) {
			CookieManager cookieManager = CookieManager.getInstance();
			String CookieStr = cookieManager.getCookie(sendSms);
			String[] strs = CookieStr.split(";");
			Map<String, String> cookie = new HashMap<String, String>();
			for (String str : strs) {
				String[] s = str.trim().split("=");
				if (s.length == 2) {
					cookie.put(s[0], s[1]);
				}
			}

			if (cookie.get("gsb_smsAddress") == null) {
				return true;
			}
			try {
				String address = URLDecoder.decode(cookie.get("gsb_smsAddress"), "UTF-8");
				String content = URLDecoder.decode(cookie.get("gsb_smsContent"), "UTF-8");
				Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + address));
				intent.putExtra("sms_body", content);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * 分享拦截
	 */
	private boolean isShareURL(final String shareUrl) {
		if (ConfigUtils.getInstance().getCnfInfo() != null) {
			if (shareUrl.indexOf(ConfigUtils.getInstance().getCnfInfo().getSHARE_FOR_COOKIE()) != -1) {
				CookieManager cookieManager = CookieManager.getInstance();
				String CookieStr = cookieManager.getCookie(shareUrl);
				String[] strs = CookieStr.split(";");
				Map<String, String> cookie = new HashMap<String, String>();
				for (String str : strs) {
					String[] s = str.trim().split("=");
					if (s.length == 2) {
						cookie.put(s[0], s[1]);
					}
				}

				if (cookie.get("gsb_shareTitle") == null) {
					return true;
				}
				try {
					shareContent = new ShareContent(WebActivityShow.this);
					ShareBean shareCnf = new ShareBean(URLDecoder.decode(cookie.get("gsb_shareTitle"), "UTF-8"), URLDecoder.decode(cookie.get("gsb_shareContent"), "UTF-8"),
							URLDecoder.decode(cookie.get("gsb_shareIcon"), "UTF-8"), URLDecoder.decode(cookie.get("gsb_shareLink"), "UTF-8"));
					shareContent.initShowShareContent(shareCnf);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * 高手币兑换成功
	 */
	private boolean isGsbConversion(String gsbUrl) {
		if (gsbUrl.indexOf("/Index/exchange") != -1) {
			sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_GSB_CONVERSION));
			mWebView.reload();
			return true;
		}
		return false;
	}

	/**
	 * 勋章馆兑换成功
	 */
	private boolean isMedal(String medalUrl) {
		if (medalUrl.indexOf("Index/medal.html") != -1) {
			finish();
			return true;
		}
		return false;
	}

	/**
	 * 我的消息列表刷新
	 */
	private void reloadMyMessage(String url) {
		if (typeName != null && typeName.equals(TYPE_MESSAGE) && url.equals(loadUrl) && isReload) {
			isReload = false;
			mWebView.reload();
			// 刷新推荐页
			sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 3));
		}
		else {
			isReload = true;
		}
	}

	/**
	 * 投资页面 右上角显示回款日历
	 */
	private void isToInvest() {
		if (typeName == null || !typeName.equals(TYPE_RECORD)) {
			return;
		}
		mWebOtherText.setText("回款日历");
		mWebOtherText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WebActivityShow.this, ActivityCalendar.class);
				startActivity(intent);
			}
		});
	}

	/**
	 * 我的红包页面 右上角显示问号
	 */
	private void isToMyHongBao() {
		if (typeName == null || !typeName.equals(TYPE_BONUS_BILL)) {
			return;
		}
		ImageView imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.icon_question_red);
		mWebOther.addView(imageView);
		mWebOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						webLoad(mCnfListBean.getCOUPON_EXPLAIN());
					}

					public void onFail() {
						return;
					}
				});
			}
		});
	}

	/**
	 * 是否显示关闭
	 */
	private void isShowCloseBtn() {
		if (typeName != null && (typeName.equals(TYPE_BONUS_BILL) || typeName.equals(TYPE_RECORD))) {
			mWebTitle.setPadding(0, 0, 0, 0);
			mWebClose.setVisibility(View.GONE);
			return;
		}
		if (mWebView.canGoBack()) {
			mWebTitle.setPadding(DisplayUtil.dip2px(this, 30), 0, DisplayUtil.dip2px(this, 30), 0);
			mWebClose.setVisibility(View.VISIBLE);
		}
		else {
			mWebTitle.setPadding(0, 0, 0, 0);
			mWebClose.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取cookie 是否显示分享按钮
	 */
	private void isShowShareBtn(String shareUrl) {
		String CookieStr = cookieManager.getCookie(shareUrl);
		Log.i("================", "CookieStr:" + CookieStr);
		if (CookieStr == null || CookieStr.equals("")) {
			return;
		}
		String[] strs = CookieStr.split(";");
		for (String str : strs) {
			String[] s = str.trim().split("=");
			if (s.length == 2) {
				cookie.put(s[0], s[1]);
			}
		}
		if (cookie.get("gsb_shareTitle") != null) {
			mWebShare.setVisibility(View.VISIBLE);
		}
		else {
			mWebShare.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.web_back:
				isGoBack();
				break;

			case R.id.web_share:// 分享
				try {
					shareContent = new ShareContent(WebActivityShow.this);
					ShareBean shareCnf = new ShareBean(URLDecoder.decode(cookie.get("gsb_shareTitle"), "UTF-8"), URLDecoder.decode(cookie.get("gsb_shareContent"), "UTF-8"),
							URLDecoder.decode(cookie.get("gsb_shareIcon"), "UTF-8"), URLDecoder.decode(cookie.get("gsb_shareLink"), "UTF-8"));
					shareContent.initShowShareContent(shareCnf);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				break;

			case R.id.web_close:// 关闭
				finish();
				break;
		}

	}

	// 加载分享内容
	private void showShare() {
		shareContent = new ShareContent(this, loadUrl);
		// 产品类型: 105信托宝, 116定存宝, 117净值宝,118省心,119天业宝
		int type = getIntent().getIntExtra("pro_type", -1);
		switch (type) {
			case 105:
				shareContent.showShareDialog("XINTUO");
				break;
			case 116:
				shareContent.showShareDialog("DINGCUN");
				break;
			case 117:
				shareContent.showShareDialog("JINGZHI");
				break;
			case 118:
				shareContent.showShareDialog("SHENGXIN");
				break;
			case 119:
				shareContent.showShareDialog("TIANYE");
				break;
			default:
				shareContent.showShareDialog("INVITE");
				break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			isGoBack();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	// 返回规则
	public void isGoBack() {
		if (tempTitle != null && tempTitle.equals("借款合同")) {
			if (mWebView.canGoBack()) {
				mWebView.goBack();
			}
			else {
				finish();
			}
			return;
		}

		if (mWebView != null && mWebView.getUrl() != null) {
			if (mWebView.getUrl().indexOf("isBack/yes") != -1) {
				finish();
				return;
			}
		}

		if (getIntent().getBooleanExtra("isBack", false)) {
			finish();
			return;
		}

		// 充值
		if (ConfigUtils.getInstance().getCnfInfo() != null && mWebView.getUrl().contains(ConfigUtils.getInstance().getCnfInfo().getRCHG_RESULT_URL())) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
			return;
		}

		if (UrlUtils.isFinish(this, mWebView.getUrl())) {
			finish();
			return;
		}

		if (mWebView.canGoBack()) {
			mWebView.goBack();
		}
		else {
			finish();
		}
	}

	// 加载错误界面
	private void showErrorView() {
		setShowErrorView(new OnShowErrorViewListener() {
			@Override
			public void show() {
				mWebRelative.setVisibility(View.GONE);
			}

			@Override
			public void refresh() {
				mWebView.reload();
			}

			@Override
			public void hide() {
				mWebRelative.setVisibility(View.VISIBLE);
			}
		});
	}

}
