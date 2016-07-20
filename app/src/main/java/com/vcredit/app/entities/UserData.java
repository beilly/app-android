package com.vcredit.app.entities;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by shibenli on 2016/5/9.
 * 用户数据
 */
@Getter
@Setter
@ToString(callSuper = true)
public class UserData extends ResultInfo implements Serializable {

    @Expose
    protected UserInfo userInfo;

    @Expose
    protected CreditInfo creditInfo;


    private UserData() {}
    private static UserData userData;

    public static UserData getInstance() {
        if (userData == null) {
            userData = new UserData();
        }
        return userData;
    }

    public static void setUserData(UserData user) {
        if (user == null){
            userData = null;
            return;
        }
        userData = getInstance();
    }

}
