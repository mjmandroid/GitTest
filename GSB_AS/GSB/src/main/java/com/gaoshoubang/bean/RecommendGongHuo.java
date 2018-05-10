package com.gaoshoubang.bean;

import com.gaoshoubang.net.convert.ListTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

public class RecommendGongHuo {
	@JsonAdapter(ListTypeAdapterFactory.class)
	private List<RecommendGongGao> gonggao;
	private RecommendHuoDong huodong;

	public RecommendGongHuo() {
		super();
	}

	public RecommendGongHuo(List<RecommendGongGao> gonggao, RecommendHuoDong huodong) {
		super();
		this.gonggao = gonggao;
		this.huodong = huodong;
	}

	public List<RecommendGongGao> getGonggao() {
		return gonggao;
	}

	public void setGonggao(List<RecommendGongGao> gonggao) {
		this.gonggao = gonggao;
	}

	public RecommendHuoDong getHuodong() {
		return huodong;
	}

	public void setHuodong(RecommendHuoDong huodong) {
		this.huodong = huodong;
	}

}
