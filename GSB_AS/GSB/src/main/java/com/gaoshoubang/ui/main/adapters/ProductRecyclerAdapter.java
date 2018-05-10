package com.gaoshoubang.ui.main.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;
import com.gaoshoubang.C;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.util.MeasureUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.gaoshoubang.C.ProductConst.FLEXIBLE;
import static com.gaoshoubang.C.ProductConst.NEW;
import static com.gaoshoubang.C.ProductConst.STABILITY;
import static com.gaoshoubang.C.ProductConst.TYPE_FLEXIBLE;
import static com.gaoshoubang.C.ProductConst.TYPE_NEW;
import static com.gaoshoubang.C.ProductConst.TYPE_STABILITY;

/**
 * Created by Administrator on 2017/5/17.
 */

public class ProductRecyclerAdapter extends BaseRecyclerAdapter<ProductRecyclerAdapter.ViewHolder> {
	private HashMap<Integer, List<ProductBean>> mProducts;
	private List<String> mTitle;        //  保存标题的顺序,按顺序显示
	private Context mContext;
	private CustomListener mListener;
	private int mLastPosition;

	public ProductRecyclerAdapter(Context context) {
		mContext = context;
		mProducts = new HashMap<>();
		mTitle = new ArrayList<>();
	}

	public void updateData(HashMap<Integer, List<ProductBean>> map, List<String> title) {
		if (map == null || title == null) {
			return;
		}
		mProducts.clear();
		mTitle.clear();
		mProducts.putAll(map);
		mTitle.addAll(title);
		notifyDataSetChanged();
	}


	@Override
	public ViewHolder getViewHolder(View view) {
		return (ViewHolder) view.getTag();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
		View view = LayoutInflater.from(mContext)
				.inflate(R.layout.item_product_all_title, parent,
						false);
		return new ViewHolder(view);
	}


	@Override
	public void onBindViewHolder(final ViewHolder viewHolder, int position, boolean isItem) {
		viewHolder.mDashLine.setLayerType(View.LAYER_TYPE_SOFTWARE, null);  //虚线不能用硬件加速
		switch (getItemViewType(position)) {
			case TYPE_NEW:
				viewHolder.mOuterTitle.setText(NEW);
				Drawable iconNew = mContext.getResources().getDrawable(R.drawable.title_icon_new);
				iconNew.setBounds(0, 0, iconNew.getMinimumWidth(), iconNew.getMinimumHeight());
				viewHolder.mOuterTitle.setCompoundDrawables(iconNew, null, null, null);
				viewHolder.mOuterDesc.setText(mContext.getResources().getString(R.string.new_product_title_desc));
				viewHolder.mListView.setAdapter(new ProductAdapter(mProducts.get(TYPE_NEW), mContext));
				MeasureUtils.setListViewHeightBasedOnChildren(viewHolder.mListView);
				if (mListener != null) {
					mListener.setClickListener(viewHolder.mListView, mProducts.get(TYPE_NEW));
				}

				break;
			case TYPE_FLEXIBLE:
				viewHolder.mOuterTitle.setText(FLEXIBLE);
				Drawable iconFlexible = mContext.getResources().getDrawable(R.drawable.title_icon_flexible);
				iconFlexible.setBounds(0, 0, iconFlexible.getMinimumWidth(), iconFlexible.getMinimumHeight());
				viewHolder.mOuterTitle.setCompoundDrawables(iconFlexible, null, null, null);
				viewHolder.mOuterDesc.setText(mContext.getResources().getString(R.string.flexible_product_title_desc));
				viewHolder.mListView.setAdapter(new ProductAdapter(mProducts.get(TYPE_FLEXIBLE), mContext));
				MeasureUtils.setListViewHeightBasedOnChildren(viewHolder.mListView);
				if (mListener != null) {
					mListener.setClickListener(viewHolder.mListView, mProducts.get(TYPE_FLEXIBLE));
				}
				break;
			case TYPE_STABILITY:
				viewHolder.mOuterTitle.setText(STABILITY);
				Drawable iconStability = mContext.getResources().getDrawable(R.drawable.title_icon_stability);
				iconStability.setBounds(0, 0, iconStability.getMinimumWidth(), iconStability.getMinimumHeight());
				viewHolder.mOuterTitle.setCompoundDrawables(iconStability, null, null, null);
//				viewHolder.mOuterDesc.setVisibility(View.VISIBLE);
				viewHolder.mOuterDesc.setText(mContext.getResources().getString(R.string.stability_product_title_desc));
				final ProductAdapter productAdapter = new ProductAdapter(mProducts.get(TYPE_STABILITY), mContext);
				viewHolder.mListView.setAdapter(productAdapter);
				MeasureUtils.setListViewHeightBasedOnChildren(viewHolder.mListView);
				if (mListener != null) {
					mListener.setClickListener(viewHolder.mListView, mProducts.get(TYPE_STABILITY));
				}
				break;
		}


	}


	@Override
	public int getAdapterItemViewType(int position) {
		switch (mTitle.get(position)) {
			case C.ProductConst.NEW:
				return TYPE_NEW;
			case STABILITY:
				return TYPE_STABILITY;
			case FLEXIBLE:
				return TYPE_FLEXIBLE;
		}
		return super.getAdapterItemViewType(position);
	}


	@Override
	public int getItemCount() {
		return mTitle.size();
	}

	@Override
	public int getAdapterItemCount() {
		return getItemCount();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final View mDashLine;
		private TextView mOuterDesc;
		private ListView mListView;
		private TextView mOuterTitle;

		public ViewHolder(View itemView) {
			super(itemView);
			mOuterTitle = ((TextView) itemView.findViewById(R.id.item_product_outer_title));
			mListView = ((ListView) itemView.findViewById(R.id.item_listView));
			mOuterDesc = ((TextView) itemView.findViewById(R.id.item_product_outer_desc));
			mDashLine = itemView.findViewById(R.id.dash_line);
			itemView.setTag(this);
		}
	}

	public void setCustomListener(CustomListener listener) {
		mListener = listener;
	}

	public interface CustomListener {
		void setClickListener(ListView listView, List<ProductBean> productBeen);
	}
}
