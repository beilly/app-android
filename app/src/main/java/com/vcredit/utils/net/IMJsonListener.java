package com.vcredit.utils.net;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;

import com.android.volley.Response;
import com.umeng.analytics.MobclickAgent;
import com.vcredit.app.BuildConfig;
import com.vcredit.app.R;
import com.vcredit.app.entities.ResponseInfo;
import com.vcredit.global.App;
import com.vcredit.app.main.login.LoginActivity;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.JsonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.view.LoadingDialog;

import org.json.JSONObject;

/**
 * 响应监听类，对正常返回进行后续处理（Listener<String>子类） 对返回信息进行预处理
 */
public class IMJsonListener implements Response.Listener<JSONObject> {

    private RequestListener requestListener;
    private Context mContext;

    public IMJsonListener(RequestListener requestListener) {
        this.requestListener = requestListener;
    }

    public IMJsonListener(RequestListener requestListener, Context context) {
        this.requestListener = requestListener;
        mContext = context;
    }

    @Override
    public void onResponse(JSONObject arg0) {
        try {
            LoadingDialog.dismiss();

            // 将JSONObject转换成String
            String responseText = arg0.toString();
            CommonUtils.LOG_D(getClass(), responseText);

            // 获得请求结果
            Resources res = mContext.getResources();

            // 返回对象为NULL、空、以及状态码为-1时
            if(CommonUtils.isNullOrEmpty(responseText)){
                this.requestListener.onError(res.getString(R.string.net_error_ununited));
                return;
            }

            // 获取状态码
            ResponseInfo responseInfo = JsonUtils.json2Object(arg0.toString(), ResponseInfo.class);

            // response不符合规范
            if(null == responseInfo){
                this.requestListener.onError(res.getString(R.string.net_error_ununited));
                return;
            }

            if("1".equalsIgnoreCase(responseInfo.getResCode())){// 判断返回码是否为1，1继续
                this.requestListener.onSuccess(JsonUtils.getJSONObjectKeyVal(responseText, "data"));
            }else if("99".equalsIgnoreCase(responseInfo.getResCode())){// 状态码为99时表示用户登录已过期
                DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("GOTO", "");
                        App.isLogined = false;
                        mContext.startActivity(intent);
                        SharedPreUtils.getInstance(mContext).saveValue(SharedPreUtils.USER_AUTOLOGIN, false);

                        App.getInstance().finishAllActivity();
                    }
                };
                TooltipUtils.showDialog(mContext,res.getString(R.string.common_tips_title), res.getString(R.string.net_token_error),
                        positiveListener, null, "确定", null);
                return;
            }else {//其他值则直接提示错误信息
                if (!TextUtils.isEmpty(responseInfo.getResCode())) {
                    this.requestListener.onError(responseInfo.getResMsg());
                } else {
                    this.requestListener.onError(res.getString(R.string.net_error_ununited));
                }
            }
        }catch (Exception e){
            if(BuildConfig.DEBUG) {//调试模式下直接抛出异常
                throw e;
            }else {//release环境下提示错误，同时上报异常
                this.requestListener.onError(mContext.getResources().getString(R.string.net_error_ununited));
                MobclickAgent.reportError(mContext,e);
            }
        }
    }
}
