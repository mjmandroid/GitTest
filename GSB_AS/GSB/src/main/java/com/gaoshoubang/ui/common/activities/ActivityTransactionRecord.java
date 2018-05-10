package com.gaoshoubang.ui.common.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.common.presenter.TransactionRecordImpl;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.common.fragments.TransactionChildFragment;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.widget.NavigationTabStrip;
import com.gaoshoubang.widget.NavigationTabStrip.OnTabStripSelectedIndexListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 交易记录
 *
 * @author Cisco
 */
public class ActivityTransactionRecord extends BaseActivity {
	private static final String TAG = "ActivityTransactionRecord";

	private ViewPager viewPager;
	private NavigationTabStrip navigationTabStrip;
	private int fragmentCount = 6;
	List<TransactionChildFragment> fragments;//fragment的集合


	@Override
	protected TransactionRecordImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_transaction_record;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		viewPager = (ViewPager) findViewById(R.id.record_viewpager);
		navigationTabStrip = (NavigationTabStrip) findViewById(R.id.record_tab);


	}

	@Override
	protected void initEvent() {
		fragments = new ArrayList<>();
		for (int i = 0; i < fragmentCount; i++) {
			TransactionChildFragment transactionChildFragment = new TransactionChildFragment();
			transactionChildFragment.setIndex(i);
			fragments.add(transactionChildFragment);
		}
		viewPager.setOffscreenPageLimit(6);
		viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public Fragment getItem(int position) {
				return fragments.get(position);
			}

			@Override
			public int getCount() {
				return 6;
			}
		});
		//初始化 tap
		navigationTabStrip.setTitles("全部", "到账", "投资", "充值", "提现", "奖励");
		navigationTabStrip.setViewPager(viewPager, 0);
		navigationTabStrip.setOnTabStripSelectedIndexListener(new OnTabStripSelectedIndexListener() {

			@Override
			public void onStartTabSelected(String title, int index) {
			}

			@Override
			public void onEndTabSelected(String title, int index) {
				//here
				viewPager.setCurrentItem(index);
			}
		});
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}

}
