package com.gaoshoubang.ui.main.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.XRefreshView.SimpleXRefreshListener;
import com.gaoshoubang.C;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.ui.main.presenter.ProductPresenterImpl;
import com.gaoshoubang.ui.main.view.ProductView;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.ui.faq.activities.ActivityCommonProblem;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.ui.main.adapters.ProductRecyclerAdapter;
import com.gaoshoubang.base.fragments.BaseFragment;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.UmEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * 理财页面
 *
 * @author Done
 */
public class ProductFragment extends BaseFragment<ProductPresenterImpl> implements OnClickListener, ProductRecyclerAdapter.CustomListener, ProductView {
	private static final String TAG = "ProductFragment";
	public ImageView medal;

	private List<ProductBean> productBean;
	//	public SwingBottomInAnimationAdapter animationAdapter;
	// 下拉刷新
	private XRefreshView xRefreshView;
	private Intent intent;
	private RecyclerView mRecyclerViewProduct;
	private ProductRecyclerAdapter mRecyclerAdapter;
	private ImageView mProductQuestion;
	private LinearLayoutManager mLinearLayoutManager;
	private HashMap<Integer, List<ProductBean>> mProductMap;
	private long mOldTime;


	@Override
	protected int getLayoutId() {
		return R.layout.fragment_product;
	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}


	public void initView() {
		new FullTitleBar(getActivity(), contentView.findViewById(R.id.title_bar));
		mProductQuestion = (ImageView) contentView.findViewById(R.id.product_titlebar_que);
		productBean = new ArrayList<>();
		xRefreshView = (XRefreshView) contentView.findViewById(R.id.product_xrefreshview);
		medal = (ImageView) contentView.findViewById(R.id.product_madel);
		mRecyclerViewProduct = ((RecyclerView) contentView.findViewById(R.id.recyclerView_product));

	}

	@Override
	protected void initEvent() {
		mRecyclerAdapter = new ProductRecyclerAdapter(getContext());
		mLinearLayoutManager = new LinearLayoutManager(getContext());
		mRecyclerViewProduct.setAdapter(mRecyclerAdapter);
		mRecyclerViewProduct.setLayoutManager(mLinearLayoutManager);

		mRecyclerAdapter.setCustomListener(this);
		mProductQuestion.setOnClickListener(this);
		medal.setOnClickListener(this);
		xRefreshView.setXRefreshViewListener(new SimpleXRefreshListener() {

			@Override
			public void onRefresh() {
				mOldTime = System.currentTimeMillis();
				mPresenter.requestProduct();
			}

			@Override
			public void onLoadMore(boolean isSlience) {

			}
		});
	}

	@Override
	protected void bindData() {
	}

	@Override
	protected void loadData() {
		mPresenter.requestProduct();
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.product_madel:
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						intent = new Intent(getActivity(), WebActivityShow.class);
						intent.putExtra("url", mCnfListBean.getMEDAL_RULE());
						startActivity(intent);
					}

					public void onFail() {
						return;
					}
				});
				break;
			case R.id.product_titlebar_que://帮助按钮
				setUmEvent(UmEvent.Gsy_investment_question);
				Intent intent = new Intent(getActivity(), ActivityCommonProblem.class);
				startActivity(intent);
				break;
		}
	}

	/**
	 * 初始化 标题对应下的产品列表
	 *
	 * @param products
	 * @param list
	 *
	 * @return
	 */
	@NonNull
	private HashMap<Integer, List<ProductBean>> initProductMap(List<ProductBean> products, List<String> list) {
		if (products == null || products.size() <= 0) {
			return null;
		}
		List<ProductBean> listNew = new ArrayList<>();
		List<ProductBean> listFlexible = new ArrayList<>();
		List<ProductBean> listStability = new ArrayList<>();
		HashMap<Integer, List<ProductBean>> hashMap = new HashMap<>();
		for (ProductBean product : products) {
			if (product.getFlag() == 3 || product.getFlag() == 4 || product.getFlag() == 5) {
				continue;
			}
			if (product.getActivityId() == 1) {      //新手标
				listNew.add(product);
				if (!list.contains(C.ProductConst.NEW)) {
					list.add(C.ProductConst.NEW);
				}
			}
			else if (product.getInittype() == 2) {  //灵活  目前只有-->月月盈
				listFlexible.add(product);
				if (!list.contains(C.ProductConst.FLEXIBLE)) {
					list.add(C.ProductConst.FLEXIBLE);
				}
			}
			else if (product.getInittype() == 1 && product.getFlag() == 1) {  //固收
				listStability.add(product);
				if (!list.contains(C.ProductConst.STABILITY)) {
					list.add(C.ProductConst.STABILITY);
				}
			}
		}

		hashMap.put(C.ProductConst.TYPE_NEW, listNew);
		hashMap.put(C.ProductConst.TYPE_FLEXIBLE, listFlexible);
		hashMap.put(C.ProductConst.TYPE_STABILITY, listStability);
		return hashMap;
	}


	private View mErrorView;
	private boolean isErrorViewShow = false;


	/**
	 * 显示网络错误的view
	 */
	public void showErrorPage() {
		if (productBean.size() > 0) {
			return;
		}
		if (mErrorView == null) {
			ViewStub errorViewStub = (ViewStub) contentView.findViewById(R.id.load_error_view);
			mErrorView = errorViewStub.inflate();
		}
//		mErrorView = contentView.findViewById(R.id.load_error_view);
		TextView refresh = (TextView) mErrorView.findViewById(R.id.error_refresh);
		refresh.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				isErrorViewShow = true;
				mPresenter.requestProduct();
				showLoad(false);
			}
		});
		mErrorView.setVisibility(View.VISIBLE);
		xRefreshView.setVisibility(View.GONE);

		View telView = contentView.findViewById(R.id.error_tel);
		telView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				telephoneDialog();
			}
		});
	}

	@Override
	public ProductPresenterImpl getPresenter() {
		return new ProductPresenterImpl();
	}

	@Override
	public void hideProgress() {
		super.hideProgress();
		xRefreshView.stopRefresh();
	}

	/**
	 * 隐藏网络错误的view
	 */
	public void hideErrorPage() {
		if (isErrorViewShow == false) {
			return;
		}
		mErrorView.setVisibility(View.GONE);
		xRefreshView.setVisibility(View.VISIBLE);
	}

	//设置条目点击事件
	@Override
	public void setClickListener(ListView listView, final List<ProductBean> productBeen) {
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				setUmEvent(UmEvent.Gsy_investment_proList);
				Intent intent = new Intent(getActivity(), WebActivityShow.class);
				intent.putExtra("url", Urls.URL_HOST_PRODUCT + productBeen.get(position).getUrl());
				intent.putExtra("pro_type", productBeen.get(position).getType());
				startActivity(intent);
			}
		});
	}


	@Override
	public void afterGetProduct(List<ProductBean> data) {
		ArrayList<String> list = new ArrayList<>();
		//初始化 标题对应下的产品列表
		mProductMap = initProductMap(data, list);
		mRecyclerAdapter.updateData(mProductMap, list);
		hideErrorPage();
		LogUtils.e("RecommendFragment", "afterGetRecommendData:" + (System.currentTimeMillis() - mOldTime));
	}
}
