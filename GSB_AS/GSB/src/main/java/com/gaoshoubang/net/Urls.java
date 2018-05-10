package com.gaoshoubang.net;


/**
 * Created by lzx on 2017/6/5.
 */

public interface Urls {
	/**
	 * http://dev-api.gaosouyi.com/
	 * http://gsb2.gaosouyi.com/Api_v4/ 正式环境
	 * http://test.gsb2.gaosouyi.com/Api_v5/ 测试环境
	 * http://dev-gsb.gaosouyi.com/Api_v5/ 开发环境
	 * http://testing-gsb.gaosouyi.com/Api_v5/测试
	 * http://beta-gsb.gaosouyi.com/Api_v5/测试
	 * <p>
	 * https://test-gsb2.gaosouyi.com/Api_v5/
	 * https://test-gsb2.gaosouyi.com
	 */

	/**
	 * https://gsb2.gaosouyi.com/Api_v5/  正式环境
	 * https://test-gsb2.gaosouyi.com/Api_v5/  测试环境
	 * https://dev-gsb2.gaosouyi.com/Api_v5/  开发环境
	 */
//	String URL_HOST = "https://gsb2.gaosouyi.com/Api_v5/";
//	String URL_HOST_PRODUCT = "https://gsb2.gaosouyi.com/";

	String URL_HOST = "https://test-gsb2.gaosouyi.com/Api_v5/"; //测试环境
	String URL_HOST_PRODUCT = "https://test-gsb2.gaosouyi.com/";
	//开发环境
//	String URL_HOST = "https://dev-gsb2.gaosouyi.com/Api_v5/";
//	String URL_HOST_PRODUCT = "https://dev-gsb2.gaosouyi.com/";


//	String URL_HOST = "https://gsb2.gaosouyi.com/Api_v5/";
//	String URL_HOST_PRODUCT = "https://gsb2.gaosouyi.com";
//
//	 	 String URL_HOST = "http://dev-gsb.gaosouyi.com/Api_v5/";
//		 String URL_HOST_PRODUCT = "http://dev.gsb.gaosouyi.com";

//    	 String URL_HOST = "https://test-gsb2.gaosouyi.com/Api_v5";
//	     String URL_HOST_PRODUCT = "https://test-gsb2.gaosouyi.com";

	// String URL_HOST = "https://test-app.gaosouyi.com/Api_v5";
	// String URL_HOST_PRODUCT = "https://test-gsb2.gaosouyi.com";
	/*	 String URL_HOST = "https://beta-gsb2.gaosouyi.com/Api_v5";
	     String URL_HOST_PRODUCT = "https://beta-gsb2.gaosouyi.com";*/
	/**
	 * 是否打印保存全局监听logs
	 */
	boolean isLogs = false;
	/**
	 * App标识
	 */
	int APPID = 4002;
	/**
	 * 前缀
	 */
	String PREFIX = "gsb2";
	/**
	 * 联动优势URL
	 */
	String PAY_URL = "pay.soopay.net";
	/**
	 * 测试的联动优势URL
	 */
	String TEST_PAY_URL = "114.113.159.203:9200";
	/**
	 * 请求
	 */
	String REQUSET_PAY_URL = "P2PApiLd";
	/**
	 * 登录拦截
	 */
	String LOGIN_FILTER = "Passport/login";

	String BACK_URL = "gaosouyi.com/Index/index.html";

	String AUTO_FILTER = "/Auto/index";
	/**
	 * 产品列表拦截
	 */
	String PRODUCT_TRANFER_INTERCEPTION = "/Account/complete";
	/**
	 * 提现拦截
	 */
	String WITHDRAW_APPLY_INTERCEPTION = "service=withdraw_apply_notify";
	/**
	 * 充值拦截
	 */
	String RECHARGE_INTERCEPTION = "service=recharge_notify";
	/**
	 * 投资拦截
	 */
	String PROJECT_TRANFER_INTERCEPTION = "service=project_tranfer_notify";
	/*风险测评拦截*/
	String RISK_ASSESSMENT_INTERCEPTION = "Evaluation/index";
	    /*ACTION =====================start===================================*/

	/*获取配置信息*/
	String ACTION_APPCONFIG = URL_HOST + "/App/getCnfList";
	/*获取分享信息*/
	String ACTION_SHARE_CONFIG = URL_HOST + "/Share/getShareCnfList";
	/*登录*/
	String ACTION_LOGIN = URL_HOST + "/Login/doLogin";
	/*注册*/
	String ACTION_REGISTER = URL_HOST + "/Reg/doReg";
	/*找回密码验证码*/
	String ACTION_PSW_MOBILE_VERIFY_CODE = URL_HOST + "/Passwd/getMobileVerifyCode";
	/*找回密码*/
	String ACTION_RESET_PSW = URL_HOST + "/Passwd/doResetPasswd";
	/*注册短信验证码*/
	String ACTION_REG_GET_MOBILE_CODE = URL_HOST + "/Reg/getMobileVerifyCode";
	/*校验验证码和手机号，注册*/
	String ACTION_REG_CHECK_MOBILE_CODE = URL_HOST + "/Reg/checkMobileVerifyCode";
	/*验证手机是否已注册*/
	String ACTION_CHECK_IS_REG = URL_HOST + "/Reg/doCheckMobile";
	/* 验证推荐码是否存在。推荐码是否存在。1：不存在；2：存在；*/
	String ACTION_CHECK_REFERRAL_CODE = URL_HOST + "/User/doAuthRcmderCode";
	/*检查新版本*/
	String ACTION_GET_LATEST_VERSION = URL_HOST + "/App/getLatestVer";
	/*推荐页面的数据*/
	String ACTION_RECOMMEND = URL_HOST + "/Rel/index";
	/*首页-(公告&活动)*/
	String ACTION_HOME_ACTIVITY_NOTICE = URL_HOST + "/GongHuo/info";
	/*关闭某条公告*/
	String ACTION_HOME_ACTIVITY_CLOSE_NOTICE = URL_HOST + "/GongHuo/close";
	/*理财产品列表*/
	String ACTION_PRODUCT = URL_HOST + "/Product/financing";
	/*发现页*/
	String ACTION_FIND = URL_HOST + "/Grade/self";
	/*我的*/
	String ACTION_MY = URL_HOST + "/User/getMyself";
	/*意见反馈*/
	String ACTION_FEEDBACK = URL_HOST + "/Problem/getCustomerBack";
	/*账户管理*/
	String ACTION_ACCOUNT_MANAGEMENT = URL_HOST + "/User/accountMment";
	/*修改密码*/
	String ACTION_MODIFY_PSW = URL_HOST + "/User/doSetPasswd";
	/*头像修改*/
	String ACTION_MODIFY_PORTRAIT = URL_HOST + "/User/doSetFace";
	/*设置昵称*/
	String ACTION_MODIFY_NICKNAME = URL_HOST + "/User/doSetNickname";
	/*设置QQ*/
	String ACTION_MODIFY_QQ = URL_HOST + "/User/doSetQq";
	/*获取邮箱验证码*/
	String ACTION_GET_EMAIL_CODE = URL_HOST + "/User/getEmailVerifyCode";
	/*设置邮箱*/
	String ACTION_MODIFY_EMAIL = URL_HOST + "/User/doSetEmail";
	/*设置性别*/
	String ACTION_MODIFY_GENDER = URL_HOST + "/User/doSetGender";
	/*签到*/
	String ACTION_SIGN_IN = URL_HOST + "/Grade/signIn";
	/*签到周期*/
	String ACTION_SIGN_CYCLE = URL_HOST + "/Grade/cycle";
	/*弹窗浮动信息*/
	String ACTION_POPUP_INFO = URL_HOST + "/Rel/iall";
	/*回款日历*/
	String ACTION_RETURNED_MONEY_CALENDER = URL_HOST + "/User/recMoneyDay";
	/*高手币兑换*/
	String ACTION_GSB_EXCHANGE = URL_HOST + "/Rank/show";
	/*常见问题*/
	String ACTION_COMMON_PROBLEM = URL_HOST + "/Help/show";
	/*消息*/
	String ACTION_MESSAGE = URL_HOST + "/My/message";
	/*资产总览*/
	String ACTION_ASSETS = URL_HOST + "/My/money";
	/*获取地址分类信息*/
	String ACTION_ADDRESS_CATALOGUE = URL_HOST + "/My/getArea";
	/*设置地址信息*/
	String ACTION_SET_ADDRESS = URL_HOST + "/My/setArea";
	/*媒体报道*/
	String ACTION_NEWS = URL_HOST + "/Message/media";
	/*交易记录*/
	String ACTION_TRANSACTION_RECORD = URL_HOST + "/My/payList";
	/*月月盈*/
	String ACTION_MONTH_PROFIT = URL_HOST + "/Product/yyy";
	/*ACTION =====================end===================================*/


	/* =====================风险测评===================================*/
	/*调查题目列表*/
	String ACTION_RISK_QUESTION_LIST = URL_HOST + "/Quesnaire/QuestionList";
	/*获取某用户指定调查项的答题记录*/
	String ACTION_RISK_ANSWER_LIST = URL_HOST + "/Quesnaire/userAnswer";
	/*批量保存用户答案*/
	String ACTION_RISK_SAVE_ANSWER = URL_HOST + "/Quesnaire/saveAnswer";


}