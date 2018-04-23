package com.arbitrator.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class BackgroundService extends Service {

    private Handler h;
    public static final long interval = 15 * 1000;

    String user, u, ud[][];
    ArrayList<String> cat;
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    private Runnable runn = new Runnable() {
        @Override
        public void run() {
            syncData();

            h.postDelayed(runn, interval);
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        h = new Handler();
        h.post(runn);
        return flags;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private synchronized void syncData() {
        getalarms();
        cat = new ArrayList<String>();
        for (int i = 0; i < ud.length; i++) {
            cat.add(ud[i][2]);
        }
    }

    private void getalarms() {
        try {
            u = getResources().getString(R.string.url);
            user = getResources().getString(R.string.user);
            spu = getSharedPreferences(user, Context.MODE_PRIVATE);
            spue = spu.edit();
            String arr[][] = null;
            Helper pa = new Helper(u + "alarm/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler2 jh = new JsonHandler2();
            JSONArray jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            ud = new String[jo.length()][4];
            for (int i = 0; i < jo.length(); i++) {
                ud[i][0] = (jo.getJSONObject(i).getString("id"));
                ud[i][1] = (jo.getJSONObject(i).getString("alarm_id"));
                ud[i][2] = (jo.getJSONObject(i).getString("alarm_time"));
                ud[i][3] = (jo.getJSONObject(i).getString("alarm_text"));
            }

        } catch (Exception e) {

        }
    }

}
