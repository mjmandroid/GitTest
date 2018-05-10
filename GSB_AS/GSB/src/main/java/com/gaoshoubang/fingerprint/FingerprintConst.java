package com.gaoshoubang.fingerprint;

/**
 * Created by Administrator on 2017/5/22.
 */

public interface FingerprintConst {
	String FINGERPRINT_TYPE = "fingerprintType";
	int FINGERPRINT_HARDWARE_ENABLE = 1;//指纹可用
	int FINGERPRINT_HARDWARE_DISABLE = 2;//指纹不可用;
	int FINGERPRINT_OPENING = 3;   //指纹开启中
	int FINGERPRINT_VERIFICATION_SUC = 4;//指纹验证成功
	int FINGERPRINT_SETTING_VERIFICATION_ClOSE = 5; //关闭指纹,需要进行验证状态.
	int FINGERPRINT_SETTING_VERIFICATION_OPEN = 6; //开启指纹,需要进行验证状态.
	int FINGERPRINT_NOT_SHOW = 7;
}
