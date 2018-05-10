package com.photopicker.widgets;

import com.photopicker.utils.OtherUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @Class: SquareImageView
 * @Description:
 * @Date: 2015/11/5
 */
public class SquareImageView extends ImageView {

	Context mContext;
	int mWidth;

	public SquareImageView(Context context) {
		this(context, null);
	}

	public SquareImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.mContext = context;
		int screenWidth = OtherUtils.getWidthInPx(mContext);
		mWidth = (screenWidth - OtherUtils.dip2px(mContext, 15)) / 4;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(mWidth, mWidth);
	}

}
