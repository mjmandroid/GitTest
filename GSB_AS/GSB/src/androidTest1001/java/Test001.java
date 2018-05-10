import android.test.InstrumentationTestCase;

import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.bean.response.LastVerBeanResponse;
import com.gaoshoubang.net.JsonCallback;
import com.gaoshoubang.util.LogUtils;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by lzx on 2017/6/7.
 */

public class Test001 extends InstrumentationTestCase {
	public void testReflect() throws ClassNotFoundException {
		Class clazz = Class.forName("com.gaoshoubang.bean.response.LastVerBeanResponse");
		Class superclass = clazz.getSuperclass();
		System.out.print((superclass != SuperResponse.class));
		LogUtils.e("Test001", "testReflect:" + (superclass == SuperResponse.class));
		assert superclass == SuperResponse.class;
	}

	public void testReflect01() throws ClassNotFoundException {
		JsonCallback<LastVerBeanResponse> jsonCallback = new JsonCallback<LastVerBeanResponse>() {
			@Override
			public void onFailure(Call call, IOException e) {

			}

			@Override
			public void onResponse(Call call, LastVerBeanResponse object, int stateCode) throws IOException {

			}
		};
		Type genericSuperclass = jsonCallback.getClass().getGenericSuperclass();
		Type type = ((ParameterizedType) genericSuperclass).getActualTypeArguments()[0];
		Class<?> aClass = Class.forName(type.toString().replace("class ", ""));

		LogUtils.e("Test001", "testReflect01:" + aClass.getSuperclass());

	}
}
