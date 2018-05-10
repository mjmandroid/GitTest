package com.gaoshoubang.widget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.ui.setting.activities.ActivityUserVerification;

/**
 * Created by KevinZZQ on 2018/3/5.
 */

public class CannotTransactTypeTimeDialog extends DialogFragment implements View.OnClickListener {
    private OnDialogClickListener onDialogClickListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setCancelable(false);
        return inflater.inflate(R.layout.dialog_cannot_transact_type_time, container, false);
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

    /**
     * 点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.iv_to_page:
                if (onDialogClickListener != null) {
                    onDialogClickListener.toPage();
                }
                break;
            case R.id.iv_dredge:
                if (onDialogClickListener != null) {
                    onDialogClickListener.close();
                }
                break;
        }
    }

    public CannotTransactTypeTimeDialog show(FragmentActivity activity, Class<ActivityUserVerification> activityUserVerificationClass) {
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(this.getClass().getSimpleName());
        if (fragment != null) {
            fragmentTransaction.remove(fragment);
        }
        show(fragmentTransaction, this.getClass().getSimpleName());
        return this;
    }

    public void setOnDialogClickListener(OnDialogClickListener onDialogClickListener) {
        this.onDialogClickListener = onDialogClickListener;
    }

    public interface OnDialogClickListener {
        void toPage();
        void close();
    }

}
