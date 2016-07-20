package com.vcredit.app.main.launch;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;

import com.vcredit.app.entities.ApkInfo;
import com.vcredit.app.entities.ResourceVersionEntity;
import com.vcredit.global.App;
import com.vcredit.global.AppConfig;
import com.vcredit.global.InterfaceConfig;
import com.vcredit.app.main.common.BaseLoginActivity;
import com.vcredit.service.DownloadObserver;
import com.vcredit.service.DownloadService;
import com.vcredit.utils.CommonUtils;
import com.vcredit.utils.HttpUtil;
import com.vcredit.utils.JsonUtils;
import com.vcredit.utils.SharedPreUtils;
import com.vcredit.utils.TooltipUtils;
import com.vcredit.utils.net.RequestListener;

import java.io.File;
import java.util.Map;
import java.util.WeakHashMap;


/**
 * Created by shibenli on 2016/3/7.
 */
public class LaunchActivity extends BaseLoginActivity {

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
           if(msg.what==1001){
                progress= (int) msg.obj;
               if(progressDialog!=null){
                   progressDialog.setProgress(progress);
                   if(progress==100){
                       progressDialog.dismiss();
                   }
               }
            }

        }
    };
    //private HttpUtil mHttpUtil;
    private boolean flag = false;
    private Intent mIntent;
    //是否要更新app
    private boolean versionFlag = false;
    /*** 系统枚举版本号*/
    private int sysEnumsVer;
    /*** 当前App版本号*/
    private int versionNo;
    //获得资源最新版本
    private static final int VERSIONCODEENUM=1001;
    //获得系统枚举
    private static final int LAUNCH_SYS_ENUM =1002;
    //获得最新安装包信息
    private static final int LAUNCH_APP_VER =1003;
    //是否要更新系统枚举
    private boolean enumFlag;
    private boolean isDownloadFlag = false;
    private String downloadUrl;
    private boolean isDownloaded = false;
    // 启动时间
    private long startSeconds;
    // 结束时间
    private long endSeconds;
    // 最小延迟毫秒数
    private final int minDelaySeconds = 2000;
    private int progress;
    private ProgressDialog progressDialog;
    private DownLoadIdBroadCast broadcast;
    private DownloadObserver downloadObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        register();
        instantiation();
        initData();
    }

    //注册动态广播，用于接收当前下载的apk的downloadId
    private void register(){
        broadcast = new DownLoadIdBroadCast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_DOWNLOAD);
        registerReceiver(broadcast,filter);
    }

    private void instantiation(){
        mIntent = new Intent();
        httpUtil.setIsOpenProgressbar(false);
    }

    protected void initData(){
        startSeconds = System.currentTimeMillis();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!HttpUtil.checkNetState(this)) {
            TooltipUtils.showDialog(this, "当前无网络，是否设置？","", positiveListener, negativeListener, "设置", "退出",false,false);
            flag = true;
        }
        if (!flag && !versionFlag) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getResourceVer();
                }
            }, 200);
        }
    }

    // dialog确定按钮监听事件
    DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent(Settings.ACTION_SETTINGS));
            flag = false;
        }
    };

    // dialog取消按钮监听事件
    DialogInterface.OnClickListener negativeListener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            LaunchActivity.this.finish();
            App.getInstance().exit(LaunchActivity.this);
        }
    };

    //获得资源最新版本
    private void getResourceVer() {
        Map<String, Object> map = new WeakHashMap<>();
        httpUtil.doPostByJson(httpUtil.getServiceUrl(InterfaceConfig.GETRESOURCEVER),
                 map,new CommRequestListener(VERSIONCODEENUM));
    }

    //获得系统枚举
    private void getAppEnums() {
        Map<String, Object> map = new WeakHashMap<>();
        httpUtil.doPostByJson(httpUtil.getServiceUrl(InterfaceConfig.GETAPPENUMS),map,
                new CommRequestListener(LAUNCH_SYS_ENUM));
    }


    /**
     * 获得最新安装包信息
     */
    private void getAppVer() {
        Map<String, Object> map = new WeakHashMap<>();
        map.put("platform", "android");
        httpUtil.doPostByJson(httpUtil.getServiceUrl(InterfaceConfig.GETLASTVERSIONS),
                map, new CommRequestListener(LAUNCH_APP_VER));
    }
    class CommRequestListener implements RequestListener{
        private int type;

        public CommRequestListener(int id) {
            type = id;
        }
        @Override
        public void onSuccess(String result) {
            CommonUtils.LOG_D(getClass(), "result = " + result);
            switch (type) {
                case VERSIONCODEENUM:
                    getLastestResourceVer(result);
                    break;
               case LAUNCH_SYS_ENUM:
                    int lastVersion = Integer.valueOf(JsonUtils
                            .getJSONObjectKeyVal(result, "versionCodeEnum"));
                    saveSysEnumsVer(lastVersion, result);
                    enumFlag = true;
                    break;
                case LAUNCH_APP_VER:
                    versionFlag = isNewVersion(result);
                    break;
            }
            finishLaunch();

        }

        @Override
        public void onError(String printMe) {
            TooltipUtils.showToastS(LaunchActivity.this,printMe);
            finishLaunch();
        }
    }


    private void finishLaunch() {
        if (enumFlag && versionFlag && !isDownloadFlag) {
            endSeconds = System.currentTimeMillis();
            // 　判断如果时间间隔小于2秒，最多2秒延时，否则不延时
            long seconds = 0;
            if ((endSeconds - startSeconds) < minDelaySeconds) {
                seconds = minDelaySeconds - (endSeconds - startSeconds);
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!instance.getValue(SharedPreUtils.APP_PACKAGE_VERSION, "").equals(versionNo + "")) {
                        IntroduceActivity.launch(LaunchActivity.this);
                        instance.saveValue(SharedPreUtils.APP_PACKAGE_VERSION, versionNo + "");
                        finish();
                    } else {
                        //不是自动登录
                        if(!mAutoLogin) {
                            openHomePage();
                        }else{
                            //如果是自动登录
                            login();
                        }
                    }
                }
            }, seconds);
        }
    }


    /**
     * 检查枚举版本号是否有更新，如有则保存此次请求返回的Json枚举字符串
     */
    private void saveSysEnumsVer(int netVer, String json) {
        instance.saveValue(
                SharedPreUtils.APP_ENUM_INFO, json);
        instance.saveValue(
                SharedPreUtils.APP_ENUM_VERSION, String.valueOf(netVer));
    }

    /**
     * 检测app是否有版本更新，如有则弹窗提示用户
     */
    private boolean isNewVersion(String result) {
        ApkInfo versionsUpdateInfo = JsonUtils.json2Object(result,
                ApkInfo.class);
        if (versionsUpdateInfo != null) {
            StringBuilder filePath = new StringBuilder();
            filePath.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            filePath.append(File.separator);
            filePath.append(AppConfig.FILEPATH);
            filePath.append(File.separator);
            filePath.append(AppConfig.APPNAME);
            filePath.append("_v");
            filePath.append(versionsUpdateInfo.getVersionName());
            filePath.append(".apk");

            File file = new File(filePath.toString());
            if (file.exists()) {
                if (isDownloading(versionsUpdateInfo.getDownloadUrl())){
                    //TooltipUtils.showDialog(this, "正在下载新版本，请稍后...", null, null, null, null, false);
                    String id=instance.getValue(SharedPreUtils.DOWNLOADID,"-1");
                    if(Long.parseLong(id)!=-1) {
                        displayProgressDialog();
                        downloadObserver = new DownloadObserver(handler, LaunchActivity.this, Long.parseLong(id));
                        getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true,downloadObserver);
                    }
                    isDownloadFlag = true;
                    return true;
                }
                downloadUrl = filePath.toString();
                isDownloaded = true;
            } else {
                downloadUrl = versionsUpdateInfo.getDownloadUrl();
                isDownloaded = false;
            }
            Intent intent = new Intent(this, UpdateViewActivity.class);
            intent.putExtra("downloadUrl", downloadUrl);
            intent.putExtra("verNo", versionsUpdateInfo.getVersionName());
            intent.putExtra("isDownloaded", isDownloaded);
            intent.putExtra("appSize", versionsUpdateInfo.getAppSize());
            intent.putExtra("updateInfo", versionsUpdateInfo.getExplain());
            startActivityForResult(intent, 0);
            overridePendingTransition(0, 0);
            return false;
        }
        return true;
    }

    /**
     * 是否正在下载
     * @param url
     * @return
     */
    private boolean isDownloading(String url) {
        Cursor c = ((DownloadManager) getSystemService(DOWNLOAD_SERVICE))
                .query(new DownloadManager.Query().setFilterByStatus(DownloadManager.STATUS_RUNNING));
        if (c.moveToFirst()) {
            String tmpURI = c.getString(c.getColumnIndex(DownloadManager.COLUMN_URI));
            if (tmpURI.equals(url)) {
                if(c!=null&&!c.isClosed()){
                    c.close();
                }
                return true;
            }
        }
        if(c!=null&&!c.isClosed()){
            c.close();
        }
        return false;
    }

    /**
     *获取最新资源版本
     */
    private void getLastestResourceVer(String resVer) {
        // 获取当前APP版本号
        versionNo = CommonUtils.getAppVersionCode(this);
        // 从存储类中获得当前系统枚举版本号
        sysEnumsVer = Integer.valueOf(instance.getValue(SharedPreUtils.APP_ENUM_VERSION, "-1"));
        ResourceVersionEntity resourceVersionEntity = JsonUtils.json2Object(resVer, ResourceVersionEntity.class);
        if(resourceVersionEntity!=null){
            // app版本是否有更新
            AppConfig.netVer = resourceVersionEntity.getVersionCodeAndroid();
            if (AppConfig.netVer > versionNo) {
                getAppVer();
            } else {
                versionFlag = true;
            }
            // 系统枚举是否有更新
            int netSysEnumsVer = resourceVersionEntity.getVersionCodeEnum();
            if (netSysEnumsVer> sysEnumsVer) {
                getAppEnums();
            } else {
                enumFlag = true;
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK&&data != null) {
            versionFlag = true;
            isDownloadFlag = data.getBooleanExtra("isFinish", true);
            if (!isDownloaded) {
                displayProgressDialog();
                //TooltipUtils.showDialog(this, "正在下载新版本，请稍后...", null, null, null, null, false);
            }
            finishLaunch();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        App.getInstance().exitBy2Click(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null) {
            handler.removeCallbacksAndMessages(null);
        }
        if(null != broadcast) {
            unregisterReceiver(broadcast);
        }
        if(null != handler) {
            handler.removeCallbacksAndMessages(null);
        }
        if(downloadObserver!=null){
            getContentResolver().unregisterContentObserver(downloadObserver);
        }
        if(progressDialog!=null){
            progressDialog.dismiss();
        }
    }


    /**
     * 用于接收下载的id
     */
    public class DownLoadIdBroadCast extends BroadcastReceiver {
        public DownLoadIdBroadCast(){}
        @Override
        public void onReceive(Context context, Intent intent) {
            long downLoadId = intent.getLongExtra("id", -1);
            if(downLoadId!=-1) {
                downloadObserver = new DownloadObserver(handler, LaunchActivity.this, downLoadId);
                getContentResolver().registerContentObserver(Uri.parse("content://downloads/"), true,downloadObserver);
            }
        }
    }
    /**
     * 进度对话框
     */
    private void displayProgressDialog(){
        // 创建ProgressDialog对象
        progressDialog = new ProgressDialog(this);
        // 设置进度条风格，风格为长形
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        // 设置ProgressDialog 标题
        progressDialog.setTitle("下载提示");
        // 设置ProgressDialog 提示信息
        progressDialog.setMessage("当前下载进度:");
        // 设置ProgressDialog 标题图标
        //progressDialog.setIcon(R.drawable.a);
        // 设置ProgressDialog 进度条进度
        progressDialog.setProgress(100);
        // 设置ProgressDialog 的进度条是否不明确
        progressDialog.setIndeterminate(false);
        // 设置ProgressDialog 是否可以按退回按键取消
        progressDialog.setCancelable(false);
        // 让ProgressDialog显示
        progressDialog.show();

    }

}
