package com.gaoshoubang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.gaoshoubang.R;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.LogUtils;

/**
 * Created by lzx on 2017/6/29.
 */

public class TabTipsView extends View {

	private Paint mTextPaint;
	private int mTextColor = Color.WHITE;
	private int mBgColor = Color.parseColor("#0090BF");
	private int mIndexMargin;
	private Paint mBgPaint;
	private RectF mRectF;
	private int mCount = 10;


	private int mIndex = 1;
	private float mMIndexTextSize;
	private float mCountTextSize;
	private float mStartX;
	private float mStartY;
	private float mIndexTextHeight;


	public TabTipsView(Context context) {
		this(context, null);
	}

	public TabTipsView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.TabTipsView);
		mMIndexTextSize = ta.getDimension(R.styleable.TabTipsView_index_text_size, 60);
		mCountTextSize = ta.getDimension(R.styleable.TabTipsView_count_text_size, 48);
		ta.recycle();
		init();
	}

	private void init() {
		mIndexMargin = DisplayUtil.dip2px(getContext(), 5f);
		//
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setColor(mTextColor);

		//背景画笔
		mBgPaint = new Paint();
		mBgPaint.setAntiAlias(true);
		mBgPaint.setColor(mBgColor);

		//背景矩形
		mRectF = new RectF();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		mRectF.left = -getMeasuredWidth();
		mRectF.top = -getMeasuredHeight();
		mRectF.right = getMeasuredWidth();
		mRectF.bottom = getMeasuredHeight();
		//测量文字宽高
		mTextPaint.setTextSize(mMIndexTextSize);
		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		mIndexTextHeight = fontMetrics.descent - fontMetrics.ascent;
		float width = mTextPaint.measureText("" + mIndex);
		mStartX = width + mIndexMargin;
		mStartY = mIndexTextHeight + mIndexMargin;

	}


	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//画背景
		canvas.drawArc(mRectF, 0f, 90f, true, mBgPaint);
		mTextPaint.setTextSize(mMIndexTextSize);

		Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
		mIndexTextHeight = fontMetrics.descent - fontMetrics.ascent;
		float width = mTextPaint.measureText("" + mIndex);
		mStartX = width + mIndexMargin;
		mStartY = mIndexTextHeight + mIndexMargin;

		//画索引
		canvas.drawText(mIndex + "", mIndexMargin, mIndexTextHeight, mTextPaint);
		//画总数
		mTextPaint.setTextSize(mCountTextSize);
		canvas.drawText("/", mStartX, mStartY, mTextPaint);
		canvas.drawText(mCount + "", mStartX + 15, mStartY + 15, mTextPaint);
	}

	public void setIndex(int index) {
		mIndex = index;
		invalidate();
	}

	/*
	* 下一个索引
	* */
	public void nextIndex() {
		if (mIndex < mCount) {
			mIndex++;
			invalidate();
		}
	}

	/*
		* 上一个索引
		* */
	public void preIndex() {
		if (mIndex > 1) {
			mIndex--;
			invalidate();
		}
	}

	public void setCount(int count) {
		mCount = count;
		invalidate();
	}

	public int getIndex() {
		return mIndex;
	}

}
