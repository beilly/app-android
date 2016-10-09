package com.vcredit.app.main.common;

import android.os.Bundle;
import android.os.Handler;

import com.vcredit.base.AbsBaseActivity;
import com.vcredit.app.entities.UserData;
import com.vcredit.app.main.MainActivity;
import com.vcredit.app.main.login.LoginActivity;
import com.vcredit.global.Constants;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.JsonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shibenli on 2016/6/30.
 * 登录功能的基类
 */
public abstract class BaseLoginActivity extends AbsBaseActivity {
    protected SharedPreUtils instance;
    protected String mUserName;
    protected boolean mAutoLogin;

    protected Handler UIHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = SharedPreUtils.getInstance(this);

        UIHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mUserName = instance.getValue(SharedPreUtils.USER_LOGINNAME, "");
                mAutoLogin = instance.getValue(SharedPreUtils.USER_AUTOLOGIN, false);
            }
        }, 200);
    }

    @Override
    protected int layout() {
        return 0;
    }

    @Override
    protected void initData() {

    }

    //自动登录
    protected void login() {
        Map<String, Object> map = new HashMap<>();
        map.put("loginName", mUserName);
        map.put("loginPwd", "");
        map.put("deviceId", CommonUtils.getIMEI(mActivity));
        map.put("loginKind", Constants.LOGIN_KIND_DEVICE);
        httpUtil.doPostByJson(httpUtil.getServiceUrl(InterfaceConfig.LOGIN), map, new RequestListener() {
            @Override
            public void onSuccess(String result) {
                CommonUtils.LOG_D(getClass(), result);
                UserData info = JsonUtils.json2Object(result, UserData.class);
                if (info != null) {
                    if (info.isOperationResult()) {
                        saveLoginInfo(info);
                        //getapplyProgressInfo();
                    } else {
                        TooltipUtils.showToastS(mActivity, info.getDisplayInfo(), 500);
                        openLoginPage();
                    }
                } else {
                    openLoginPage();
                }
            }

            @Override
            public void onError(String printMe) {
                TooltipUtils.showToastS(mActivity, printMe, 500);
                openLoginPage();
            }
        });
    }

    /**
     * 保存用户信息到本地
     */
    protected void saveLoginInfo(UserData info) {
        UserData.setUserData(info);
        instance.saveValue(SharedPreUtils.USER_LOGINNAME, mUserName);
        instance.saveValue(SharedPreUtils.USER_AUTOLOGIN, true);
        openMainActivity();
    }

    /**
     * 去主页
     */
    protected void openMainActivity() {
        TooltipUtils.showToastS(mActivity, "登录成功！", 500);
        MainActivity.launch(this, MainActivity.class);
        finish();
    }

    /**
     * 登录页
     */
    protected void openLoginPage() {
        instance.saveValue(SharedPreUtils.USER_AUTOLOGIN, false);
        launch(this, LoginActivity.class);
        finish();
    }

    /**
     * 自动登录失败或不自动登录到首页
     */
    protected void openHomePage() {
        //TODO 自动登录失败的处理
        openLoginPage();
    }
}
