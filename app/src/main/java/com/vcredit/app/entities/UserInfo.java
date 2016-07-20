package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by wangzhengji on 2015/4/10.
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UserInfo implements Serializable {
    /**
     * {
     "realName": "赖长平",
     "mobileNo": "13761197891",
     "isAuthName": false,
     "unreadMsgQty": 3,
     "cardQty": 5,
     "serviceTel": "400-820-5181"
     }

     */
    //用户姓名
    @Expose
    private String realName;

    //手机号码
    @Expose
    private String mobileNo;

    //是否认证实名（true 已认证 false 未认证）
    @Expose
    private boolean isAuthName;

    //消息数
    @Expose
    private int unreadMsgQty;

    //奖品数
    @Expose
    private int cardQty;

    @Expose
    private String serviceTel;

    @Expose
    private String accessToken;

}
