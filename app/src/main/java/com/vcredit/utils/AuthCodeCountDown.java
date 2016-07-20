package com.vcredit.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

import com.vcredit.app.R;
import com.vcredit.global.Constants;

/**
 * Created by zhuofeng on 2015/9/7.
 */
public class AuthCodeCountDown extends CountDownTimer {

    private Button mButton;
    private Context mContext;
    private Handler mHandler;
    private TimeOutChangeMode mListener;
    private int resouce;
    public  interface TimeOutChangeMode{
        void changeMode();
    }

    public void setTimeOutChangeModeListener(TimeOutChangeMode mListener) {
        this.mListener = mListener;
    }

    /**
     * @param millisInFuture The number of millis in the future from the call
     *                       to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                       is called.
     * @param resouce  获取验证码按钮默认背景
     */
    public AuthCodeCountDown(long millisInFuture, Button btn, Context context, Handler handler,int resouce) {
        super(millisInFuture, 1000);
        this.mButton = btn;
        mContext = context;
        mHandler = handler;
        this.resouce=resouce;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        long second = 0L;
        if (millisUntilFinished > 0)
            second = millisUntilFinished / 1000;
        if (mButton != null)
            mButton.setText(second + "秒");
    }

    @Override
    public void onFinish() {
        if(null == mListener) {
            CommonUtils.LOG_D(getClass(),"xcqw listener 为null");
            if (mButton != null) {
                mButton.setText("发送");
                mButton.setEnabled(true);
                mButton.setBackgroundResource(resouce);
                mButton.setTextColor(mContext.getResources().getColor(R.color.bg_main));
            }
        }else{
            CommonUtils.LOG_D(getClass(),"xcqw listener 不为null");
            mListener.changeMode();
        }
        if (mHandler != null) {
            Message message =Message.obtain();
            message.what = Constants.REFRESH_AUTH_STATE;
            mHandler.sendMessage(message);
        }
    }
}
