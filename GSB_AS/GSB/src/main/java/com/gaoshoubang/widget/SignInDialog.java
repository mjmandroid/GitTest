package com.gaoshoubang.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.SignInBean;
import com.gaoshoubang.bean.SignInCyclesBean;
import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.net.request.BaseRequest;
import com.gaoshoubang.receiver.BroadCastUtil;
import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.util.LockUtils;
import com.gaoshoubang.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 签到dialog
 *
 * @author Cisco
 */
public class SignInDialog extends Dialog {
	protected static final String TAG = "SignInDialog";

	private GridView gridView1;
	private GridView gridView2;
	private TextView signExit;

	private SignInAdapter signInAdapter1;
	private SignInAdapter signInAdapter2;
	private List<SignInCyclesBean> signInCyclesBean;
	private List<SignInCyclesBean> signInCyclesBean1;
	private List<SignInCyclesBean> signInCyclesBean2;

	private Activity mActivity;
	private LoadDialog loadDialog;

	public SignInDialog(Activity activity) {
		super(activity, R.style.Dialog);
		this.mActivity = activity;
		setContentView(R.layout.dialog_sign);
		setCancelable(false);
		getWindow().getAttributes().gravity = Gravity.CENTER;
		loadDialog = new LoadDialog(activity);
		initView();
		requestGradeCycle();
		show();
	}

	private void initView() {
		gridView1 = (GridView) findViewById(R.id.sign_grid_1);
		gridView2 = (GridView) findViewById(R.id.sign_grid_2);
		signExit = (TextView) findViewById(R.id.sign_finish);

		signInCyclesBean = new ArrayList<SignInCyclesBean>();
		signInCyclesBean1 = new ArrayList<SignInCyclesBean>();
		signInAdapter1 = new SignInAdapter(signInCyclesBean1, (Activity) mActivity);
		gridView1.setAdapter(signInAdapter1);

		signInCyclesBean2 = new ArrayList<SignInCyclesBean>();
		signInAdapter2 = new SignInAdapter(signInCyclesBean2, (Activity) mActivity);
		gridView2.setAdapter(signInAdapter2);

		signExit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
	}

	/**
	 * 签到
	 */
	private void requestGradeSignIn() {
		NetworkManager.get(Urls.ACTION_SIGN_IN).execute(new JsonCallback<DataResponse<SignInBean>>() {
			@Override
			public void onLoginMsgInvalidate() {
				LockUtils.autoEnterLockOrLogin(mActivity);
			}

			@Override
			public void onSuccess(DataResponse<SignInBean> signInBeanGenericResponse, Call call, Response response) {
				SignInBean bean = signInBeanGenericResponse.data;
				if (bean.getSign() == null) {
					return;
				}
				signInAdapter1.setSignPosition(bean.getWeek());
				signInAdapter1.notifyDataSetChanged();
				signInAdapter2.setSignPosition(bean.getWeek());
				signInAdapter2.notifyDataSetChanged();
				mActivity.sendBroadcast(new Intent(BroadCastUtil.ACTION_REFRESH_HOME).putExtra(BroadCastUtil.INTENT_ACTION_REFRESH_HOME, 2));
			}

			@Override
			public void onError(Call call, Response response, Exception e) {
//				HttpServer.checkLoadFailReason(mActivity);
				CommonUtils.checkLoadFailReason(mActivity);
			}

			@Override
			public void onReceiveOtherErr(int code, String msg) {
				ToastUtil.toast(mActivity, msg);
			}

			@Override
			public void onAfter(DataResponse<SignInBean> signInBeanGenericResponse, Exception e) {
				super.onAfter(signInBeanGenericResponse, e);
				loadDialog.dismiss();
			}

			@Override
			public void onBefore(BaseRequest baseRequest) {
				super.onBefore(baseRequest);
				loadDialog.show();
			}
		});
	}

	// 签到周期数据
	private void requestGradeCycle() {
		NetworkManager.get(Urls.ACTION_SIGN_CYCLE)
				.execute(new JsonCallback<DataResponse<List<SignInCyclesBean>>>() {
					@Override
					public void onLoginMsgInvalidate() {
						LockUtils.autoEnterLockOrLogin(mActivity);
					}

					@Override
					public void onSuccess(DataResponse<List<SignInCyclesBean>> listGenericResponse, Call call, Response response) {
						if (listGenericResponse.data == null || listGenericResponse.data.size() == 0) {
							return;
						}
						signInCyclesBean.addAll(listGenericResponse.data);
						if (signInCyclesBean.size() >= 7) {
							signInCyclesBean1.addAll(signInCyclesBean.subList(0, 4));
							signInCyclesBean2.addAll(signInCyclesBean.subList(4, 7));
							signInAdapter1.notifyDataSetChanged();
							signInAdapter2.notifyDataSetChanged();
							requestGradeSignIn();
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
//						HttpServer.checkLoadFailReason(mActivity);
						CommonUtils.checkLoadFailReason(mActivity);
					}

					@Override
					public void onBefore(BaseRequest baseRequest) {
						super.onBefore(baseRequest);
						loadDialog.show();
					}

					@Override
					public void onAfter(DataResponse<List<SignInCyclesBean>> listGenericResponse, Exception e) {
						super.onAfter(listGenericResponse, e);
						loadDialog.dismiss();

					}

					@Override
					public void onReceiveOtherErr(int code, String msg) {
						ToastUtil.toast(mActivity, msg);
					}
				});
	}

	public class SignInAdapter extends BaseAdapter {

		private Activity activity;
		private List<SignInCyclesBean> list;
		private int signPosition = -1;

		public SignInAdapter(List<SignInCyclesBean> list, Activity activity) {
			this.list = list;
			this.activity = activity;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public void setSignPosition(int signPosition) {
			this.signPosition = signPosition;
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.item_sign, null);
			}
			ViewHolder viewHolder = new ViewHolder(convertView);
			switch (list.get(position).getDay()) {
				case 1:
					viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_0);
					break;
				case 2:
					if (signPosition >= 2) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_1);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_1);
					}
					break;
				case 3:
					if (signPosition >= 3) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_2);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_2);
					}
					break;
				case 4:
					if (signPosition >= 4) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_3);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_3);
					}
					break;
				case 5:
					if (signPosition >= 5) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_4);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_4);
					}
					break;
				case 6:
					if (signPosition >= 6) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_5);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_5);
					}
					break;
				case 7:
					if (signPosition >= 7) {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_sel_6);
					}
					else {
						viewHolder.dayBg.setBackgroundResource(R.drawable.sign_dialog_nor_6);
					}
					break;
			}

			if (signPosition == list.get(position).getDay()) {
				viewHolder.dayBgSel.setVisibility(View.VISIBLE);
			}

			viewHolder.day.setText("第" + list.get(position).getDay() + "天");
			viewHolder.val.setText("高手币x" + list.get(position).getVal());

			return convertView;
		}

		class ViewHolder {
			ImageView dayBg;
			ImageView dayBgSel;
			TextView day;
			TextView val;

			public ViewHolder(View view) {
				dayBg = (ImageView) view.findViewById(R.id.sign_day_bg);
				dayBgSel = (ImageView) view.findViewById(R.id.sign_day_sel_bg);
				day = (TextView) view.findViewById(R.id.sign_day);
				val = (TextView) view.findViewById(R.id.sign_add_gaoshoubi);
			}
		}

	}

}
