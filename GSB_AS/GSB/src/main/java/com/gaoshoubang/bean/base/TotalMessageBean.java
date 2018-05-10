package com.gaoshoubang.bean.base;

import com.gaoshoubang.bean.MessageBean;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class TotalMessageBean extends SuperResponse {
	@SerializedName("data")
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<MessageBean> data;
	public int page;
	public int size;
	public int total;

}
