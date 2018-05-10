package com.gaoshoubang.util.permission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by namee on 2015. 11. 18..
 * Register a method invoked when permission requests are denied.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PermissionError {
	int value() default PermissionUtil.PERMISSIONS_REQUEST_CODE;
}
