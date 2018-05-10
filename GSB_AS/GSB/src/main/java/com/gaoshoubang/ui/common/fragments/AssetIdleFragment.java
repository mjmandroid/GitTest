package com.gaoshoubang.ui.common.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.AssetBean;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.base.fragments.SimpleBaseFragment;
import com.gaoshoubang.util.UmEvent;

import java.text.DecimalFormat;

/**
 * 闲置资金
 *
 * @author Cisco
 */
public class AssetIdleFragment extends SimpleBaseFragment implements OnClickListener {

	private TextView money;
	private TextView idleOut;
	private TextView idleRecharge;
	private Intent intent;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle bundle) {
		if (view != null) {
			money = (TextView) view.findViewById(R.id.idle_money);
			idleOut = (TextView) view.findViewById(R.id.idle_out);
			idleRecharge = (TextView) view.findViewById(R.id.idle_recharge);

			idleOut.setOnClickListener(this);
			idleRecharge.setOnClickListener(this);
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_asset_idle;
	}

	@Override
	protected void initView() {

	}

	@Override
	protected void initEvent() {

	}

	@Override
	public void onDestroyView() {
		((ViewGroup) contentView.getParent()).removeView(contentView);
		super.onDestroyView();
	}

	public void initData(AssetBean assetBean) {
		if (assetBean == null) {
			return;
		}
		DecimalFormat df = new DecimalFormat("#0.00");
		money.setText("" + df.format(assetBean.getTwo().getBal()));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.idle_out:
				setUmEvent(UmEvent.Gsy_userMoney_tixian);
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						intent = new Intent(getActivity(), WebActivityShow.class);
						intent.putExtra("url", mCnfListBean.getWD_URL());
						intent.putExtra("type", WebActivityShow.TYPE_WD);
						startActivity(intent);
					}

					public void onFail() {
						return;
					}
				});
				break;

			case R.id.idle_recharge:
				setUmEvent(UmEvent.Gsy_userMoney_chongzhi);
				ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
					@Override
					public void success(CnfListBean mCnfListBean) {
						intent = new Intent(getActivity(), WebActivityShow.class);
						intent.putExtra("url", mCnfListBean.getRCHG_URL());
						intent.putExtra("type", WebActivityShow.TYPE_RECHARGE);
						intent.putExtra("extra", "&flag=2");
						startActivity(intent);
					}

					public void onFail() {
						return;
					}
				});
				break;
		}
	}
}