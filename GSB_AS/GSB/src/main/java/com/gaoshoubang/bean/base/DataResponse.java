package com.gaoshoubang.bean.base;

/**
 * Created by Administrator on 2017/6/2.
 */

public class DataResponse<T> extends SuperResponse {
	/**
	 * 大部分接口返回的实体类类型, 泛型传入具体的数据类型.
	 */
	/*返回的数据类型,有可能是实体类,数组类型, 数组为空时返回的为"",注意异常捕捉.*/
	public T data;
}
