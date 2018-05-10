package com.gaoshoubang.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gaoshoubang.R;

public class ToastUtil {

	private static Toast toast;
	private static LinearLayout linearLayout;
	private static TextView textView;

	/**
	 * @param content
	 */
	public static void toast(Context context, String content) {
		if (TextUtils.isEmpty(content)) {
			return;
		}
		if (toast == null) {
			toast = new Toast(context);
		}
		Log.i("toast", content);
		toast.setView(createToastView(context, content));
		toast.show();
	}

	private static View createToastView(Context context, String content) {
		if (linearLayout == null) {
			linearLayout = new LinearLayout(context);
			linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			int padding = DisplayUtil.dip2px(context, 8);
			linearLayout.setPadding(padding, padding, padding, padding);
			linearLayout.setBackgroundResource(R.drawable.shape_toast_bg);

			textView = new TextView(context);
			linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			textView.setTextSize(12.0f);
			textView.setTextColor(Color.parseColor("#FFFFFF"));
			textView.setText(content);
			linearLayout.addView(textView);
		}
		else {
			textView.setText(content);
		}
		return linearLayout;

	}

	public static void cancelToast() {
		toast.cancel();
	}
}
