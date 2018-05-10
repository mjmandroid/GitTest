package com.gaoshoubang.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.FindBean;
import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.ui.main.presenter.FindPresenterImpl;
import com.gaoshoubang.ui.main.view.FindView;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.widget.DragView;
import com.gaoshoubang.widget.DragView.OnDrayViewClick;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.widget.SignInDialog;
import com.gaoshoubang.ui.information.activities.ActivityInformation;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.ui.common.activities.ActivityNews;
import com.gaoshoubang.ui.common.activities.ActivityShare;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.ui.main.adapters.FindNewsAdapter;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.DecimalFormat;
import java.util.List;

@SuppressLint("NewApi")
public class FindFragment extends BaseFragment<FindPresenterImpl> implements View.OnClickListener, FindView {
	private static final String TAG = "FindFragment";

	public FindBean findBean;

	private XRefreshView xRefreshView;
	private TextView nickName;
	private ImageView headImg;
	private TextView grade;
	private TextView gradeName;
	private TextView gaoShouMa;
	private TextView signDay;
	private LinearLayout inviteLinear;
	private TextView inviteText;
	private DragView dragImg;
	// 好友 赏金
	private LinearLayout teamLinear;
	private ImageView teamIcon;
	private TextView teamTitle;
	private TextView teamDes;
	private TextView teamCount;
	private ImageView teamImg;

	// 升级
	private LinearLayout goGradeLinear;
	private ImageView gradeIcon;
	private TextView goGradeTitle;
	private TextView goGradeDes;
	private TextView goGradeCount;

	// 勋章馆
	private LinearLayout honourLinear;
	private ImageView honourIcon;
	private TextView honourTitle;
	private TextView honourDes;
	private TextView honourCount;

	// 高手币兑换
	private LinearLayout conversionLinear;
	private ImageView conversionIcon;
	private TextView conversionTitle;
	private TextView conversionDes;
	private TextView conversionCount;

	// 高家大院
	private LinearLayout bbsLinear;
	private ImageView bbsIcon;
	private TextView bbsTitle;
	private TextView bbsDes;
	private TextView bbsCount;

	public LinearLayout moreNews;

	public ListView mlistView;
	private List<NewsBean> findNews;
	private FindNewsAdapter mFindNewsAdapter;
	private LinearLayout footerRefresh;
	private TextView footerRefreshBtn;
	private SwingBottomInAnimationAdapter animationAdapter;

	private CnfListBean cnfListBean;
	private Intent intent;

	public View maskLogin;
	private ImageView mInviteIv;
	private View mFooterView;
	private long mOldTime;


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_find;
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (GsbApplication.getUserId() == null || findBean == null) {
//		if (GsbApplication.getUserId() == null) {
			maskLogin.setVisibility(View.VISIBLE);
		}
	}

	public void initView() {
		new FullTitleBar(getActivity(), contentView.findViewById(R.id.title_bar));

		maskLogin = contentView.findViewById(R.id.mask_login);
		xRefreshView = (XRefreshView) contentView.findViewById(R.id.find_xrefreshview);
		mlistView = (ListView) contentView.findViewById(R.id.find_news_list);
		View headerView = View.inflate(getActivity(), R.layout.fragment_find_header, null);

		nickName = (TextView) headerView.findViewById(R.id.find_nickname);
		headImg = (ImageView) headerView.findViewById(R.id.find_head_img);
		grade = (TextView) headerView.findViewById(R.id.find_grade);
		gradeName = (TextView) headerView.findViewById(R.id.find_grade_name);
		gaoShouMa = (TextView) headerView.findViewById(R.id.find_gaoshouma);
		signDay = (TextView) headerView.findViewById(R.id.find_sign_day);
		inviteLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_invite);
		inviteText = (TextView) headerView.findViewById(R.id.find_invite_text);
		mInviteIv = ((ImageView) headerView.findViewById(R.id.invite_icon_iv));

		// 白拿理财
		teamLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_team);
		teamIcon = (ImageView) headerView.findViewById(R.id.find_team_icon);
		teamTitle = (TextView) headerView.findViewById(R.id.find_team_title);
		teamDes = (TextView) headerView.findViewById(R.id.find_team_content);
		teamCount = (TextView) headerView.findViewById(R.id.find_team_count);
		teamImg = (ImageView) headerView.findViewById(R.id.find_team_img);

		// 升级
		goGradeLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_grade);
		gradeIcon = (ImageView) headerView.findViewById(R.id.find_grade_icon);
		goGradeTitle = (TextView) headerView.findViewById(R.id.find_grade_title);
		goGradeDes = (TextView) headerView.findViewById(R.id.find_grade_content);
		goGradeCount = (TextView) headerView.findViewById(R.id.find_grade_count);

		// 勋章馆
		honourLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_honour);
		honourIcon = (ImageView) headerView.findViewById(R.id.find_honour_icon);
		honourTitle = (TextView) headerView.findViewById(R.id.find_honour_title);
		honourDes = (TextView) headerView.findViewById(R.id.find_honour_content);
		honourCount = (TextView) headerView.findViewById(R.id.find_honour_count);

		// 高手币兑换
		conversionLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_conversion);
		conversionIcon = (ImageView) headerView.findViewById(R.id.find_conversion_icon);
		conversionTitle = (TextView) headerView.findViewById(R.id.find_conversion_title);
		conversionDes = (TextView) headerView.findViewById(R.id.find_conversion_content);
		conversionCount = (TextView) headerView.findViewById(R.id.find_conversion_count);

		// 高家大院
		bbsLinear = (LinearLayout) headerView.findViewById(R.id.find_ly_bbs);
		bbsIcon = (ImageView) headerView.findViewById(R.id.find_bbs_icon);
		bbsTitle = (TextView) headerView.findViewById(R.id.find_bbs_title);
		bbsDes = (TextView) headerView.findViewById(R.id.find_bbs_content);
		bbsCount = (TextView) headerView.findViewById(R.id.find_bbs_count);

		moreNews = (LinearLayout) headerView.findViewById(R.id.find_more_ly);

		mFooterView = View.inflate(getActivity(), R.layout.fragment_find_footer, null);
		footerRefresh = (LinearLayout) mFooterView.findViewById(R.id.footer_refresh);
		footerRefreshBtn = (TextView) mFooterView.findViewById(R.id.footer_refresh_btn);
		mlistView.addHeaderView(headerView);
		mlistView.addFooterView(mFooterView);


	}

	@Override
	protected void initEvent() {
		mFindNewsAdapter = new FindNewsAdapter(getActivity());
		animationAdapter = new SwingBottomInAnimationAdapter(mFindNewsAdapter);
		animationAdapter.setAbsListView(mlistView);
		mlistView.setAdapter(animationAdapter);

		moreNews.setOnClickListener(this);
		headImg.setOnClickListener(this);
		signDay.setOnClickListener(this);
		inviteLinear.setOnClickListener(this);
		teamLinear.setOnClickListener(this);
		goGradeLinear.setOnClickListener(this);
		honourLinear.setOnClickListener(this);
		conversionLinear.setOnClickListener(this);
		bbsLinear.setOnClickListener(this);
		footerRefreshBtn.setOnClickListener(this);
		maskLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getContext(), ActivityLogin.class);
				startActivity(intent);
			}
		});

		ripple(inviteLinear);
		ripple(teamLinear);
		ripple(goGradeLinear);
		ripple(honourLinear);
		ripple(conversionLinear);
		ripple(bbsLinear);

		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh() {
				mOldTime = System.currentTimeMillis();
				mPresenter.requestGradeSelf();
			}

			@Override
			public void onLoadMore(boolean isSlience) {
			}
		});

		dragImg = (DragView) contentView.findViewById(R.id.find_dragView);
		dragImg.setSaveKey("find_x", "find_y");
		dragImg.setViewXY(DisplayUtil.getScreenWidth(getActivity()) - DisplayUtil.dip2px(getActivity(), 44), DisplayUtil.dip2px(getActivity(), 460));
		dragImg.setViewAnimator(false);
		dragViewAnimator(dragImg);

		dragImg.setOnDrayViewClick(new OnDrayViewClick() {
			@Override
			public void onClick() {
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						setUmEvent(UmEvent.Gsy_find_share);
						if (GsbApplication.getUserId() == null) {
							return;
						}
						intent = new Intent(getActivity(), ActivityShare.class);
						intent.putExtra("team", findBean.getTeam().getTeam());
						intent.putExtra("team_labelUrl", findBean.getTeam().getLabelUrl());
						intent.putExtra("shareUrl", findBean.getShareUrl());
						startActivity(intent);
						return;
					}

					public void onFail() {
						return;
					}
				});
			}
		});

		mFooterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				intent = new Intent(getActivity(), ActivityNews.class);
				startActivity(intent);
			}
		});

		mlistView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0 || findNews.get(position - 1).getUrl().equals("")) {
					return;
				}
				Intent intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", findNews.get(position - 1).getUrl());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void bindData() {

	}

	@Override
	protected void loadData() {
		if (contentView != null && findBean == null) {
//			initView();
			showLoad(true);
			mPresenter.requestGradeSelf();
		}

	}

	private void initData() {
		maskLogin.setVisibility(View.GONE);
		nickName.setText(findBean.getNickname());
		ImageLoader.getInstance().displayImage(findBean.getFaceUrl(), headImg, new ImageLoaderUtils().headerOption(500));
		if (findBean.getFaceUrl() != null && !findBean.getFaceUrl().equals("")) {
			UserSharedPreferenceUtils.saveBitmap(findBean.getFaceUrl());
		}
		UserSharedPreferenceUtils.setNickName(findBean.getNickname());
		grade.setText("VIP " + findBean.getRank());
		gradeName.setText(findBean.getRankname());
		gaoShouMa.setText("高手码: " + findBean.getUid());
		gaoShouMa.setVisibility(View.VISIBLE);

		if (findBean.getSignInfo().getSign() != 1) {
			signDay.setText("已签到(" + findBean.getSignInfo().getWeek() + ")");
		}
		else {
			signDay.setText("签到(" + findBean.getSignInfo().getWeek() + ")");
			if (findBean.getSignInfo().getWeek() == 0) {
				signDay.setText("签到");
			}
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		inviteText.setText("已邀请" + findBean.getOnes() + "人，累计赚取" + df.format(Double.parseDouble(findBean.getMoney())) + "元");
		// 白拿理财
		ImageLoader.getInstance().displayImage(findBean.getTeam().getLogo(), teamIcon, new ImageLoaderUtils().findOption());
		teamTitle.setText(findBean.getTeam().getTitle());
		teamDes.setText(findBean.getTeam().getCc());
		teamCount.setText(findBean.getTeam().getR_str());
		if (findBean.getTeam().getTeam() == 0) {
			teamImg.setVisibility(View.INVISIBLE);
		}
		else {
			teamImg.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(findBean.getTeam().getMiniLogo(), teamImg, new ImageLoaderUtils().defaultOptions());
		}
		mInviteIv.setVisibility(findBean.getTeam().getIsOpen() == 1 ? View.VISIBLE : View.GONE);

		// 升级
		ImageLoader.getInstance().displayImage(findBean.getUpInfo().getLogo(), gradeIcon, new ImageLoaderUtils().findOption());
		goGradeTitle.setText(findBean.getUpInfo().getTitle());
		goGradeDes.setText(findBean.getUpInfo().getCc());
		goGradeCount.setText(findBean.getUpInfo().getR_str());

		// 勋章馆
		ImageLoader.getInstance().displayImage(findBean.getHonour().getLogo(), honourIcon, new ImageLoaderUtils().findOption());
		honourTitle.setText(findBean.getHonour().getTitle());
		honourDes.setText(findBean.getHonour().getCc());
		honourCount.setText(findBean.getHonour().getR_str());

		// 高手币兑换
		ImageLoader.getInstance().displayImage(findBean.getValInfo().getLogo(), conversionIcon, new ImageLoaderUtils().findOption());
		conversionTitle.setText(findBean.getValInfo().getTitle());
		conversionDes.setText(findBean.getValInfo().getCc());
		conversionCount.setText(findBean.getValInfo().getR_str());

		// 高家大院
		ImageLoader.getInstance().displayImage(findBean.getBbs().getLogo(), bbsIcon, new ImageLoaderUtils().findOption());
		bbsTitle.setText(findBean.getBbs().getTitle());
		bbsDes.setText(findBean.getBbs().getCc());
		bbsCount.setText(findBean.getBbs().getR_str());
	}

	@Override
	public void onClick(View v) {
		if (findBean == null) {
			return;
		}
		if (GsbApplication.getUserId() == null) {
			intent = new Intent(getActivity(), ActivityLogin.class);
			startActivity(intent);
			return;
		}
		switch (v.getId()) {
			case R.id.find_ly_honour:// 勋章馆
				if (findBean.getHonour().getUrl().equals("")) {
					return;
				}
				setUmEvent(UmEvent.Gsy_find_XZG);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", findBean.getHonour().getUrl());
				startActivity(intent);
				return;

			case R.id.find_ly_bbs:// 高家大院
				if (findBean.getBbs().getUrl().equals("")) {
					return;
				}
				setUmEvent(UmEvent.Gsy_find_gjdy);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", findBean.getBbs().getUrl());
				startActivity(intent);
				return;

			case R.id.find_sign_day:// 签到
				setUmEvent(UmEvent.Gsy_find_click);
				if (findBean.getSignInfo().getSign() == 1) {
					SignInDialog signInDialog = new SignInDialog(getActivity());
					signInDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						@Override
						public void onDismiss(DialogInterface dialog) {
							mPresenter.requestGradeSelf();
						}
					});
				}
				else {
					PromptDialog promptDialog = new PromptDialog(getActivity());
					promptDialog.setContentText("温馨提示", "您已完成今日签到,明天再来哦~");
					promptDialog.setContentButton("我知道了", null);
					promptDialog.show();
				}
				return;

			case R.id.find_more_ly:
				intent = new Intent(getActivity(), ActivityNews.class);
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
			case R.id.find_head_img:// 头像
				setUmEvent(UmEvent.Gsy_find_head);
				intent = new Intent(getActivity(), ActivityInformation.class);
				startActivity(intent);
				break;

			case R.id.find_ly_grade:// 升级
				setUmEvent(UmEvent.gsy_find_upgrade);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getMY_GRADE_URL());
				startActivity(intent);
				break;

			case R.id.find_ly_invite:// 领赏金
				setUmEvent(UmEvent.Gsy_find_yaoQing);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getINVITE_REWARD_URL());
				startActivity(intent);
				break;

			case R.id.find_ly_team:// 理财军团
				if (findBean.getTeam().getUrl().equals("")) {
					return;
				}
				setUmEvent(UmEvent.Gsy_find_lcjt);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", findBean.getTeam().getUrl());
				startActivity(intent);
				break;

			case R.id.find_ly_conversion:// 高手币兑换
				setUmEvent(UmEvent.Gsy_find_gsbExchange);
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", cnfListBean.getSHOP_URL());
				startActivity(intent);
				return;

			case R.id.footer_refresh_btn:// 重新加载媒体报道
				mPresenter.requestMessageMedia(5);
				return;
		}
	}

	private AnimatorSet dragAnimatorSet = new AnimatorSet();

	private void dragViewAnimator(View view) {
		dragViewHandler.removeMessages(0);
		ObjectAnimator objectScaleX = ObjectAnimator.ofFloat(dragImg, "scaleX", 1f, 0.9f, 1.1f, 0.85f, 1f);
		ObjectAnimator objectScaleY = ObjectAnimator.ofFloat(dragImg, "scaleY", 1f, 0.9f, 1.1f, 0.85f, 1f);
		dragAnimatorSet.playTogether(objectScaleX, objectScaleY);
		dragAnimatorSet.setDuration(1200);
		dragViewHandler.sendEmptyMessage(0);
	}

	Handler dragViewHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dragAnimatorSet.start();
			dragViewHandler.sendMessageDelayed(new Message(), 1200);
		}
	};


	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();
	}

	private View mErrorView;
	private boolean isErrorViewShow = false;

	/**
	 * 显示网络错误的view
	 */
	public void showErrorPage() {
		if (findBean != null) {
			return;
		}
		if (mErrorView == null) {
			ViewStub errorViewStub = (ViewStub) contentView.findViewById(R.id.load_error_view);
			mErrorView = errorViewStub.inflate();
		}
		isErrorViewShow = true;
//		mErrorView = contentView.findViewById(R.id.load_error_view);
		TextView refresh = (TextView) mErrorView.findViewById(R.id.error_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isErrorViewShow = true;  //点击按钮里面?
				mPresenter.requestGradeSelf();
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
	}

	@Override
	public FindPresenterImpl getPresenter() {
		return new FindPresenterImpl();
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

	@Override
	public void afterGetFindData(FindBean data) {
		try {
			findBean = data;
			initData();
			mPresenter.requestMessageMedia(5);
			hideErrorPage();
		} catch (Exception e) {
			e.printStackTrace();
			ToastUtil.toast(getActivity(), getString(R.string.hint_data_convert_error));
		}
		LogUtils.e("RecommendFragment", "afterGetRecommendData:" + (System.currentTimeMillis() - mOldTime));
	}

	@Override
	public void showFooterRefresh() {
		footerRefresh.setVisibility(View.VISIBLE);
	}

	@Override
	public void afterGetMessageMedia(List<NewsBean> data) {
		findNews = data;
		mFindNewsAdapter.updateData(findNews);
		animationAdapter.reset();
		footerRefresh.setVisibility(View.GONE);
	}
}
