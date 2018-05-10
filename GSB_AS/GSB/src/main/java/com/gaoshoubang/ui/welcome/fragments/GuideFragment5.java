package com.gaoshoubang.ui.welcome.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.util.UserSharedPreferenceUtils;

public class GuideFragment5 extends Fragment {

	private View contentView;
	private Button btn;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.fragment_guide_5, container, false);
		}
		if (contentView != null) {
			return contentView;
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle bundle) {
		if (view != null) {
			btn = (Button) contentView.findViewById(R.id.iv_next);
			btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), MainActivity.class);
					getActivity().startActivity(intent);
					new UserSharedPreferenceUtils().setFirst(false);
					getActivity().finish();
				}
			});
		}
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

}