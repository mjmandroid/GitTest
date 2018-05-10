package com.gaoshoubang;

/**
 * Created by lzx on 2017/6/23.
 * 全局的常量类
 */

public interface C {

	/**
	 * 产品页的常量
	 */

	interface ProductConst {
		String NEW = "新手专享";
		String STABILITY = "固收投资";
		String FLEXIBLE = "灵活投资";
		int TYPE_NEW = 1;
		int TYPE_STABILITY = 2;
		int TYPE_FLEXIBLE = 3;
	}

	/**
	 * 配置的常量
	 */

	class Config {
		public static boolean isLogs = false;
		public static String APPID = "4002";

	}

	interface RiskAssessment {
		String CONSERVATIVE = "保守型";
		String PRUDENT = "稳健型";
		String GROWTH = "成长型";
		String AGGRESSIVE = "进取型";

		String TEST_RISK_ASSESSMENT = "TEST_RISK_ASSESSMENT";

	}
}
