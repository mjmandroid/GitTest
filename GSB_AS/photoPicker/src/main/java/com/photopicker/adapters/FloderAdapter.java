package com.photopicker.adapters;

import java.util.List;

import com.photopicke.R;
import com.photopicker.beans.PhotoFolder;
import com.photopicker.utils.ImageLoader;
import com.photopicker.utils.OtherUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Class: FloderAdapter
 * @Description: 图片目录适配器
 */
public class FloderAdapter extends BaseAdapter {

	List<PhotoFolder> mDatas;
	Context mContext;
	int mWidth;

	public FloderAdapter(Context context, List<PhotoFolder> mDatas) {
		this.mDatas = mDatas;
		this.mContext = context;
		mWidth = OtherUtils.dip2px(context, 80);
	}

	@Override
	public int getCount() {
		if (mDatas != null) {
			return mDatas.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_floder_layout, null);
			holder.photoIV = (ImageView) convertView.findViewById(R.id.imageview_floder_img);
			holder.floderNameTV = (TextView) convertView.findViewById(R.id.textview_floder_name);
			holder.photoNumTV = (TextView) convertView.findViewById(R.id.textview_photo_num);
			holder.selectIV = (ImageView) convertView.findViewById(R.id.imageview_floder_select);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.selectIV.setVisibility(View.GONE);
		holder.photoIV.setImageResource(R.drawable.ic_photo_loading);
		PhotoFolder folder = mDatas.get(position);

		if (folder.isSelected()) {

			holder.selectIV.setVisibility(View.VISIBLE);
		}
		holder.floderNameTV.setText(folder.getName());
		holder.photoNumTV.setText(folder.getPhotoList().size() + "张");
		//处理越界异常
		if (folder.getPhotoList() != null && folder.getPhotoList().size() > 0) {
			ImageLoader.getInstance().display(folder.getPhotoList().get(0).getPath(), holder.photoIV, mWidth, mWidth);
		}
		return convertView;
	}

	private class ViewHolder {
		private ImageView photoIV;
		private TextView floderNameTV;
		private TextView photoNumTV;
		private ImageView selectIV;
	}

}
