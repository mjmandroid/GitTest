package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.RelWindowBean;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class TotalRelWindowBean extends SuperResponse {
	public int show;
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<RelWindowBean> data;

}
