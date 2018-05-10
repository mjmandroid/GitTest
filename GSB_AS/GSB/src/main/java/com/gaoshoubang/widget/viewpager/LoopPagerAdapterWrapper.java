package com.gaoshoubang.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class LoopPagerAdapterWrapper extends PagerAdapter {

	private List<String> mList;
	private OnBannerItemClickListener onBannerItemClickListener;
	private Context context;

	public LoopPagerAdapterWrapper(Context context, List<String> list, OnBannerItemClickListener onBannerItemClickListener) {
		this.context = context;
		this.mList = list;
		this.onBannerItemClickListener = onBannerItemClickListener;
	}

	@Override
	public int getCount() {
		if (mList.size() == 1) {
			return mList.size();
		}
		return Short.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = getDefaultItemView(position % mList.size());
		container.addView(view);
		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

	private View getDefaultItemView(final int currentIndex) {
		ImageView view = null;
		if (view == null) {
			view = new ImageView(context);
			view.setScaleType(ScaleType.FIT_XY);
			ImageLoader.getInstance().displayImage(mList.get(currentIndex), view, new ImageLoaderUtils().adOption());
		}
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (onBannerItemClickListener != null) {
					onBannerItemClickListener.onBannerClick(currentIndex, view);
				}
			}
		});
		return view;
	}
}