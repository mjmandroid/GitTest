package com.gaoshoubang.ui.main.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ReplacementSpan;
import android.text.style.SubscriptSpan;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.RecommendAct;
import com.gaoshoubang.bean.RecommendAlt;
import com.gaoshoubang.bean.RecommendBean;
import com.gaoshoubang.bean.RecommendGongHuo;
import com.gaoshoubang.bean.RecommendPro;
import com.gaoshoubang.bean.RecommendRel;
import com.gaoshoubang.bean.RelWindowBean;
import com.gaoshoubang.ui.main.presenter.RecommendPresenterImpl;
import com.gaoshoubang.ui.main.view.RecommendView;
import com.gaoshoubang.ui.login.activities.ActivityLogin;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.ui.main.adapters.RecommendRelAdapter;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.widget.DragView;
import com.gaoshoubang.widget.DragView.OnDrayViewClick;
import com.gaoshoubang.widget.MarqueeText;
import com.gaoshoubang.widget.gallery.ClipViewPager;
import com.gaoshoubang.widget.gallery.GalleryImageAdapter;
import com.gaoshoubang.widget.gallery.ScalePageTransformer;
import com.gaoshoubang.widget.viewpager.IndicatorLocation;
import com.gaoshoubang.widget.viewpager.LoopViewPagerLayout;
import com.gaoshoubang.widget.viewpager.OnBannerItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 首页推荐
 *
 * @author Cisco
 */
public class RecommendFragment extends BaseFragment<RecommendPresenterImpl> implements OnClickListener, RecommendView {
	protected static final String TAG = "RecommendFragment";
	private LoopViewPagerLayout mLoopViewPagerLayout;
	private List<String> imageUrls;
	private XRefreshView xRefreshView;
	private ImageView adImg_1;
	public GridView grid;
	private LinearLayout notificationBar;
	private MarqueeText notificationContent;
	private ImageView notificationClose;
	private DragView dragView;
	private LinearLayout proFrame;
	private TextView proTitle;
	private LinearLayout proTags;
	private TextView proIntstRate;
	private TextView proMoney;
	private TextView proDay;
	private RecommendBean recommendBean;
	private List<RecommendAct> recommendAct;
	private List<RecommendAlt> recommendAlt;
	private List<RecommendRel> recommendRel;
	private List<RecommendPro> recommendPro;
	private RecommendGongHuo recommendGongHuo;
	private RecommendRelAdapter recommendRelAdapter;
	private Intent intent;
	private ClipViewPager mViewPager;
	private GalleryImageAdapter galleryImageAdapter;
	private List<RelWindowBean> relWindowBean;
	private FrameLayout mRmFragmentFl;
	private PopupWindow popGallery;
	private String showPopGalleryDate;
	private long mOldTime;

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_recommend;
	}

	@Override
	public void onDestroyView() {
		mLoopViewPagerLayout.stopLoop();
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

	public void initView() {
		mLoopViewPagerLayout = (LoopViewPagerLayout) contentView.findViewById(R.id.rm_loopviewpager);
		mRmFragmentFl = (FrameLayout) contentView.findViewById(R.id.rm_fragment_fl);
		xRefreshView = (XRefreshView) contentView.findViewById(R.id.rm_xrefreshview);
		adImg_1 = (ImageView) contentView.findViewById(R.id.rm_pic1);
		grid = (GridView) contentView.findViewById(R.id.rm_grid);
		notificationBar = (LinearLayout) contentView.findViewById(R.id.rm_notification_bar);
		notificationContent = (MarqueeText) contentView.findViewById(R.id.rm_notification_content);
		notificationClose = (ImageView) contentView.findViewById(R.id.rm_notification_close);
		dragView = (DragView) contentView.findViewById(R.id.rm_dragView);
		proFrame = (LinearLayout) contentView.findViewById(R.id.rm_pro_frame);
		proTitle = (TextView) contentView.findViewById(R.id.rm_pro_title);
		proTags = (LinearLayout) contentView.findViewById(R.id.rm_pro_tags);
		proIntstRate = (TextView) contentView.findViewById(R.id.rm_pro_intstRate);
		proMoney = (TextView) contentView.findViewById(R.id.rm_pro_limit_money);
		proDay = (TextView) contentView.findViewById(R.id.rm_pro_limit_day);
	}

	@Override
	protected void initEvent() {
		//轮播图事件
		mLoopViewPagerLayout.setLoop_ms(4000);// 轮播的速度(毫秒)
		mLoopViewPagerLayout.setLoop_duration(1200);// 滑动的速率(毫秒)
		mLoopViewPagerLayout.setIndicatorLocation(IndicatorLocation.Center);// 指示器位置-中Center
		mLoopViewPagerLayout.initializeData(getActivity());// 初始化数据
		mLoopViewPagerLayout.setOnBannerItemClickListener(new OnBannerItemClickListener() {
			@Override
			public void onBannerClick(int index, View view) {
				intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", recommendAct.get(index).getUrl());
				intent.putExtra("type", recommendAct.get(index).getTitle());
				startActivity(intent);
			}
		});
		proFrame.setOnClickListener(this);
		adImg_1.setOnClickListener(this);
		notificationClose.setOnClickListener(this);

		xRefreshView.setAutoLoadMore(false);
		xRefreshView.setPullLoadEnable(true);
		xRefreshView.mFooterView.setFooterStateReadyText("上拉查看更多理财产品");
		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh() {
				mOldTime = System.currentTimeMillis();
				mPresenter.requestRecommend();
				mPresenter.requestActivityAndNotice();
			}

			@Override
			public void onLoadMore(boolean isSilence) {
				xRefreshView.stopLoadMore(true);
				MainActivity mainActivity = (MainActivity) getActivity();
				mainActivity.viewPager.setCurrentItem(1, false);
			}
		});

		recommendRelAdapter = new RecommendRelAdapter(getContext());
		grid.setAdapter(recommendRelAdapter);
		grid.setOnItemClickListener(OnItemClickListenerRel);
	}

	@Override
	protected void bindData() {
		if (recommendAct.size() > 0) {
			imageUrls = new ArrayList<>();
			for (int i = 0; i < recommendAct.size(); i++) {
				imageUrls.add(recommendAct.get(i).getLogo_url());
			}
			mLoopViewPagerLayout.setLoopData(imageUrls);// 设置数据
			mLoopViewPagerLayout.startLoop();
		}

		if (recommendBean != null && recommendBean.getRel_intro() != null) {
			ImageLoader.getInstance().displayImage(recommendBean.getRel_intro().getLogo_url(), adImg_1, new ImageLoaderUtils().recommendOptions(8));
		}

		//推荐产品实体类
		RecommendPro recommendPro = recommendBean.getRel_pro().get(0);
		proTitle.setText(recommendPro.getTitle());
		// 添加标签
		String tags = recommendPro.getTags();
		if (tags != null && !tags.equals("")) {
			int tagsCont = 0;
			proTags.setVisibility(View.VISIBLE);
			proTags.removeAllViews();
			if (!tags.contains(",")) {
				addTags(tags);
			}else {
				for (int i = 0; i < tags.length() - 1; i++) {
					if (tags.charAt(i) == ',') {
						addTags(tags.substring(tagsCont, i));

						tagsCont = i + 1;
					}
				}
				addTags(tags.substring(tagsCont, tags.length()));
			}
		}
		else {
			proTags.setVisibility(View.GONE);
		}
		setRateText(recommendPro.getRate() + "%");

		proMoney.setText(recommendPro.getUseStr() + " " + recommendPro.getUseFund());
		proDay.setText(recommendPro.getLimitStr() + " " + recommendPro.getLimit());


	}

	@Override
	protected void loadData() {
		if (contentView != null && (recommendBean == null || recommendGongHuo == null)) {
			recommendAct = new ArrayList<>();
			recommendAlt = new ArrayList<>();
			recommendRel = new ArrayList<>();
			recommendPro = new ArrayList<>();
			relWindowBean = new ArrayList<>();
			initView();
			mPresenter.requestRecommend();//请求网络
			mPresenter.requestActivityAndNotice();//活动公告
			showLoad(false);

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			showPopGalleryDate = sdf.format(new Date());
			if (!showPopGalleryDate.equals(UserSharedPreferenceUtils.getPopShowDate())) {
				mPresenter.requestRelWindow();
			}
		}
	}


	private void addTags(String tag) {
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = DisplayUtil.dip2px(getActivity(), 8);
		TextView textView = new TextView(getActivity());
		textView.setBackgroundResource(R.drawable.shape_tag_bg_1);
		textView.setTextColor(Color.parseColor("#4076d0"));
		textView.setTextSize(11);
		textView.setGravity(Gravity.CENTER);
		textView.setText(tag);
		textView.setLayoutParams(lp);
		proTags.addView(textView);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rm_pic1:// ad图片1
				if (recommendBean != null) {
					setUmEvent(UmEvent.gsy_homePage_firstPropagandaAct);
					if (recommendBean == null) {
						return;
					}
					intent = new Intent(getActivity(), WebActivityShow.class);
					intent.putExtra("url", recommendBean.getRel_intro().getUrl());
					intent.putExtra("type", recommendBean.getRel_intro().getTitle());
					startActivity(intent);
					return;
				}

			case R.id.rm_pro_frame:// 推荐的产品
				if (recommendPro.size() > 0) {
					setUmEvent(UmEvent.gsy_homePage_threePro);
					intent = new Intent(getActivity(), WebActivityShow.class);
					intent.putExtra("url", recommendPro.get(0).getUrl());
					intent.putExtra("pro_type", recommendPro.get(0).getType());
					startActivity(intent);
					return;
				}

			case R.id.rm_notification_close:// 隐藏通知
				if (recommendGongHuo != null) {
					notificationBar.setVisibility(View.GONE);
					mPresenter.requestActivityAndNoticeClose(recommendGongHuo.getGonggao().get(0).getId());
//					requestGongHuoClose(recommendGongHuo.getGonggao().get(0).getId());
					return;
				}
		}

	}

	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();
	}

	/**
	 * DESC :  设置百分比文本,使用富文本实现 <br/>
	 *
	 * @param rawRateText 百分比,带百分比符号.
	 */
	private void setRateText(String rawRateText) {
		int endPosition = rawRateText.indexOf('%');
		SpannableString finalRateText = new SpannableString(rawRateText);
		// 设置数字部分
		Typeface typeFace =
				Typeface.createFromAsset(getActivity().getAssets(), "fonts/PingFangBold.ttf");
		finalRateText.setSpan(typeFace, 0, endPosition, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);// 设置字体样式
		finalRateText.setSpan(new AbsoluteSizeSpan(DisplayUtil.sp2px(getContext(), 48)), 0, endPosition,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);// 设置分数字体大小
		finalRateText.setSpan(new ForegroundColorSpan(Color.parseColor("#df504f")), 0, endPosition,
				Spanned.SPAN_INCLUSIVE_EXCLUSIVE);// 颜色

		// 设置符号部分
		finalRateText.setSpan(new SubscriptSpan() {

		                      }, endPosition, endPosition + 1,
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);// 设置下标
		finalRateText.setSpan(new ReplacementSpan() {

			                      private int mWidth;
			                      private final Paint mPaint = new Paint();

			                      {
				                      mPaint.setStyle(Paint.Style.STROKE);
				                      mPaint.setAntiAlias(true);
				                      mPaint.setColor(Color.parseColor("#f15353"));
				                      mPaint.setTextSize(DisplayUtil.sp2px(getContext(), 18));
			                      }

			                      @Override
			                      public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
				                      mWidth = (int) mPaint.measureText(text, start, end);
				                      return mWidth;
			                      }

			                      @Override
			                      public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y,
			                                       int bottom, Paint paint) {
				                      canvas.drawText(text, start, end, x, y, mPaint);
			                      }
		                      }, endPosition, endPosition + 1,
				Spanned.SPAN_INCLUSIVE_INCLUSIVE);
		proIntstRate.setText(finalRateText);
	}

	// 推荐信息
	OnItemClickListener OnItemClickListenerRel = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (recommendRel.get(position).getDesc().equals("1") && GsbApplication.getUserId() == null) {
				intent = new Intent(getActivity(), ActivityLogin.class);
				startActivity(intent);
				return;
			}
			switch (position) {
				case 0:
					setUmEvent(UmEvent.gsy_homePage_firstFctAct);
					break;
				case 1:
					setUmEvent(UmEvent.gsy_homePage_secondFctAct);
					break;
				case 2:
					setUmEvent(UmEvent.gsy_homePage_threeFctAct);
					break;
				case 3:
					setUmEvent(UmEvent.gsy_homePage_fourFctAct);
					break;
			}
			intent = new Intent(getActivity(), WebActivityShow.class);
			intent.putExtra("url", recommendRel.get(position).getUrl());
			intent.putExtra("type", recommendRel.get(position).getTitle());
			startActivity(intent);
		}
	};

	// 显示活动公告或红包
	@SuppressLint("UseValueOf")
	private void initActivityAndNotice() {
		// 显示活动红包图标
		long time = System.currentTimeMillis();
		if (new Long(recommendGongHuo.getHuodong().getEnd_time()) > (time / 1000)) {
			dragView.setVisibility(View.VISIBLE);
			dragView.setSaveKey("rm_x", "rm_y");
			dragView.setOnDrayViewClick(new OnDrayViewClick() {
				@Override
				public void onClick() {
					setUmEvent(UmEvent.Gsy_homePage_Red);
					intent = new Intent(getActivity(), WebActivityShow.class);
					intent.putExtra("url", recommendGongHuo.getHuodong().getUrl());
					startActivity(intent);
				}
			});
		}
		else {
			dragView.setVisibility(View.GONE);
		}
		// 显示公告栏
		if (recommendGongHuo.getGonggao().size() == 0) {
			notificationBar.setVisibility(View.GONE);
			return;
		}
		if (new Long(recommendGongHuo.getGonggao().get(0).getEnd_time()) > (time / 1000)) {
			notificationBar.setVisibility(View.VISIBLE);
			notificationContent.setText(recommendGongHuo.getGonggao().get(0).getDesc());
			notificationContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					setUmEvent(UmEvent.Gsy_homePage_Notice);
					intent = new Intent(getActivity(), WebActivityShow.class);
					intent.putExtra("url", recommendGongHuo.getGonggao().get(0).getUrl());
					startActivity(intent);
				}

			});
		}
		else {
			notificationBar.setVisibility(View.GONE);
		}
	}

	// 显示浮窗
	@SuppressLint("NewApi")
	private void showPopGallery() {
		if (popGallery == null) {
			View popView = View.inflate(getActivity(), R.layout.pop_viewpage_gallery, null);
			popGallery = new PopupWindow();
			popGallery.setContentView(popView);
			popGallery.setWidth(LayoutParams.MATCH_PARENT);
			popGallery.setHeight(LayoutParams.MATCH_PARENT);
			ColorDrawable dw = new ColorDrawable();
			dw.setColor(Color.parseColor("#b2000000"));
			popGallery.setBackgroundDrawable(dw);
			popGallery.setOutsideTouchable(false);
			mViewPager = (ClipViewPager) popView.findViewById(R.id.viewpager_gallery);
			ImageView close = (ImageView) popView.findViewById(R.id.viewpager_gallery_close);
			popView.findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return mViewPager.dispatchTouchEvent(event);
				}
			});
			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (popGallery.isShowing()) {
						popGallery.dismiss();
						UserSharedPreferenceUtils.setPopShowDate(showPopGalleryDate);
					}
				}
			});
			mViewPager.setPageTransformer(true, new ScalePageTransformer());
			galleryImageAdapter = new GalleryImageAdapter(getActivity());
			mViewPager.setAdapter(galleryImageAdapter);
			initGalleryData();
		}
		if (!popGallery.isShowing()) {
			popGallery.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.CENTER, 0, 0);
		}
	}

	private void initGalleryData() {
		if (relWindowBean.size() == 0) {
			return;
		}
		List<ImageView> imageViewList = new ArrayList<ImageView>();
		for (int i = 0; i < relWindowBean.size(); i++) {
			final ImageView imageView = new ImageView(getActivity());
			imageView.setScaleType(ScaleType.FIT_XY);
			imageView.setAdjustViewBounds(true);
			ImageLoader.getInstance().displayImage(relWindowBean.get(i).getImg(), imageView, new ImageLoaderUtils().galleryOptions());
			imageView.setTag(i);
			imageView.setOnClickListener(onGalleryImgClick);
			imageViewList.add(imageView);
		}
		// 设置OffscreenPageLimit
		mViewPager.setOffscreenPageLimit(imageViewList.size());
		galleryImageAdapter.addAll(imageViewList);
	}

	OnClickListener onGalleryImgClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			if (relWindowBean.get(tag).getUrl() == null || relWindowBean.get(tag).getUrl().equals("")) {
				return;
			}
			setUmEvent(UmEvent.Gsy_homePage_JumpAct);
			intent = new Intent(getActivity(), WebActivityShow.class);
			intent.putExtra("url", relWindowBean.get(tag).getUrl());
			intent.putExtra("title", relWindowBean.get(tag).getTitle());
			startActivity(intent);
		}
	};

	private View mErrorView;
	private boolean isErrorViewShow = false;

	/**
	 * 显示网络错误的view
	 */
	public void showErrorPage() {
		if (recommendBean != null) {
			return;
		}
		if (mErrorView == null) {
			ViewStub errorViewStub = (ViewStub) contentView.findViewById(R.id.load_error_view);
			mErrorView = errorViewStub.inflate();
		}
//        mErrorView = contentView.findViewById(R.id.load_error_view);
		TextView refresh = (TextView) mErrorView.findViewById(R.id.error_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isErrorViewShow = true;
				mPresenter.requestRecommend();
				showLoad(false);
			}
		});
		mErrorView.setVisibility(View.VISIBLE);
		mRmFragmentFl.setVisibility(View.GONE);

		View telView = contentView.findViewById(R.id.error_tel);
		telView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				telephoneDialog();
			}
		});
	}

	@Override
	public RecommendPresenterImpl getPresenter() {
		return new RecommendPresenterImpl();
	}

	/**
	 * 隐藏网络错误的view
	 */
	private void hideErrorPage() {
		if (isErrorViewShow == false) {
			return;
		}
		mErrorView.setVisibility(View.GONE);
		mRmFragmentFl.setVisibility(View.VISIBLE);
	}

	@Override
	public void afterGetRecommendData(RecommendBean recommendBean) {

		this.recommendBean = recommendBean;
		recommendAct.clear();
		recommendAlt.clear();
		recommendRel.clear();
		recommendPro.clear();
		recommendAct.addAll(recommendBean.getRel_act());
		recommendAlt.addAll(recommendBean.getRel_alt());
		recommendPro.addAll(recommendBean.getRel_pro());
		recommendRel.addAll(recommendBean.getRel_rel());
//		recommendRelAdapter.notifyDataSetChanged();
		recommendRelAdapter.updateData(recommendBean.getRel_rel());
		mLoopViewPagerLayout.stopLoop();
		bindData();
		hideErrorPage();
		LogUtils.e("RecommendFragment", "afterGetRecommendData:"+(System.currentTimeMillis()-mOldTime));
	}

	@Override
	public void afterGetActivityAndNoticeData(RecommendGongHuo data) {
		this.recommendGongHuo = data;
		initActivityAndNotice();
	}

	/**
	 * 显示悬浮
	 * @param data
	 */
	@Override
	public void afterGetRelWindowData(List<RelWindowBean> data) {
		relWindowBean.clear();
		relWindowBean.addAll(data);
		showPopGallery();
	}
}
