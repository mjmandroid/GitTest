package com.gaoshoubang.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gaoshoubang.R;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.LogUtils;

/**
 * Created by lzx on 2017/6/27.
 */

public class CountDownView extends View {

	private static final String TAG = CountDownView.class.getSimpleName();
	private static final int BACKGROUND_COLOR = 0x50555555;
	private static final float BORDER_WIDTH = 10f;
	private static final int BORDER_COLOR = 0xAAFF0000;
	private static final String TEXT = "跳过";
	private static final float TEXT_SIZE = 36f;
	private static final int TEXT_COLOR = 0xFFFFFFFF;

	private int backgroundColor;
	private float borderWidth;
	private int borderColor;
	private String text;
	private int textColor;
	private float textSize;
	private int mRadius = 0;

	private Paint circlePaint;
	private TextPaint textPaint;
	private Paint borderPaint;

	private float progress = 0f;
//	private StaticLayout staticLayout;


	private CountDownListener listener;
	/*总时间*/
	private long countDownTime = 3000;
	/*剩余时间*/
	private long currentTime = countDownTime;
	private int mTextWidth;
	private ValueAnimator mAnimator;

	public CountDownView(Context context) {
		this(context, null);
	}

	public CountDownView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mRadius = DisplayUtil.dip2px(context, 40);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CountDownView);
		backgroundColor = ta.getColor(R.styleable.CountDownView_background_color, BACKGROUND_COLOR);
		borderWidth = ta.getDimension(R.styleable.CountDownView_circle_border_width, BORDER_WIDTH);
		borderColor = ta.getColor(R.styleable.CountDownView_circle_border_color, BORDER_COLOR);
		text = ta.getString(R.styleable.CountDownView_text);
		if (text == null) {
			text = TEXT;
		}
		textSize = ta.getDimension(R.styleable.CountDownView_text_size, TEXT_SIZE);
		textColor = ta.getColor(R.styleable.CountDownView_text_color, TEXT_COLOR);
		ta.recycle();
		init();
	}

	private void init() {
		circlePaint = new Paint();
		circlePaint.setAntiAlias(true);
		circlePaint.setDither(true);
		circlePaint.setColor(backgroundColor);
		circlePaint.setStyle(Paint.Style.FILL);

		textPaint = new TextPaint();
		textPaint.setAntiAlias(true);
		textPaint.setDither(true);
		textPaint.setColor(textColor);
		textPaint.setTextSize(textSize);
		textPaint.setTextAlign(Paint.Align.CENTER);

		borderPaint = new Paint();
		borderPaint.setAntiAlias(true);
		borderPaint.setDither(true);
		borderPaint.setColor(borderColor);
		borderPaint.setStrokeWidth(borderWidth);
		borderPaint.setStyle(Paint.Style.STROKE);
//		setOnClickListener(new );
		mTextWidth = (int) textPaint.measureText(text.substring(0, (text.length() + 1) / 2));

//		StaticLayout build = new StaticLayout.Builder().setText().build();
//		staticLayout = new StaticLayout(text+countDownTime/1000+"s", textPaint, mTextWidth, Layout.Alignment.ALIGN_NORMAL, 1F, 0, false);


	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (widthMode != MeasureSpec.EXACTLY) {
//			width = staticLayout.getWidth();
			width = mRadius;
		}
		if (heightMode != MeasureSpec.EXACTLY) {
//			height = staticLayout.getHeight();
			height = mRadius;
		}
		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mAnimator.cancel();
		return super.onTouchEvent(event);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width = getMeasuredWidth();
		int height = getMeasuredHeight();
		int min = Math.min(width, height);
		//画底盘
		canvas.drawCircle(width / 2, height / 2, min / 2, circlePaint);
		//画边框
		RectF rectF;
		if (width > height) {
			rectF = new RectF(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2, width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2);
		}
		else {
			rectF = new RectF(borderWidth / 2, height / 2 - min / 2 + borderWidth / 2, width - borderWidth / 2, height / 2 - borderWidth / 2 + min / 2);
		}
		canvas.drawArc(rectF, -90, progress, false, borderPaint);
		//画居中的文字
		canvas.drawText(text, width / 2, height / 2, textPaint);
		canvas.drawText(currentTime / 1000 + "s", width / 2, height / 5 * 4, textPaint);

	}

	public void start() {
		mAnimator = ValueAnimator.ofFloat(0f, 360f);
		mAnimator.setDuration(countDownTime);
		mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				currentTime = countDownTime - animation.getCurrentPlayTime() + 1000;
				progress = ((float) animation.getAnimatedValue());
//				LogUtils.e("CountDownView", "onAnimationUpdate:"+progress);
				invalidate();
			}
		});
		mAnimator.addListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				listener.onFinish();
			}

			@Override
			public void onAnimationCancel(Animator animation) {
				listener.onFinish();
			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		mAnimator.start();
	}

	public void setListener(CountDownListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mAnimator.cancel();
	}

	public interface CountDownListener {
		void onFinish();
	}
}
