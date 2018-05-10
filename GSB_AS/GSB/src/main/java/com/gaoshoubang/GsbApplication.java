package com.gaoshoubang;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.StringCallback;
import com.gaoshoubang.service.LogService;
import com.gaoshoubang.receiver.PermanentBroadcastReceiver;
import com.gaoshoubang.util.FilesPath;
import com.gaoshoubang.util.LockUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.AnalyticsConfig;

import java.io.File;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import okhttp3.Call;
import okhttp3.Response;

/**
 * @author Cisco
 */
public class GsbApplication extends Application {
	protected static final String TAG = "GsbApplication";
	public static GsbApplication gsbApplication;
	/**
	 * 是否APP在前台
	 */
	public static boolean isActive = true;

	public static GsbApplication getGsbApplication() {
		return gsbApplication;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		gsbApplication = this;
		NetworkManager.getInstance(); //初始化网络模块
		LockUtils.getInstance();//初始化解锁模块

		initReceiver();
		initImageLoader(this);
		initJPush();

		requestAppConfig();

		if (C.Config.isLogs) {
			startService(new Intent(this, LogService.class));
		}
//        com.wanjian.sak.LayoutManager.init(this);

//        SAK.unInstall(this);
	}

	private void initJPush() {
		JPushInterface.init(this);
		if (!TextUtils.isEmpty(GsbApplication.getUserId())) {
			JPushInterface.setAlias(this, GsbApplication.getUserId(), new TagAliasCallback() {
				@Override
				public void gotResult(int i, String s, Set<String> set) {
					if (i == 0) {
						Log.i("zhj", "jpush success");
					}
				}
			});
		}
	}

	// 初始化ImageLoader
	private void initImageLoader(Context context) {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.denyCacheImageMultipleSizesInMemory();// 缓存大图（不缩放）的文件在内存中
		config.threadPriority(Thread.NORM_PRIORITY - 2);// 线程优先级
		config.threadPoolSize(4);// 开启4个线程
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());// 缓存的文件名用Md5加密
		config.diskCacheSize(50 * 1024 * 1024); // 文件缓存 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		// config.writeDebugLogs();
		// config.memoryCache(new LruMemoryCache(50 * 1024 * 1024));
		config.memoryCache(new WeakMemoryCache());
		if (null != getExternalCacheDir()) {
			config.diskCache(new UnlimitedDiskCache(this.getExternalCacheDir())); // default
		}
		else {
			config.diskCache(new UnlimitedDiskCache(new File(FilesPath.path)));//文件缓存
		}
		// Log.i(TAG, this.getCacheDir().toString());
		ImageLoader.getInstance().init(config.build());
	}

	/**
	 * 获取App安装包信息
	 *
	 * @return
	 */
	public PackageInfo getPackageInfo() {
		PackageInfo info = null;
		try {
			info = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace(System.err);
		}
		if (info == null) {
			info = new PackageInfo();
		}
		return info;
	}

	public String getIMEI() {
		TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
		return tm.getDeviceId();

	}

	/**
	 * 返回用户ID 未登录时返回空
	 *
	 * @return
	 */
	public static String getUserId() {
		return ConfigUtils.getInstance().getUid();
	}

	// 友盟渠道号
	public String getChannelCode() {
		if (gsbApplication == null) {
			return null;
		}
		return AnalyticsConfig.getChannel(this);
	}

	// 配置信息
	private void requestAppConfig() {
		// TODO: 2017/6/6 appconfig
		NetworkManager.get(Urls.ACTION_APPCONFIG).execute(new StringCallback() {
			@Override
			public void onSuccess(String s, Call call, Response response) {
				ConfigUtils.getInstance().setCnfInfo(s);
			}

			@Override
			public void onError(Call call, Response response, Exception e) {
				Log.i(TAG, "配置信息获取失败");
			}
		});


	}

	/**
	 * 复写这两个方法,系统改变字体时回调,将字体缩放比例设置为默认.
	 *
	 * @des 字体不会随系统改变
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (newConfig.fontScale != 1)//非默认值
		{
			getResources();
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public Resources getResources() {
		Resources res = super.getResources();
		if (res.getConfiguration().fontScale != 1) {//非默认值
			Configuration newConfig = new Configuration();
			newConfig.setToDefaults();//设置默认
			res.updateConfiguration(newConfig, res.getDisplayMetrics());
		}
		return res;
	}

	private void initReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_TIME_TICK);
		PermanentBroadcastReceiver receiver = new PermanentBroadcastReceiver();
		registerReceiver(receiver, filter);
	}

}
