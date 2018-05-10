package com.gaoshoubang.bean.base;

/**
 * Created by Administrator on 2017/6/2.
 */
/* 当回调没有传入泛型的时候,默认使用该实体类
* 简单的相应类,没有具体的数据
* */
public class SimpleResponse {
	public int state;
	public String msg;

	public DataResponse toGenericResponse() {
		DataResponse genericResponse = new DataResponse();
		genericResponse.state = state;
		genericResponse.msg = msg;
		return genericResponse;
	}
}
