package com.ningjiahao.phhcomic.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.ningjiahao.phhcomic.R;
import com.ningjiahao.phhcomic.helper.SDCardHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by 甯宁寧 on 2016-11-23.
 */

public class DownLoadService extends IntentService{
    private String mDownloadUrl;
    private Notification notification;
    private NotificationManager notificationManager;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public DownLoadService(){
        super("DownLoadService");

    }


    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager= (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
    }

    /*@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){
            notifyMsg("温馨提醒", "文件下载失败", 0);
            stopSelf();
        }
        return super.onStartCommand(intent, flags, startId);
    }*/
    private void notifyMsg(String title, String content, int progress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.icon).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon)).setContentTitle(title);
        if (progress > 0 && progress < 100) {
            //下载进行中
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(content);
        if (progress >= 100) {
            //下载完成
            builder.setContentIntent(getInstallIntent());
        }
        notification = builder.build();
        notificationManager.notify(0, notification);
    }

  /*  @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent==null){
            notifyMsg("温馨提醒", "文件下载失败", 0);
            stopSelf();
        }
        mDownloadUrl=intent.getStringExtra("key");
        notifyMsg("温馨提醒", "开始下载", 0);
        HttpURLConnection conn = null;
        InputStream is = null;
        File file = Environment.getExternalStoragePublicDirectory("Download");
        // 指定存放图片的路径及图片的名称
        File newFile = new File(file, "download.apk");
        try {
            URL url = new URL(mDownloadUrl);
            conn = (HttpURLConnection) url.openConnection();
            Toast.makeText(this, "下载开始", Toast.LENGTH_SHORT).show();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(8000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                is = conn.getInputStream();
                byte[] buff = new byte[100];
                int len;
                // 将下载的图片写入到 SD 卡中
                FileOutputStream fos = new FileOutputStream(newFile);
                while ((len = is.read(buff)) != -1) {
                    fos.write(buff, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "网络出错", Toast.LENGTH_SHORT).show();
        } finally {
            if (is != null || conn != null) {
                try {
                    conn.disconnect();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        getInstallIntent();
    }


    private PendingIntent getInstallIntent(){
        /*File file = new File(SDCardHelper.getSDCardPublicDir(DIRECTORY_DOWNLOADS) + "APP文件名");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);*/
        File file = new File(SDCardHelper.getSDCardPublicDir(DIRECTORY_DOWNLOADS) + "download.apk");
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
        return null;
    }
    private void downloadFile(String url){

    }
}
