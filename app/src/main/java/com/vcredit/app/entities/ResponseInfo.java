package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shibenli on 2016/3/7.
 */
public class ResponseInfo {
    @SerializedName("resCode")
    @Expose
    public String resCode;
    @SerializedName("resMsg")
    @Expose
    public String resMsg;

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }
}
