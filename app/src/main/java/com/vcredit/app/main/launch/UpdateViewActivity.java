package com.vcredit.app.main.launch;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.vcredit.app.R;
import com.vcredit.base.BaseActivity;
import com.vcredit.global.App;
import com.vcredit.global.AppConfig;
import com.vcredit.utils.CommonUtils;


/**
 * 更新提示页
 * Created by zhuofeng on 2015/8/6.
 */
public class UpdateViewActivity extends BaseActivity {

    private String downloadUrl;
    private StringBuffer fileName = new StringBuffer();
    private float appSize;
    private Intent intent;
    private String netAppVersion;
    private String updateInfo;
    private boolean isDownloaded;

    private Dialog dialog;
    private String[] info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_activity);
        instantiation();
    }

    protected void instantiation() {
        intent = this.getIntent();
        downloadUrl = intent.getStringExtra("downloadUrl");
        appSize = intent.getFloatExtra("appSize", 1f);
        netAppVersion = intent.getStringExtra("verNo");
        updateInfo = intent.getStringExtra("updateInfo");
        isDownloaded = intent.getBooleanExtra("isDownloaded", false);
        if (TextUtils.isEmpty(updateInfo)) {
            updateInfo = "无";
        }

        info = updateInfo.split("\\\\n");

        if (isDownloaded) {
            showInstallApkDialog();
        } else {
            fileName.append(AppConfig.APPNAME);
            fileName.append("_v");
            fileName.append(netAppVersion);
            fileName.append(".apk");
            showNewVerDialog();
        }

        dialog.show();

        /*SharedPreUtils.getInstance(UpdateViewActivity.this).
                saveValue(SharedPreUtils.NETAPPUPDATE, netAppVersion + "");*/
    }

    // 版本更新提示窗
    private void showNewVerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到有新版本，是否立即下载更新？");
        builder.setMessage(getInfo());
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                CommonUtils.startDownload(downloadUrl, UpdateViewActivity.this, fileName.toString(), appSize * 1024);
//                arg0.cancel();

                intent.putExtra("isFinish", true);
                setResult(RESULT_OK, intent);
                UpdateViewActivity.this.finish();
                overridePendingTransition(0, 0);
                //App.getInstance().exit(UpdateViewActivity.this);
//                TooltipUtils.showToastL(UpdateViewActivity.this, "正在下载新版本，请稍后...");
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                App.getInstance().exit(UpdateViewActivity.this);
            }
        });
    }

    // 已下载更新，提示用户是否立即安装
    private void showInstallApkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("检测到你已下载了新版本，是否立即安装更新？");
        builder.setMessage(getInfo());
        builder.setPositiveButton("立即安装", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                CommonUtils.installApkByGuide(UpdateViewActivity.this, downloadUrl);
                intent.putExtra("isFinish", true);
                setResult(RESULT_OK, intent);
                UpdateViewActivity.this.finish();
                overridePendingTransition(0, 0);
//                arg0.cancel();
//                App.getInstance().exit();
            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                arg0.cancel();
            }
        });
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                App.getInstance().exit(UpdateViewActivity.this);
//                intent.putExtra("isFinish", true);
//                setResult(RESULT_OK, intent);
//                UpdateViewActivity.this.finish();
//                overridePendingTransition(0, 0);

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            intent.putExtra("isFinish", true);
//            setResult(RESULT_OK, intent);
//            UpdateViewActivity.this.finish();
//            overridePendingTransition(0, 0);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private String getInfo() {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("更新内容：");
        int len = info.length;
        for (int i = 0; i < len; i++) {
            stringBuffer.append("\n");
            stringBuffer.append(info[i]);
        }
        return stringBuffer.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
