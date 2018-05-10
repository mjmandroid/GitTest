package com.gaoshoubang.ui.main.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.andview.refreshview.callback.IFooterCallBack;
import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.bean.base.TotalSelfBean;
import com.gaoshoubang.ui.main.presenter.MyPresenterImpl;
import com.gaoshoubang.ui.main.view.MyView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.ui.common.activities.ActivityAsset;
import com.gaoshoubang.ui.common.activities.ActivityCalendar;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.ui.common.activities.ActivityMessage;
import com.gaoshoubang.ui.faq.activities.ActivityMore;
import com.gaoshoubang.ui.common.activities.ActivityTransactionRecord;
import com.gaoshoubang.ui.setting.activities.ActivityUserManager;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.util.ClearUtils;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.util.WaveDrawable;
import com.gaoshoubang.widget.RippleIntroView;
import com.gaoshoubang.widget.wheel.MyDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;

/**
 * 我
 *
 * @author Done
 */
public class MyFragment extends BaseFragment<MyPresenterImpl> implements OnClickListener, MyView {
	private static final String TAG = "MyFragment";

	private XRefreshView xRefreshView;
	private ImageView setting;
	private LinearLayout message;
	public TextView messageTag;
	private TextView hasMoney;// 闲置资金
	private TextView allMoney;// 总资产
	private TextView mMyAssetsTV;
	private LinearLayout asset1;
	private LinearLayout asset2;
	//	private Button login;
	private TextView giveMoney;// 累计收益
	private Button deposit;
	private Button recharge;
	private LinearLayout invest;
	private LinearLayout openAccount_ly;
	private ImageView accountImg;// 未开户显示的图片
	private TextView investCount;// 我的投资
	private LinearLayout calender;
	private LinearLayout record;
	private LinearLayout about;
	private LinearLayout hongbao;// 红包
	private TextView hongbaoCount;// 红包数
	private LinearLayout gaoshoubi;// 高手币
	private TextView gaoshoubiCount;// 高手币数量
	private boolean mIsHideMoney;

	public SelfBean selfBean;
	private Intent intent;
	private CnfListBean cnfListBean;

	private ImageView mImageView0;
	private ImageView mImageView1;
	private ImageView mImageView2;
	private WaveDrawable mWaveDrawable0;
	private WaveDrawable mWaveDrawable1;
	private WaveDrawable mWaveDrawable2;

	public View maskLogin;
	private DecimalFormat mFormat;
	private ImageView mHideMoneyIV;
	private RippleIntroView mRippleIntroView;
	private View mErrorView;
	private boolean isErrorViewShow = false;
	private long mOldTime;


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_my;
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (TextUtils.isEmpty(GsbApplication.getUserId()) || selfBean == null) {
			maskLogin.setVisibility(View.VISIBLE);

		}
		resetMoneyByState();
		if (!TextUtils.isEmpty(GsbApplication.getUserId())) {
			if (!UserSharedPreferenceUtils.getFingerprintGuideSetting()) {
				setting.post(new Runnable() {
					@Override
					public void run() {
						showGuideView();
					}
				});
			}
		}
	}

	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			//相当于Fragment的onResume
			if (!TextUtils.isEmpty(GsbApplication.getUserId())) {
				if (!UserSharedPreferenceUtils.getFingerprintGuideSetting()) {
					showGuideView();
				}
			}
		}
	}


	/**
	 * 根据保存的状态回显全部金额
	 */

	private void resetMoneyByState() {
		if (selfBean == null) {
			return;
		}
		boolean hideMoneyState = UserSharedPreferenceUtils.getHideMoneyState();
		mIsHideMoney = hideMoneyState;
		if (hideMoneyState) {
			hideMoney();
		}
		else {
			showMoney();
		}
	}

	public void initView() {
		maskLogin = contentView.findViewById(R.id.mask_login);
		xRefreshView = (XRefreshView) contentView.findViewById(R.id.my_xrefreshview);
		setting = (ImageView) contentView.findViewById(R.id.my_setting);
		message = (LinearLayout) contentView.findViewById(R.id.my_message);
		messageTag = (TextView) contentView.findViewById(R.id.my_message_tag);
		hasMoney = (TextView) contentView.findViewById(R.id.my_has_money);
//		login = (Button) contentView.findViewById(R.id.btn_nowlogin);
		mMyAssetsTV = (TextView) contentView.findViewById(R.id.my_assets_all_tv);
		asset1 = (LinearLayout) contentView.findViewById(R.id.my_asset_1);
		asset2 = (LinearLayout) contentView.findViewById(R.id.my_asset_2);
		allMoney = (TextView) contentView.findViewById(R.id.my_allmoney);
		giveMoney = (TextView) contentView.findViewById(R.id.my_give_money);
		openAccount_ly = (LinearLayout) contentView.findViewById(R.id.my_opened_ly);
		accountImg = (ImageView) contentView.findViewById(R.id.my_account_img);
		deposit = (Button) contentView.findViewById(R.id.my_deposit);
		recharge = (Button) contentView.findViewById(R.id.my_recharge);
		invest = (LinearLayout) contentView.findViewById(R.id.my_invest);
		investCount = (TextView) contentView.findViewById(R.id.my_invest_count);
		calender = (LinearLayout) contentView.findViewById(R.id.my_calender);
		record = (LinearLayout) contentView.findViewById(R.id.my_record);
		hongbao = (LinearLayout) contentView.findViewById(R.id.my_hongbao);
		hongbaoCount = (TextView) contentView.findViewById(R.id.my_hongbao_count);
		gaoshoubi = (LinearLayout) contentView.findViewById(R.id.my_gaoshoubi);
		gaoshoubiCount = (TextView) contentView.findViewById(R.id.my_gaoshoubi_count);
		about = (LinearLayout) contentView.findViewById(R.id.my_about);
		mHideMoneyIV = ((ImageView) contentView.findViewById(R.id.imageView_hide_money));
	}

	@Override
	protected void initEvent() {
		mHideMoneyIV.setOnClickListener(this);
		setting.setOnClickListener(this);
		message.setOnClickListener(this);
//		login.setOnClickListener(this);
		mMyAssetsTV.setOnClickListener(this);
		allMoney.setOnClickListener(this);
		asset1.setOnClickListener(this);
		asset2.setOnClickListener(this);
		deposit.setOnClickListener(this);
		recharge.setOnClickListener(this);
		accountImg.setOnClickListener(this);
		invest.setOnClickListener(this);
		calender.setOnClickListener(this);
		record.setOnClickListener(this);
		hongbao.setOnClickListener(this);
		gaoshoubi.setOnClickListener(this);
		about.setOnClickListener(this);
		maskLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ActivityLogin.class);
				startActivity(intent);
			}
		});

		ripple(deposit);
		ripple(recharge);
		ripple(invest);
		ripple(calender);
		ripple(record);
		ripple(hongbao);
		ripple(gaoshoubi);
		ripple(message);
		ripple(about);

		initWave();

		Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/PingFangBold.ttf");
		hasMoney.setTypeface(typeFace);
		allMoney.setTypeface(typeFace);
		giveMoney.setTypeface(typeFace);

		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh() {
				mOldTime = System.currentTimeMillis();
				mPresenter.requestMyself();
			}

			@Override
			public void onLoadMore(boolean isSilence) {
			}
		});
	}

	@Override
	protected void bindData() {

	}

	@Override
	protected void loadData() {
		if (contentView != null && selfBean == null) {
//			initView();
			showLoad(true);
			mPresenter.requestMyself();
		}
	}

	private void showGuideView() {
		mRippleIntroView = ((RippleIntroView) contentView.findViewById(R.id.layout_ripple));
		mRippleIntroView.setVisibility(View.VISIBLE);
		mRippleIntroView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mRippleIntroView.setVisibility(View.GONE);
				UserSharedPreferenceUtils.setFingerprintGuideSetting(true);
			}
		});

		mRippleIntroView.setHighLightArea(setting, R.drawable.guide_fingerprint_my_setting, true, false, true);
	}


	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();

	}

	@Override
	public MyPresenterImpl getPresenter() {
		return new MyPresenterImpl();
	}


	private void initWave() {
		mImageView0 = (ImageView) contentView.findViewById(R.id.my_wave0);
		mImageView1 = (ImageView) contentView.findViewById(R.id.my_wave1);
		mImageView2 = (ImageView) contentView.findViewById(R.id.my_wave2);
		mWaveDrawable0 = new WaveDrawable(getActivity(), R.drawable.my_bg_wave);
		mWaveDrawable1 = new WaveDrawable(getActivity(), R.drawable.my_bg_wave);
		mWaveDrawable2 = new WaveDrawable(getActivity(), R.drawable.my_bg_wave);

		mImageView0.setImageDrawable(mWaveDrawable0);
		mImageView1.setImageDrawable(mWaveDrawable1);
		mImageView2.setImageDrawable(mWaveDrawable2);

		mWaveDrawable0.setIndeterminate(false);
		mWaveDrawable1.setIndeterminate(false);
		mWaveDrawable2.setIndeterminate(false);

		mWaveDrawable0.setWaveAlpha(45);
		mWaveDrawable0.setLevel(7800);
		mWaveDrawable0.setWaveAmplitude(40);
		mWaveDrawable0.setWaveLength(450);
		mWaveDrawable0.setWaveSpeed(4);

		mWaveDrawable1.setWaveAlpha(55);
		mWaveDrawable1.setLevel(8500);
		mWaveDrawable1.setWaveAmplitude(60);
		mWaveDrawable1.setWaveLength(600);
		mWaveDrawable1.setWaveSpeed(1);

		mWaveDrawable2.setWaveAlpha(35);
		mWaveDrawable2.setLevel(8100);
		mWaveDrawable2.setWaveAmplitude(30);
		mWaveDrawable2.setWaveLength(500);
		mWaveDrawable2.setWaveSpeed(3);
	}

	private void initData() {
		if (GsbApplication.getUserId() != null) {
//			login.setVisibility(View.GONE);
			allMoney.setVisibility(View.VISIBLE);
		}
		else {
//			login.setVisibility(View.VISIBLE);
			allMoney.setVisibility(View.GONE);
		}
		maskLogin.setVisibility(View.GONE);
		// 消息提示
		MainActivity mainActivity = (MainActivity) getActivity();
		if (selfBean.getNewMessage() > 0) {
			messageTag.setVisibility(View.VISIBLE);
			mainActivity.menuMsgTag.setVisibility(View.VISIBLE);
			if (selfBean.getNewMessage() > 99) {
				messageTag.setText("99+");
			}
			else {
				messageTag.setText("" + selfBean.getNewMessage());
			}
		}
		else {
			messageTag.setVisibility(View.GONE);
			mainActivity.menuMsgTag.setVisibility(View.GONE);
		}
		mFormat = new DecimalFormat("#0.00");
		// 闲置资金
		hasMoney.setText("" + mFormat.format(selfBean.getBal()));
		// hasMoney.setDuration(1000);
		// hasMoney.start();
		// 总资产
		allMoney.setText("" + mFormat.format(selfBean.getTotalAsset()));
		// 累计收益
		giveMoney.setText("" + mFormat.format(selfBean.getAccumInIntst()));
		// giveMoney.setDuration(1000);
		// giveMoney.start();

		// 我的投资
		if (Integer.valueOf(selfBean.getInVote()) > 0) {
			investCount.setText("待到账" + selfBean.getInVote() + "笔");
		}

		// 红包数量
		String hongbaoStr;
		if (selfBean.getOldCash() > 0) {
			hongbaoStr = "<font color='#0f9adb'>" + selfBean.getOldCash() + "</font>" + "个红包快过期了";
		}
		else {
			if (selfBean.getCash() == 0) {
				hongbaoStr = "口袋空空如也";
			}
			else {
				hongbaoStr = "<font color='#e60012'>" + selfBean.getCash() + "</font>" + "个红包可用";
			}
		}
		hongbaoCount.setText(Html.fromHtml(hongbaoStr));

		// 高手币数量
		gaoshoubiCount.setText(selfBean.getPowerTotal() + "个");

		// 开户/投资 图片
		//逻辑变动
		if (selfBean.getHasOpenUmp() == 2) {
			if (selfBean.getHasPayUmp() == 2) {
//				accountImg.setVisibility(View.GONE);
				openAccount_ly.setVisibility(View.VISIBLE);
			}
			else {
//				ImageLoader.getInstance().displayImage(selfBean.getUnPayImg(), accountImg, new ImageLoaderUtils().defaultOptions());
//				accountImg.setVisibility(View.VISIBLE);
				openAccount_ly.setVisibility(View.GONE);
			}
		}
		else {
//			ImageLoader.getInstance().displayImage(selfBean.getHasOpenImg(), accountImg, new ImageLoaderUtils().defaultOptions());
//			accountImg.setVisibility(View.VISIBLE);
			openAccount_ly.setVisibility(View.GONE);
		}
		if (!TextUtils.isEmpty(selfBean.balanceImg)) {
			accountImg.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(selfBean.balanceImg, accountImg, new ImageLoaderUtils().noCacheOptions());
		}
		else {
			accountImg.setVisibility(View.GONE);
		}

		//数据回显
		resetMoneyByState();
	}

	@Override
	public void onClick(View v) {
		if (selfBean == null) {
			return;
		}
		// 不需要登录
		switch (v.getId()) {
			case R.id.my_about:// 关于与反馈
				setUmEvent(UmEvent.Gsy_my_aboutAndHelp);
				intent = new Intent(getActivity(), ActivityMore.class);
				startActivity(intent);
				return;
		}
		// 需要登录
		if (GsbApplication.getUserId() == null) {
			intent = new Intent(getActivity(), ActivityLogin.class);
			startActivity(intent);
			return;
		}
		switch (v.getId()) {
			case R.id.my_setting:// 设置
				setUmEvent(UmEvent.Gsy_my_head);
				intent = new Intent(getActivity(), ActivityUserManager.class);
				startActivity(intent);
				return;

		/*	case R.id.btn_nowlogin:// 登录
				intent = new Intent(getActivity(), ActivityLogin.class);
				startActivity(intent);
				return;*/

			case R.id.my_calender:// 回款日历
				setUmEvent(UmEvent.Gsy_my_PaymentCalendar);
				intent = new Intent(getActivity(), ActivityCalendar.class);
				startActivity(intent);
				return;

			case R.id.my_assets_all_tv:// 我的资产-总资产
				setUmEvent(UmEvent.Gsy_my_myMyMoneyMessage);
				intent = new Intent(getActivity(), ActivityAsset.class);
				intent.putExtra("asset_type", 0);
				startActivity(intent);
				return;

			case R.id.my_asset_1:// 我的资产-累计收益
				setUmEvent(UmEvent.Gsy_my_myMyMoneyMessage);
				intent = new Intent(getActivity(), ActivityAsset.class);
				intent.putExtra("asset_type", 1);
				startActivity(intent);
				return;

			case R.id.my_asset_2:// 我的资产-闲置资金
				setUmEvent(UmEvent.Gsy_my_myMyMoneyMessage);
				intent = new Intent(getActivity(), ActivityAsset.class);
				intent.putExtra("asset_type", 2);
				startActivity(intent);
				return;

		}

		// 以下为需要获取配置信息的操作
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
			case R.id.my_message:// 消息
				setUmEvent(UmEvent.Gsy_my_message);
				intent = new Intent(getActivity(), ActivityMessage.class);
				startActivity(intent);
				return;

			case R.id.my_account_img:// 开户/投资图片
//				if (selfBean.getHasOpenUmp() == 2) {
//					if (selfBean.getHasPayUmp() != 2) {
				if (selfBean.balanceUrl == 1) {
					MainActivity mainActivity = (MainActivity) getActivity();
					mainActivity.viewPager.setCurrentItem(1, false);

					// TODO: 2017/5/18  产品页的动画
//                        mainActivity.mProductFragment.mProductFragment.animationAdapter.reset();
//                        mainActivity.mProductFragment.mProductFragment.animationAdapter.notifyDataSetChanged();
//					}
				}
				else if (selfBean.balanceUrl == 2) {
					intent = new Intent(getActivity(), WebActivityShow.class);
					intent.putExtra("url", cnfListBean.getUMP_URL());
					startActivity(intent);
				}
				return;

			case R.id.my_deposit:// 提现
				setUmEvent(UmEvent.Gsy_my_Withdrawals);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getWD_URL());
				intent.putExtra("type", WebActivityShow.TYPE_WD);
				startActivity(intent);
				return;

			case R.id.my_hongbao:// 红包
				setUmEvent(UmEvent.Gsy_my_invitation);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getGSB_BONUS_BILL());
				intent.putExtra("type", WebActivityShow.TYPE_BONUS_BILL);
				startActivity(intent);
				return;

			case R.id.my_recharge:// 充值
				setUmEvent(UmEvent.Gsy_my_Recharge);
				intent = new Intent(getActivity(), WebActivityShow.class);
				LogUtils.e("MyFragment", "onClick:" + cnfListBean.getRCHG_URL());
				intent.putExtra("url", cnfListBean.getRCHG_URL());
				intent.putExtra("type", WebActivityShow.TYPE_RECHARGE);
				intent.putExtra("extra", "&flag=2");
				startActivity(intent);
				return;

			case R.id.my_invest:// 我的投资
				setUmEvent(UmEvent.Gsy_my_myInvestment);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getGSB_INVEST_RECORD());
				intent.putExtra("type", WebActivityShow.TYPE_RECORD);
				// intent.putExtra("isBack", true);
				startActivity(intent);
				return;

			case R.id.my_record:// 交易记录
				setUmEvent(UmEvent.Gsy_my_TransactionRecord);
				intent = new Intent(getActivity(), ActivityTransactionRecord.class);
				startActivity(intent);
				return;

			case R.id.my_gaoshoubi:// 高手币
				setUmEvent(UmEvent.Gsy_my_upgrade);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getSHOP_URL());
				startActivity(intent);
				return;
			case R.id.imageView_hide_money:
				LogUtils.e("MyFragment", "onClick:" + mIsHideMoney);

				if (!mIsHideMoney) {
					hideMoney();
					mIsHideMoney = true;
				}
				else {
					showMoney();
					mIsHideMoney = false;
				}
				UserSharedPreferenceUtils.setHideMoneyState(mIsHideMoney);
				break;
			case R.id.my_allmoney:
				setUmEvent(UmEvent.Gsy_my_myMyMoneyMessage);
				intent = new Intent(getActivity(), ActivityAsset.class);
				intent.putExtra("asset_type", 0);
				startActivity(intent);
				break;
		}
	}

	private void showMoney() {
		allMoney.setText("" + mFormat.format(selfBean.getTotalAsset()));
		hasMoney.setText("" + mFormat.format(selfBean.getBal()));
		giveMoney.setText("" + mFormat.format(selfBean.getAccumInIntst()));
		mHideMoneyIV.setBackgroundResource(R.drawable.show_money);
	}
	public void deposit() {
		MyDialog myDialog = new MyDialog();
		myDialog.setOnDialogClickListener(new MyDialog.OnDialogClickListener() {
			@Override
			public void toPage() {
				setUmEvent(UmEvent.Gsy_my_Withdrawals);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getWD_URL());
				intent.putExtra("type", WebActivityShow.TYPE_WD);
				startActivity(intent);
			}

			@Override
			public void close() {

			}
		});
	}

	private void hideMoney() {
		allMoney.setText("*****");
		hasMoney.setText("*****");
		giveMoney.setText("*****");
		mHideMoneyIV.setBackgroundResource(R.drawable.hide_money);
	}

	/**
	 * 显示网络错误的view
	 */
	public void showErrorPage(int type) {
		if (selfBean != null) {
			return;
		}

		isErrorViewShow = true;
		if (mErrorView == null) {
			ViewStub errorViewStub = (ViewStub) contentView.findViewById(R.id.load_error_view);
			mErrorView = errorViewStub.inflate();
		}
//        mErrorView = contentView.findViewById(R.id.load_error_view);
		TextView refresh = (TextView) mErrorView.findViewById(R.id.error_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isErrorViewShow = true;  //在里面????
				mPresenter.requestMyself();
				showLoad(false);
			}
		});
		mErrorView.setVisibility(View.VISIBLE);
		xRefreshView.setVisibility(View.GONE);

		View telView = contentView.findViewById(R.id.error_tel);
		telView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				telephoneDialog();
			}
		});

		TextView title = (TextView) mErrorView.findViewById(R.id.error_title);
		switch (type) {
			case 0:
				title.setText("网络君出问题了，重启网络试试！");
				refresh.setVisibility(View.VISIBLE);
				break;

			case 1:
				title.setText("服务器需要休息了,请稍后再试!");
				refresh.setVisibility(View.GONE);
				break;
			case 2:
				refresh.setVisibility(View.GONE);
				title.setText("数据空空如也");
				break;
		}
	}

	@Override
	public void afterMyData(TotalSelfBean totalSelfBean) {
		String isShowModifyPasswd = totalSelfBean.getIsShowModifyPasswd();
		try {
			selfBean = totalSelfBean.self;
			UserSharedPreferenceUtils.setRiskAssessmentScore(selfBean.riskScore);
			UserSharedPreferenceUtils.setRiskAssessmentID(selfBean.riskId);
			initData();
			hideErrorPage();
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.toast(getActivity(), getString(R.string.hint_data_convert_error));
		}

		if (isShowModifyPasswd.equals("1")) {
			new ClearUtils().ClearUserInfo(getActivity());
			ToastUtil.toast(getActivity(), getString(R.string.password_expired_hint));
			return;
		}
		LogUtils.e("RecommendFragment", "afterGetRecommendData:" + (System.currentTimeMillis() - mOldTime));
	}

	/**
	 * 隐藏网络错误的view
	 */
	private void hideErrorPage() {
		if (isErrorViewShow == false) {
			return;
		}
		mErrorView.setVisibility(View.GONE);
		xRefreshView.setVisibility(View.VISIBLE);
	}

}
