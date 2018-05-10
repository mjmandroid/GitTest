package com.gaoshoubang.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.util.ToastUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 二维码显示
 */
public class QRCodeDialog extends Dialog implements View.OnClickListener {
	private Context context;
	private View qrRelative;
	private ImageView close;
	private TextView title;
	private TextView shareText;
	private ImageView qrImg;
	private TextView saveImgToCard;

	public QRCodeDialog(Context context) {
		super(context, R.style.CustomProgressDialog);
		setContentView(R.layout.dialog_qrcode);
		this.context = context;
		setCancelable(false);
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams lay = getWindow().getAttributes();
		lay.width = dm.widthPixels;
		lay.height = dm.heightPixels;

		initView();

	}

	private void initView() {
		qrRelative = findViewById(R.id.qr_relative);
		close = (ImageView) findViewById(R.id.qr_close);
		title = (TextView) findViewById(R.id.qr_title);
		shareText = (TextView) findViewById(R.id.qr_share_text);
		qrImg = (ImageView) findViewById(R.id.qr_img);
		saveImgToCard = (TextView) findViewById(R.id.qr_save);

		close.setOnClickListener(this);
		saveImgToCard.setOnClickListener(this);

		ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
			@Override
			public void success(CnfListBean mCnfListBean) {
				title.setText(Html.fromHtml("扫一扫送你" + "<font color='#f15353'>" + mCnfListBean.getREG_LABEL_SHARE() + "</font>"));
			}

			public void onFail() {
				return;
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.qr_close:
				dismiss();
				break;

			case R.id.qr_save:
				dismiss();
				close.setVisibility(View.GONE);
				shareText.setVisibility(View.VISIBLE);
				createViewBitmap(qrRelative);
				break;
		}
	}

	/**
	 * 显示二维码图片
	 */
	public void setQRImg(String qrPath) {
		ImageLoader.getInstance().displayImage("file://" + qrPath, qrImg);
	}

	// 将view生成图片
	private void createViewBitmap(View v) {
		Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		v.draw(canvas);

		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		saveBitmap(bitmap);
	}

	// 保存bitmap到根目录
	private void saveBitmap(Bitmap bitmap) {
		float x = 1080 / (float) bitmap.getWidth();
		Matrix matrix = new Matrix();
		matrix.postScale(x, x);
		Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

		String path = Environment.getExternalStorageDirectory().getAbsolutePath();
		File file = new File(path, "高搜易理财分享二维码.png");
		if (file.exists()) {
			file.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(file);
			resizeBmp.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			// 刷新图库
			MediaScannerConnection.scanFile(context, new String[]{file.toString()}, null, null);
			ToastUtil.toast(context, "已将二维码保存到SD卡根目录");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			ToastUtil.toast(context, "保存失败,请重试");
		} catch (IOException e) {
			e.printStackTrace();
			ToastUtil.toast(context, "保存失败,请重试");
		}
	}

}
