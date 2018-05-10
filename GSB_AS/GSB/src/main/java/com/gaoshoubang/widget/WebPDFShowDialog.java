package com.gaoshoubang.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.util.CommonUtils;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.downloader.DownloadThread;
import com.gaoshoubang.util.downloader.FileDownloader;
import com.gaoshoubang.util.downloader.FileDownloader.DownloadProgressListener;
import com.gaoshoubang.util.downloader.FileService;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 浏览器检测到pdf文件下载并预览
 */
public class WebPDFShowDialog extends Dialog implements View.OnClickListener {
	protected final String TAG = "WebPDFShowDialog";

	private Activity activity;
	private String pdfUrl;

	private TextView pdfName;
	private RelativeLayout progressRelative;
	private NumberProgressBar progressBar;
	private TextView incrementSize;
	private TextView totalSize;
	private RelativeLayout cancel;
	private RelativeLayout define;
	private TextView cancelText;
	private TextView defineText;

	/**
	 * 文件夹保存路径
	 */
	private File downloadPath;
	/**
	 * 文件名
	 */
	private String downName;
	/**
	 * 临时名字
	 */
	private String downTempName;
	/**
	 * 新安装包的大小
	 */
	private long pdfSize;
	private DecimalFormat decimalFormat = new DecimalFormat("#.##");

	private DownloadTask task;
	private boolean isDowning = false;

	public WebPDFShowDialog(Activity activity, String pdfUrl) {
		super(activity, R.style.Dialog);
		setContentView(R.layout.dialog_web_pdf_show);
		this.activity = activity;
		this.pdfUrl = pdfUrl;

		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams lay = getWindow().getAttributes();
		lay.width = dm.widthPixels / 12 * 10;

		initView();
		initData();
	}

	private void initView() {
		pdfName = (TextView) findViewById(R.id.pdf_name);
		progressRelative = (RelativeLayout) findViewById(R.id.pdf_progress_rl);
		progressBar = (NumberProgressBar) findViewById(R.id.pdf_progress_bar);
		incrementSize = (TextView) findViewById(R.id.pdf_increment_size);
		totalSize = (TextView) findViewById(R.id.pdf_total_size);
		cancel = (RelativeLayout) findViewById(R.id.pdf_cancel_rl);
		define = (RelativeLayout) findViewById(R.id.pdf_define_rl);
		cancelText = (TextView) findViewById(R.id.pdf_cancel);
		defineText = (TextView) findViewById(R.id.pdf_define);

		cancel.setOnClickListener(this);
		define.setOnClickListener(this);

		setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				exit();
			}
		});

		progressBar.setMax(100);
	}

	private void initData() {
		if (pdfUrl.lastIndexOf('/') != -1) {
			downName = pdfUrl.substring(pdfUrl.lastIndexOf('/') + 1, pdfUrl.length());
		}
		downTempName = "temp" + downName;

		pdfName.setText("文件名: " + downName);
		downloadPath = new File(FilesPath.path + "/");

		if (checkDownlaod() > 0) {
			downlaodState(1);
		}
		// 已完成下载
		if (fileIsExists()) {
			downlaodState(3);
		}

		task = new DownloadTask(pdfUrl, downloadPath, true);
		new Thread(task).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pdf_cancel_rl:
				if (fileIsExists()) {
					deleteFile();
					downlaodState(0);
					download(pdfUrl, downloadPath);
					return;
				}
				dismiss();
				break;

			case R.id.pdf_define_rl:
				if (fileIsExists()) {
					getPdfFileIntent(downloadPath + "/" + downName);
					return;
				}
				if (isDowning) {
					downlaodState(2);
					return;
				}
				download(pdfUrl, downloadPath);
				break;
		}
	}

	/**
	 * 按钮状态
	 *
	 * @param state 0.正在下载 1.继续下载 2.停止下载 3.下载完成
	 */
	private void downlaodState(int state) {
		switch (state) {
			case 0:// 正在下载
				progressRelative.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.GONE);
				defineText.setText("正在下载");
				defineText.setTextColor(Color.parseColor("#da4415"));
				defineText.setBackgroundResource(R.drawable.dialog_cancel_btn_selector);
				break;

			case 1:// 继续下载
				progressRelative.setVisibility(View.VISIBLE);
				cancel.setVisibility(View.GONE);
				defineText.setText("继续下载");
				defineText.setTextColor(Color.parseColor("#da4415"));
				defineText.setBackgroundResource(R.drawable.dialog_cancel_btn_selector);
				progressBar.setProgress((int) ((float) checkDownlaod() / pdfSize * 100));
				incrementSize.setText(decimalFormat.format(checkDownlaod() / (1024.0 * 1024.0)) + "MB");
				break;

			case 2:// 停止下载
				isDowning = false;
				defineText.setText("继续下载");
				exit();
				break;

			case 3:// 下载完成
				progressRelative.setVisibility(View.GONE);
				cancel.setVisibility(View.VISIBLE);
				cancelText.setText("重新下载");
				defineText.setText("马上打开");
				defineText.setTextColor(Color.parseColor("#ffffff"));
				defineText.setBackgroundResource(R.drawable.btn_selector);
				break;
		}
	}

	/**
	 * 开始下载
	 *
	 * @param path
	 * @param savDir
	 */
	private void download(final String path, final File savDir) {
		if (!CommonUtils.isNetworkConnected(activity)) {
			ToastUtil.toast(activity, "网络连接不可用，请检查网络设置");
			return;
		}
		if (!isWifiConnected(activity)) {
			final PromptDialog promptDialog = new PromptDialog(activity);
			promptDialog.setContentText("提示", "当前未连接WiFi是否继续下载");
			promptDialog.setContentButton("任性一回", "取消");
			promptDialog.setDefineOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					promptDialog.dismiss();
					downlaodState(0);

					task = new DownloadTask(path, savDir);
					new Thread(task).start();
				}
			});
			promptDialog.show();
			return;
		}

		downlaodState(0);

		task = new DownloadTask(path, savDir);
		new Thread(task).start();
	}

	/**
	 * 停止下载
	 */
	private void exit() {
		if (task != null) {
			task.exit();
		}
	}

	// 检测已下载的长度
	private int checkDownlaod() {
		/* 已下载文件长度 */
		int downloadSize = 0;
		DownloadThread[] threads;
		/* 缓存各线程下载的长度 */
		Map<Integer, Integer> data = new ConcurrentHashMap<Integer, Integer>();
		FileService fileService = new FileService(activity);
		/* 线程数 */
		threads = new DownloadThread[2];// 实例化线程数组
		Map<Integer, Integer> logdata = fileService.getData(pdfUrl);// 获取下载记录
		if (logdata.size() > 0) {// 如果存在下载记录
			for (Map.Entry<Integer, Integer> entry : logdata.entrySet())
				data.put(entry.getKey(), entry.getValue());// 把各条线程已经下载的数据长度放入data中
		}
		if (data.size() == threads.length) {
			// 下面计算所有线程已经下载的数据总长度
			for (int i = 0; i < threads.length; i++) {
				downloadSize += data.get(i + 1);
			}
		}
		Log.i("-----------------------", "已经下载的长度" + downloadSize);
		return downloadSize;
	}

	private class DownloadTask implements Runnable {
		private String path;
		private File saveDir;
		private boolean isGetSize;
		private FileDownloader loader;

		public DownloadTask(String path, File saveDir) {
			this.path = path;
			this.saveDir = saveDir;
		}

		public DownloadTask(String path, File saveDir, boolean isGetSize) {
			this.path = path;
			this.saveDir = saveDir;
			this.isGetSize = isGetSize;
		}

		public void exit() {
			if (loader != null) {
				loader.exit();
			}
		}

		DownloadProgressListener downloadProgressListener = new DownloadProgressListener() {
			@Override
			public void onDownloadSize(int size) {
				Message msg = new Message();
				msg.what = PROCESSING;
				msg.getData().putInt("size", size);
				handler.sendMessage(msg);
			}
		};

		public void run() {
			try {
				loader = new FileDownloader(activity, path, saveDir, downTempName, 2);// 实例化一个文件下载器
				pdfSize = loader.getFileSize();
				if (isGetSize) {
					handler.sendMessage(handler.obtainMessage(GETFILESIZE));
					return;
				}
				handler.sendMessage(handler.obtainMessage(REFRESH));
				loader.download(downloadProgressListener);
			} catch (Exception e) {
				handler.sendMessage(handler.obtainMessage(FAILURE));
				e.printStackTrace();
			}
		}

	}

	private static final int PROCESSING = 0;
	private static final int REFRESH = 1;
	private static final int FAILURE = 2;
	private static final int GETFILESIZE = 3;
	private Handler handler = new UIHandler();

	private class UIHandler extends Handler {
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case PROCESSING: // 更新进度
					Log.i(TAG, "msg.getData():" + msg.getData().getInt("size"));
					incrementSize.setText(decimalFormat.format(msg.getData().getInt("size") / (1024.0 * 1024.0)) + "MB");
					float num = (float) msg.getData().getInt("size") / pdfSize;
					int result = (int) (num * 100);
					progressBar.setProgress(result);

					if (msg.getData().getInt("size") == pdfSize) {
						downlaodState(3);
						File oleFile = new File(downloadPath, downTempName);
						File newFile = new File(downloadPath, downName);
						oleFile.renameTo(newFile); // 重命名
						getPdfFileIntent(downloadPath + "/" + downName);
					}

					break;
				case REFRESH: // 刷新,进入下载状态
					isDowning = true;
					downlaodState(0);
					totalSize.setText("/" + decimalFormat.format(pdfSize / (1024.0 * 1024.0)) + "MB");
					break;

				case FAILURE: // 下载失败
					exit();
					isDowning = false;
					defineText.setText("重新下载");
					ToastUtil.toast(activity, "下载失败,请检查网络连接");
					break;

				case GETFILESIZE:// 获取文件大小改变状态
					totalSize.setText("/" + decimalFormat.format(pdfSize / (1024.0 * 1024.0)) + "MB");
					if (checkDownlaod() > 0) {
						downlaodState(1);
					}
					break;
			}
		}
	}

	// 是否连接WIFI
	public boolean isWifiConnected(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifiNetworkInfo.isConnected();
	}

	/**
	 * 检测文件是否存在
	 */
	private boolean fileIsExists() {
		try {
			File f = new File(downloadPath, downName);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 删除文件
	 */
	public void deleteFile() {
		File file = new File(downloadPath, downName);
		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			}
		}
	}

	// 跳转pdf打开
	private void getPdfFileIntent(String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri = Uri.fromFile(new File(path));
		intent.setDataAndType(uri, "application/pdf");
		activity.startActivity(intent);
	}
}
