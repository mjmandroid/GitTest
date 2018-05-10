package com.gaoshoubang.bean.response;

import com.gaoshoubang.bean.LatestVerBean;
import com.gaoshoubang.bean.base.SuperResponse;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/6/2.
 */

public class LastVerBeanResponse extends SuperResponse {
	public int isAppMustUpdate;
	@SerializedName("ver")
	public LatestVerBean ver;

}
