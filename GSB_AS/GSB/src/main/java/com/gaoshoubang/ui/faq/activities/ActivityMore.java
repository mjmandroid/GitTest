package com.gaoshoubang.ui.faq.activities;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.UmEvent;

/**
 * 关于和帮助
 *
 * @author Cisco
 */
public class ActivityMore extends BaseActivity implements OnClickListener {

	private LinearLayout help;
	private LinearLayout feedBack;
	private LinearLayout connection;
	private LinearLayout about;

	private Intent intent;

	@Override
	protected BasePresenterImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_more;
	}

	@Override
	protected void initView() {
		new FullTitleBar(this, "#ffffff");

		help = (LinearLayout) findViewById(R.id.my_help);
		feedBack = (LinearLayout) findViewById(R.id.more_feedback);
		connection = (LinearLayout) findViewById(R.id.more_connection);
		about = (LinearLayout) findViewById(R.id.more_about);

		ripple(help);
		ripple(feedBack);
		ripple(connection);
		ripple(about);

	}

	@Override
	protected void initEvent() {
		help.setOnClickListener(this);
		feedBack.setOnClickListener(this);
		connection.setOnClickListener(this);
		about.setOnClickListener(this);
	}

	@Override
	protected void loadData() {

	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.my_help:// 帮助中心
				setUmEvent(UmEvent.Gsy_aboutAndHelp_question);
				intent = new Intent(this, ActivityCommonProblem.class);
				startActivity(intent);
				break;

			case R.id.more_feedback:// 我要反馈
				setUmEvent(UmEvent.Gsy_my_myQuestionBack);
				intent = new Intent(this, ActivityFeedback.class);
				startActivity(intent);
				break;

			case R.id.more_connection:// 联系我们
				setUmEvent(UmEvent.Gsy_aboutAndHelp_about);
				intent = new Intent(this, ActivityAbout.class);
				startActivity(intent);
				break;

			case R.id.more_about:// 关于我们
				setUmEvent(UmEvent.Gsy_about_aboutGsy);
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean cnfListBean) {
						Intent intent = new Intent(ActivityMore.this, WebActivityShow.class);
						intent.putExtra("url", cnfListBean.getABOUT_ME_URL());
						intent.putExtra("type", WebActivityShow.TYPE_ABOU_WE);
						startActivity(intent);
					}

					public void onFail() {
					}
				});

				break;
		}
	}

}
