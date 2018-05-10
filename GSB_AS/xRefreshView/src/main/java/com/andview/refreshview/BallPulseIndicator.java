package com.andview.refreshview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class BallPulseIndicator extends Drawable implements Animatable {
	private HashMap<ValueAnimator, ValueAnimator.AnimatorUpdateListener> mUpdateListeners = new HashMap<>();
	public static final float SCALE = 1.0f;
	private static final Rect ZERO_BOUNDS_RECT = new Rect();
	protected Rect drawBounds = ZERO_BOUNDS_RECT;
	private boolean mHasAnimators;
	//scale x ,y
	private float[] scaleFloats = new float[]{SCALE,
			SCALE,
			SCALE};

	private Paint mPaint;
	private ArrayList<ValueAnimator> mAnimators;


	public BallPulseIndicator() {
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setAntiAlias(true);
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
					invalidateSelf();
				}
			});
			animators.add(scaleAnim);
		}
		return animators;
	}

	public void addUpdateListener(ValueAnimator animator, ValueAnimator.AnimatorUpdateListener updateListener) {
		mUpdateListeners.put(animator, updateListener);
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}

	@Override
	public boolean isRunning() {
		return false;
	}

	@Override
	public void draw(@NonNull Canvas canvas) {

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
	protected void onBoundsChange(Rect bounds) {
		super.onBoundsChange(bounds);
		setDrawBounds(bounds);
	}

	public void setDrawBounds(Rect drawBounds) {
		setDrawBounds(drawBounds.left, drawBounds.top, drawBounds.right, drawBounds.bottom);
	}

	public void setDrawBounds(int left, int top, int right, int bottom) {
		this.drawBounds = new Rect(left, top, right, bottom);
	}

	public int getWidth() {
		return drawBounds.width();
	}

	public int getHeight() {
		return drawBounds.height();
	}

	@Override
	public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

	}

	@Override
	public void setColorFilter(@Nullable ColorFilter colorFilter) {

	}

	@Override
	public int getOpacity() {
		return PixelFormat.OPAQUE;
	}

	private void ensureAnimators() {
		if (!mHasAnimators) {
			mAnimators = onCreateAnimators();
			mHasAnimators = true;
		}
	}
}
