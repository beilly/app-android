package com.vcredit.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.vcredit.app.R;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.utils.net.GetJsonRequest;
import com.vcredit.utils.net.IMErrorListenr;
import com.vcredit.utils.net.IMJsonListener;
import com.vcredit.utils.net.PostJsonRequest;
import com.vcredit.utils.net.RequestListener;
import com.vcredit.view.LoadingDialog;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 网络通讯类
 */
public class HttpUtil {

    /**
     * 请求队列
     */
    private RequestQueue queue;
    /**
     * 上下文对象
     */
    private Context context;
    /**
     * 是否显示进度条
     */
    public static boolean isOpenProgressbar = true;
    /**
     * 会话识别号
     */
    public static String sessionId = "0000";

    /***
     * 构造函数
     */
    private HttpUtil(Context context) {
        queue = Volley.newRequestQueue(context);
        this.context = context;
        isOpenProgressbar = true;
    }

    private static HttpUtil httpUtil;

    public static HttpUtil getInstance(Context context) {
        if (httpUtil == null) {
            httpUtil = new HttpUtil(context);
        }
        if (context != httpUtil.context) {
            httpUtil.cancelAllRequestQueue();
            httpUtil = new HttpUtil(context);
        }

        return httpUtil;
    }

    /**
     * postJson请求
     *
     * @param url
     * @param params          Map
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doPostByJson(String url, Map<String, Object> params,
                                            RequestListener requestListener) {
        return doPostByJson(url, params, requestListener, isOpenProgressbar);
    }


    public Request<JSONObject> doPostByJson(String url, JSONObject jsonObject,
                                            RequestListener requestListener) {
        return doPostByJson(url, jsonObject, requestListener, isOpenProgressbar);
    }

    /**
     * postJson请求
     *
     * @param url
     * @param params          Map
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doPostByJson(String url, Map<String, Object> params,
                                            RequestListener requestListener, boolean isOpenProgressbar) {
        JSONObject jsonObject = new JSONObject(params);
        return doPostByJson(url, jsonObject, requestListener, isOpenProgressbar);
    }

    /**
     * postJson请求
     *
     * @param url
     * @param jsonObject      JSONObject
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doPostByJson(String url, JSONObject jsonObject,
                                            RequestListener requestListener, boolean isOpenProgressbar) {
        // 网络检查
        if (!checkNetState(context)) {
            Toast.makeText(context, R.string.net_error_check, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        isShowProgressDialog(isOpenProgressbar);

        CommonUtils.LOG_D(getClass(), "url = %s,  params = %s", url, jsonObject.toString());
        Request<JSONObject> request = queue.add(new PostJsonRequest(url, jsonObject,
                new IMJsonListener(requestListener, context),
                new IMErrorListenr(requestListener)));

        // 为请求添加context标记
        request.setTag(context);
        return request;
    }

    /**
     * getJson请求
     *
     * @param url
     * @param requestListener
     * @return
     */
    public Request<JSONObject> doGetByJson(String url, RequestListener requestListener) {
        // 网络检查
        if (!checkNetState(context)) {
            Toast.makeText(context, R.string.net_error_check, Toast.LENGTH_SHORT)
                    .show();
            return null;
        }

        isShowProgressDialog();

        // 加入请求队列
        Request<JSONObject> request = queue.add(new GetJsonRequest(url, new JSONObject(),
                new IMJsonListener(requestListener, context),
                new IMErrorListenr(requestListener)));

        // 为请求添加context标记
        request.setTag(context);
        return request;
    }

    /**
     * 清除当前Activity的请求队列
     */
    public void cancelAllRequestQueue() {
        if (queue != null && context != null) {
            queue.cancelAll(context);
            queue.stop();
            queue = null;
            context = null;
            httpUtil = null;
        }
    }

    /**
     * 设置ProgressDialog的显示模式
     * true 为自动模式（ProgressDialog的显示消失和请求的开始结束关联）默认
     * false 为手动模式（ProgressDialog的显示消失由开发者自己控制）
     *
     * @param isOpen
     */
    public void setIsOpenProgressbar(boolean isOpen) {
        isOpenProgressbar = isOpen;
    }

    /**
     * 检测网络状态（true、可用 false、不可用）
     */
    public static boolean checkNetState(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager.getActiveNetworkInfo() != null) {
            return connManager.getActiveNetworkInfo().isAvailable();
        }

        return false;
    }

    /**
     * 获得完整的接口服务地址
     *
     * @param str
     * @return
     */
    public static String getServiceUrl(String str) {
        return new StringBuffer(InterfaceConfig.SERVER_URL).append(str).toString();
    }

    @Deprecated
    private synchronized void isShowProgressDialog() {
        // 是否显示进度条
        isShowProgressDialog(isOpenProgressbar);
    }

    private synchronized void isShowProgressDialog(boolean isOpenProgressbar) {
        // 是否显示进度条
        if (isOpenProgressbar) {
            ProgressDialog progressDialog = LoadingDialog.show(context, null);
            progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    // 当用户手动取消progressDialog时，取消队列
                    if (queue != null)
                        queue.cancelAll(context);

                    if (context != null && !"MainActivity".equals(context.getClass().getSimpleName()) && context instanceof Activity)
                        ((Activity) context).finish();// 取消网络链接时，关闭相应界面，防止界面异常
                }
            });
        }
    }

    /**
     * 创建参数字典
     *
     * @param useToken
     * @return
     */
    public synchronized static Map<String, Object> createParams(boolean useToken) {
        Map<String, Object> params = new HashMap<>();
        if (useToken) {
            //      params.put("accessToken", UserData.getInstance().getAccessToken());
        }
        return params;
    }
}
