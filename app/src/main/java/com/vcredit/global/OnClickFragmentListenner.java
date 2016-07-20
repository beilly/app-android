package com.vcredit.global;

import android.view.View;

import java.util.Map;

/**
 * 从Fragment向外层回调的点击事件接口
 * Created by shibenli on 2016/3/23.
 */
public interface OnClickFragmentListenner{

    /**
     * 带字典参数的回调
     * @param view
     * @param params
     */
    public void onStatusFragmentClick(View view, Map<String, Object> params);
}
