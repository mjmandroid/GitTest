package com.gaoshoubang.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.gaoshoubang.R;

public class ShareDialogUtils extends Dialog {
	public ShareDialogUtils(Context context) {
		super(context, 0);
	}

	public ShareDialogUtils(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;

		private ShareDialogUtils dialog;

		private View.OnClickListener onClickListener;

		public Builder(Context context) {
			this.context = context;
		}

		public void setClickListener(View.OnClickListener onClickListener) {
			this.onClickListener = onClickListener;
		}

		public ShareDialogUtils create() {
			LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog = new ShareDialogUtils(this.context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_share, null);
			dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			Window win = dialog.getWindow();
			win.getDecorView().setPadding(0, 0, 0, 0);
			WindowManager.LayoutParams lp = win.getAttributes();
			lp.width = WindowManager.LayoutParams.MATCH_PARENT;
			lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
			win.setAttributes(lp);

			if (onClickListener != null) {
				layout.findViewById(R.id.ll_share_qq).setOnClickListener(onClickListener);
				layout.findViewById(R.id.ll_share_sina).setOnClickListener(onClickListener);
				layout.findViewById(R.id.ll_share_sms).setOnClickListener(onClickListener);
				layout.findViewById(R.id.ll_share_url).setOnClickListener(onClickListener);
				layout.findViewById(R.id.ll_share_weixin).setOnClickListener(onClickListener);
				layout.findViewById(R.id.ll_share_weixin_pyq).setOnClickListener(onClickListener);
			}

			layout.findViewById(R.id.tv_dismiss).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			return dialog;
		}

		public void dismiss() {
			if (dialog != null) {
				dialog.dismiss();
			}
		}

	}

}
