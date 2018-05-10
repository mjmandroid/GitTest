package com.gaoshoubang.net.callback;


import android.content.Context;

import com.gaoshoubang.bean.base.DataResponse;
import com.gaoshoubang.bean.base.SimpleResponse;
import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.net.Constants;
import com.gaoshoubang.net.convert.Convert;
import com.gaoshoubang.net.exception.InValidateException;
import com.gaoshoubang.net.exception.OtherErrorException;
import com.gaoshoubang.util.LogUtils;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;
/*
* 使用默认解析的数据必须 是  基础包装后的数据, 即要带 code, msg
*
* */

public abstract class JsonCallback<T extends SuperResponse> extends AbsCallback<T> {

	@Override
	public T convertSuccess(Response response) throws Exception {
//		LogUtils.e("JsonCallback", "convertSuccess:" + response.request().url());
		//以下代码是通过泛型解析实际参数,泛型必须传
		JsonReader jsonReader = new JsonReader(response.body().charStream());

		//取出JsonCallback 的泛型  :...DataResponse<Void>
		Type genType = getClass().getGenericSuperclass();
		//从上述的类中取出真实的泛型参数，有些类可能有多个泛型，所以是数值
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		Type type = params[0];//具体的数据类型

		//不是基础包装的泛型只有一层 .
		if (!(type instanceof ParameterizedType)) {
			T data = Convert.fromJson(jsonReader, type);
			response.close();
			if (data == null) {
				throw new IllegalStateException("解析错误");
			}
			int code = data.state;
			switch (code) {
				case Constants.RESPONSE_SUCCESS:
					return data;
				case Constants.RESPONSE_INVALIDATE:
					throw InValidateException.getInstance();
				default:
					throw new OtherErrorException(code, data.msg);
			}
		}
		Type rawType = ((ParameterizedType) type).getRawType();

		Type typeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];

		if (typeArgument == Void.class) {
			//无数据类型,表示没有data数据的情况（以  new JsonCallback<DataResponse<Void>>(this)  以这种形式传递的泛型)
			SimpleResponse simpleResponse = Convert.fromJson(jsonReader, SimpleResponse.class);
			response.close();
			if (simpleResponse == null) {
				throw new IllegalStateException("解析出错");
			}
			switch (simpleResponse.state) {
				case Constants.RESPONSE_SUCCESS:
					return (T) simpleResponse.toGenericResponse();
				case Constants.RESPONSE_INVALIDATE:
					throw InValidateException.getInstance();
				default:
					throw new OtherErrorException(simpleResponse.state, simpleResponse.msg);
			}
		}
		else if (rawType == DataResponse.class) {
			//有数据类型，表示有data
			DataResponse genericResponse = Convert.fromJson(jsonReader, type);
			if (genericResponse == null) {
				throw new IllegalStateException("解析错误!");
			}
			response.close();
			int code = genericResponse.state;
			switch (code) {
				case Constants.RESPONSE_SUCCESS:
					return (T) genericResponse;
				case Constants.RESPONSE_INVALIDATE:
					throw InValidateException.getInstance();
				default:
					throw new OtherErrorException(code, genericResponse.msg);
			}
		}
		else {
			LogUtils.e("JsonCallback", "convertFailed:" + response.request().url());
			response.close();
			throw new IllegalStateException("基类错误无法解析!");
		}
	}

	/*
	* 其他状态码的处理回调,不包含登录失效的情况
	* @param code 状态码
	* @param msg  错误信息
	* */
	public abstract void onReceiveOtherErr(int code, String msg);

	public abstract void onLoginMsgInvalidate();

}