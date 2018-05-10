package com.gaoshoubang.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

/**
 * Created by haji on 2015/12/2.
 * 可设置清除按钮的Edittext
 */
public class ClearEditText extends EditText {


	public ClearEditText(Context context, AttributeSet attrs,
	                     int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ClearEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ClearEditText(Context context) {
		super(context);
	}


	private View iv_clear;

	/**
	 * 设置清除按钮
	 *
	 * @param iv_clear
	 */
	public void setIvClear(final View iv_clear) {
		this.iv_clear = iv_clear;
		iv_clear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClearEditText.this.setText("");
			}
		});
		this.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					changeState();
				}
				else {
					iv_clear.setVisibility(View.INVISIBLE);
				}
			}
		});

		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
			                          int count) {
				changeState();

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
			                              int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private void changeState() {
		if (iv_clear != null) {
			if (this.getText().toString().length() > 0) {
				iv_clear.setVisibility(View.VISIBLE);
			}
			else {
				iv_clear.setVisibility(View.INVISIBLE);
			}
		}
	}
}
