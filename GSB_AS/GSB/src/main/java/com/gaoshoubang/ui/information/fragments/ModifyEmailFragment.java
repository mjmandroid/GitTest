package com.gaoshoubang.ui.information.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * Created by lzx on 2017/6/9.
 */

public class ModifyEmailFragment extends Fragment implements View.OnClickListener {
	private TextView mTitle;
	private EditText mInputEdit;

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.activity_set_info, container);
		mTitle = ((TextView) view.findViewById(R.id.info_title));
		mInputEdit = ((EditText) view.findViewById(R.id.info_edit));
		view.findViewById(R.id.info_edit_clear).setOnClickListener(this);
		view.findViewById(R.id.info_next).setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.info_next:
				break;
			case R.id.info_edit_clear:
				break;
		}

	}
}
