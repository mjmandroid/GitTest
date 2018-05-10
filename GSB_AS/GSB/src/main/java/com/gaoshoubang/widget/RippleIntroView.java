package com.gaoshoubang.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.util.LogUtils;

import java.io.InputStream;

/*
*
* 蒙层引导页, 需要注意,显示的时机.
* */
public class RippleIntroView extends View implements Runnable {
	private int mMaxRadius = 70;
	private int mInterval = 20;
	private int count = 0;
	private Bitmap mCacheBitmap;
	private Paint mRipplePaint;
	private Paint mCirclePaint;
	private View mHighLightView;
	private int mHighLightLeft;
	private int mHighLightTop;
	private boolean mIsRipple = false;
	private boolean mIsAbove = false;
	private Paint mPaint;
	private Bitmap mHintBitmap;
	private int mHintViewLeft;
	private int mHintViewTop;


	public RippleIntroView(Context context) {
		this(context, null);
	}

	public RippleIntroView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RippleIntroView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mRipplePaint = new Paint();
		mRipplePaint.setAntiAlias(true);
		mRipplePaint.setStyle(Paint.Style.STROKE);
		mRipplePaint.setColor(Color.WHITE);
		mRipplePaint.setStrokeWidth(2.f);

		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setStyle(Paint.Style.FILL);
		mCirclePaint.setColor(Color.WHITE);
		setFitsSystemWindows(true);
		setClickable(true);
		setBackgroundColor(Color.parseColor("#AA000000"));


		mPaint = new Paint();
		mPaint.setColor(Color.BLACK);
		mPaint.setAlpha(255);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		//高亮区域截图
		if (mHighLightView != null) {

//			Bitmap bitmap = mHighLightView.getDrawingCache();
			mHighLightView.setDrawingCacheEnabled(false);
			mHighLightView.setDrawingCacheEnabled(true);
			//注意背景色问题
//			mHighLightView.setDrawingCacheBackgroundColor(Color.WHITE);
			Bitmap drawingCache = mHighLightView.getDrawingCache();
			canvas.drawBitmap(drawingCache, mHighLightLeft, mHighLightTop, mPaint);
			mHighLightView.destroyDrawingCache();

		}
		//提示区域
		if (mHintBitmap != null) {
			canvas.drawBitmap(mHintBitmap, mHintViewLeft, mHintViewTop, mPaint);
		}
//		水波纹
		if (mIsRipple) {
			drawRipple(canvas);
		}
	}


	private void drawRipple(Canvas canvas) {
		if (mHighLightView == null) return;
		final int pw = mHighLightView.getWidth();
		final int ph = mHighLightView.getHeight();

		if (pw == 0 || ph == 0) return;

		final float px = mHighLightLeft + pw / 2;
		final float py = mHighLightTop + ph / 2;
		final int rw = pw / 2;
		int save = canvas.save();
		for (int step = count; step <= mMaxRadius; step += mInterval) {
			mRipplePaint.setAlpha(255 * (mMaxRadius - step) / mMaxRadius);
			canvas.drawCircle(px, py, (float) (rw + step), mRipplePaint);
		}
		canvas.restoreToCount(save);
		postDelayed(this, 80);
	}

	@Override
	public void run() {
		removeCallbacks(this);
		count += 2;
		count %= mInterval;
		invalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mCacheBitmap != null) {
			mCacheBitmap.recycle();
			mCacheBitmap = null;
		}
	}


	/*
	* @param
	* */
	public void setHighLightArea(View highLightView, int hintResId, boolean isCenter, boolean isAbove, boolean isRipple) {
		mIsRipple = isRipple;
		//获得高亮区域的坐标
		int[] location = new int[2];
		highLightView.getLocationOnScreen(location);
		mHighLightView = highLightView;
		//在viewpager当中,宽度需要校正
		mHighLightLeft = location[0] % CommonUtils.getScreenDispaly(getContext())[0];
		mHighLightTop = location[1];
		LogUtils.e("RippleIntroView", "setHighLightArea:" + mHighLightTop);
		//提示区域坐标
		if (isCenter) {
			mHintViewLeft = mHighLightLeft + mHighLightView.getWidth() / 2;
		}
		else {
			mHintViewLeft = mHighLightLeft;
		}

		try {
			InputStream is = getResources().openRawResource(hintResId);
			mHintBitmap = BitmapFactory.decodeStream(is);
			if (isAbove) {///
				mHintViewTop = mHighLightTop - mHintBitmap.getHeight();
			}
			else {
				mHintViewTop = mHighLightTop + mHighLightView.getHeight();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		invalidate();
	}
}
