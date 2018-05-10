package com.gaoshoubang.ui.setting.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.GsbApplication;
import com.gaoshoubang.R;
import com.gaoshoubang.bean.SelfBean;
import com.gaoshoubang.ui.common.activities.ActivityLock;
import com.gaoshoubang.ui.common.activities.WebActivityShow;
import com.gaoshoubang.ui.assessment.activities.ActivityRiskAssessment;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.ui.information.activities.ActivityInformation;
import com.gaoshoubang.ui.information.presenter.ResetPasswordBean;
import com.gaoshoubang.ui.information.presenter.ResetPasswordImpl;
import com.gaoshoubang.ui.password.activities.ActivitySetPasswd;
import com.gaoshoubang.ui.main.activities.MainActivity;
import com.gaoshoubang.util.RiskAssessmentUtils;
import com.gaoshoubang.widget.CannotTransactTypeTimeDialog;
import com.gaoshoubang.widget.CommonTitleBar;
import com.gaoshoubang.widget.PromptDialog;
import com.gaoshoubang.widget.RippleIntroView;
import com.gaoshoubang.fingerprint.FingerprintConst;
import com.gaoshoubang.fingerprint.FingerprintIdentify;
import com.gaoshoubang.fingerprint.FingerprintUtils;
import com.gaoshoubang.bean.response.AccManageResponse;
import com.gaoshoubang.bean.CnfListBean;
import com.gaoshoubang.base.presenter.BasePresenterImpl;
import com.gaoshoubang.net.ConfigUtils;
import com.gaoshoubang.net.ConfigUtils.OnGetConfigInfo;
import com.gaoshoubang.net.NetworkManager;
import com.gaoshoubang.net.Urls;
import com.gaoshoubang.net.callback.JsonCallbackWrapper;
import com.gaoshoubang.util.ClearUtils;
import com.gaoshoubang.util.LogUtils;
import com.gaoshoubang.util.ToastUtil;
import com.gaoshoubang.util.UmEvent;
import com.gaoshoubang.util.UserSharedPreferenceUtils;
import com.gaoshoubang.widget.wheel.BankDepositDialog1;

import okhttp3.Call;
import okhttp3.Response;

import static com.gaoshoubang.R.id.account_setpwd_tv;


/**
 * 账户管理
 *
 * @author Cisco
 */
public class ActivityUserManager extends BaseActivity implements OnClickListener {
    private static final String TAG = "ActivityUserManager";
    private LinearLayout modifydata;
    private LinearLayout ump;
    private LinearLayout card;
    private LinearLayout setpwd;
    private LinearLayout payPwd;
    private LinearLayout lockPwd;
    private TextView exit;
    private TextView umpState;
    private TextView cardState;
    private TextView account_setpwd_tv;
    private ImageView lockPwdState;
    private TextView mGestureSetting;
    private TextView mAccountSetpwdTv;
    private TextView mAccountAccompLish;//修改资料

    private CnfListBean cnfListBean;


    private int hasPass = -1; //1未修改，2.已修改
    private int hasOpenUmp = -1; // 是否已经开通联动优势(Union Mobile Pay)。1：未开通；2：已开通；
    // private int hasOpenAutoBid = -1;// 是否已开启自动投标。1：否；2：是；
    private int hasOpenBankCard = -1;// 是否开通银行卡。1：否；2：是；
    private String unBindCardUrl;// 绑卡url；
    private String bindCardUrl;// 用户已经绑定的银行卡信息url；


    private Intent intent;
    private LinearLayout mLockFingerprint;
    //	private ImageView mLockFingerprintIv;
    private TextView mLockFingerprintIv;
    private FingerprintIdentify mFingerIdentify;
    private RippleIntroView mRippleIntroView;
    private SelfBean selfBean;
    private CommonTitleBar mTitle;
    private LinearLayout mRiskAssessment;
    private TextView mRiskAssessmentTVType;
    private ImageView mRiskAssessmentIvType;
    private TextView mRiskTransactionType;//交易密码
    private String RiskTransaction;//1.未修改，2已修改
    private int datum;

    @Override
    protected BasePresenterImpl getPresenter() {
        return null;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_manager;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
        if (fingerprintType == 3) {
            mLockFingerprintIv.setText("已设置");
            mLockFingerprintIv.setTextColor(getResources().getColor(R.color.user_managerenot));
        } else {
            LogUtils.e("ActivityUserManager", "onStart:" + fingerprintType);
            mLockFingerprintIv.setText("未设置");
            mLockFingerprintIv.setTextColor(getResources().getColor(R.color.user_managersetting));
        }
        requestAccountMment();
        LogUtils.e("ActivityUserManager", "onResume:" + mTitle.getWidth());

        int riskAssessmentScore = UserSharedPreferenceUtils.getRiskAssessmentScore();
        if (riskAssessmentScore > 0) {
            mRiskAssessmentTVType.setVisibility(View.VISIBLE);
            mRiskAssessmentTVType.setText(RiskAssessmentUtils.getAssessmentType(riskAssessmentScore));
        } else {
			mRiskAssessmentTVType.setVisibility(View.GONE);

        }
        String modifiedData = UserSharedPreferenceUtils.getNickName();
        if (TextUtils.isEmpty(modifiedData)) {
            mAccountAccompLish.setText("已完成");
            mAccountAccompLish.setTextColor(getResources().getColor(R.color.user_managerenot));
        } else {
            mAccountAccompLish.setText("未完成");
            mAccountAccompLish.setTextColor(getResources().getColor(R.color.user_managersetting));
        }
//        if ("2".equals(datum)){
//            mAccountAccompLish.setText("未完成");
//            mAccountAccompLish.setTextColor(getResources().getColor(R.color.user_managersetting));
//        }else {
//            mAccountAccompLish.setText("已经完成");
//            mAccountAccompLish.setTextColor(getResources().getColor(R.color.user_managerenot));
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    protected void initView() {
//		new FullTitleBar(this, "#ffffff");
        modifydata = (LinearLayout) findViewById(R.id.account_modifydata);
        ump = (LinearLayout) findViewById(R.id.account_ump);
        card = (LinearLayout) findViewById(R.id.account_card);
        setpwd = (LinearLayout) findViewById(R.id.account_setpwd);
        payPwd = (LinearLayout) findViewById(R.id.account_pay_pwd);
        lockPwd = (LinearLayout) findViewById(R.id.account_lock_pwd);
        exit = (TextView) findViewById(R.id.account_exit);
        umpState = (TextView) findViewById(R.id.account_ump_state);
        cardState = (TextView) findViewById(R.id.account_card_state);
        mGestureSetting = (TextView) findViewById(R.id.gesture_setting);
//        lockPwdState = (ImageView) findViewById(R.id.account_lock_pwd_state);
        mLockFingerprint = (LinearLayout) findViewById(R.id.account_lock_fingerprint);
        mLockFingerprintIv = (TextView) findViewById(R.id.account_lock_fingerprint_iv);
        mAccountSetpwdTv = (TextView) findViewById(R.id.account_setpwd_tv);
        mAccountAccompLish = (TextView) findViewById(R.id.account_accomplish);
        mRippleIntroView = (RippleIntroView) findViewById(R.id.layout_ripple);
        mTitle = ((CommonTitleBar) findViewById(R.id.title));
        mRiskAssessment = ((LinearLayout) findViewById(R.id.risk_assessment));
        mRiskAssessmentTVType = ((TextView) findViewById(R.id.risk_assessment_tv_type));
        mRiskAssessmentIvType = ((ImageView) findViewById(R.id.risk_assessment_iv_type));
        mRiskTransactionType = (TextView) findViewById(R.id.risk_transaction_type);


    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
//		showGuideView();
        //首次进入,显示引导页
        if (hasFocus) {
            if (!UserSharedPreferenceUtils.getFingerprintGuideToggle()) {
                showGuideView();
            }
        }
    }

    private void showGuideView() {
        // TODO: 2017/7/5  蒙层
        mRippleIntroView.setHighLightArea(mLockFingerprint, R.drawable.guide_fingerprint_toggle, false, true, false);
        mRippleIntroView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mRippleIntroView.setVisibility(View.GONE);
                UserSharedPreferenceUtils.setFingerprintGuideToggle(true);
            }
        });
        mRippleIntroView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void initEvent() {
        ripple(modifydata);
        ripple(ump);
        ripple(card);
        ripple(setpwd);

        modifydata.setOnClickListener(this);
        ump.setOnClickListener(this);
        card.setOnClickListener(this);
        setpwd.setOnClickListener(this);
        payPwd.setOnClickListener(this);
        lockPwd.setOnClickListener(this);
        exit.setOnClickListener(this);
//		lockPwdState.setOnClickListener(this);
        mAccountSetpwdTv.setOnClickListener(this);
        mGestureSetting.setOnClickListener(this);
        mLockFingerprint.setOnClickListener(this);
        mRiskAssessment.setOnClickListener(this);
    }

    @Override
    protected void loadData() {
        mFingerIdentify = new FingerprintIdentify(this); // 初始化指纹模块
        //需要登录后初始化,指纹设置状态
        FingerprintUtils.initFingerprintStatus(mFingerIdentify, GsbApplication.getUserId());
    }

    @Override
    protected void bindData() {

    }


    private void initDate() {
        if (hasOpenUmp == 2) {
            umpState.setText("已开通");
            umpState.setTextColor(getResources().getColor(R.color.user_managerenot));
            payPwd.setVisibility(View.VISIBLE);
        } else {
            umpState.setText("未开通");
            umpState.setTextColor(getResources().getColor(R.color.user_managersetting));
            payPwd.setVisibility(View.GONE);
        }
        if (hasOpenBankCard == 2) {
            cardState.setText("未设置快捷卡");
            cardState.setTextColor(getResources().getColor(R.color.user_managersetting));
        } else {
            cardState.setText("已设置");
            cardState.setTextColor(getResources().getColor(R.color.user_managerenot));
        }
        // 手势
        if (UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()) != null) {
//			lockPwdState.setBackgroundResource(R.drawable.icon_on);
            mGestureSetting.setText("已设置");
            mGestureSetting.setTextColor(getResources().getColor(R.color.user_managerenot));

        } else {
//			lockPwdState.setBackgroundResource(R.drawable.icon_off);
            mGestureSetting.setText("未设置");
            mGestureSetting.setTextColor(getResources().getColor(R.color.user_managersetting));
        }
        if (hasPass == 2){
            mAccountSetpwdTv.setText("已设置");
            mAccountSetpwdTv.setTextColor(getResources().getColor(R.color.user_managerenot));
        }else {
            mAccountSetpwdTv.setText("未设置");
            mAccountSetpwdTv.setTextColor(getResources().getColor(R.color.user_managersetting));
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (1 == resultCode) {
            switch (requestCode) {
                case 1:
                    mAccountSetpwdTv.setText("已设置");
                    break;
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.account_modifydata:// 修改资料
                setUmEvent(UmEvent.Gsy_account_changeData);
                startActivity(new Intent(this,ActivityInformation.class));
                break;

            case R.id.account_setpwd:// 修改登录密码
                setUmEvent(UmEvent.Gsy_account_changeLoginPwd);
//                startActivity(intent);
                startActivity(new Intent(this,ActivitySetPasswd.class));
                break;

            case R.id.account_lock_pwd:// 手势密码
                setLockPwdDialog();
                break;
            case R.id.risk_assessment:  //风险测评
                Intent intentRisk = new Intent(this, ActivityRiskAssessment.class);
                startActivity(intentRisk);
                break;
            case R.id.account_exit:// 退出登录
                setUmEvent(UmEvent.Gsy_account_signOut);
                exitDialog();
                break;
            case R.id.account_lock_fingerprint:
                if (!mFingerIdentify.isHardwareEnable()) {
                    ToastUtil.toast(this, "不支持该设备");
                    break;
                } else if (!mFingerIdentify.isRegisteredFingerprint()) {
                    ToastUtil.toast(this, "还没有录入指纹,请先到系统设置录入指纹");
                    break;
                }
                if (UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()) == null) {
                    final PromptDialog promptDialog = new PromptDialog(this);
                    promptDialog.setContentText("请先开启手势解锁", null);
                    promptDialog.setDefineOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            intent = new Intent(ActivityUserManager.this, ActivityLock.class);
                            intent.putExtra("type", 0);
                            intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
                            startActivity(intent);
                            promptDialog.dismiss();
                        }
                    });
                    promptDialog.show();
                } else {
                    fingerprintSwitch();
                }
                break;

        }

        // 需要获取配置信息的操作
        ConfigUtils.getInstance().getCnfInfo(new OnGetConfigInfo() {
            @Override
            public void success(CnfListBean mCnfListBean) {
                cnfListBean = mCnfListBean;
            }

            public void onFail() {
                return;
            }
        });
        switch (v.getId()) {
            case R.id.account_ump:// 资金托管
                setUmEvent(UmEvent.Gsy_account_moneyAccount);
                intent = new Intent(this, WebActivityShow.class);
                if (hasOpenUmp == 2) {
                    intent.putExtra("url", cnfListBean.getFUND_CUSTODY_URL());
                } else {
                    intent.putExtra("url", cnfListBean.getUMP_URL());// 未开通链接
                }
                intent.putExtra("type", WebActivityShow.TYPE_UMP);
                startActivity(intent);
                return;

            case R.id.account_card:// 我的银行卡
                setUmEvent(UmEvent.Gsy_account_myCard);
                intent = new Intent(this, WebActivityShow.class);
                if (hasOpenBankCard == 2) {
                    intent.putExtra("url", bindCardUrl);
                    intent.putExtra("type", WebActivityShow.TYPE_MYCARD);
                } else {
                    if (hasOpenUmp != 2) {
                        // 未开通联动,先跳转开通联动
                        intent.putExtra("url", cnfListBean.getUMP_URL());
                        intent.putExtra("type", WebActivityShow.TYPE_UMP);
                    } else {
                        intent.putExtra("url", unBindCardUrl);
                        intent.putExtra("type", WebActivityShow.TYPE_ADDCARD);
                    }
                }
                startActivity(intent);
                return;

            case R.id.account_pay_pwd:// 修改支付密码
                setUmEvent(UmEvent.Gsy_account_changePaymentPwd);
                intent = new Intent(this, WebActivityShow.class);
                intent.putExtra("url", cnfListBean.getUPDATE_PAY_PWD());
                intent.putExtra("type", WebActivityShow.TYPE_UPDATE_PAY_PWD);
                startActivity(intent);
                return;

        }
    }


    private void fingerprintSwitch() {
        intent = new Intent(ActivityUserManager.this, ActivityLock.class);
        int fingerprintType = UserSharedPreferenceUtils.getFingerprintType(GsbApplication.getUserId());
        if (fingerprintType == FingerprintConst.FINGERPRINT_OPENING) {
            //已开启指纹,关闭指纹解锁

            intent.putExtra("type", 1);
            intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_ClOSE);//设置状态
            startActivity(intent);
            return;
        } else if (fingerprintType == FingerprintConst.FINGERPRINT_HARDWARE_ENABLE) {//支持指纹,没开启,
            if (TextUtils.isEmpty(UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()))) {
                intent.putExtra("type", 0);
                //进行指纹验证和手势设置
                intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_HARDWARE_ENABLE);

            } else {
                //进行指纹验证
                intent.getIntExtra("type", 1);
                intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_OPEN);
            }
            startActivity(intent);
        } else if (fingerprintType == FingerprintConst.FINGERPRINT_VERIFICATION_SUC) {
            if (TextUtils.isEmpty(UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()))) {
//				ToastUtil.toast(this, "开启指纹需要先开启手势解锁");
                intent.putExtra("type", 0);
                intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_VERIFICATION_SUC);
                startActivity(intent);
            } else {
                intent.putExtra("type", 1);
                intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_SETTING_VERIFICATION_OPEN);
                startActivity(intent);
            }
        } else if (fingerprintType == FingerprintConst.FINGERPRINT_HARDWARE_DISABLE) {
            if (!mFingerIdentify.isHardwareEnable()) {
                ToastUtil.toast(this, "不支持该设备");
            } else if (!mFingerIdentify.isRegisteredFingerprint()) {
                ToastUtil.toast(this, "还没有录入指纹,请先到系统设置录入指纹");
            }
        }
    }

    // 设置手势
    private void setLockPwdDialog() {
        if (UserSharedPreferenceUtils.getGestureLock(GsbApplication.getUserId()) == null) {
            intent = new Intent(ActivityUserManager.this, ActivityLock.class);
            intent.putExtra("type", 0);
            intent.putExtra(FingerprintConst.FINGERPRINT_TYPE, FingerprintConst.FINGERPRINT_NOT_SHOW);
            startActivity(intent);
        }
        else {
            final PromptDialog promptDialog = new PromptDialog(this);
            promptDialog.setContentText("是否取消手势密码", null);
            promptDialog.setDefineOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ActivityUserManager.this, ActivityUserVerification.class);
                    intent.putExtra("type", 1);
                    startActivity(intent);
                    promptDialog.dismiss();
                }
            });
            promptDialog.show();
        }
    }
    // 退出
    private void exitDialog() {
        final PromptDialog promptDialog = new PromptDialog(this);
        promptDialog.setContentText("确定退出登录?", null);
        promptDialog.setDefineOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new ClearUtils().ClearUserInfo(ActivityUserManager.this);
                Intent intent = new Intent(ActivityUserManager.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                promptDialog.dismiss();
            }
        });
        promptDialog.show();
    }

    private void requestAccountMment() {
        NetworkManager.get(Urls.ACTION_ACCOUNT_MANAGEMENT)
                .execute(new JsonCallbackWrapper<AccManageResponse>(this) {
                    @Override
                    public void onSuccess(AccManageResponse accManageResponse, Call call, Response response) {
                        afterGetAccountManagement(accManageResponse);
                    }

                });
    }

    private void afterGetAccountManagement(AccManageResponse accManageResponse) {
        hasOpenUmp = accManageResponse.hasOpenUmp;
//        hasOpenBankCard = accManageResponse.hasOpenBankCard;
        unBindCardUrl = accManageResponse.unBindCardUrl;
        bindCardUrl = accManageResponse.bindCardUrl;
        initDate();

    }


}
