package com.arbitrator.Services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.arbitrator.Activities.MainActivity;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            Notification a = sbn.getNotification();
            String r = a.tickerText.toString();
            MainActivity.asd("m" + r.replace("Message", "Msg"));
            Thread.sleep(5000);
            MainActivity.asd("f");
        } catch (Exception e) {
        }
    }


}
