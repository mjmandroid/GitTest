package com.gaoshoubang.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zzq on 2018/1/22.
 * 账户有关的正则属性
 */

public class AccountValidatorUtil {
	private static AccountValidatorUtil instance;

	private AccountValidatorUtil(){}

	public synchronized static AccountValidatorUtil getInstance(){
		if (instance == null)
			instance = new AccountValidatorUtil();
		return instance;
	}


	public AccountValidatorUtil isPhone(String phone,String errorMsg) {
		Pattern p = Pattern
				.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(17[^4,\\D])|(18[0-9]))\\d{8}$");
		Matcher m = p.matcher(phone);
		if(!m.matches()){
			throw new IllegalArgumentException(errorMsg);
		}
		return this;
	}

	public AccountValidatorUtil isId(String id,String errorMsg){
		Pattern p = Pattern
				.compile("^[0-9]{8}$");
		Matcher m = p.matcher(id);
		if(!m.matches()){
			throw new IllegalArgumentException(errorMsg);
		}
		return this;
	}

	public AccountValidatorUtil isPassword(String password,String errorMsg){
		Pattern p = Pattern.compile("^[0-9a-zA-Z]{6,16}$");
		Matcher m = p.matcher(password);
		if (!m.matches()){
			throw new IllegalArgumentException(errorMsg);
		}
		return this;
	}

	public AccountValidatorUtil isLatters(String latters,String errorMsg){
		Pattern p = Pattern.compile("^[a-zA-Z]{1,15}$");
		Matcher m = p.matcher(latters);
		if (!m.matches()){
			throw new IllegalArgumentException(errorMsg);
		}
		return this;
	}

	public AccountValidatorUtil isCode(String code,String errorMsg){
		Pattern p = Pattern.compile("^[0-9]{6}$");
		Matcher m = p.matcher(code);
		if (!m.matches())
			throw new IllegalArgumentException(errorMsg);
		return this;
	}

	public AccountValidatorUtil isNumber(String number,String errorMsg){
		Pattern p = Pattern.compile("^[0-9]+$");
		Matcher m = p.matcher(number);
		if (!m.matches())
			throw new IllegalArgumentException(errorMsg);
		return this;
	}

	public AccountValidatorUtil isNull(String content,String msg){
		if (TextUtils.isEmpty(content)){
			throw new NullPointerException(msg);
		}
		return this;
	}

	public AccountValidatorUtil isEqualse(String val1,String val2,String msg){
		if (!val1.equals(val2)){
			throw new IllegalArgumentException(msg);
		}
		return this;
	}

	public AccountValidatorUtil isNull(Object object,String msg){
		if (null == object)
			throw new NullPointerException(msg);
		return this;
	}
}
