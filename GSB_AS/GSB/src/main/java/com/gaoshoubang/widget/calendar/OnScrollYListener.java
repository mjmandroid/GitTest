package com.gaoshoubang.widget.calendar;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * ListView的滑动监听实现类，监听listview垂直方向滑动的距离
 */
public abstract class OnScrollYListener implements OnScrollListener {

	private HashMap<Integer, Integer> mItemHeights = new HashMap<Integer, Integer>();
	private ListView mListView;

	private HashMap<Integer, TextView> mItemTextViews = new HashMap<Integer, TextView>();
	private MonthView monthView;

	public OnScrollYListener(ListView listView) {
		this.mListView = listView;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (mListView == null || mListView.getAdapter() == null) {
			return;
		}
		// listview当前在屏幕可见的第一个item，也就是firstVisibleItem索引对应的item
		View firstVisibleChild = mListView.getChildAt(0);
		if (firstVisibleChild == null) {
			return;
		}
		if (!mItemHeights.containsKey(firstVisibleItem)) {
			// 将每个item以 key:index, value:height 存入map
			mItemHeights.put(firstVisibleItem, firstVisibleChild.getHeight());
		}
		if (!mItemTextViews.containsKey(firstVisibleItem)) {
			monthView = (MonthView) view.getChildAt(0);
			if (monthView.getChildAt(0) != null) {
				TextView textView = (TextView) monthView.getChildAt(0);
				textView.setTag(firstVisibleChild);
				if (mItemTextViews.get(firstVisibleChild) == null) {
					mItemTextViews.put(firstVisibleItem, textView);
				}
			}
		}
		int scrollY = computeScrollY(firstVisibleItem, firstVisibleChild);
		onScrollY(scrollY, firstVisibleItem, mItemHeights, mItemTextViews);
	}

	private int computeScrollY(int firstVisibleItem, View firstVisibleChild) {
		int scrollY = 0;
		int sum = 0, count = 0;
		for (int i = 0; i <= firstVisibleItem; i++) {
			Integer h = mItemHeights.get(i);
			// 当快速滑动listview时，firstVisibleItem不是连续增长，ex 0,1,3,7....
			if (h == null) {
				continue;
			}
			sum += h;
			count++;
		}
		if (count == 0) {
			return 0;
		}
		// 已经记录的item的高度的平均值
		int avarage = sum / count;
		// 已记录的item高度总和+没有记录的item的高度总和
		scrollY = sum + avarage * (firstVisibleItem + 1 - count);
		// 第一个item可能这时候还有一半在屏幕内，这时候要减去这个item在屏幕内部分的高度
		scrollY -= firstVisibleChild.getBottom();
		return scrollY;
	}

	/**
	 * 垂直方向滚动距离
	 *
	 * @param scrolledY
	 */
	protected abstract void onScrollY(int scrolledY, int itemIndex, Map<Integer, Integer> mItemHeights, HashMap<Integer, TextView> mItemTextViews);

}
