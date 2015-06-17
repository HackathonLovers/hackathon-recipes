package com.h4ckademy.lextrend.lextrendcamera;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.net.Inet4Address;
import java.net.Socket;

public class NotificationService extends Service{
    // Write the correct ip of your local conection in SERVERIP
    private final String SERVERIP = "192.168.1.149";
    private final String SERVERPORT = "8082";
    private NotificationThread notificationThread;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.wtf("Service", "Creating service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.wtf("On start service", "Intent received");
        notificationThread = new NotificationThread(this);
        notificationThread.execute(SERVERIP, SERVERPORT);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //App.getInstance().cancelPendingRequests(getClass().getSimpleName());
        super.onDestroy();
        //Write here the stops of the sockets/thread
        Log.wtf("On destroy service", "Destroying the service");

        try {
            notificationThread.cancel(true);
        }
        catch (Exception e) {
            Log.wtf("Close Socket", "Impossible");
        }
    }



}
