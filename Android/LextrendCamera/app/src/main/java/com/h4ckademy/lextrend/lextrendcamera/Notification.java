package com.h4ckademy.lextrend.lextrendcamera;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class Notification {

    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notification;

    public Notification (Context context) {
        this.context = context;
    }

    public void makeNotify() {
        createNotification();
        showNotification();
    }

    private void createNotification() {
        notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent serverService = new Intent(context, Mjpeg_Activity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, serverService, 0);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        notification = new NotificationCompat.Builder(context)
                .setContentTitle("Movement Detected")
                .setContentText("The camera has detected movement")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setSound(alarmSound);

    }

    private void showNotification() {
        notificationManager.notify(0,notification.build());
    }
}
