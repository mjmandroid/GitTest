package com.gaoshoubang.ui.information.presenter;

import java.io.Serializable;

/**
 * Created by KevinZZQ on 2018/3/6.
 */

public class ResetPasswordBean implements Serializable {
    private String oldPwdStr;
    private String newPwdStr;
    private int hasPass;

    public String getOldPwdStr() {
        return oldPwdStr;
    }

    public String getNewPwdStr() {
        return newPwdStr;
    }

    public int getHasPass() {
        return hasPass;
    }

    public void setOldPwdStr(String oldPwdStr) {
        this.oldPwdStr = oldPwdStr;
    }

    public void setNewPwdStr(String newPwdStr) {
        this.newPwdStr = newPwdStr;
    }

    public void setHasPass(int hasPass) {
        this.hasPass = hasPass;
    }
}
