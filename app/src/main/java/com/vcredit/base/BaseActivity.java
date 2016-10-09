package com.vcredit.base;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.vcredit.app.entities.UserData;
import com.vcredit.global.App;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.keyboard.KeyboardHelper;

import java.io.Serializable;
import java.util.Collection;

public class BaseActivity extends FragmentActivity {

    /**
     * 用户数据
     */
    protected UserData userData;
    /**
     * 保留app引用
     */
    protected App app;

    protected HttpUtil httpUtil;

    protected Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CommonUtils.LOG_D(getClass(), "BaseActicity_onCreate");
        refreshData(savedInstanceState);
    }

    // 点击空白区域 自动隐藏软键盘
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.LOG_D(getClass(), "BaseActicity_onDestroy");
        KeyboardHelper.hideKeyboard();
        httpUtil.cancelAllRequestQueue();
        httpUtil = null;
        app.finishActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData(null);
        MobclickAgent.onResume(this);
        CommonUtils.LOG_D(getClass(), "BaseActicity_onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        CommonUtils.LOG_D(getClass(), "BaseActicity_onPause");
        TooltipUtils.cancelAllToast();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        CommonUtils.LOG_D(getClass(), "BaseActicity_onSaveInstanceState");
        outState.putSerializable("UserData", userData);
    }

    /**
     * app意外终止时，使用该方法对数据进行恢复
     *
     * @param savedInstanceState
     */
    protected void refreshData(Bundle savedInstanceState) {
        app = App.getInstance();
        mActivity = this;
        app.addActivity(this);
        httpUtil = HttpUtil.getInstance(this);
        if (null != savedInstanceState) {
            refreshFromsavedInstanceState(savedInstanceState);
        }
        userData = UserData.getInstance();
    }

    protected void refreshFromsavedInstanceState(@NonNull Bundle savedInstanceState){
        UserData.setUserData((UserData) savedInstanceState.getSerializable("UserData"));
    }

    /**
     * replace Fragment
     * @param layout
     * @param fragment
     * @return
     */
    protected boolean replaceFragment(int layout, Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (!fragmentManager.isDestroyed()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (!isDestroyed() && !fragmentManager.isDestroyed()) {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(layout, fragment)
                            .commitAllowingStateLoss();
                }else {
                    return false;
                }
            } else {
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(layout, fragment)
                        .commitAllowingStateLoss();
            }
        }else {
            return false;
        }

        return true;
    }

    /**
     * 判断 TextView是否有数据
     * @param textViews
     * @return 有无效数据就返回true
     */
    public static boolean isTextViewHasNull(TextView... textViews){
        return BaseFragment.isTextViewHasNull(textViews);
    }

    /**
     * 判断 TextView是否有数据
     * @param textViews
     * @return 有无效数据就返回true
     */
    public static boolean isTextViewHasNull(Collection<TextView> textViews){
        return BaseFragment.isTextViewHasNull(textViews);
    }

    /**
     * 启动界面
     **/
    public static void launch(Activity self, Class<?> target) {
        Intent intent = new Intent();
        intent.setClass(self, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        self.startActivity(intent);
    }

    /**
     * 启动界面
     **/
    public static void launch(Activity self, String key, Serializable value, Class<?> target) {
        Intent intent = new Intent();
        intent.putExtra(key, value);
        intent.setClass(self, target);
        self.startActivity(intent);
    }

    /**
     * 启动界面
     **/
    public static void launch(Activity self, Bundle bundle, Class<?> target) {
        Intent intent = new Intent();
        intent.putExtras(bundle);
        intent.setClass(self, target);
        self.startActivity(intent);
    }

    /**
     * 启动当前界面，关闭时需返回结果
     **/
    public static void launchWithResult(Activity self, String key, Object value, Class<?> target, int requestCode) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) value);
        intent.putExtras(bundle);
        intent.setClass(self, target);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        self.startActivityForResult(intent, requestCode);
    }

    /**
     * 关闭当前界面，回传结果
     **/
    public void finishWithResult(String key, Object value, Class<?> target, int resultCode) {
        Intent intent = new Intent(this, target);
        Bundle bundle = new Bundle();
        bundle.putSerializable(key, (Serializable) value);
        intent.putExtras(bundle);
        this.setResult(resultCode, intent);
        this.finish();
    }
}
