package com.gaoshoubang.widget.wheel;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * Created by KevinZZQ on 2018/2/28.
 */

public class BankDepositDialog1 extends Dialog {
    private TextView tv_details;//详情
    private TextView tv_dredge;//开通
    private TextView prompt_title;
    private TextView promptCancel;
    private RelativeLayout promptRlDefine;
    private RelativeLayout promptRlcancel;

    public BankDepositDialog1(Context context) {
        super(context, R.style.Dialog);
        setContentView(R.layout.bank_deposit_dialog);
        initView();
    }

    private void initView() {
        tv_details = (TextView) findViewById(R.id.tv_details);
        tv_dredge = (TextView) findViewById(R.id.tv_dredge);
        prompt_title = (TextView) findViewById(R.id.prompt_title);
        tv_dredge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
//        promptRlDefine.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
//
//        promptRlcancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dismiss();
//            }
//        });
    }

    /**
     * 设置提示标题和内容
     *
     * @param title
     * @param content
     */
//    public void setContentText(String title, String content) {
//        if (title != null && !title.equals("")) {
//            promptTitle.setVisibility(View.VISIBLE);
//            promptTitle.setText(title);
//        }
//        if (content != null && !content.equals("")) {
//            promptContent.setVisibility(View.VISIBLE);
//            promptContent.setText(content);
//        }
//    }

    /**
     * 设置按钮的内容,null 为隐藏
     *
     * @param define
     * @param cancel
     */
//    public void setContentButton(String define, String cancel) {
//        if (define == null) {
//            promptRlDefine.setVisibility(View.GONE);
//        }
//        if (cancel == null) {
//            promptRlcancel.setVisibility(View.GONE);
//        }
////        promptDefine.setText(define);
////        promptCancel.setText(cancel);
//    }

    /**
     * 确定的监听
     *
     * @param defineOnClickListener
     */
    public void setDefineOnClickListener(View.OnClickListener defineOnClickListener) {
        tv_dredge.setOnClickListener(defineOnClickListener);
    }

//    /**
//     * 取消的监听
//     *
//     * @param cancelOnClickListener
//     */
//    public void setCancelOnClickListener(View.OnClickListener cancelOnClickListener) {
//        promptRlcancel.setOnClickListener(cancelOnClickListener);
//    }
}
