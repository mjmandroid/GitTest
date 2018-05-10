package com.gaoshoubang.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * Created by Administrator on 2017/5/15.
 */

public class FingerprintDialog extends Dialog {
	private onFingerprintRecognize mListener;
	private TextView mTvTips;

	public FingerprintDialog(@NonNull Context context) {
		this(context, R.style.Dialog);
	}

	public FingerprintDialog(@NonNull Context context, @StyleRes int themeResId) {
		super(context, themeResId);
		setContentView(R.layout.fingerprint_dialog);
		mTvTips = ((TextView) findViewById(R.id.tv_tips));
		findViewById(R.id.fingerprint_dialog_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				if (mListener != null) {
					mListener.onIdentifyCancel();
				}
			}
		});
		if (mListener != null) {
			mListener.onIdentifyStart();
		}
		setCanceledOnTouchOutside(false);
	}

	public void setOnFingerprintRecognize(onFingerprintRecognize listener) {
		mListener = listener;
	}


	public interface onFingerprintRecognize {
		void onIdentifyStart();


		void onIdentifyCancel();
	}

	public void setTvTips(String content) {
		mTvTips.setVisibility(View.VISIBLE);
		mTvTips.setText(content);
		TranslateAnimation translate = new TranslateAnimation(0, 100, 0, 0);
		translate.setInterpolator(new AnticipateInterpolator());
		translate.setStartTime(500);
		translate.setRepeatCount(Animation.REVERSE);
		mTvTips.startAnimation(translate);
	}

}
