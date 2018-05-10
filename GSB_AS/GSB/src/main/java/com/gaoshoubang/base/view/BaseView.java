package com.gaoshoubang.base.view;

/**
 * Created by lzx on 2017/5/3.
 */

public interface BaseView {
	void showProgress();

	void hideProgress();


	/**
	 * 请求网络数据失败,用于检查错误原因,给出提示
	 */
	void onRequestFail();


	void showMsg(String msg);

	/**
	 * 显示其他状态码提示信息
	 *
	 * @param code 状态码
	 * @param msg 错误信息
	 */
	void showOtherErrMsg(int code, String msg);

	/**
	 * 登录失效
	 */
	void loginInvalidate();

}
