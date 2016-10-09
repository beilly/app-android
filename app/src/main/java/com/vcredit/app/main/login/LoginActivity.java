package com.vcredit.app.main.login;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import com.vcredit.app.R;
import com.vcredit.app.entities.UserData;
import com.vcredit.app.main.MainActivity;
import com.vcredit.app.main.common.PopWithWebViewActivity;
import com.vcredit.app.main.common.ShowWithWebViewActivity;
import com.vcredit.base.AbsBaseActivity;
import com.vcredit.global.Constants;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.EncryptUtils;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.JsonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.VerifyUtils;
import com.vcredit.utils.net.AbsRequestListener;
import com.vcredit.utils.net.RequestListener;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by shibenli on 2016/3/7.
 * 登录页面
 */
public class LoginActivity extends AbsBaseActivity implements TextWatcher {
    //手机号码
    @BindView(R.id.et_phone)
    protected TextView etPhone;

    //登录密码
    @BindView(R.id.et_pwd)
    protected TextView etPassword;

    @BindView(R.id.btn_login)
    protected Button btnLogin;

    @Override
    protected int layout() {
        return R.layout.login_normal_activity;
    }

    @Override
    protected void initData() {
        etPhone.addTextChangedListener(this);
        etPassword.addTextChangedListener(this);
    }

    protected boolean inputCheck(){
        String msg = null;
        if (!VerifyUtils.isValidMobileNo(etPhone.getText().toString())){
            msg = getString(R.string.str_invalid_phone);
        }else if (!VerifyUtils.isValidPwd(etPassword.getText().toString())){
            msg = getString(R.string.str_invalid_password);
        }

        if (!TextUtils.isEmpty(msg)){
            TooltipUtils.showToastS(this, msg);
            return false;
        }

        return true;
    }

    @OnClick({R.id.btn_login, R.id.tv_forget_pwd, R.id.tv_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login: {//登录
                if (!inputCheck()){
                    return;
                }

                Map<String, Object> map = new HashMap<>();
                map.put("mobileNo", etPhone.getText().toString());
                map.put("password", EncryptUtils.md5(etPassword.getText().toString()));
                map.put("deviceId", CommonUtils.getIMEI(LoginActivity.this));
                map.put("loginKind", Constants.LOGIN_KIND_NORMAL);
                httpUtil.doPostByJson(HttpUtil.getServiceUrl(InterfaceConfig.LOGIN), map, loginRequestListener);
            }
            break;
            case R.id.tv_forget_pwd: {//忘记密码
                PopWithWebViewActivity.launch(this, PopWithWebViewActivity.KEY_URL, "http://www.baidu.com", PopWithWebViewActivity.class);
            }
            break;
            case R.id.tv_register: {//注册
                ShowWithWebViewActivity.launch(this, ShowWithWebViewActivity.KEY_URL, "http://www.baidu.com", ShowWithWebViewActivity.class);
            }
            break;

            default:
                CommonUtils.LOG_D(getClass(), view);
        }
    }

    protected RequestListener loginRequestListener = new AbsRequestListener(this) {
        @Override
        public void onSuccess(String result) {
            UserData userData = JsonUtils.json2Object(result, UserData.class);
            if (userData.isOperationResult()){//登录成功
                saveLoginInfo(userData);
            }else {
                TooltipUtils.showToastS(mActivity, userData);
            }
        }
    };

    //保存用户信息到本地
    private void saveLoginInfo(UserData info) {
        UserData.setUserData(info);
        SharedPreUtils.getInstance(this).saveValue(SharedPreUtils.USER_LOGINNAME, info.getUserInfo().getMobileNo());
        SharedPreUtils.getInstance(this).saveValue(SharedPreUtils.USER_AUTOLOGIN, true);
        openMainActivity();
    }

    //去首页
    private void openMainActivity() {
        TooltipUtils.showToastS(LoginActivity.this, "登录成功！", 500);
        launch(this, MainActivity.class);
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        btnLogin.setEnabled(!isTextViewHasNull(etPhone, etPassword));
    }
}
