package com.andview.refreshview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.ScaleAnimation;

import com.xrefreshview.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.width;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import static com.andview.refreshview.BallPulseIndicator.SCALE;

/**
 * Created by Administrator on 2017/5/24.
 */

public class LoadingView extends View implements Animatable {
	private ArrayList<ValueAnimator> mAnimators;
	private float mScale;
	private ScaleAnimation mScaleAnimation;
	private Paint mPaint;
	private float mLeftCx;
	private float mRightCx;
	private float mRadius;
	private float mCy;
	private float mMargin;
	private boolean mIsLeft;
	private int dis;
	private int mStartColor = 0xFF605A;
	private int mEndColor = 0x619AF3;

	public static final float SCALE = 1.0f;
	private HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new HashMap<>();
	private float[] scaleFloats = new float[]{SCALE,
			SCALE, SCALE};


	public LoadingView(Context context) {
		this(context, null);
	}

	public LoadingView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);

		mCy = mRadius;
		mLeftCx = mRadius;
		mRightCx = mLeftCx * 2 + mMargin + mRadius;
		mPaint = new Paint();
		mPaint.setAntiAlias(true);

		mAnimators = onCreateAnimators();


	}

	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		super.onWindowFocusChanged(hasWindowFocus);
		if (!hasWindowFocus) {
			endAnimation();
		}

	}

	private void endAnimation() {

	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if (visibility != VISIBLE) {
			endAnimation();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		float circleSpacing = 4;
		float radius = (Math.min(getWidth(), getHeight()) - circleSpacing * 2) / 6;
		float x = getWidth() / 2 - (radius * 2 + circleSpacing);
		float y = getHeight() / 2;
		for (int i = 0; i < 3; i++) {
			canvas.save();
			float translateX = x + (radius * 2) * i + circleSpacing * i;
			canvas.translate(translateX, y);
			canvas.scale(scaleFloats[i], scaleFloats[i]);
			canvas.drawCircle(0, 0, radius, mPaint);
			canvas.restore();
		}
	}


	@Override
	public void start() {
		startAnimators();
		invalidate();
	}

	@Override
	public void stop() {
		stopAnimators();
	}

	@Override
	public boolean isRunning() {
		return false;
	}

	public ArrayList<ValueAnimator> onCreateAnimators() {
		ArrayList<ValueAnimator> animators = new ArrayList<>();
		int[] delays = new int[]{120, 240, 360};
		for (int i = 0; i < 3; i++) {
			final int index = i;

			ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.3f, 1);

			scaleAnim.setDuration(750);
			scaleAnim.setRepeatCount(-1);
			scaleAnim.setStartDelay(delays[i]);

			addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					scaleFloats[index] = (float) animation.getAnimatedValue();
					invalidate();
				}
			});
			animators.add(scaleAnim);
		}
		return animators;
	}

	public void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener) {
		mUpdateListeners.put(animator, updateListener);
	}

	private void startAnimators() {
		for (int i = 0; i < mAnimators.size(); i++) {
			ValueAnimator animator = mAnimators.get(i);

			//when the animator restart , add the updateListener again because they
			// was removed by animator stop .
			ValueAnimator.AnimatorUpdateListener updateListener = mUpdateListeners.get(animator);
			if (updateListener != null) {
				animator.addUpdateListener(updateListener);
			}

			animator.start();
		}
	}

	private void stopAnimators() {
		if (mAnimators != null) {
			for (ValueAnimator animator : mAnimators) {
				if (animator != null && animator.isStarted()) {
					animator.removeAllUpdateListeners();
					animator.end();
				}
			}
		}
	}
}
