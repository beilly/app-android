package com.vcredit.global;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.mcxiaoke.packer.helper.PackerNg;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;

import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by wangzhengji on 2016/1/26.
 */
public class App extends MultiDexApplication {
    /** 注销队列  */
    private static Stack<Activity> activityStack = new Stack<>();

    /** 添加Activity到堆栈   */
    public void addActivity(Activity activity) {
        if(activity != null && !activityStack.contains(activity)){
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /** app退出 */
    public void exit(Context context)
    {
        try {
            finishAllActivity();
            MobclickAgent.onKillProcess(this);
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 双击退出标志位
    private static Boolean isExit = false;

    /**
     * 双击退出app
     *
     * @param activity
     */
    public boolean exitBy2Click(Activity activity) {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            TooltipUtils.showToastS(activity, "再按一次退出程序");
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

            return false;
        } else {
            //退出app
            exit(activity);
            return true;
        }
    }

    /** 获得Application对象 */
    private static App appInstance;
    public static App getInstance() {
        return appInstance;
    }
    public static boolean isLogined = false;
    public static String channel = "unknown";

    @Override
    public void onCreate() {
        super.onCreate();
        appInstance = this;
        iconFont =  Typeface.createFromAsset(getAssets(), AppConfig.ICONFONT_PATH);
        Fresco.initialize(getApplicationContext());

        MobclickAgent.setDebugMode(AppConfig.DEBUG);
        // 如果没有使用PackerNg打包添加渠道，默认返回的是""
        // com.mcxiaoke.packer.helper.PackerNg
        channel = PackerNg.getMarket(this,"dev");
        // 之后就可以使用了，比如友盟可以这样设置
        AnalyticsConfig.setChannel(channel);

        if(AppConfig.DEBUG){
            LeakCanary.install(this);// 内存泄露检测工具
        }
    }


    /** 获得当前App版本号 */
    public int getVersionCode(){
        return CommonUtils.getPackageInfo(this).versionCode;
    }

    /** 获得当前App枚举版本号 */
    public int getEnumsVer(){
        return Integer.valueOf(SharedPreUtils.getInstance(this)
                .getValue(SharedPreUtils.APP_ENUM_VERSION, "1"));
    }

    /** 使用的字体图标 */
    public static Typeface iconFont;
}
