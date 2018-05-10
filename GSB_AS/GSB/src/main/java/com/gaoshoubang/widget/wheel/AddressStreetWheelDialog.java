package com.gaoshoubang.widget.wheel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.widget.wheel.adapters.ArrayWheelAdapter;

/**
 * 街道显示wheelDialog
 */
public class AddressStreetWheelDialog extends Dialog implements OnClickListener, OnWheelChangedListener {
	private Context context;

	private View mView;
	private TextView sancel;
	private TextView save;
	private WheelView mViewStreet;

	/**
	 * 街道
	 */
	protected String[] mSteetDatas;

	public AddressStreetWheelDialog(Context context, String[] mSteetDatas) {
		super(context, R.style.CustomProgressDialog);
		setContentView(R.layout.dialog_address_street_wheel);
		this.context = context;
		this.mSteetDatas = mSteetDatas;

		LayoutParams lay = getWindow().getAttributes();
		setParams(lay);
		setCanceledOnTouchOutside(true);
		setCancelable(true);

		init();
		setUpData();
	}

	private void setParams(LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels - rect.top;
		lay.width = dm.widthPixels;
	}

	private void init() {
		mView = findViewById(R.id.dialog_view);
		sancel = (TextView) findViewById(R.id.wheel_cancel);
		save = (TextView) findViewById(R.id.wheel_save);
		mViewStreet = (WheelView) findViewById(R.id.id_street);

		mView.setOnClickListener(this);
		sancel.setOnClickListener(this);

		mViewStreet.addChangingListener(this);
	}

	public void setSaveOnClickListener(View.OnClickListener onClickListener) {
		save.setOnClickListener(onClickListener);
	}

	public String getSteetName() {
		return mSteetDatas[mViewStreet.getCurrentItem()];
	}

	public int getCurrentItem() {
		return mViewStreet.getCurrentItem();
	}

	/**
	 * 设置滑动到的位置
	 *
	 * @param id
	 */
	public void setStreetCurrentItem(String name) {
		if (mSteetDatas.length == 0) {
			return;
		}
		for (int i = 0; i < mSteetDatas.length; i++) {
			if (mSteetDatas[i].equals(name)) {
				mViewStreet.setCurrentItem(i);
				return;
			}
		}
	}

	private void setUpData() {
		mViewStreet.setViewAdapter(new ArrayWheelAdapter<String>(context, mSteetDatas));
		// 设置可见条目数量
		mViewStreet.setVisibleItems(7);
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.dialog_view:
				dismiss();
				break;

			case R.id.wheel_cancel:
				dismiss();
				break;
		}
	}
}
