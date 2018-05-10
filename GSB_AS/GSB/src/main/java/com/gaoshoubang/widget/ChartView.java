package com.gaoshoubang.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.gaoshoubang.util.DisplayUtil;

import java.util.List;

/**
 * 环形图表
 *
 * @author Cisco
 */
public class ChartView extends View {

	private Context context;
	private int maxValue = 100;

	private String mTxtHint1;
	private String mTxtHint2;

	private boolean isAnimated = true;// 是否启用动画
	private boolean isShadowShowing = false;// 是否描边
	private String shadowBackgroundColor = "#f3f3f3";// 描边的颜色
	private String chartBackgroundColor = "#ffffff";// 内部背景颜色
	private float animationSpeed = 10.0f;// 动画速度
	private float globalCurrentAngle = 0.0f;// 开始角度

	private int circleLineStrokeWidth = 18;// 宽度
	private int shadowLineStrokeWidth = 1;// 描线的宽度
	private int countLineStrokeWidth;// 总宽度
	private int spacingWidth = 2;// 间隔空隙

	private int width;
	private int height;

	private Paint mPaint;
	private List<ChartItem> chartItemsList;

	public ChartView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ChartView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		this.context = context;

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.FILL);

		circleLineStrokeWidth = DisplayUtil.dip2px(context, circleLineStrokeWidth);
		shadowLineStrokeWidth = DisplayUtil.dip2px(context, shadowLineStrokeWidth);
		countLineStrokeWidth = circleLineStrokeWidth + shadowLineStrokeWidth * 2;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		if (width != height) {
			return;
		}

		if (this.isAnimated) {
			animatedDraw(canvas);
		}
		else {
			regularDraw(canvas);
		}

		initTxtHint1(canvas);
		initTxtHint2(canvas);
		canvas.restore();
	}

	private void initTxtHint1(Canvas canvas) {
		if (!TextUtils.isEmpty(mTxtHint1)) {
			int textHeight;
			int textWidth;
			textHeight = DisplayUtil.sp2px(context, 13);
			mPaint.setTextSize(textHeight);
			mPaint.setColor(0xffababab);
			textWidth = (int) mPaint.measureText(mTxtHint1, 0, mTxtHint1.length());
			canvas.drawText(mTxtHint1, width / 2 - textWidth / 2, (height + textHeight) / 2 - 2 * textHeight, mPaint);
		}
	}

	private void initTxtHint2(Canvas canvas) {
		if (!TextUtils.isEmpty(mTxtHint1)) {
			int textHeight;
			int textWidth;
			new DisplayUtil();
			textHeight = DisplayUtil.sp2px(context, 25);
			mPaint.setTextSize(textHeight);
			mPaint.setColor(0xff333333);
			textWidth = (int) mPaint.measureText(mTxtHint2, 0, mTxtHint2.length());
			canvas.drawText(mTxtHint2, width / 2 - textWidth / 2, height / 2 + textHeight * 2 / 3, mPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	@Override
	protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
		this.width = width;
		this.height = height;
	}

	private int measureWidth(int widthMeasureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(widthMeasureSpec);
		int specSize = MeasureSpec.getSize(widthMeasureSpec);
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenWidth = size.x;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		else {
			result = screenWidth;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		this.width = result;
		return result;
	}

	private int measureHeight(int heightMeasureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(heightMeasureSpec);
		int specSize = MeasureSpec.getSize(heightMeasureSpec);
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = windowManager.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenHeight = size.y;
		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		}
		else {
			result = screenHeight;
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		this.height = result;
		return result;
	}

	private void regularDraw(Canvas canvas) {
		Paint insideShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		insideShadowPaint.setColor(Color.parseColor(shadowBackgroundColor));
		Paint insideChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		insideChartPaint.setColor(Color.parseColor(chartBackgroundColor));

		RectF rect = new RectF();
		rect.set(shadowLineStrokeWidth / 2, shadowLineStrokeWidth / 2, width - shadowLineStrokeWidth / 2, height - shadowLineStrokeWidth / 2);
		drawMainCircle(canvas, insideShadowPaint, insideChartPaint, rect);
		canvas.rotate(-90f, rect.centerX(), rect.centerY());

		if (this.chartItemsList != null && this.maxValue > 0) {
			drawItems(canvas);
			canvas.rotate(90f, rect.centerX(), rect.centerY());
		}
	}

	private void animatedDraw(Canvas canvas) {
		Paint insideShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		insideShadowPaint.setColor(Color.parseColor(shadowBackgroundColor));
		insideShadowPaint.setDither(true);

		Paint insideChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		insideChartPaint.setColor(Color.parseColor(chartBackgroundColor));
		insideChartPaint.setDither(true);

		RectF rect = new RectF();
		rect.set(countLineStrokeWidth, countLineStrokeWidth, width - countLineStrokeWidth, height - countLineStrokeWidth);

		drawMainCircle(canvas, insideShadowPaint, insideChartPaint, rect);
		canvas.rotate(-90f, rect.centerX(), rect.centerY());

		if (chartItemsList != null && maxValue > 0) {
			drawItems(canvas);

			Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.parseColor(chartBackgroundColor));
			paint.setDither(true);
			RectF oval = new RectF();
			oval.set(shadowLineStrokeWidth, shadowLineStrokeWidth, width - shadowLineStrokeWidth, height - shadowLineStrokeWidth);
			Path path = new Path();
			path.moveTo(oval.centerX(), oval.centerY());

			globalCurrentAngle += animationSpeed; // <-- animation speed
			path.addArc(oval, globalCurrentAngle, 360.0f - globalCurrentAngle);

			path.lineTo(oval.centerX(), oval.centerY());
			canvas.drawPath(path, paint);

			canvas.rotate(90f, rect.centerX(), rect.centerY());

			if (globalCurrentAngle >= 360) {
				globalCurrentAngle = 0.0f;
				canvas.rotate(-90f, rect.centerX(), rect.centerY());
				drawItems(canvas);
				canvas.rotate(90f, rect.centerX(), rect.centerY());
				return;
			}
			invalidate();
		}
	}

	private void drawMainCircle(Canvas canvas, Paint insideShadowPaint, Paint insideChartPaint, RectF mainRectangle) {
		if (isShadowShowing) {
			canvas.drawCircle(width / 2, height / 2, width / 2, insideShadowPaint);
		}
		canvas.drawArc(mainRectangle, 0f, 360f, false, insideChartPaint);
	}

	private void drawItems(Canvas canvas) {
		float startAngle = 0f;
		Paint currentPaint = new Paint();
		currentPaint.setAntiAlias(true);
		currentPaint.setDither(true);
		currentPaint.setStrokeWidth(circleLineStrokeWidth - shadowLineStrokeWidth);
		currentPaint.setStyle(Style.STROKE);

		Paint paint2 = new Paint();
		paint2.setAntiAlias(true);
		paint2.setDither(true);
		paint2.setStrokeWidth(circleLineStrokeWidth);
		paint2.setStyle(Style.STROKE);
		paint2.setColor(Color.WHITE);
		RectF rectF2 = new RectF();
		rectF2.set(countLineStrokeWidth / 2 - 1, countLineStrokeWidth / 2 - 1, width - countLineStrokeWidth / 2 + 1, height - countLineStrokeWidth / 2 + 1);

		RectF mainRectangle = new RectF();
		mainRectangle.set(countLineStrokeWidth / 2, countLineStrokeWidth / 2, width - countLineStrokeWidth / 2, height - countLineStrokeWidth / 2);
		for (int i = 0; i < chartItemsList.size(); ++i) {
			ChartItem currentItem = chartItemsList.get(i);
			int color = currentItem.color;
			int value = currentItem.value;

			float currentPercentValue = getPercent(value, maxValue);
			float currentAngle = currentPercentValue * 360;
			if (chartItemsList.size() != 1 && i == chartItemsList.size() - 1) {
				currentAngle = 360 - startAngle - spacingWidth;
			}

			currentPaint.setColor(color);
			canvas.drawArc(mainRectangle, startAngle, currentAngle, false, currentPaint);
			// if (chartItemsList.size() != 1) {
			// canvas.drawArc(rectF2, startAngle + currentAngle, spacingWidth,
			// false, paint2);
			// }
			startAngle += currentAngle;
		}

	}

	public void setAnimationState(boolean state) {
		this.isAnimated = state;
		invalidate();
	}

	public void setAnimationSpeed(float animationSpeed) {
		this.animationSpeed = animationSpeed;
		invalidate();
	}

	public void setShadowShowingState(boolean state) {
		this.isShadowShowing = state;
		invalidate();
	}

	public boolean getAnimationState() {
		return this.isAnimated;
	}

	public boolean getShadowShowingState() {
		return this.isShadowShowing;
	}

	public void setChartItemsList(List<ChartItem> itemsList) {
		this.chartItemsList = itemsList;
		invalidate();
	}

	public void setShadowBackgroundColor(String color) {
		this.shadowBackgroundColor = color;
		invalidate();
	}

	public void setChartBackgroundColor(String color) {
		this.chartBackgroundColor = color;
		invalidate();
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		invalidate();
	}

	private float getPercent(int value, int maxValue) {
		float result = (float) value / maxValue;
		return result;
	}

	public String getmTxtHint1() {
		return mTxtHint1;
	}

	public void setmTxtHint1(String mTxtHint1) {
		this.mTxtHint1 = mTxtHint1;
	}

	public String getmTxtHint2() {
		return mTxtHint2;
	}

	public void setmTxtHint2(String mTxtHint2) {
		this.mTxtHint2 = mTxtHint2;
	}

	public int getCircleLineStrokeWidth() {
		return circleLineStrokeWidth;
	}

	public void setCircleLineStrokeWidth(int circleLineStrokeWidth) {
		this.circleLineStrokeWidth = DisplayUtil.dip2px(context, circleLineStrokeWidth);
	}

	public int getShadowLineStrokeWidth() {
		return shadowLineStrokeWidth;
	}

	public void setShadowLineStrokeWidth(int shadowLineStrokeWidth) {
		this.shadowLineStrokeWidth = DisplayUtil.dip2px(context, shadowLineStrokeWidth);
	}

	public int getSpacingWidth() {
		return spacingWidth;
	}

	public void setSpacingWidth(int spacingWidth) {
		this.spacingWidth = spacingWidth;
	}

	public static class ChartItem {
		public int color;
		public int value;

		public ChartItem(int value, int color) {
			this.color = color;
			this.value = value;
		}
	}
}
