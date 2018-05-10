package com.gaoshoubang.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.util.DisplayUtil;

/**
 * 适合大部分Activity页面的titleBar
 * <p>
 * 使用右侧按钮时，需要先设置OnNextButtonListener的实例进来
 *
 * @author Cisco
 */
public class CommonTitleBar extends RelativeLayout {
	protected static final String TAG = "CommonTitleBar";
	private Context context;
	private String title;
	private int textSize;
	private int textColor;

	private boolean isShowRightBtn;
	private String rightBtnText;
	private int rightBtnTextColor;
	private int rightBtnTextSize;

	private TextView leftTextView;
	private TextView titleTextView;
	private TextView rightTextView;

	private boolean isShowLeftText;
	private String leftText;
	private int leftTextColor;
	private int leftTextSize;

	private OnNextButtonListener onNextButtonListener;
	private OnBackButtonListener onBackButtonListener;

	public void setOnBackButtonListener(OnBackButtonListener onBackButtonListener) {
		this.onBackButtonListener = onBackButtonListener;
	}

	public void setOnNextButtonListener(OnNextButtonListener onNextButtonListener) {
		this.onNextButtonListener = onNextButtonListener;
	}

	public CommonTitleBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public void setTitle(String title) {
		titleTextView.setText(title);
	}

	private boolean isShowUnderSplitLine;

	public CommonTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
		title = a.getString(R.styleable.TitleBar_android_text);
		textSize = a.getDimensionPixelSize(R.styleable.TitleBar_android_textSize, 16);
		textSize = DisplayUtil.px2sp(context, textSize);
		textColor = a.getColor(R.styleable.TitleBar_android_textColor, Color.parseColor("#FFE493"));
		isShowUnderSplitLine = a.getBoolean(R.styleable.TitleBar_isShowUnderSplitLine, true);

		if (isShowLeftText = a.getBoolean(R.styleable.TitleBar_isShowLeftText, false)) {
			leftText = a.getString(R.styleable.TitleBar_leftText);
			leftTextColor = a.getColor(R.styleable.TitleBar_leftTextColor, Color.parseColor("#da4415"));
			leftTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_leftTextSize, 14);
			//leftTextSize = DisplayUtil.px2sp(context, leftTextSize);
		}

		if (isShowRightBtn = a.getBoolean(R.styleable.TitleBar_isShowRightBtn, false)) {
			rightBtnText = a.getString(R.styleable.TitleBar_rightBtnText);
			rightBtnTextColor = a.getColor(R.styleable.TitleBar_rightBtnTextColor, Color.parseColor("#da4415"));
			rightBtnTextSize = a.getDimensionPixelSize(R.styleable.TitleBar_rightBtnTextSize, 14);
			rightBtnTextSize = DisplayUtil.px2sp(context, rightBtnTextSize);
		}
		a.recycle();
		init();
	}

	public CommonTitleBar(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private ImageView backBtnImageView;

	@SuppressLint("NewApi")
	private void init() {
		// Drawable drawableTop = getBackground();
		// if (drawableTop == null) {
		// drawableTop = new ColorDrawable(Color.parseColor("#f1f1f1"));
		// }
		// Drawable drawableBottom =
		// getResources().getDrawable(R.drawable.titlebar_bottom);
		// LayerDrawable ld = new LayerDrawable(new Drawable[] { drawableTop,
		// drawableBottom });
		// setBackground(ld);
		// 设置返回按钮与标题栏的距离,还是在布局文件中控制
		// this.setPadding(DisplayUtil.dip2px(context, 15), 0, 0, 0);

		// 标题文字
		titleTextView = new TextView(context);
		titleTextView.setText(title);
		titleTextView.setTextColor(textColor);
		titleTextView.setTextSize(textSize);
		// 字体加粗
		// TextPaint paint = titleTextView.getPaint();
		// paint.setFakeBoldText(true);
		// 标题文字居中Param
		LayoutParams params1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params1.addRule(RelativeLayout.CENTER_IN_PARENT);
		this.addView(titleTextView, params1);

		// 添加返回按钮
		backBtnImageView = new ImageView(context);
		backBtnImageView.setBackgroundResource(R.drawable.selector_title_back);
		backBtnImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onBackButtonListener != null) {
					onBackButtonListener.onBack();
				}
				else {
					((Activity) context).finish();
				}

			}
		});
		LayoutParams params2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// params2.leftMargin = DisplayUtil.dip2px(context, 10);
		params2.addRule(RelativeLayout.CENTER_VERTICAL);
		this.addView(backBtnImageView, params2);

		// 显示左边的字
		if (isShowLeftText) {
			leftTextView = new TextView(context);
			leftTextView.setText(leftText);
			leftTextView.setTextColor(leftTextColor);
			leftTextView.setTextSize(leftTextSize);
			leftTextView.setSingleLine(true);
			leftTextView.setMaxEms(5);
			leftTextView.setEllipsize(TruncateAt.END);

			leftTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onBackButtonListener != null) {
						onBackButtonListener.onBack();
					}
					else {
						((Activity) context).finish();
					}

				}
			});

			LayoutParams params3 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params3.leftMargin = DisplayUtil.dip2px(context, 32);
			params3.addRule(RelativeLayout.CENTER_VERTICAL);
			this.addView(leftTextView, params3);
		}

		// 是否要显示右边的文字
		if (isShowRightBtn) {
			rightTextView = new TextView(context);
			rightTextView.setText(rightBtnText);
			rightTextView.setTextColor(rightBtnTextColor);
			rightTextView.setTextSize(rightBtnTextSize);
			rightTextView.setPadding(10, 6, 10, 6);
			// rightTextView.setBackgroundResource(R.drawable.selector_common_titlebar_right_btn);
			rightTextView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (onNextButtonListener != null) {
						onNextButtonListener.onNext();
					}
				}
			});

			LayoutParams params4 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params4.rightMargin = DisplayUtil.dip2px(context, 5);
			params4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params4.addRule(RelativeLayout.CENTER_VERTICAL);
			this.addView(rightTextView, params4);
		}
		// 底部线
		if (isShowUnderSplitLine) {
			TextView textView = new TextView(context);
			LayoutParams params5 = new LayoutParams(LayoutParams.MATCH_PARENT, 1);
			textView.setLayoutParams(params5);
			textView.setBackgroundColor(Color.parseColor("#DEDEDE"));
			params5.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			this.addView(textView);
		}
	}

	/**
	 * 设置是否显示返回按钮
	 *
	 * @param isVisible true 显示 false 不显示
	 */
	public void setBackBtnVisibleState(boolean isVisible) {
		backBtnImageView.setVisibility(isVisible ? View.VISIBLE : View.GONE);

	}

	/**
	 * 设置右侧按钮的文字
	 *
	 * @param rightBtnString
	 */
	public void setNextBtnText(String rightBtnString) {
		if (null != rightTextView) {
			rightTextView.setText(rightBtnString);
		}
	}

	public interface OnNextButtonListener {
		void onNext();
	}

	public interface OnBackButtonListener {
		void onBack();
	}

	public void setRightBtnVisible(int visible) {
		rightTextView.setVisibility(visible);
	}
}
