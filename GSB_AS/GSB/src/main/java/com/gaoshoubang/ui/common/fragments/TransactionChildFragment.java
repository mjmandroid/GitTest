package com.gaoshoubang.ui.common.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.TransactionBean;
import com.gaoshoubang.bean.response.TransactionResponse;
import com.gaoshoubang.ui.common.presenter.TransactionRecordImpl;
import com.gaoshoubang.ui.common.view.TransactionView;
import com.gaoshoubang.ui.common.adapters.TransactionAdapter;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.widget.PromptDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzx on 2017/6/29.
 */

public class TransactionChildFragment extends BaseFragment<TransactionRecordImpl> implements TransactionView {

	private ListView mListView;
	private TransactionAdapter mTransactionAdapter;
	private XRefreshView xRefreshView;
	private ImageView showLoading;
	private AnimationDrawable mAnimation;
	private View errorView;
	private ViewStub mErrorViewStub;
	private TextView errorTitle;
	private TextView errorRefresh;
	private List<TransactionBean> mTransactionBeanList;
	private int mIndex;
	private boolean hasMore;
	private View mFooterView;
	private int currentPage;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_transaction_record_list;
	}

	@Override
	protected void initView() {
		mListView = (ListView) contentView.findViewById(R.id.record_list);
		xRefreshView = (XRefreshView) contentView.findViewById(R.id.record_xrefreshview);
		/*加载动画*/
		showLoading = (ImageView) contentView.findViewById(R.id.show_loading);
		mAnimation = (AnimationDrawable) showLoading.getBackground();
		//错误页相关东西
		if (errorView == null) {
			mErrorViewStub = ((ViewStub) contentView.findViewById(R.id.load_error_view));
			errorView = mErrorViewStub.inflate();
		}
		errorTitle = (TextView) errorView.findViewById(R.id.error_title);
		errorRefresh = (TextView) errorView.findViewById(R.id.error_refresh);
		View telView = errorView.findViewById(R.id.error_tel);
		showLoading.post(new Runnable() {
			@Override
			public void run() {
				mAnimation.start();
			}
		});

		errorRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				requestMyPayList(indexPage, 1, indexView);
				mPresenter.requestMyPayList(mIndex, 1);
			}
		});

		telView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final PromptDialog promptDialog = new PromptDialog(getContext());
				promptDialog.setContentText("是否拨打客服热线?", null);
				promptDialog.setDefineOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000685333"));
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
						promptDialog.dismiss();
					}
				});
				promptDialog.show();
			}
		});

		mFooterView = footerView();
	}

	@Override
	protected void initEvent() {
		mTransactionAdapter = new TransactionAdapter(getContext());
		mListView.setAdapter(mTransactionAdapter);

		//加载框架初始化
		xRefreshView.setAutoLoadMore(true);
		xRefreshView.setPullLoadEnable(true);
		xRefreshView.mFooterView.setFooterStateReadyText("上拉加载更多内容");
		xRefreshView.setXRefreshViewListener(mSimpleXRefreshListener);
	}

	XRefreshView.SimpleXRefreshListener mSimpleXRefreshListener = new XRefreshView.SimpleXRefreshListener() {
		@Override
		public void onRefresh() {
			mPresenter.requestMyPayList(mIndex, 1);
		}

		@Override
		public void onLoadMore(boolean isSlience) {
			//加载更多处理
			if (!hasMore) {
				return;
			}
			mPresenter.requestMyPayList(mIndex, currentPage + 1, false);
		}
	};

	@Override
	protected void bindData() {

	}

	@Override
	protected void loadData() {
		//进入页面加载第一页
		mTransactionBeanList = new ArrayList<>();
		mPresenter.requestMyPayList(mIndex, 1);
	}

	@Override
	public TransactionRecordImpl getPresenter() {
		return new TransactionRecordImpl();
	}


	@Override
	public void afterGetTransactionRecord(TransactionResponse transactionResponse) {
		xRefreshView.stopRefresh();
		xRefreshView.stopLoadMore();
//		 当前页等于请求页,数据没有变化
		if (currentPage == transactionResponse.getPage()) {
			return;
		}
		if (transactionResponse.data.size() == 0 && mTransactionBeanList.size() == 0) {
			errorTitle.setText("数据空空如也!");
			errorRefresh.setVisibility(View.GONE);
			errorView.setVisibility(View.VISIBLE);
			return;
		}

//		if (transactionResponse.)
		//当前页小于最大页数.有加载更多
		if (transactionResponse.getPage() < transactionResponse.getMaxPage()) {
			hasMore = true;
			xRefreshView.setPullLoadEnable(true);
			if (mListView.getFooterViewsCount() > 0) {
				mListView.removeFooterView(mFooterView);
			}
			xRefreshView.mFooterView.setFooterStateReadyText("上拉加载更多内容");
		}
		else {
			hasMore = false;
			xRefreshView.setPullLoadEnable(false);
			if (mListView.getFooterViewsCount() == 0) {
				mListView.addFooterView(mFooterView);
			}
		}
		//数据更新
		currentPage = transactionResponse.getPage();
		mTransactionBeanList.addAll(transactionResponse.data);//缓存?
		mTransactionAdapter.updateData(mTransactionBeanList);
		errorView.setVisibility(View.GONE);
	}

	public void setIndex(int index) {
		mIndex = index;
	}

	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();
	}

	/*
		* 加载更多
		* */
	private View footerView() {
		TextView footerText = new TextView(getContext());
		footerText.setTextSize(13);
		footerText.setTextColor(0xff434343);
//		footerText.setGravity(Gravity.CENTER);
//		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(getContext(), 35));
//		lp.gravity = Gravity.CENTER;
//		footerText.setLayoutParams(lp);
		footerText.setText("没有更多内容了");
		footerText.setGravity(Gravity.CENTER);
		return footerText;
	}
}
