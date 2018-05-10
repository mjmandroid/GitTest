package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.SignInCyclesBean;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/9.
 */

public class TotalSignInCyclesBean extends SuperResponse {
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<SignInCyclesBean> data;
}
