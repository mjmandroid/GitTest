package com.gaoshoubang.widget.wheel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.CityBean;
import com.gaoshoubang.net.JsonUtil;
import com.gaoshoubang.widget.wheel.adapters.ArrayWheelAdapter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddressWheelDialog extends Dialog implements OnClickListener, OnWheelChangedListener {
	private Context context;

	private View mView;
	private TextView sancel;
	private TextView save;
	private WheelView mViewProvince;
	private WheelView mViewCity;
	private WheelView mViewDistrict;

	private List<CityBean> cityBeanList;// 省
	private List<CityBean.CitySubs> citySubsList;// 市
	private List<CityBean.DistrictSubs> districtSubsList;// 区

	/**
	 * 所有省
	 */
	protected String[] mProvinceDatas;
	/**
	 * key - 省 value - 市
	 */
	protected Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
	/**
	 * key - 市 values - 区
	 */
	protected Map<String, String[]> mDistrictDatasMap = new HashMap<String, String[]>();
	/**
	 * 当前省的名称
	 */
	protected String mCurrentProviceName;
	/**
	 * 当前市的名称
	 */
	protected String mCurrentCityName;
	/**
	 * 当前区的名称
	 */
	protected String mCurrentDistrictName = "";

	public AddressWheelDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		setContentView(R.layout.dialog_address_wheel);
		this.context = context;

		LayoutParams lay = getWindow().getAttributes();
		setParams(lay);
		setCanceledOnTouchOutside(true);
		setCancelable(true);

		cityBeanList = new ArrayList<CityBean>();
		citySubsList = new ArrayList<CityBean.CitySubs>();
		districtSubsList = new ArrayList<CityBean.DistrictSubs>();

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
		mViewProvince = (WheelView) findViewById(R.id.id_province);
		mViewCity = (WheelView) findViewById(R.id.id_city);
		mViewDistrict = (WheelView) findViewById(R.id.id_district);

		mView.setOnClickListener(this);
		sancel.setOnClickListener(this);

		mViewProvince.addChangingListener(this);
		mViewCity.addChangingListener(this);
		mViewDistrict.addChangingListener(this);
	}

	public void setSaveOnClickListener(View.OnClickListener onClickListener) {
		save.setOnClickListener(onClickListener);
	}

	/**
	 * 选择地址
	 */
	public String getSelectedResult() {
		if (mCurrentCityName.contains(mCurrentProviceName)) {
			return mCurrentCityName + " " + mCurrentDistrictName;
		}
		return mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentDistrictName;
	}

	/**
	 * 获取街道id
	 */
	public String getDistrictId() {
		if (cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getSubs() == null) {
			return cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getId();
		}
		return cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getSubs().get(mViewDistrict.getCurrentItem()).getId();
	}

	/**
	 * 省市区id
	 */
	public String[] getAddressId() {
		String[] address = new String[3];
		address[0] = cityBeanList.get(mViewProvince.getCurrentItem()).getId();
		address[1] = cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getId();
		if (cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getSubs() != null) {
			address[2] = cityBeanList.get(mViewProvince.getCurrentItem()).getSubs().get(mViewCity.getCurrentItem()).getSubs().get(mViewDistrict.getCurrentItem()).getId();
		}
		else {
			address[2] = "";
		}
		return address;
	}

	/**
	 * 滑动到指定位置
	 */
	public void setProvinceCurrentItem(String provinceId, String cityId, String districtId) {
		int i = 0, j = 0, k = 0;
		for (i = 0; i < cityBeanList.size(); i++) {
			if (provinceId.equals(cityBeanList.get(i).getId())) {
				mViewProvince.setCurrentItem(i);
				break;
			}
		}
		if (!cityId.equals(0) && cityBeanList.get(i).getSubs() != null) {
			for (j = 0; j < cityBeanList.get(i).getSubs().size(); j++) {
				if (cityId.equals(cityBeanList.get(i).getSubs().get(j).getId())) {
					mViewCity.setCurrentItem(j);
					break;
				}
			}
		}
		if (!districtId.equals(0) && cityBeanList.get(i).getSubs() != null && cityBeanList.get(i).getSubs().get(j).getSubs() != null) {
			for (k = 0; k < cityBeanList.get(i).getSubs().get(j).getSubs().size(); k++) {
				if (districtId.equals(cityBeanList.get(i).getSubs().get(j).getSubs().get(k).getId())) {
					mViewDistrict.setCurrentItem(k);
					break;
				}
			}
		}
	}

	private void setUpData() {
		initData();
		mViewProvince.setViewAdapter(new ArrayWheelAdapter<String>(context, mProvinceDatas));
		// 设置可见条目数量
		mViewProvince.setVisibleItems(7);
		mViewCity.setVisibleItems(7);
		mViewDistrict.setVisibleItems(7);
		updateCities();
		updateAreas();
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas() {
		int pCurrent = mViewCity.getCurrentItem();
		mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
		String[] areas = mDistrictDatasMap.get(mCurrentCityName);

		mCurrentDistrictName = "";
		if (mDistrictDatasMap.get(mCurrentCityName) != null) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[0];
		}

		if (areas == null) {
			areas = new String[]{""};
		}
		mViewDistrict.setViewAdapter(new ArrayWheelAdapter<String>(context, areas));
		mViewDistrict.setCurrentItem(0);
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities() {
		int pCurrent = mViewProvince.getCurrentItem();
		mCurrentProviceName = mProvinceDatas[pCurrent];
		String[] cities = mCitisDatasMap.get(mCurrentProviceName);
		if (cities == null) {
			cities = new String[]{""};
		}
		mViewCity.setViewAdapter(new ArrayWheelAdapter<String>(context, cities));
		mViewCity.setCurrentItem(0);
		updateAreas();
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mViewProvince) {
			updateCities();
		}
		else if (wheel == mViewCity) {
			updateAreas();
		}
		else if (wheel == mViewDistrict) {
			mCurrentDistrictName = mDistrictDatasMap.get(mCurrentCityName)[newValue];
		}
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

	private void initData() {
		AssetManager asset = context.getAssets();
		try {
			InputStream input = asset.open("area.json");
			int lenght = input.available();
			byte[] buffer = new byte[lenght];
			input.read(buffer);
			String result = new String(buffer, "utf8");
			input.close();
			cityBeanList.clear();
			cityBeanList.addAll(JsonUtil.getObjectList(JsonUtil.getDataStr(result), CityBean.class));
			if (cityBeanList.size() == 0) {
				return;
			}
			// */ 初始化默认选中的省、市、区
			if (cityBeanList != null && !cityBeanList.isEmpty()) {
				mCurrentProviceName = cityBeanList.get(0).getName();
				mCurrentCityName = cityBeanList.get(0).getSubs().get(0).getName();
				mCurrentDistrictName = cityBeanList.get(0).getSubs().get(0).getSubs().get(0).getName();
			}

			mProvinceDatas = new String[cityBeanList.size()];
			for (int i = 0; i < cityBeanList.size(); i++) {
				// 遍历所有省的数据
				mProvinceDatas[i] = cityBeanList.get(i).getName();
				citySubsList = cityBeanList.get(i).getSubs();
				String[] cityNames = new String[citySubsList.size()];
				for (int j = 0; j < citySubsList.size(); j++) {
					// 遍历省下面的所有市的数据
					cityNames[j] = citySubsList.get(j).getName();
					districtSubsList = citySubsList.get(j).getSubs();
					if (districtSubsList == null) {
						continue;
					}
					String[] distrinctNameArray = new String[districtSubsList.size()];
					for (int k = 0; k < districtSubsList.size(); k++) {
						// 遍历市下面所有区/县的数据
						distrinctNameArray[k] = districtSubsList.get(k).getName();
					}
					// 市-区/县的数据，保存到mDistrictDatasMap
					mDistrictDatasMap.put(cityNames[j], distrinctNameArray);
				}
				// 省-市的数据，保存到mCitisDatasMap
				mCitisDatasMap.put(cityBeanList.get(i).getName(), cityNames);
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
