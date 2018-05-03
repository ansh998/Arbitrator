package com.arbitrator.Services;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.arbitrator.Activities.MainActivity;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
@SuppressLint("OverrideAbstract")
public class NotificationService extends NotificationListenerService {

    public static boolean isOn = false;

    String user, u, dev_id;
    SharedPreferences spu;

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        try {
            u = getResources().getString(R.string.url);
            user = getResources().getString(R.string.user);
            spu = getSharedPreferences(user, Context.MODE_PRIVATE);
            dev_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

            if (spu.getInt("notif", 0) == 1) {
                Notification a = sbn.getNotification();
                String r = a.tickerText.toString();
                MainActivity.asd("m" + r.replace("Message", "Msg"));

                if (spu.getString("sync", "0").equalsIgnoreCase("1") && MainActivity.f_s == 1) {
                    String val = "m=" + r + "=" + Build.MODEL + "-" + dev_id.substring(4, 9);
                    sendpref(val);
                }

                Thread.sleep(5000);
                MainActivity.asd("f");
            }
        } catch (Exception e) {
        }
    }

    private void sendpref(String val) {
        try {
            JSONObject jo = null;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String arr[][] = {
                    {"id", spu.getString("id", "-1")},
                    {"pref_id", s.format(c.getTime())},
                    {"pref_para", val},
                    {"pref_val", "0"}
            };
            Helper pa = new Helper(u + "prefrence", 2, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(20, TimeUnit.MINUTES);
            if (jo.isNull("error")) {
                Log.i("sendpref", "done");
            } else {
                Log.e("send_pref", jo.toString());
            }
        } catch (Exception e) {
            Log.e("send_pref", "down");
            e.printStackTrace();
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
