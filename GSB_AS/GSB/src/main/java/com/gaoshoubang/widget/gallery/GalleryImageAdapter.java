package com.gaoshoubang.widget.gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class GalleryImageAdapter extends RecyclingPagerAdapter {

	private final List<ImageView> mList;
	private final Context mContext;

	public GalleryImageAdapter(Context context) {
		mList = new ArrayList<ImageView>();
		mContext = context;

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	public void addAll(List<ImageView> list) {
		mList.addAll(list);
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		ImageView imageView = null;
		if (convertView == null) {
			imageView = new ImageView(mContext);
		}
		else {
			imageView = (ImageView) convertView;
		}
		imageView = mList.get(position);
		imageView.setTag(position);
		return imageView;
	}
}
