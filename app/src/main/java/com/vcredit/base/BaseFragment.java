package com.vcredit.base;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vcredit.app.R;
import com.vcredit.app.entities.UserData;
import com.vcredit.global.App;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.InputTools;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.view.DataPickerDialog;

import java.util.Collection;

/**
 * Fragment基类
 * Created by zhuofeng on 2015/6/17.
 */
public class BaseFragment extends Fragment {
    /**
     * 用户信息
     */
    protected UserData userData;
    /**
     * 保留app引用
     */
    protected App app;

    /**
     * 保留Activity引用
     */
    protected Activity activity;

    protected HttpUtil httpUtil;

    /**
     * 保留View引用
     */
    protected View mainView;

    /**
     * 标示是否是第一次调起显示
     */
    protected boolean isFirst = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst = mainView == null;
        CommonUtils.LOG_D(getClass(), "BaseFragment_onCreate");
        refreshData(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData(null);
        CommonUtils.LOG_D(getClass(), "BaseFragment_onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        CommonUtils.LOG_D(getClass(), "BaseFragment_onPause");
        InputTools.HideKeyboard(activity.getWindow().getDecorView());
        TooltipUtils.cancelAllToast();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CommonUtils.LOG_D(getClass(), "BaseFragment_onDestroy");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // 使当前FragmentActivity销毁时不保存fragment
        //super.onSaveInstanceState(outState);
        CommonUtils.LOG_D(getClass(), "BaseFragment_onSaveInstanceState");
        outState.putSerializable("UserData", userData);
    }

    /**
     * app意外终止时，使用该方法对数据进行恢复
     * @param savedInstanceState
     */
    protected void refreshData(Bundle savedInstanceState) {
        app = App.getInstance();
        activity = getActivity();
		httpUtil = HttpUtil.getInstance(activity);
        if (null != savedInstanceState) {
            refreshFromsavedInstanceState(savedInstanceState);
        }
        userData = UserData.getInstance();
    }

    protected void refreshFromsavedInstanceState(@NonNull Bundle savedInstanceState){
        UserData.setUserData((UserData) savedInstanceState.getSerializable("UserData"));
    }

    protected DataPickerDialog getDataPickerDialog(DataPickerDialog.OnDataSetListener callBack) {
        return new DataPickerDialog(activity, callBack).setSelectionDivider(new ColorDrawable(getResources().getColor(R.color.font_hint_gray)))
                .setSelectionDividerHeight(2);
    }

    /**
     * replace Fragment
     * @param layout
     * @param fragment
     * @return
     */
    protected boolean replaceFragment(int layout, Fragment fragment){
        FragmentManager fragmentManager = getChildFragmentManager();
        if (!fragmentManager.isDestroyed()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                if (activity != null && !activity.isDestroyed() && !fragmentManager.isDestroyed()) {
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
     * .从ViewGroup移除view
     * @param mainView
     */
    public static void removeViewFromParent(View mainView) {
        if (mainView != null && mainView.getParent() != null) {
            ((ViewGroup) mainView.getParent()).removeView(mainView);
        }
    }

    /**
     * 判断 TextView是否有数据
     * @param textViews
     * @return 有无效数据就返回true
     */
    public static boolean isTextViewHasNull(TextView... textViews){
        boolean result = false;
        if (textViews == null || textViews.length < 0) {
            return false;
        }else {
            for(TextView et : textViews){
                result = TextUtils.isEmpty(et.getText().toString());
                if (result)
                    break;
            }
        }

        return result;
    }

    /**
     * 判断 TextView是否有数据
     * @param textViews
     * @return 有无效数据就返回true
     */
    public static boolean isTextViewHasNull(Collection<TextView> textViews){
        boolean result = false;
        if (textViews == null || textViews.size() < 0) {
            return false;
        }else {
            for(TextView et : textViews){
                result = TextUtils.isEmpty(et.getText().toString());
                if (result)
                    break;
            }
        }

        return result;
    }
}
