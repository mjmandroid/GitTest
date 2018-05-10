package com.gaoshoubang.ui.information.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.net.Uri;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.ClipView;
import com.gaoshoubang.widget.CommonTitleBar;
import com.gaoshoubang.widget.CommonTitleBar.OnNextButtonListener;
import com.gaoshoubang.widget.LoadDialog;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.util.DisplayUtil;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @图片的放缩
 * @移动、截图
 */
public class ActivityClipPicture extends BaseActivity implements OnTouchListener {
	private static final String TAG = "ClipPictureActivity";

	private CommonTitleBar commonTitleBar;
	private ImageView srcPic;
	private ClipView clipview;

	// 移动和缩放图像
	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;

	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;

	private String imgUri;
	private String path;

	private LoadDialog loadDialog;


	@Override
	protected BasePresenterImpl getPresenter() {
		return null;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_clip_picture;
	}

	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		loadDialog = new LoadDialog(this);


		commonTitleBar = (CommonTitleBar) findViewById(R.id.clip_title);
		srcPic = (ImageView) this.findViewById(R.id.src_pic);
		srcPic.setOnTouchListener(this);

		commonTitleBar.setOnNextButtonListener(new OnNextButtonListener() {
			@Override
			public void onNext() {
				try {
					path = FilesPath.headPhoto;
					Bitmap fianBitmap = getBitmap();
					FileOutputStream out = new FileOutputStream(path);
					fianBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
					out.flush();
					out.close();
					Intent intent = new Intent();
					setResult(RESULT_OK, intent);
					finish();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
		});
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void loadData() {
		imgUri = getIntent().getStringExtra("imgUri");
		Uri data = getIntent().getData();
		Log.i(TAG, "imgUri:" + imgUri);
		if (imgUri != null && !imgUri.equals("")) {
			ImageLoader.getInstance().displayImage(imgUri, srcPic, new ImageLoaderUtils().defaultOptions(), new ImageLoadingListener() {
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					loadDialog.show();
				}

				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					loadDialog.dismiss();
				}

				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					loadDialog.dismiss();
				}

				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					loadDialog.dismiss();
				}
			});
		}
	}

	@Override
	protected void bindData() {


	}

	/* 多点触摸放大缩小，和单点移动图片的功能 */
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				savedMatrix.set(matrix);
				// 設置初始點位置
				start.set(event.getX(), event.getY());
				Log.d(TAG, "mode=DRAG");
				mode = DRAG;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				Log.d(TAG, "oldDist=" + oldDist);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
					Log.d(TAG, "mode=ZOOM");
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				Log.d(TAG, "mode=NONE");
				break;
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					// ...
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				}
				else if (mode == ZOOM) {
					float newDist = spacing(event);
					Log.d(TAG, "newDist=" + newDist);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
		}

		view.setImageMatrix(matrix);
		return true; // indicate event was handled
	}

	/**
	 * 确定前两个手指之间的距离
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * 计算中期前两个手指
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	/* 获取矩形区域内的截图 */
	private Bitmap getBitmap() {
		getBarHeight();
		Bitmap screenShoot = takeScreenShot();

		clipview = (ClipView) findViewById(R.id.clipview);
		int width = clipview.getWidth();
		int height = clipview.getHeight();
		Bitmap finalBitmap = Bitmap.createBitmap(screenShoot, (width - height / 2) / 2, height / 4 + titleBarHeight + statusBarHeight, height / 2, height / 2);
		return finalBitmap;
	}

	int statusBarHeight = 0;
	int titleBarHeight = 0;

	private void getBarHeight() {
		// 获取状态栏高度
		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		statusBarHeight = frame.top;
		// int contenttop =
		// this.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
		// statusBarHeight是上面所求的状态栏的高度
		titleBarHeight = DisplayUtil.dip2px(this, 48);

		Log.v(TAG, "statusBarHeight = " + statusBarHeight + ", titleBarHeight = " + titleBarHeight);
	}

	// 获取Activity的截屏
	private Bitmap takeScreenShot() {
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();
		return view.getDrawingCache();
	}

}