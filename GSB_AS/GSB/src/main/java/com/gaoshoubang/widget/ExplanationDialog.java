package com.gaoshoubang.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * 问题解释提示框
 *
 * @author Cisco
 */
public class ExplanationDialog extends Dialog {
	private TextView define;

	public ExplanationDialog(Context context) {
		super(context, R.style.Dialog);
		setContentView(R.layout.dialog_explanation);
		define = (TextView) findViewById(R.id.explanationdialog_define);

		define.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
