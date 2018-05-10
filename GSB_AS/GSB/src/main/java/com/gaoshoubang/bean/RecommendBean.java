package com.gaoshoubang.bean;

import java.util.List;

/**
 * 首页数据
 *
 * @author Cisco
 */
public class RecommendBean {
	private List<RecommendAct> rel_act;
	private List<RecommendAlt> rel_alt;  //这里
	private List<RecommendPro> rel_pro;
	private List<RecommendRel> rel_rel;
	private RecommendIntro rel_intro;
	private int rel_msgCnt;

	public RecommendBean() {
		super();
	}

	public RecommendBean(List<RecommendAct> rel_act, List<RecommendPro> rel_pro, List<RecommendRel> rel_rel, int rel_msgCnt) {
		super();
		this.rel_act = rel_act;
//		this.rel_alt = rel_alt;
		this.rel_pro = rel_pro;
		this.rel_rel = rel_rel;
		this.rel_msgCnt = rel_msgCnt;
	}

	public List<RecommendAct> getRel_act() {
		return rel_act;
	}

	public void setRel_act(List<RecommendAct> rel_act) {
		this.rel_act = rel_act;
	}

	public List<RecommendAlt> getRel_alt() {
		return rel_alt;
	}

	public void setRel_alt(List<RecommendAlt> rel_alt) {
		this.rel_alt = rel_alt;
	}

	public List<RecommendPro> getRel_pro() {
		return rel_pro;
	}

	public void setRel_pro(List<RecommendPro> rel_pro) {
		this.rel_pro = rel_pro;
	}

	public List<RecommendRel> getRel_rel() {
		return rel_rel;
	}

	public void setRel_rel(List<RecommendRel> rel_rel) {
		this.rel_rel = rel_rel;
	}

	public int getRel_msgCnt() {
		return rel_msgCnt;
	}

	public void setRel_msgCnt(int rel_msgCnt) {
		this.rel_msgCnt = rel_msgCnt;
	}

	public RecommendIntro getRel_intro() {
		return rel_intro;
	}

	public void setRel_intro(RecommendIntro rel_intro) {
		this.rel_intro = rel_intro;
	}

}
