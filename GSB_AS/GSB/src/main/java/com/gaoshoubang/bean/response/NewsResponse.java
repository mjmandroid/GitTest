package com.gaoshoubang.bean.response;

import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/8.
 */

public class NewsResponse extends SuperResponse {
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<NewsBean> data;
	public int page;
	public int size;
	public int total;

}
