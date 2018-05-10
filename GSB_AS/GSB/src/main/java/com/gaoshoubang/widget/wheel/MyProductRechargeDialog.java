package com.gaoshoubang.widget.wheel;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;

/**
 * Created by KevinZZQ on 2018/3/5.
 */

public class MyProductRechargeDialog extends DialogFragment implements View.OnClickListener {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(false);
        return inflater.inflate(R.layout.dialog_recharge, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        ImageView iv_dredge = (ImageView) view.findViewById(R.id.iv_dredge);
        TextView iv_to_page = (TextView) view.findViewById(R.id.iv_to_page);
        iv_dredge.setOnClickListener(this);
        iv_to_page.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {

    }
}
