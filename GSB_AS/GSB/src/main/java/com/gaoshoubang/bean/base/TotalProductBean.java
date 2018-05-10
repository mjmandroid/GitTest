package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5.
 */

public class TotalProductBean extends SuperResponse {
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<ProductBean> data;
}
