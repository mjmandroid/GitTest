package com.gaoshoubang.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.gaoshoubang.net.ConfigUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 退出 清理用户数据
 *
 * @author Cisco
 */
public class ClearUtils {

	/**
	 * 退出清除
	 *
	 * @param context
	 */
	@SuppressLint("NewApi")
	public static void ClearUserInfo(Context context) {
		try {
			ConfigUtils.getInstance().clearLoginInfo();
			//  兼容低版本api
//			CookieManager.getInstance().removeAllCookie();// 清除cookie
			clearCookies(context);
			DataCleanManager.cleanCustomCache(FilesPath.path);// 清除缓存文件
			ImageLoader.getInstance().clearDiskCache();
			ImageLoader.getInstance().clearMemoryCache();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void clearCookies(Context context) {
		CookieSyncManager cookieSyncMngr =
				CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

}
