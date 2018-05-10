package com.gaoshoubang.util;

import android.content.Context;
import android.text.TextUtils;

import com.gaoshoubang.C;
import com.gaoshoubang.R;

/**
 * Created by lzx on 2017/7/5.
 */

public class RiskAssessmentUtils {
	/*
	* 根据分数来返回类型
	* */
	public static String getAssessmentType(int value) {
		if (value > 0 && value <= 20) {
			return C.RiskAssessment.CONSERVATIVE;
		}
		else if (value > 20 && value <= 40) {
			return C.RiskAssessment.PRUDENT;
		}
		else if (value > 40 && value <= 60) {
			return C.RiskAssessment.GROWTH;
		}
		else if (value > 60 && value <= 80) {
			return C.RiskAssessment.AGGRESSIVE;
		}
		return null;
	}

	public static String getAssessmentDesc(String type, Context context) {
		if (TextUtils.isEmpty(type)) {
			return null;
		}
		switch (type) {
			case C.RiskAssessment.CONSERVATIVE:
				return context.getString(R.string.finish_risk_desc_conservative);
			case C.RiskAssessment.PRUDENT:
				return context.getString(R.string.finish_risk_desc_prudent);
			case C.RiskAssessment.GROWTH:
				return context.getString(R.string.finish_risk_desc_growth);
			case C.RiskAssessment.AGGRESSIVE:
				return context.getString(R.string.finish_risk_desc_aggressive);
		}
		return null;
	}
}
