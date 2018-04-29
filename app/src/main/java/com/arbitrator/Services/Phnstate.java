package com.arbitrator.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.arbitrator.Activities.MainActivity;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class Phnstate extends BroadcastReceiver {

    String user, u, dev_id;
    SharedPreferences spu;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                u = context.getResources().getString(R.string.url);
                user = context.getResources().getString(R.string.user);
                spu = context.getSharedPreferences(user, Context.MODE_PRIVATE);
                dev_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);


                if (spu.getInt("call", 0) == 1) {

                    Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(incomingNumber));

                    String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

                    String contactName = "";
                    Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            contactName = cursor.getString(0);
                        }
                        cursor.close();
                    }
                    if (contactName.length() == 0)
                        contactName = incomingNumber;


                    Toast.makeText(context, "Ringing State Number is -" + contactName, Toast.LENGTH_LONG).show();
                    MainActivity.asd("c" + contactName);

                    String val = "c:" + contactName + ":" + Build.MODEL + "-" + dev_id.substring(4, 9);
                    sendpref(val, context);

                    Thread.sleep(5000);
                    MainActivity.asd("f");

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendpref(String val, Context cd) {
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
            Helper pa = new Helper(u + "prefrence", 2, arr, cd);
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(10, TimeUnit.MINUTES);
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
}
