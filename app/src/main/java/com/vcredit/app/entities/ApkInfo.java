package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * apk下载信息
 * Created by zhuofeng on 2015/12/8.
 */
public class ApkInfo implements Serializable {

    @Expose
    private String versionName;//	string	app最新版版本号（显示版本号，如1.0.1beta）
    @Expose
    private int versionCode;//	int	app最新版本号（数字版本号，用于做版本比较，初始版本100）
    @Expose
    private String downloadUrl;//	string	下载地址
    @Expose
    private float appSize;//	float	安装包大小(单位：M)
    @Expose
    private String explain;//	string	版本介绍

    public String fileName;

    public Boolean isDownloaded;

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public float getAppSize() {
        return appSize;
    }

    public void setAppSize(int appSize) {
        this.appSize = appSize;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }
    @Override
    public String toString() {
        return "[versionName="+versionName+",versionCode="+
                versionCode+",downloadUrl="+downloadUrl+",appSize="+appSize
                +",explain="+explain+"]";
    }
}
