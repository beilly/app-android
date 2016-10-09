package com.vcredit.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Unbinder;
import com.vcredit.global.OnClickFragmentListenner;
import com.vcredit.utils.CommonUtils;

import butterknife.ButterKnife;

/**
 * Created by shibenli on 2016/5/30.
 */
public abstract class AbsBaseFragment extends BaseFragment implements View.OnClickListener {
    final protected int REQUESTCODE_APPLYCREDIT = 0x0040;

    protected OnClickFragmentListenner fragmentListenner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnClickFragmentListenner){
            fragmentListenner = (OnClickFragmentListenner) fragment;
        }else if (activity instanceof OnClickFragmentListenner){
            fragmentListenner = (OnClickFragmentListenner) activity;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(layout(), container, false);
        } else {
            removeViewFromParent(mainView);
        }
        ButterKnife.bind(this, mainView);
        initView();
        return mainView;
    }

    @LayoutRes
    protected abstract int layout();

    /**
     * 初始化界面方法
     */
    private void initView() {
        // step 1 初始化标题栏（如果代码较多，可创建一个新方法。如标题栏内容需根据入口页定置请在step 2完成后再初始化标题栏）
        initTitleBar();

        // step 2 初始化数据（如果该页需从其他界面或接口获得数据请在此方法中获取，无需要则无需该方法）
        initData();
        // step 3 数据绑定（获取数据后在此方法中对控件进行赋值）
        dataBind();
    }

    /**
     * 初始化标题栏
     */
    protected void initTitleBar(){}

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 数据绑定
     */
    protected abstract void dataBind();

    protected void refreshView(){

    }

    @Override
    public void onResume() {
        super.onResume();
        refreshView();
    }

    @Override
    public void onClick(View view) {
        CommonUtils.LOG_D(getClass(), view);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}