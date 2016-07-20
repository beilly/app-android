package com.vcredit.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.StatFs;

import com.vcredit.service.DownloadService;

/**
 * Created by wangzhengji on 2016/3/9.
 */
public class DownloadUtils {

    /**
     * 判断存储空间大小是否满足条件
     *
     * @param sizeByte
     * @return
     */
    public static boolean isAvaiableSpace(float sizeByte) {
        boolean ishasSpace = false;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            String sdcard = Environment.getExternalStorageDirectory().getPath();
            StatFs statFs = new StatFs(sdcard);
            long blockSize = statFs.getBlockSize();
            long blocks = statFs.getAvailableBlocks();
            float availableSpare = blocks * blockSize;
            if (availableSpare > (sizeByte + 1024 * 1024)) {
                ishasSpace = true;
            }
        }
        return ishasSpace;
    }

    /**
     * 开始安装apk文件
     *
     * @param context
     * @param localFilePath
     */
    public static void installApkByGuide(Context context, String localFilePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + localFilePath),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * 下载
     *
     * @param url      下载链接
     * @param context
     * @param fileName 文件名
     * @param size     文件大小（b）
     */
    public static void startDownload(String url, Context context,
                                     String fileName, Float size) {
        if (CommonUtils.detectSdcardIsExist()) {
            if (isAvaiableSpace(size)) {
                Intent intent = new Intent(context, DownloadService.class);
                intent.putExtra("downloadUrl", url);
                intent.putExtra("fileName", fileName);
                context.startService(intent);
            } else {
                TooltipUtils.showToastS((Activity) context, "存储卡空间不足");
            }
        } else {
            TooltipUtils.showToastS((Activity) context, "请检查存储卡是否安装");
        }
    }
}
