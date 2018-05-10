package com.gaoshoubang.net;

/**
 * Created by lzx on 2017/6/6.
 *
 * @des 网络请求 参数的key的常量类
 */

public interface ParamsConstants {
	String SHARE_CODE = "code";
	/*login=====================start=================*/
	String MOBILE_NUM = "mobile";
	String PASSWORD = "passwd";
	String IMEI_CODE = "imei";

	/*TUSER========START=========*/
	String TUSER = "tuser";
	String PARTNER_CODE = "partnerCode";
	String OPEN_ID = "openid";
	String UNION_ID = "unionid";
	String NICKNAME = "nickname";
	String GENDER = "gender";
	String FACE_URL = "faceUrl";

	/*TUSER========END=========*/
	/*login=====================end=============================*/

	String REFERRAL_CODE = "rcmderCode";
	String IMSI_CODE = "imsi";
	String PROMOTER_CODE = "promoterCode";
	String IS_LOGIN = "isLogin";


	String VERIFY_METHOD = "method"; // 验证码发送方式
	String MOBILE_VERIFY_CODE = "mobileVerifyCode";
	String CHANEL_CODE = "fr";
	String NOTICE_ID = "id";

	/*product=====================start=============================*/
	/**
	 * 理财产品列表
	 *
	 * @param p 第几页
	 * @param pageSize 每页显示条数
	 * @param tag 0所有 1固收 2浮收
	 * @param callback
	 */
	String PRODUCT_P = "p";
	String PRODUCT_PAGE_SIZE = "pageSize";
	String PRODUCT_STATUS = "status";
	String PRODUCT_TAG = "tag";
	/*product=====================end=============================*/


	String MY_SHOW_MODIFY_PSW = "isShowModifyPasswd";
	String FEEDBACK_DES = "description";
	String OLD_PASSWORD = "oldPasswd";
	String NEW_PASSWORD = "newPasswd";
	String HAS_PASS = "hansPass";//修改密码状态
	String QQ = "qq";
	String EMAIL_ADDRESS = "email";
	String EMAIL_VERIFY_CODE = "verifyCode";
	String PORTRAIT_TITLE = "face";

	/*ADDRESS=====================start=============================*/
	String ADDRESS_ID = "id";
	String ADDRESS_NAME = "name";
	String ADDRESS_PHONE = "phone";
	String ADDRESS_PROVINCE = "province";
	String ADDRESS_CITY = "city";
	String ADDDRESS_AREA = "area";
	String ADDRESS_TOWN = "town";
	String ADDRESS = "address";
	/*ADDRESS=====================end=============================*/

	String TYPE = "type";
	String APP_ID = "appId";
	String LOGIN_VALUE = "loginValue";
	String VERSION_NUM = "verNum";
	String tIME = "time";
	String UID = "uid";
	//调查问卷ID
	String QUESTION_TYPE_ID = "qnid";
	//题目ID
	String QUESTION_ID = "qid";
	String ANSWER_ID = "optionId";   //  	答案ID
	String ANSWER_VALUE = "value";    //答案分数

	String ANSWER_NAME = "answers";
}
