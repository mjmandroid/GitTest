package com.gaoshoubang.fingerprint;

import com.gaoshoubang.util.UserSharedPreferenceUtils;

/**
 * Created by Administrator on 2017/5/24.
 */

public class FingerprintUtils {
	public static void initFingerprintStatus(FingerprintIdentify fingerprintIdentify, String uid) {
		int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(uid);
		if (fingerprintType == -1) {//指纹配置文件没初始化
			if (!fingerprintIdentify.isHardwareEnable()) {
				UserSharedPreferenceUtils.setFingerprintType(uid, 2);
			}
			else {
				UserSharedPreferenceUtils.setFingerprintType(uid, 1);
			}
		}
	}
}
