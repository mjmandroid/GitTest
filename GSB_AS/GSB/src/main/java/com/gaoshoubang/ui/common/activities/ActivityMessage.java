package com.gaoshoubang.ui.common.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.MessageBean;
import com.gaoshoubang.ui.common.presenter.MessagePresenterImpl;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.common.adapters.MessageAdapter;
import com.gaoshoubang.ui.common.view.MessageView;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;


public class ActivityMessage extends BaseActivity<MessagePresenterImpl> implements MessageView {
	private static final String TAG = "ActivityMessage";

	private ListView messageList;
	private XRefreshView xRefreshView;
	private ImageView showLoading;

	private List<MessageBean> listBean;
	private MessageAdapter messageAdapter;
	private SwingBottomInAnimationAdapter animationAdapter;

	private AnimationDrawable mAnimation;


	@Override
	protected MessagePresenterImpl getPresenter() {
		return new MessagePresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_message;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 3));
	}

	protected void initView() {
		new FullTitleBar(this);
//		mMessagePresenter = new MessagePresenterImpl();
//		mMessagePresenter.attachView(this);

		xRefreshView = (XRefreshView) findViewById(R.id.message_xrefreshview);
		messageList = (ListView) findViewById(R.id.message_list);
		showLoading = (ImageView) findViewById(R.id.show_loading);


		mAnimation = (AnimationDrawable) showLoading.getBackground();
		showLoading.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});

		listBean = new ArrayList<MessageBean>();
		messageAdapter = new MessageAdapter(listBean, this);
		animationAdapter = new SwingBottomInAnimationAdapter(messageAdapter);
		animationAdapter.setAbsListView(messageList);
		messageList.setAdapter(animationAdapter);
		showErrorView();

	}

	@Override
	protected void initEvent() {
		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh() {
				mPresenter.requestMyMessage();
			}

			@Override
			public void onLoadMore(boolean isSlience) {
			}
		});
		messageList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (listBean.get(position).getUrl().equals("")) {
					return;
				}
				Intent intent = new Intent(ActivityMessage.this, WebActivityShow.class);
				intent.putExtra("url", listBean.get(position).getUrl());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void loadData() {
		mPresenter.requestMyMessage();
	}

	@Override
	protected void bindData() {

	}


	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		super.showOtherErrMsg(statusCode, msg);
		if (listBean.size() == 0) {
			showErrorPage();
		}
	}

	@Override
	public void hideProgress() {
		super.hideProgress();
		showLoading.setVisibility(View.GONE);
		xRefreshView.stopRefresh();
	}

	public void afterGetMessages(List<MessageBean> messages) {

		listBean.clear();
		listBean.addAll(messages);
		animationAdapter.reset();
		messageAdapter.notifyDataSetChanged();
		hideErrorPage();


	}

	@Override
	public void onRequestFail() {
		super.onRequestFail();
		if (listBean.size() == 0) {
			showErrorPage();
		}
	}

	// 加载错误界面
	private void showErrorView() {
		setShowErrorView(new OnShowErrorViewListener() {
			@Override
			public void show() {
				showLoading.setVisibility(View.GONE);
			}

			@Override
			public void refresh() {
				showLoading.setVisibility(View.VISIBLE);
				mPresenter.requestMyMessage();
			}

			@Override
			public void hide() {
				showLoading.setVisibility(View.GONE);
			}
		});
	}
}
