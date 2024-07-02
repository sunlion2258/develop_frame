package com.sun.dev.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.io.File;

/**
 * Created by fengwj on 2024/6/29.
 * 通过系统相册更新
 *
 */
public class PhotoUtils {
    private static final String CHANNEL_ID = "photo_update_channel";
    private static final int NOTIFICATION_ID = 1;

    // 调用此方法将新的图片添加到系统相册
    public static void addToSystemGallery(Context context, File imageFile) {
        MediaScannerConnection.scanFile(context,
                new String[]{imageFile.getAbsolutePath()},
                null,
                (path, uri) -> {
                    // 扫描完成后，可以发送通知
                    sendNotification(context);
                });
    }

    // 发送通知
    private static void sendNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // 适配 Android 8.0 及以上的通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Photo Update Channel";
            String description = "Channel for photo updates";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_gallery)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), android.R.mipmap.sym_def_app_icon))
                .setContentTitle("新照片已添加到相册")
                .setContentText("点击查看新照片")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // 显示通知
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
