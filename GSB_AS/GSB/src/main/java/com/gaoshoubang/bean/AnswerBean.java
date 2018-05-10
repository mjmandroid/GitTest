package com.gaoshoubang.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lzx on 2017/7/4.
 */

public class AnswerBean {
	@SerializedName("qid")
	public String questionId;  //题目ID
	public String optionId;   //  	答案ID
	public String value;    //答案分数
}
