package com.gaoshoubang.fingerprint;

import android.app.Activity;

import com.gaoshoubang.fingerprint.base.BaseFingerprint;
import com.gaoshoubang.fingerprint.impl.AndroidFingerprint;
import com.gaoshoubang.fingerprint.impl.MeiZuFingerprint;
import com.gaoshoubang.fingerprint.impl.SamsungFingerprint;


public class FingerprintIdentify {

	private BaseFingerprint mFingerprint;
	private BaseFingerprint mSubFingerprint;

	public FingerprintIdentify(Activity activity) {
		this(activity, null);
	}

	public FingerprintIdentify(Activity activity, BaseFingerprint.FingerprintIdentifyExceptionListener exceptionListener) {
		AndroidFingerprint androidFingerprint = new AndroidFingerprint(activity, exceptionListener);
		if (androidFingerprint.isHardwareEnable()) {
			mSubFingerprint = androidFingerprint;
			if (androidFingerprint.isRegisteredFingerprint()) {
				mFingerprint = androidFingerprint;
				return;
			}
		}

		SamsungFingerprint samsungFingerprint = new SamsungFingerprint(activity, exceptionListener);
		if (samsungFingerprint.isHardwareEnable()) {
			mSubFingerprint = samsungFingerprint;
			if (samsungFingerprint.isRegisteredFingerprint()) {
				mFingerprint = samsungFingerprint;
				return;
			}
		}

		MeiZuFingerprint meiZuFingerprint = new MeiZuFingerprint(activity, exceptionListener);
		if (meiZuFingerprint.isHardwareEnable()) {
			mSubFingerprint = meiZuFingerprint;
			if (meiZuFingerprint.isRegisteredFingerprint()) {
				mFingerprint = meiZuFingerprint;
			}
		}
	}

	public void startIdentify(int maxAvailableTimes, BaseFingerprint.FingerprintIdentifyListener listener) {
		if (!isFingerprintEnable()) {
			return;
		}

		mFingerprint.startIdentify(maxAvailableTimes, listener);
	}

	public void cancelIdentify() {
		if (mFingerprint != null) {
			mFingerprint.cancelIdentify();
		}
	}

	public void resumeIdentify() {
		if (!isFingerprintEnable()) {
			return;
		}

		mFingerprint.resumeIdentify();
	}

	public boolean isFingerprintEnable() {
		return mFingerprint != null && mFingerprint.isEnable();
	}

	public boolean isHardwareEnable() {
		return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isHardwareEnable());
	}

	public boolean isRegisteredFingerprint() {
		return isFingerprintEnable() || (mSubFingerprint != null && mSubFingerprint.isRegisteredFingerprint());
	}
}