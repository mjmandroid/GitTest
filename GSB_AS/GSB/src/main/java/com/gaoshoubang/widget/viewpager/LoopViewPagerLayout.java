package com.gaoshoubang.widget.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.util.DisplayUtil;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;
import java.util.List;

public class LoopViewPagerLayout extends RelativeLayout {
	private FrameLayout indicatorFrameLayout;
	private DecoratorViewPager loopViewPager;
	private LinearLayout indicatorLayout;
	private LinearLayout animIndicatorLayout;
	private OnBannerItemClickListener onBannerItemClickListener = null;
	private LoopPagerAdapterWrapper loopPagerAdapterWrapper;
	private int totalDistance;// Little red dot all the distance to move
	private int size = DisplayUtil.dip2px(getContext(), 8);// The size of the
	// set
	// point;
	private List<String> list;// banner data
	private TextView animIndicator;// Little red dot on the move
	private TextView[] indicators;// Initializes the white dots
	private static final int MESSAGE_LOOP = 5;
	private int loop_ms = 4000;// loop speed(ms)
	private int loop_style = -1; // loop style(enum values[-1:empty,1:depth
	// 2:zoom])
	private IndicatorLocation indicatorLocation = IndicatorLocation.Center; // Indicator
	// Location(enum
	// values[1:left,0:depth
	// 2:right])
	private int loop_duration = 2000;// loop rate(ms)

	private Context context;
	private Handler handler = new Handler() {
		@Override
		public void dispatchMessage(Message msg) {
			super.dispatchMessage(msg);
			if (msg.what == MESSAGE_LOOP) {
				if (loopViewPager.getCurrentItem() < Short.MAX_VALUE - 1) {
					loopViewPager.setCurrentItem(loopViewPager.getCurrentItem() + 1, true);
					sendEmptyMessageDelayed(MESSAGE_LOOP, getLoop_ms());
				}
			}
		}
	};

	public LoopViewPagerLayout(Context context) {
		super(context);
		this.context = context;
		L.e("Initialize LoopViewPagerLayout ---> context");
	}

	public LoopViewPagerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		L.e("Initialize LoopViewPagerLayout ---> context, attrs");
	}

	public LoopViewPagerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		L.e("Initialize LoopViewPagerLayout ---> context, attrs, defStyleAttr");
	}

	/**
	 * onBannerItemClickListener
	 *
	 * @param onBannerItemClickListener onBannerItemClickListener
	 */
	public void setOnBannerItemClickListener(OnBannerItemClickListener onBannerItemClickListener) {
		this.onBannerItemClickListener = onBannerItemClickListener;
	}

	/**
	 * Be sure to initialize the View
	 */
	private void initializeView() {
		L.e("LoopViewPager ---> initializeView");
		float density = getResources().getDisplayMetrics().density;

		loopViewPager = new DecoratorViewPager(getContext());
		LayoutParams loop_params = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
		addView(loopViewPager, loop_params);

		// TODO FrameLayout
		indicatorFrameLayout = new FrameLayout(getContext());
		LayoutParams f_params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ((int) (20 * density)));
		f_params.addRule(RelativeLayout.CENTER_HORIZONTAL);// android:layout_centerHorizontal="true"
		f_params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);// android:layout_alignParentBottom="true"

		switch (indicatorLocation) {
			case Left:
				f_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);// android:layout_alignParentLeft="true"
				break;
			case Right:
				f_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// android:layout_alignParentRight="true"
				break;
			default:
				break;
		}

		f_params.setMargins(((int) (10 * density)), 0, ((int) (10 * density)), 0);
		addView(indicatorFrameLayout, f_params);

		// TODO indicatorLayout
		indicatorLayout = new LinearLayout(getContext());
		FrameLayout.LayoutParams ind_params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
		indicatorLayout.setGravity(Gravity.CENTER);
		indicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
		indicatorFrameLayout.addView(indicatorLayout, ind_params);

		// TODO animIndicatorLayout
		animIndicatorLayout = new LinearLayout(getContext());
		FrameLayout.LayoutParams ind_params2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
		animIndicatorLayout.setGravity(Gravity.CENTER | Gravity.START);
		animIndicatorLayout.setOrientation(LinearLayout.HORIZONTAL);
		indicatorFrameLayout.addView(animIndicatorLayout, ind_params2);
	}

	/**
	 * Be sure to initialize the Data
	 *
	 * @param context context
	 */
	public void initializeData(Context context) {
		initializeView();

		L.e("LoopViewPager ---> initializeData");
		// TODO To prevent the flower screen
		if (loop_duration > loop_ms) {
			loop_duration = loop_ms;
		}

		// TODO loop_duration
		try {
			Field mField = ViewPager.class.getDeclaredField("mScroller");
			mField.setAccessible(true);
			LoopScroller mScroller = new LoopScroller(context);
			// LoopScroller mScroller = new LoopScroller(context, new
			// AccelerateInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// AnticipateInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// PathInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// BounceInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// OvershootInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// AnticipateOvershootInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// LinearInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// AccelerateInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// DecelerateInterpolator());
			// LoopScroller mScroller = new LoopScroller(context, new
			// CycleInterpolator(20));
			// 可以用setDuration的方式调整速率
			mScroller.setmDuration(loop_duration);
			mField.set(loopViewPager, mScroller);
		} catch (Exception e) {
			e.printStackTrace();
		}

		loopViewPager.setPageTransformer(true, new AccordionTransformer());

		// TODO Listener
		loopViewPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						//L.e("ACTION_DOWN");
						stopLoop();
						break;
					case MotionEvent.ACTION_MOVE:
						//L.e("ACTION_MOVE");
						stopLoop();
						break;
					case MotionEvent.ACTION_UP:
						//L.e("ACTION_UP");
						startLoop();
						break;
					default:
						break;
				}
				return false;
			}
		});
	}

	/**
	 * initialize the Data
	 *
//	 * @param bannerInfos BannerInfo
	 */
	public void setLoopData(List<String> list) {
		L.e("LoopViewPager ---> setLoopData");
		if (list != null && list.size() > 0) {
			this.list = list;
		}
		else {
			throw new NullPointerException("LoopViewPagerLayout bannerInfos is null or bannerInfos.size() isEmpty");
		}

		// TODO Initialize multiple times, clear images and little red dot
		if (indicatorLayout.getChildCount() > 0) {
			indicatorLayout.removeAllViews();
			removeView(animIndicator);
		}
		loopPagerAdapterWrapper = new LoopPagerAdapterWrapper(context, list, onBannerItemClickListener);
		loopViewPager.setAdapter(loopPagerAdapterWrapper);
		loopViewPager.setNestedpParent(indicatorLayout);

		if (list.size() > 1) {
			indicatorLayout.getViewTreeObserver().addOnPreDrawListener(new IndicatorPreDrawListener());
			loopViewPager.addOnPageChangeListener(new ViewPageChangeListener());

			int index;
			if (50 % list.size() == 0) {
				index = 50;
			}
			else {
				index = 50 - 50 % list.size();
			}
			loopViewPager.setCurrentItem(index);

			InitIndicator();
			InitLittleRed();
		}

		loopPagerAdapterWrapper.notifyDataSetChanged();
	}

	private void InitIndicator() {
		indicatorLayout.removeAllViews();
		indicators = new TextView[list.size()];
		for (int i = 0; i < indicators.length; i++) {
			indicators[i] = new TextView(getContext());
			indicators[i].setGravity(Gravity.CENTER);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
			if (i != indicators.length - 1) {
				params.setMargins(0, 0, size, 0);
			}
			else {
				params.setMargins(0, 0, 0, 0);
			}
			indicators[i].setLayoutParams(params);
			indicators[i].setBackgroundResource(R.drawable.indicator_normal_background);// 设置默认的背景颜色
			indicatorLayout.addView(indicators[i]);
		}

	}

	private void InitLittleRed() {
		animIndicatorLayout.removeAllViews();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
		animIndicator = new TextView(getContext());
		animIndicator.setGravity(Gravity.CENTER);
		animIndicator.setBackgroundResource(R.drawable.indicator_selected_background);// 设置选中的背景颜色
		animIndicatorLayout.addView(animIndicator, params);
	}

	public int getLoop_ms() {
		if (loop_ms < 1500) {
			loop_ms = 1500;
		}
		return loop_ms;
	}

	/**
	 * loop speed
	 *
	 * @param loop_ms (ms)
	 */
	public void setLoop_ms(int loop_ms) {
		this.loop_ms = loop_ms;
	}

	/**
	 * loop rate
	 *
	 * @param loop_duration (ms)
	 */
	public void setLoop_duration(int loop_duration) {
		this.loop_duration = loop_duration;
	}

	/**
	 * indicator_location
	 *
	 * @param indicatorLocation (enum values[1:left,0:depth,2:right])
	 */
	public void setIndicatorLocation(IndicatorLocation indicatorLocation) {
		this.indicatorLocation = indicatorLocation;
	}

	/**
	 * startLoop
	 */
	public void startLoop() {
		handler.removeCallbacksAndMessages(MESSAGE_LOOP);
		handler.sendEmptyMessageDelayed(MESSAGE_LOOP, getLoop_ms());
		//L.e("LoopViewPager ---> startLoop");
	}

	/**
	 * stopLoop Be sure to in onDestory,To prevent a memory leak
	 */
	public void stopLoop() {
		handler.removeMessages(MESSAGE_LOOP);
		//L.e("LoopViewPager ---> stopLoop");
	}

	/**
	 * LoopViewPager
	 *
	 * @return ViewPager
	 */
	public ViewPager getLoopViewPager() {
		return loopViewPager;
	}

	/**
	 * OnPageChangeListener
	 */
	private class ViewPageChangeListener implements ViewPager.OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			if (loopPagerAdapterWrapper.getCount() > 0) {
				float length = ((position % list.size()) + positionOffset) / (list.size() - 1);
				// TODO To prevent the last picture the little red dot slip out.
				if (length >= 1) {
					length = 1;
				}
				float path = length * totalDistance;
				// L.e("path " + path + " = length * " + length + "
				// totalDistance " + totalDistance);
				ViewCompat.setTranslationX(animIndicator, path);
			}
		}

		@Override
		public void onPageSelected(int position) {

		}

		@Override
		public void onPageScrollStateChanged(int state) {

		}
	}

	/**
	 * OnPreDrawListener
	 */
	private class IndicatorPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
		@SuppressLint("NewApi")
		@Override
		public boolean onPreDraw() {
			Rect firstRect = new Rect();
			indicatorLayout.getChildAt(0).getGlobalVisibleRect(firstRect);

			L.e("firstRect = " + firstRect.toShortString());
			Rect lastRect = new Rect();
			indicatorLayout.getChildAt(indicators.length - 1).getGlobalVisibleRect(lastRect);

			L.e("lastRect = " + lastRect.toShortString());

			totalDistance = lastRect.left - firstRect.left;
			L.e("totalDistance = " + totalDistance);

			indicatorLayout.getViewTreeObserver().removeOnPreDrawListener(this);

			return false;
		}
	}
}