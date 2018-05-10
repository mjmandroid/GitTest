package com.gaoshoubang.net;

import android.content.Context;
import android.text.TextUtils;

import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.bean.ShareBean;
import com.gaoshoubang.bean.response.ShareListsResponse;
import com.gaoshoubang.net.callback.JsonCallback;
import com.gaoshoubang.util.CommonUtils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class ShareInfoUtil {
	private static final String TAG = "ShareInfoUtil";
	private Context context;
	private LoadDialog loadDialog;

	public ShareInfoUtil(Context context) {
		this.context = context;
	}

	/**
	 * 根据链接或类型匹配分享信息
	 *
	 * @param state
	 * @param onGetShareInfo
	 */
	public void getShareCnfContent(String state, OnGetShareInfo onGetShareInfo) {
		if (onGetShareInfo == null) {
			return;
		}
		requestGetShareCnfList(state, onGetShareInfo);
	}

	// 获取分享信息
	private void requestGetShareCnfList(final String typeState, final OnGetShareInfo onGetShareInfo) {
		loadDialog = new LoadDialog(context);
		loadDialog.show();
		// TODO: 2017/6/6 share
		NetworkManager.get(Urls.ACTION_SHARE_CONFIG).params("code", "").execute(new JsonCallback<ShareListsResponse>() {
			@Override
			public void onReceiveOtherErr(int code, String msg) {
				handleError(onGetShareInfo);
			}

			@Override
			public void onLoginMsgInvalidate() {
				handleError(onGetShareInfo);
			}

			@Override
			public void onSuccess(ShareListsResponse listGenericResponse, Call call, Response response) {
				handleSuccess(listGenericResponse.shareCnfList, onGetShareInfo, typeState);
			}

			@Override
			public void onError(Call call, Response response, Exception e) {
				handleError(onGetShareInfo);
			}
		});

	}

	private void handleSuccess(List<ShareBean> shareConfigs, OnGetShareInfo onGetShareInfo, String typeState) {
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
		if (onGetShareInfo == null || shareConfigs == null || TextUtils.isEmpty(typeState)) {
			return;
		}
		List<ShareBean> shareCnfList = shareConfigs;
		for (ShareBean bean : shareCnfList) {
			if (typeState.equals(bean.destUrl) || typeState.equals(bean.code)) {
				onGetShareInfo.success(bean);
				return;
			}
		}
	}

	private void handleError(OnGetShareInfo onGetShareInfo) {
//		HttpServer.checkLoadFailReason(context);
		CommonUtils.checkLoadFailReason(context);
		if (loadDialog != null && loadDialog.isShowing()) {
			loadDialog.dismiss();
		}
		if (onGetShareInfo != null) {
			onGetShareInfo.onFail();
		}
	}

	public interface OnGetShareInfo {
		void success(ShareBean shareCnf);

		void onFail();
	}
}
