package com.gaoshoubang.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.gaoshoubang.util.LogUtils;

public class MobileNumberEditText extends android.support.v7.widget.AppCompatEditText {

	public MobileNumberEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	public MobileNumberEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MobileNumberEditText(Context context) {
		super(context);
		init();
	}

	private void init() {
		setFocusableInTouchMode(true);
		this.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				changeState();

				// Editable editable = MobileNumberEditText.this.getText();
				//
				// String text = editable.toString();
				// String newText = "";
				//
				// boolean change = false;
				//
				// char[] strs = text.toCharArray();
				// if (strs.length >= 4) {
				// if (!" ".equals(strs[3] + ""))
				// change = true;
				//
				// if (strs.length >= 9) {
				// if (!" ".equals(strs[8] + ""))
				// change = true;
				// }
				// }
				//
				// if (change) {
				// strs = text.replaceAll(" ", "").toCharArray();
				// for (int i = 0; i < strs.length; i++) {
				// newText += strs[i];
				// if (i == 2 || i == 6) {
				// newText += " ";
				// }
				// }
				// MobileNumberEditText.this.setText(newText);
				// // 设置新光标所在的位置
				// MobileNumberEditText.this.setSelection(newText.length());
				// }
				if (s == null || s.length() == 0) {
					return;
				}
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < s.length(); i++) {
					if (i != 3 && i != 8 && s.charAt(i) == ' ') {
						continue;
					}
					else {
						sb.append(s.charAt(i));
						if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
							sb.insert(sb.length() - 1, ' ');
						}
					}
				}
				if (!sb.toString().equals(s.toString())) {
					int index = start + 1;
					if (sb.charAt(start) == ' ') {
						if (before == 0) {
							index++;
						}
						else {
							index--;
						}
					}
					else {
						if (before == 1) {
							index--;
						}
					}
					MobileNumberEditText.this.setText(sb.toString());
					MobileNumberEditText.this.setSelection(index);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		LogUtils.e("MobileNumberEditText", "onTouchEvent:");
		return super.onTouchEvent(event);

	}

	//// TODO: 2017/6/28
	public String getTextForString() {
		return MobileNumberEditText.this.getText().toString().replaceAll(" ", "");
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
				MobileNumberEditText.this.setText("");
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
