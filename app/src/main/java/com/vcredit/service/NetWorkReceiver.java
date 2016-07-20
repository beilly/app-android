/**      
 * BroadCastDemo.java Create on 2014-1-13     
 *      
 * Copyright (c) 2014 by GreenShore Network
 * Company: 上海绿岸网络科技有限公司(Shanghai GreenShore Network Technology Co.,Ltd.)

 */

package com.vcredit.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

import org.greenrobot.eventbus.EventBus;

/**
 * @ClassName: BroadCastDemo
 * @Description: 网络状态监听广播接收
 * @author 王卫
 * @date 2014-1-13 下午4:47:57
 * @Version 1.0
 * 
 */
public class NetWorkReceiver extends BroadcastReceiver {
	protected final String TAG = getClass().getSimpleName();
	public static final String ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

	private Context mContext = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		mContext = context;
		if (ACTION.equals(intent.getAction())) {
			// 获取手机的连接服务管理器，这里是连接管理器类
			ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			State wifiState = null;
			State mobileState = null;
			if (wifiInfo != null) {
				wifiState = wifiInfo.getState();
			}
			if (mobileInfo != null) {
				mobileState = mobileInfo.getState();
			}
			if (wifiState != null && (wifiState == State.CONNECTED || wifiState == State.CONNECTING)) {
			    doNetworkChanged(context, EnvStatusType.ENV_STATUS_WIFI);
			} else if (mobileState != null && (mobileState == State.CONNECTED || mobileState == State.CONNECTING)) {
			    doNetworkChanged(context, EnvStatusType.ENV_STATUS_MOBILE_3G);
			} else {
				if (!checkNetState(context)) {
				    doNetworkChanged(context, EnvStatusType.ENV_STATUS_NONETWORK);
				} else {
				    doNetworkChanged(context, EnvStatusType.ENV_STATUS_WIFI);
				}
			}
		}

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
     * 网络变化回调方法
     * @param context
     * @param networkType
     */
    private void doNetworkChanged(Context context, EnvStatusType networkType) {
        EventBus.getDefault().post(networkType);
    }


	public enum EnvStatusType {
		ENV_STATUS_NONETWORK(0),
		ENV_STATUS_MOBILE_2G(0x8001),
		ENV_STATUS_MOBILE_3G(0x8002),
		ENV_STATUS_WIFI(0x8003),
		ENV_ONLINE_MASK(0x8000);

		private int value;

		EnvStatusType(int value) {
			this.value = value;
		}

		public int value() {
			return value;
		}
	}
}
