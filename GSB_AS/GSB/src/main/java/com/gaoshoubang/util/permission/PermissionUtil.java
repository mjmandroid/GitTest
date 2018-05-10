package com.gaoshoubang.util.permission;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description: 权限工具类.
 */
public class PermissionUtil {
	private static final String TAG = "PermissionUtil";

	public static final int PERMISSIONS_REQUEST_CODE = 100001;

	// 定位需要权限
	public static final String[] PERMISSIONS_GROUP_LOACATION = {
			Manifest.permission.ACCESS_COARSE_LOCATION,
			Manifest.permission.ACCESS_FINE_LOCATION};

	// 通讯录操作权限
	public static final String[] PERMISSIONS_GROUP_CONTCATS = {
			Manifest.permission.GET_ACCOUNTS,
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.WRITE_CONTACTS};

	// 相机需要权限
	public static final String[] PERMISSIONS_GROUP_CAMERA = {
			Manifest.permission.CAMERA,
	};

	// 录音需要权限
	public static final String[] PERMISSIONS_GROUP_RECORD_AUDIO = {
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.RECORD_AUDIO};
	//外部存储权限
	public static final String[] PERMISSIONS_GROUP_STORAGE = {
			Manifest.permission.READ_EXTERNAL_STORAGE,
			Manifest.permission.WRITE_EXTERNAL_STORAGE
	};

	private String[] mPermissions;
	private int mRequestCode;
	private Object object;

	private PermissionUtil(Object object) {
		this.object = object;
		this.mRequestCode = PERMISSIONS_REQUEST_CODE;
	}

	/**
	 * checkLocation:(检查定位服务权限). <br>
	 *
	 * @param context
	 *
	 * @return
	 * @author lzx
	 * @since JDK 1.7
	 */
	public static boolean checkLocation(Context context) {
		return check(context, PERMISSIONS_GROUP_LOACATION);
	}

	/**
	 * checkRecoreAudio:(检查录音权限). <br>
	 *
	 * @param context
	 *
	 * @return
	 * @author lzx
	 * @since JDK 1.7
	 */
	public static boolean checkRecoreAudio(Context context) {
		return check(context, PERMISSIONS_GROUP_RECORD_AUDIO);
	}

	/**
	 * checkCamera:(检查相机权限). <br>
	 *
	 * @param context
	 *
	 * @return
	 * @author lzx
	 * @since JDK 1.7
	 */
	public static boolean checkCamera(Context context) {
		return check(context, PERMISSIONS_GROUP_CAMERA);
	}

	/**
	 * checkContacts:(检查通讯录操作权限). <br>
	 *
	 * @param context
	 *
	 * @return
	 * @author lzx
	 * @since JDK 1.7
	 */
	public static boolean checkContacts(Context context) {
		return check(context, PERMISSIONS_GROUP_CONTCATS);
	}

	/**
	 * 外部存储
	 *
	 * @param context
	 *
	 * @return
	 */
	public static boolean checkStorage(Context context) {
		return check(context, PERMISSIONS_GROUP_STORAGE);
	}

	/**
	 * check:(检查权限). <br>
	 *
	 * @author lzx
	 * @since JDK 1.7
	 */
	public static boolean check(Context context, String... permissions) {
		try {
			if (null == context) {
				throw new RuntimeException("Context is null.");
			}
			for (int i = 0; i < permissions.length; i++) {
				Integer check = context.checkPermission(permissions[i],
						Binder.getCallingPid(), Binder.getCallingUid());
				if (check == -1) {
					return false;
				}
			}
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage(), e);
			return false;
		}
	}

	/**
	 * 检查权限
	 *
	 * @param context
	 * @param permissionsBase
	 *
	 * @return
	 */
	public static boolean check(Context context, String[]... permissionsBase) {
		for (int i = 0; i < permissionsBase.length; i++) {
			String[] permissions = permissionsBase[i];
			boolean isCheck = check(context, permissions);
			if (isCheck == false) {
				return false;
			}
		}
		return true;
	}

	public static PermissionUtil with(Activity activity) {
		return new PermissionUtil(activity);
	}

	public static PermissionUtil with(Application application) {
		return new PermissionUtil(application);
	}

	public static PermissionUtil with(Fragment fragment) {
		return new PermissionUtil(fragment);
	}

	public PermissionUtil permissions(String... permissions) {
		this.mPermissions = permissions;
		return this;
	}

	public PermissionUtil permissions(String[]... permissionsBase) {
		List<String> permissionList = new ArrayList<>();
		for (String[] permissions : permissionsBase) {
			permissionList.addAll(permissionsToList(permissions));
		}
		this.mPermissions = permissionList.toArray(new String[permissionList.size()]);
		return this;
	}

	public PermissionUtil addRequestCode(int requestCode) {
		this.mRequestCode = requestCode;
		return this;
	}

	@TargetApi(value = Build.VERSION_CODES.M)
	public void request() {
		requestPermissions(object, mRequestCode, mPermissions);
	}

	public static void needPermission(Object obj, int requestCode, String[] permissions) {
		requestPermissions(obj, requestCode, permissions);
	}

	public static void needPermission(Object obj, int requestCode, String permission) {
		needPermission(obj, requestCode, new String[]{permission});
	}

	private static void requestPermissions(Object obj, int requestCode, String[] permissions) {
		if (isOverMarshmallow()) {
			if (permissions.length > 0) {
				if (obj instanceof Activity) {
					((Activity) obj).requestPermissions(permissions, requestCode);
				}
				else if (obj instanceof Fragment) {
					((Fragment) obj).requestPermissions(permissions, requestCode);
				}
				else {
					throw new IllegalArgumentException(obj.getClass().getName() + " is not supported");
				}

			}
		}
		else {
			onRequestPermissionsResult(obj, requestCode, permissions);
		}
	}

	private static void onSuccess(Object obj, int requestCode) {
		Method executeMethod = findMethodWithRequestCode(obj.getClass(),
				PermissionSuccess.class, requestCode);

		executeMethod(obj, executeMethod);
	}

	private static void onError(Object obj, int requestCode) {
		Method executeMethod = findMethodWithRequestCode(obj.getClass(),
				PermissionError.class, requestCode);

		executeMethod(obj, executeMethod);
	}

	private static Method executeMethod(Object obj, Method executeMethod) {
		try {
			if (executeMethod != null) {
				if (!executeMethod.isAccessible()) executeMethod.setAccessible(true);
				executeMethod.invoke(obj, new Object[]{});
			}
		} catch (Exception e) {
			executeMethod = null;
			e.printStackTrace();
		} finally {
			return executeMethod;
		}

	}

	/**
	 * 请求权限结果
	 *
	 * @param obj
	 * @param requestCode
	 * @param permissions
	 */
	public static void onRequestPermissionsResult(Object obj, int requestCode, String[] permissions) {
		if (check(getContext(obj), permissions)) {
			onSuccess(obj, requestCode);
		}
		else {
			onError(obj, requestCode);
		}
	}

	/**
	 * 请求权限结果
	 *
	 * @param obj
	 * @param requestCode
	 * @param permissions
	 * @param grantResults
	 */
	@Deprecated
	public static void onRequestPermissionsResult(Object obj, int requestCode, String[] permissions,
	                                              int[] grantResults) {
		List<String> permissionList = new ArrayList<>();
		for (int i = 0; i < grantResults.length; i++) {
			if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
				permissionList.add(permissions[i]);
			}
		}
		if (permissionList.size() > 0) {
			onError(obj, requestCode);
		}
		else {
			onSuccess(obj, requestCode);
		}
	}

	public static List<String> permissionsToList(String... permission) {
		List<String> permissionList = new ArrayList<>();
		for (String value : permission) {
			permissionList.add(value);
		}
		return permissionList;
	}

	public static <A extends Annotation> Method findMethodWithRequestCode(Class clazz,
	                                                                      Class<A> annotation, int requestCode) {
		for (Method method : clazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(annotation)) {
				if (isEqualRequestCodeFromAnntation(method, annotation, requestCode)) {
					return method;
				}
			}
		}
		return null;
	}

	public static boolean isEqualRequestCodeFromAnntation(Method m, Class clazz, int requestCode) {
		if (clazz.equals(PermissionError.class)) {
			return requestCode == m.getAnnotation(PermissionError.class).value();
		}
		else if (clazz.equals(PermissionSuccess.class)) {
			return requestCode == m.getAnnotation(PermissionSuccess.class).value();
		}
		else {
			return false;
		}
	}

	private static boolean isOverMarshmallow() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
	}

	private static Context getContext(Object object) {
		if (object instanceof Activity) {
			return (Activity) object;
		}
		else if (object instanceof Fragment) {
			return ((Fragment) object).getActivity();
		}
		return null;
	}
}
