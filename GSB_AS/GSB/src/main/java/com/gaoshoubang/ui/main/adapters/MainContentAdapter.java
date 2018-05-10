package com.gaoshoubang.ui.main.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

public class MainContentAdapter extends FragmentPagerAdapter {
	private static final String TAG = "MainContentAdapter";
	private List<Fragment> fragments = null;

	public MainContentAdapter(FragmentManager fm, List<Fragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		super.destroyItem(container, position, object);
		// Log.i(TAG,"destroyItem: "+position);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// Log.i(TAG,"instantiateItem: "+position);
		return super.instantiateItem(container, position);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Bundle bundle = fragments.get(position).getArguments();
		if (bundle != null) {
			return bundle.getString("tabString");
		}
		return "";
	}
}
