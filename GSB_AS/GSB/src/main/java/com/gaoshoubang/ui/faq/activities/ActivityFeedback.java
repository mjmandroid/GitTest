package com.gaoshoubang.ui.faq.activities;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.faq.presenter.FeedbackPresenterImpl;
import com.gaoshoubang.ui.faq.view.FeedbackView;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ToastUtil;

/**
 * 用户反馈
 *
 * @author Cisco
 */
public class ActivityFeedback extends BaseActivity<FeedbackPresenterImpl> implements FeedbackView {

	private EditText content;
	private TextView submit;


	@Override
	protected FeedbackPresenterImpl getPresenter() {
		return new FeedbackPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_feedback;
	}

	@Override
	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		content = (EditText) findViewById(R.id.feedback_edit);
		submit = (TextView) findViewById(R.id.feedback_submit);
	}

	@Override
	protected void initEvent() {
		submit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (content.getText().toString().equals("")) {
					ToastUtil.toast(ActivityFeedback.this, "请输入反馈内容");
					return;
				}
				mPresenter.requestCustomerBack(content.getText().toString());
			}
		});
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}


	@Override
	public void afterFeedback() {
		ToastUtil.toast(ActivityFeedback.this, "感谢您的反馈");
		finish();
	}
}
