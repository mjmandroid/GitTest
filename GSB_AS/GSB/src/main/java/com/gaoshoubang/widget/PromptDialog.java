package com.gaoshoubang.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * 通用提示框
 *
 * @author Cisco
 */
public class PromptDialog extends Dialog {
	private TextView promptTitle;
	private TextView promptContent;
	private TextView promptDefine;
	private TextView promptCancel;
	private RelativeLayout promptRlDefine;
	private RelativeLayout promptRlcancel;

	public PromptDialog(Context context) {
		super(context, R.style.Dialog);
		setContentView(R.layout.dialog_prompt);
		initView();
	}

	private void initView() {
		promptTitle = (TextView) findViewById(R.id.prompt_title);
		promptContent = (TextView) findViewById(R.id.prompt_content);
		promptDefine = (TextView) findViewById(R.id.prompt_define);
		promptCancel = (TextView) findViewById(R.id.prompt_cancel);
		promptRlDefine = (RelativeLayout) findViewById(R.id.prompt_define_rl);
		promptRlcancel = (RelativeLayout) findViewById(R.id.prompt_cancel_rl);

		promptRlDefine.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});

		promptRlcancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/**
	 * 设置提示标题和内容
	 *
	 * @param title
	 * @param content
	 */
	public void setContentText(String title, String content) {
		if (title != null && !title.equals("")) {
			promptTitle.setVisibility(View.VISIBLE);
			promptTitle.setText(title);
		}
		if (content != null && !content.equals("")) {
			promptContent.setVisibility(View.VISIBLE);
			promptContent.setText(content);
		}
	}

	/**
	 * 设置按钮的内容,null 为隐藏
	 *
	 * @param define
	 * @param cancel
	 */
	public void setContentButton(String define, String cancel) {
		if (define == null) {
			promptRlDefine.setVisibility(View.GONE);
		}
		if (cancel == null) {
			promptRlcancel.setVisibility(View.GONE);
		}
		promptDefine.setText(define);
		promptCancel.setText(cancel);
	}

	/**
	 * 确定的监听
	 *
	 * @param defineOnClickListener
	 */
	public void setDefineOnClickListener(View.OnClickListener defineOnClickListener) {
		promptDefine.setOnClickListener(defineOnClickListener);
	}

	/**
	 * 取消的监听
	 *
	 * @param cancelOnClickListener
	 */
	public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
		promptRlcancel.setOnClickListener(cancelOnClickListener);
	}

}