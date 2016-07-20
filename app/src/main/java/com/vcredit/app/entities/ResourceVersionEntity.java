package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

/**
 * Created by chenlei on 2016/4/9.
 */
public class ResourceVersionEntity {

    /**
     * verCodeByIOS : 100
     * verCodeByAndroid : 100
     * versionCodeEnum : 1
     */
    @Expose
    private int versionCodeIOS;
    @Expose
    private int versionCodeAndroid;
    @Expose
    private int versionCodeEnum;

    public int getVersionCodeIOS() {
        return versionCodeIOS;
    }

    public void setVersionCodeIOS(int versionCodeIOS) {
        this.versionCodeIOS = versionCodeIOS;
    }

    public int getVersionCodeAndroid() {
        return versionCodeAndroid;
    }

    public void setVersionCodeAndroid(int versionCodeAndroid) {
        this.versionCodeAndroid = versionCodeAndroid;
    }

    public int getVersionCodeEnum() {
        return versionCodeEnum;
    }

    public void setVersionCodeEnum(int versionCodeEnum) {
        this.versionCodeEnum = versionCodeEnum;
    }
}
