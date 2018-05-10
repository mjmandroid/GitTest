package com.gaoshoubang.util;

import android.os.Environment;

import java.io.File;

public class FilesPath {
	public static String path = isExist(Environment.getExternalStorageDirectory().getAbsolutePath() + "/android/data/com.gaoshoubang/files/");
	public static String appDowload = path;
	public static String headPhoto = path + "headPhoto.png";
	public static String cameraPath = path + "cameraPath.png";
	public static String qrCodePath = path + "qrCode.png";

	/**
	 * @param path 文件夹路径
	 */
	public static String isExist(String path) {
		File file = new File(path);
		// 判断文件夹是否存在,如果不存在则创建文件夹
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

}
