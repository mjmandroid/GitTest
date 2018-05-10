package com.gaoshoubang.ui.information.activities;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AddressBean;
import com.gaoshoubang.bean.CityStreetBean;
import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.ui.information.presenter.AddressPresenterImpl;
import com.gaoshoubang.ui.information.view.AdressView;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.AccountValidatorUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.widget.wheel.AddressStreetWheelDialog;
import com.gaoshoubang.widget.wheel.AddressWheelDialog;

import static com.gaoshoubang.R.id.address_save;

/**
 * 修改个人中心地址页面
 */
public class ActivityAddress extends BaseActivity<AddressPresenterImpl> implements OnClickListener, AdressView {
	protected static final String TAG = "ActivityAddress";
	private EditText name;
	private EditText phone;
	private TextView addressCity;
	private TextView addressStreet;
	private EditText addressDetails;
	private Button btnSave;

	private AddressWheelDialog mAddressWheelDialog;//市区dialog
	private AddressStreetWheelDialog mAddressStreetWheelDialog;//街道dialog
	private CityStreetBean cityStreetBean;
	private String[] mSteetDatas;
	private String townId;
	private String[] addressId;

	private SelfBean.Addr selfBeanAddr;

	@Override
	protected AddressPresenterImpl getPresenter() {
		return new AddressPresenterImpl();
	}

	@Override
	public int getLayoutId() {
		return R.layout.activity_address;
	}

	@Override
	public void initView() {

		new FullTitleBar(this);

		mAddressWheelDialog = new AddressWheelDialog(this);
		name = (EditText) findViewById(R.id.address_name);
		phone = (EditText) findViewById(R.id.address_phone);
		addressCity = (TextView) findViewById(R.id.address_city);
		addressStreet = (TextView) findViewById(R.id.address_street);
		addressDetails = (EditText) findViewById(R.id.address_details);
		btnSave = (Button) findViewById(address_save);
	}

	@Override
	public void initEvent() {
		addressCity.setOnClickListener(this);
		addressStreet.setOnClickListener(this);
		btnSave.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		selfBeanAddr = (SelfBean.Addr) this.getIntent().getSerializableExtra("selfBeanAddr");
		if (selfBeanAddr != null) {
			name.setText(selfBeanAddr.getName());
			phone.setText(selfBeanAddr.getPhone());
			//省-市-县
			addressCity.setText(selfBeanAddr.getProvince_name() + " " + selfBeanAddr.getCity_name() + " " + selfBeanAddr.getArea_name());
			addressStreet.setText(selfBeanAddr.getTown_name());
			addressDetails.setText(selfBeanAddr.getAddress());
			mAddressWheelDialog.setProvinceCurrentItem(selfBeanAddr.getProvince(), selfBeanAddr.getCity(), selfBeanAddr.getArea());

			townId = selfBeanAddr.getTown();
			addressId = mAddressWheelDialog.getAddressId();
			mPresenter.requestMygetArea(selfBeanAddr.getArea());
		}
	}

	@Override
	protected void bindData() {

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.address_city:
				mAddressWheelDialog.show();
				mAddressWheelDialog.setSaveOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						mSteetDatas = new String[]{""};
						addressStreet.setText("");
						mAddressStreetWheelDialog = null;
						townId = "";
						addressId = mAddressWheelDialog.getAddressId();

						addressCity.setText(mAddressWheelDialog.getSelectedResult());
						mAddressWheelDialog.dismiss();
						mPresenter.requestMygetArea(mAddressWheelDialog.getDistrictId());
					}
				});

				break;

			case R.id.address_street://街道选择
				showStreetDialog();
				break;

			case address_save:
				if (TextUtils.isEmpty(name.getText())) {
					ToastUtil.toast(ActivityAddress.this, "请输入收件人姓名");
					return;
				}
				if (TextUtils.isEmpty(phone.getText())) {
					ToastUtil.toast(ActivityAddress.this, "请填写联系人号码");
					return;
				}
				if (TextUtils.isEmpty(addressCity.getText())) {
					ToastUtil.toast(ActivityAddress.this, "请选择所在地区");
					return;
				}
				if (TextUtils.isEmpty(addressStreet.getText())){
					ToastUtil.toast(ActivityAddress.this, "请选择所在街道");
					return;
				}
				if (TextUtils.isEmpty(addressDetails.getText())){
					ToastUtil.toast(ActivityAddress.this, "请选择详细地址");
					return;
				}
				AddressBean bean = new AddressBean(
						name.getText().toString(),
						phone.getText().toString(),
						addressId[0],
						addressId[1],
						addressId[2],
						townId, addressDetails.getText().toString());
				requestMysetArea(bean);
				break;
		}
	}

	/**
	 * 地址检查
	 */
	private void showExamine() {
		if (TextUtils.isEmpty(name.getText())) {
			ToastUtil.toast(ActivityAddress.this, "请输入收件人姓名");
			return;
		}
		if (TextUtils.isEmpty(phone.getText())) {
			ToastUtil.toast(ActivityAddress.this, "请填写联系人号码");
			return;
		}
		if (TextUtils.isEmpty(addressCity.getText())) {
			ToastUtil.toast(ActivityAddress.this, "请选择所在地区");
			return;
		}
		if (TextUtils.isEmpty(addressStreet.getText())){
			ToastUtil.toast(ActivityAddress.this, "请选择所在街道");
			return;
		}
		if (TextUtils.isEmpty(addressDetails.getText())){
			ToastUtil.toast(ActivityAddress.this, "请选择详细地址");
			return;
		}
		AddressBean bean = new AddressBean(
				name.getText().toString(),
				phone.getText().toString(),
				addressId[0],
				addressId[1],
				addressId[2],
				townId, addressDetails.getText().toString());
		requestMysetArea(bean);
	}

	// 显示街道的dialog
	private void showStreetDialog() {
		if (addressCity.getText().toString().isEmpty()) {
			ToastUtil.toast(ActivityAddress.this, "请选择所在地区");
			return;
		}
		if (cityStreetBean == null || cityStreetBean.getSubs() == null) {
			ToastUtil.toast(ActivityAddress.this, "暂无街道信息");
			return;
		}
		mSteetDatas = new String[cityStreetBean.getSubs().size()];
		for (int i = 0; i < cityStreetBean.getSubs().size(); i++) {
			mSteetDatas[i] = cityStreetBean.getSubs().get(i).getName();
		}
		if (mAddressStreetWheelDialog == null) {
			mAddressStreetWheelDialog = new AddressStreetWheelDialog(this, mSteetDatas);
			if (selfBeanAddr != null) {
				mAddressStreetWheelDialog.setStreetCurrentItem(selfBeanAddr.getTown_name());
			}
		}
		mAddressStreetWheelDialog.show();
		mAddressStreetWheelDialog.setSaveOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				townId = cityStreetBean.getSubs().get(mAddressStreetWheelDialog.getCurrentItem()).getId();
				addressStreet.setText(mAddressStreetWheelDialog.getSteetName());
				mAddressStreetWheelDialog.dismiss();
			}
		});
	}

	// 保存地址
	private void requestMysetArea(AddressBean bean) {
		if (TextUtils.isEmpty(bean.name)) {
			ToastUtil.toast(ActivityAddress.this, "请填写收件人姓名");
			return;
		}
		if (TextUtils.isEmpty(bean.phone)) {
			ToastUtil.toast(ActivityAddress.this, "请填写联系人号码");
			return;
		}
		if (addressCity.getText().toString().isEmpty()) {
			ToastUtil.toast(ActivityAddress.this, "请选择所在地区");
			return;
		}
		if (TextUtils.isEmpty(bean.address)) {
			ToastUtil.toast(ActivityAddress.this, "请填写详细地址");
			return;
		}
		mPresenter.requestMySetArea(bean);
	}

	@Override
	public void afterGetCityStreetBean(CityStreetBean bean) {
		cityStreetBean = bean;
	}

	@Override
	public void afterSetArea(String state) {
		if (state != null && state.equals("1")) {
			ToastUtil.toast(ActivityAddress.this, "保存地址成功");
			sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_MDIFYINFO));
			finish();
		}
		else {
			ToastUtil.toast(ActivityAddress.this, "保存失败");
		}
	}


}
