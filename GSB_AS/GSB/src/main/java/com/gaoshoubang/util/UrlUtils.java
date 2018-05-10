package com.gaoshoubang.util;

import android.app.Activity;

import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.Urls;

/**
 * 处理一些链接操作的帮助类
 *
 * @author ZhuangHaojie
 */
public class UrlUtils {

	public static boolean isFinish(Activity activity, String url) {

		if (url == null) {
			return true;
		}

		boolean isFinish = false;

		/**
		 * 提现
		 */
		if (ConfigUtils.getInstance().getCnfInfo() != null) {
			if (url.indexOf(ConfigUtils.getInstance().getCnfInfo().getWD_RESULT_URL()) != -1) {
				isFinish = true;
				return isFinish;
			}
		}

		if (url.indexOf(Urls.WITHDRAW_APPLY_INTERCEPTION) != -1) {
			isFinish = true;
			return isFinish;
		}


		if (url.indexOf(Urls.RECHARGE_INTERCEPTION) != -1) {
			isFinish = true;
			return isFinish;
		}

		/**
		 * 投资
		 */
		if (ConfigUtils.getInstance().getCnfInfo() != null) {
			if (url.indexOf(ConfigUtils.getInstance().getCnfInfo().getINV_RESULT_URL()) != -1) {
				isFinish = true;
				return isFinish;
			}
		}

		if (url.indexOf(Urls.PROJECT_TRANFER_INTERCEPTION) != -1) {
			isFinish = true;
			return isFinish;
		}

		/**
		 * 绑卡
		 */
		if (ConfigUtils.getInstance().getCnfInfo() != null) {
			if (url.indexOf(ConfigUtils.getInstance().getCnfInfo().getUMP_BIND_BANK_CARD_RESULT_URL()) != -1) {
				isFinish = true;
				return isFinish;
			}
		}

		return isFinish;
	}

}
