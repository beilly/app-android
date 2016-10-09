package com.vcredit.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by shibenli on 2016/7/6.
 */
public abstract class AbsBaseActivity extends BaseActivity{
    Unbinder bind = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int layout = layout();
        if (layout > 0) {
            setContentView(layout);
            bind = ButterKnife.bind(this);
            initView();
        }
    }

    protected abstract @LayoutRes int layout();

    /**
     * 初始化界面方法
     */
    private void initView() {
        // step 1 初始化标题栏（如果代码较多，可创建一个新方法。如标题栏内容需根据入口页定置请在step 2完成后再初始化标题栏）
        initTitleBar();

        // step 2 初始化数据（如果该页需从其他界面或接口获得数据请在此方法中获取，无需要则无需该方法）
        initData();
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
     * 输入数据有效性检测
     * @return true 标示数据合法，false标示数据不合法
     */
    protected boolean inputCheck(){
        return true;
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
    }
}
