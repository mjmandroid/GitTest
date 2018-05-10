package com.gaoshoubang.base.presenter;


/**
 * Created by lzx on 2017/5/3.
 */

public interface BasePresenter {
//    void onResume();

	void onCreate();

	/**
	 * 加载数据
	 */
//    void loadData();


	void onDestroy();

	/**
	 * 出现其他错误的返回码时回调
	 *
	 * @param stateCode 返回码
	 */
	void onOtherError(int stateCode, String msg);
}
