package com.vcredit.utils.net;

import android.content.Context;
import android.support.annotation.NonNull;

import com.vcredit.utils.TooltipUtils;

/**
 * Created by shibenli on 2016/6/13.
 * 通用的网络请求回调，发生错误时打印错误的Toast
 */
public abstract class AbsRequestListener implements RequestListener{
    @NonNull
    protected Context context;
    public AbsRequestListener(Context context){
        this.context = context;
    }

    @Override
    public void onError(String printMe) {
        TooltipUtils.showToastS(context, printMe);
    }
}
