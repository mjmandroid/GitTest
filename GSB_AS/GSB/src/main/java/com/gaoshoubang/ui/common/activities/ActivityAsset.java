package com.gaoshoubang.ui.common.activities;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.NavigationTabStrip;
import com.gaoshoubang.widget.NavigationTabStrip.OnTabStripSelectedIndexListener;
import com.gaoshoubang.bean.AssetBean;
import com.gaoshoubang.ui.common.presenter.AssetPresenterImpl;
import com.gaoshoubang.ui.main.adapters.MainContentAdapter;
import com.gaoshoubang.ui.common.fragments.AssetAmountFragment;
import com.gaoshoubang.ui.common.fragments.AssetEarningsFragment;
import com.gaoshoubang.ui.common.fragments.AssetIdleFragment;
import com.gaoshoubang.ui.common.view.AssetView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.UmEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的总资产
 */
public class ActivityAsset extends BaseActivity<AssetPresenterImpl> implements OnClickListener, AssetView {
	protected static final String TAG = "ActivityAsset";

	private ViewPager viewPager;
	private NavigationTabStrip navigationTabStrip;
	private ImageView back;
	private TextView record;

	private List<Fragment> fragments = new ArrayList<Fragment>();//fragment集合

	private AssetAmountFragment assetAmountFragment;//总资产
	private AssetEarningsFragment assetEarningsFragment;//累计收益
	private AssetIdleFragment assetIdleFragment;//闲置资金

	public AssetBean mAssetBean;

	@Override
	protected AssetPresenterImpl getPresenter() {
		return new AssetPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_asset;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		back = (ImageView) findViewById(R.id.asset_back);
		record = (TextView) findViewById(R.id.asset_record);
		viewPager = (ViewPager) findViewById(R.id.asset_viewpager);
		navigationTabStrip = (NavigationTabStrip) findViewById(R.id.asset_tab);


		assetAmountFragment = new AssetAmountFragment();
		assetEarningsFragment = new AssetEarningsFragment();
		assetIdleFragment = new AssetIdleFragment();

		fragments.add(assetAmountFragment);
		fragments.add(assetEarningsFragment);
		fragments.add(assetIdleFragment);

		viewPager.setAdapter(new MainContentAdapter(getSupportFragmentManager(), fragments));
		viewPager.setOffscreenPageLimit(3);

		navigationTabStrip.setTitles("总资产", "累计收益", "闲置资金");
		navigationTabStrip.setViewPager(viewPager, getIntent().getIntExtra("asset_type", 0));
		navigationTabStrip.setOnTabStripSelectedIndexListener(new OnTabStripSelectedIndexListener() {
			@Override
			public void onStartTabSelected(String title, int index) {

			}

			@Override
			public void onEndTabSelected(String title, int index) {
				switch (index) {
					case 0:
						setUmEvent(UmEvent.Gsy_userMoney_allMoney);
						break;
					case 1:
						setUmEvent(UmEvent.Gsy_userMoney_allGetMoney);
						break;
					case 2:
						setUmEvent(UmEvent.Gsy_userMoney_freeMoney);
						break;
				}
			}
		});
	}

	@Override
	protected void initEvent() {
		back.setOnClickListener(this);
		record.setOnClickListener(this);
	}

	@Override
	protected void loadData() {
		mPresenter.loadData();
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.asset_back:
				finish();
				break;

			case R.id.asset_record:
				setUmEvent(UmEvent.Gsy_my_TransactionRecord);
				Intent intent = new Intent(this, ActivityTransactionRecord.class);
//				Intent intent = new Intent(this, ActivityTransactionRecord.class);
				startActivity(intent);
				break;
		}
	}


	@Override
	public void afterGetMyMoney(AssetBean assetBean) {
		mAssetBean = assetBean;
		assetAmountFragment.initData(assetBean);
		assetEarningsFragment.initData(assetBean);
		assetIdleFragment.initData(assetBean);
	}
}
