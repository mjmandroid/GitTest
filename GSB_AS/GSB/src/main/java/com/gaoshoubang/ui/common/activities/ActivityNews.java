package com.gaoshoubang.ui.common.activities;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.bean.response.NewsResponse;
import com.gaoshoubang.ui.common.presenter.NewsPresenterImpl;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.common.adapters.NewsAdapter;
import com.gaoshoubang.ui.common.view.NewsView;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FullTitleBar;
import com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

import java.util.ArrayList;
import java.util.List;

public class ActivityNews extends BaseActivity<NewsPresenterImpl> implements NewsView {
	private static final String TAG = "ActivityNews";

	private ListView newsList;
	private XRefreshView xRefreshView;
	private ImageView showLoading;

	private List<NewsBean> newsBean;
	private NewsAdapter mNewsAdapter;
	private SwingBottomInAnimationAdapter animationAdapter;
	private List<NewsBean> temp;
	private boolean isHasData = true;
	private int page;
	private TextView footerText;
	private FrameLayout frameLayout;
	private AnimationDrawable mAnimation;

	@Override
	protected NewsPresenterImpl getPresenter() {
		return new NewsPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_news;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void initView() {
		new FullTitleBar(this);
		xRefreshView = (XRefreshView) findViewById(R.id.news_xrefreshview);
		newsList = (ListView) findViewById(R.id.news_list);
		showLoading = (ImageView) findViewById(R.id.show_loading);

		newsBean = new ArrayList<NewsBean>();
		mNewsAdapter = new NewsAdapter(newsBean, this);
		animationAdapter = new SwingBottomInAnimationAdapter(mNewsAdapter);
		animationAdapter.setAbsListView(newsList);
		newsList.setAdapter(animationAdapter);

		frameLayout = new FrameLayout(this);
		newsList.addFooterView(frameLayout);
		footerText = new TextView(this);
		footerText.setTextSize(13);
		footerText.setTextColor(0xff434343);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(this, 35));
		lp.gravity = Gravity.CENTER;
		footerText.setLayoutParams(lp);
		footerText.setText("没有更多内容了");
		footerText.setGravity(Gravity.CENTER);

		xRefreshView.setAutoLoadMore(true);
		xRefreshView.setPullLoadEnable(true);
		xRefreshView.mFooterView.setFooterStateReadyText("上拉加载更多内容");


		mAnimation = (AnimationDrawable) showLoading.getBackground();
		showLoading.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});
		showErrorView();
	}

	@Override
	protected void initEvent() {
		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {
			@Override
			public void onRefresh() {

				animationAdapter.reset();
				mPresenter.requestMessageMedia(1);

			}

			@Override
			public void onLoadMore(boolean isSlience) {
				if (!isHasData) {
					return;
				}
				mPresenter.requestMessageMedia(page + 1);

			}
		});

		newsList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (newsBean.get(position).getUrl().equals("")) {
					return;
				}
				Intent intent = new Intent(ActivityNews.this, WebActivityShow.class);
				intent.putExtra("url", newsBean.get(position).getUrl());
				startActivity(intent);
			}
		});
	}

	@Override
	protected void loadData() {
		mPresenter.requestMessageMedia(1);
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();
		xRefreshView.stopLoadMore();
		showLoading.setVisibility(View.GONE);
		dismissLoad(true);
	}


	@Override
	public void showOtherErrMsg(int statusCode, String msg) {
		super.showOtherErrMsg(statusCode, msg);
		if (newsBean.size() == 0 && temp == null) {
			showErrorPage();
		}
	}

	@Override
	public void onRequestFail() {
		super.onRequestFail();
		if (newsBean.size() == 0 && temp == null) {
			showErrorPage();
		}
		else {
			newsList.addFooterView(footerText);
			footerText.setText("网络连接不可用，请检查您的网络设置");
		}
	}

	public void afterGetNews(int p, NewsResponse newsResponse) {
		if (p == 1) {
			newsBean.clear();
		}
		temp = newsResponse.data;
		if (temp.size() < 15) {
			isHasData = false;
			xRefreshView.setPullLoadEnable(false);
			frameLayout.removeAllViews();
			frameLayout.addView(footerText);
		}
		else {
			isHasData = true;
			xRefreshView.setPullLoadEnable(true);
			frameLayout.removeAllViews();
		}
		//   page
		page = newsResponse.page;
		newsBean.addAll(temp);
		mNewsAdapter.notifyDataSetChanged();
		hideErrorPage();

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
				mPresenter.requestMessageMedia(1);
			}

			@Override
			public void hide() {
				showLoading.setVisibility(View.GONE);
			}
		});
	}
}
