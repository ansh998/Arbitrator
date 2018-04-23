package com.arbitrator.Services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.arbitrator.Activities.MainActivity;
import com.arbitrator.R;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService {

    public static boolean isOn = false;

    String user;
    SharedPreferences spu;

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            user = getResources().getString(R.string.user);
            spu = getSharedPreferences(user, Context.MODE_PRIVATE);

            if (spu.getInt("notif", 0) == 1) {
                Notification a = sbn.getNotification();
                String r = a.tickerText.toString();
                MainActivity.asd("m" + r.replace("Message", "Msg"));
                Thread.sleep(5000);
                MainActivity.asd("f");
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onListenerDisconnected() {
        isOn = false;
    }

    @Override
    public void onListenerConnected() {
        isOn = true;
    }
}
