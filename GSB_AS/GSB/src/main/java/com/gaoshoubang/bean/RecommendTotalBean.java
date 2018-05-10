package com.gaoshoubang.bean;

/**
 * Created by Administrator on 2017/5/3.
 */

public class RecommendTotalBean {

	/**
	 * state : 200
	 * msg :
	 * data : {"rel_act":[{"id":"294","title":"理财军团","logo_url":"http://gsywww.oss-cn-shenzhen.aliyuncs.com/uploads/2017/01/21/5882d1907d13c.jpg","desc":"","url":"https://test-hd.gaosouyi.com/financecorps","state":1,"logo_title":"","share":{},"start_time":1493795119,"end_time":1493831119},{"id":"283","title":"高搜易B轮融资1亿元","logo_url":"http://gsywww.oss-cn-shenzhen.aliyuncs.com/uploads/2017/02/27/58b3fdfdeaad0.jpg","desc":"","url":"https://hd.gaosouyi.com/zhongqiu","state":1,"logo_title":"","share":{},"start_time":1493795119,"end_time":1493831119},{"id":"301","title":"勋章馆璀璨升级","logo_url":"http://gsywww.oss-cn-shenzhen.aliyuncs.com/uploads/2017/01/20/588177c2904e1.jpg","desc":"","url":"https://beta-hd.gaosouyi.com/zhongqiu","state":1,"logo_title":"勋章馆璀璨升级","share":{},"start_time":1493795119,"end_time":1493831119}],"rel_intro":{"id":"307","title":"新手专享668元红包","logo_url":"http://gsywww.oss-cn-shenzhen.aliyuncs.com/uploads/2017/01/19/58808fcf8545c.jpg","url":"http://hd.gaosouyi.com/newuser"},"rel_alt":[],"rel_pro":[{"id":"153964","title":"信托宝\u2022省心225期","desc":"","url":"https://test-gsb2.gaosouyi.com/xintuobao/view/id/153964.html","limitStr":"理财期限","limit":"91天","logo_url":"","type":"105","tags":"首投专享,限投一次","rate":"9.9","payTime":"1493782566","beginTime":"1493868966","useStr":"剩余可投","useFund":"1,000,000元"}],"rel_rel":[{"id":"289","title":"关于我们","nums":0,"desc":"3","logo_url":"http://enc.gaosouyi.com/ueditor/php/upload/image/20170119/1484792061981905.png","url":"https://test-gsb2.gaosouyi.com/About/index.html"},{"id":"288","title":"安全保障","nums":0,"desc":"3","logo_url":"http://enc.gaosouyi.com/ueditor/php/upload/image/20170119/1484792077170822.png","url":"https://test-gsb2.gaosouyi.com/About/safe.html"},{"id":"287","title":"新手福利","nums":0,"desc":"3","logo_url":"http://enc.gaosouyi.com/ueditor/php/upload/image/20170119/1484792103853182.png","url":"https://test-gsb2.gaosouyi.com/task/index.html"},{"id":"286","title":"邀请有奖","nums":0,"desc":"3","logo_url":"http://enc.gaosouyi.com/ueditor/php/upload/image/20170119/1484792121195593.png","url":"https://test-gsb2.gaosouyi.com/Member/wode.html"}],"rel_msgCnt":0}
	 */

	private int state;
	private String msg;
	private RecommendBean data;

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public RecommendBean getData() {
		return data;
	}

	public void setData(RecommendBean data) {
		this.data = data;
	}

}
