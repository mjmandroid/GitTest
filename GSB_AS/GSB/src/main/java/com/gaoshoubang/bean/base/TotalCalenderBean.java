package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.CalenderBean;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class TotalCalenderBean extends SuperResponse {
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<CalenderBean> data;
}
