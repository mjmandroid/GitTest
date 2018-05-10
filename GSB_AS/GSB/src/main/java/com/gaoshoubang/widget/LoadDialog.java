package com.gaoshoubang.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager.LayoutParams;

import com.gaoshoubang.R;

/**
 * 加载dialog
 */
public class LoadDialog extends Dialog {
	private Context context;
	private boolean isShow = true;
	private View progressContainer;

	public LoadDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		setContentView(R.layout.dialog_progressbar);
		this.context = context;
		progressContainer = findViewById(R.id.progress_container);

		LayoutParams lay = getWindow().getAttributes();
		setParams(lay);
		setCancelable(false);
		setCanceledOnTouchOutside(false);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (isShowing()) {
					setCancelable(true);
					setCanceledOnTouchOutside(true);
					progressContainer.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dismissLoad();
						}
					});
				}
			}
		}, 5000);

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				isShow = false;
			}
		});
	}

	private void setParams(LayoutParams lay) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		Rect rect = new Rect();
		View view = getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.height = dm.heightPixels - rect.top;
		lay.width = dm.widthPixels;
	}

	/**
	 * 等待一会才显示
	 */
	public void showLoad() {
		isShow = true;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isShowing() && isShow) {
					show();
				}
			}
		}, 1300);
	}

	public void dismissLoad() {
		isShow = false;
		if (isShowing()) {
			dismiss();
		}
	}
}
