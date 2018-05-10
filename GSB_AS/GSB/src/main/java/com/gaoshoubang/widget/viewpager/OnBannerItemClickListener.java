package com.gaoshoubang.widget.viewpager;

import android.view.View;

/**
 * Banner Click
 */
public interface OnBannerItemClickListener {
	/**
	 * banner click
	 *
	 * @param index subscript
	 * @param banner bean
	 */
	void onBannerClick(int index, View view);
}
