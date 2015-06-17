package com.h4ckademy.lextrend.lextrendcamera;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class NotificationThread extends AsyncTask<String, Void, Void>{

    private Context context;
    public NotificationThread (Context context) {
        this.context = context;
    }
    @Override
    protected Void doInBackground(String... IP) {
        final String serverIP = IP[0];
        final int serverPort = Integer.parseInt(IP[1]);
        final Notification notification = new Notification(context);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Socket socket = new Socket(serverIP, serverPort);
                    Log.wtf("Socket", "Socket created");
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (socket.isConnected()) {
                        String datos = input.readLine();
                        Log.wtf("Datos", datos);
                        if (datos.equals("Change")) {
                            notification.makeNotify();
                        }
                    }

                } catch (Exception e) {
                    Log.wtf("Socket", "Socket has some trouble opening");
                }
            }

        });
        thread.start();
        return null;
    }

}
