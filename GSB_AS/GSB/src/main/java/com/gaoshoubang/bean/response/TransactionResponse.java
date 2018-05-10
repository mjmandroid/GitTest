package com.gaoshoubang.bean.response;

import com.gaoshoubang.bean.TransactionBean;
import com.gaoshoubang.bean.base.SuperResponse;
import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/5/11.
 */

public class TransactionResponse extends SuperResponse {
	@JsonAdapter(ListTypeAdapterFactory.class)
	public List<TransactionBean> data;

	private int maxPage;
	private int page;
	private int size;
	private String total;

	public int getMaxPage() {
		return maxPage;
	}

	public void setMaxPage(int maxPage) {
		this.maxPage = maxPage;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
