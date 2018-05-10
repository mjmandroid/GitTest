package com.gaoshoubang.ui.welcome.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaoshoubang.R;

public class GuideFragment3 extends Fragment {
	private View contentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.fragment_guide_3, container, false);
		}
		if (contentView != null) {
			return contentView;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle bundle) {
		if (view != null) {
		}
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

}