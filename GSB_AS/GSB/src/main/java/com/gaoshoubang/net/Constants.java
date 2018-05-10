package com.gaoshoubang.net;

import com.gaoshoubang.util.encrypt.Md5Utils;

/**
 * Created by lzx on 2017/6/5.
 */

public interface Constants {
	String KEY = "gaosouyi_for_love_20170303";
	String TOKEN = Md5Utils.encrypt(Md5Utils.createRandom(32));
	String FIRST_KEY = "X-GSY-TOKEN";
	String SECOND_KEY = "X-GSY-VERIFY";
	/*响应的类型*/
	int RESPONSE_SUCCESS = 200;
	int RESPONSE_INVALIDATE = 4007001;


}
