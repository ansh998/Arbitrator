package com.arbitrator;

import android.Manifest;
import android.app.AlarmManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Handler;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Systemser {


    private final Context context;


    public Systemser(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Systemser(Context context, SharedPreferences sp) {
        this.context = context;
    }

    String t = "";
    SharedPreferences q;
    String user, u;
    ArrayList<String> ar;

    public void caller(String[] parts) {

        Pattern p = Pattern.compile("\\d+");
        if (parts.length == 2) {
            Matcher m = p.matcher(parts[1]);
            if (m.find()) {
                t = "Calling " + parts[1];
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                call(parts[1]);
            } else {
                ar = new ArrayList<>();
                String NAME = "";
                for (int i = 1; i < parts.length; i++) {
                    NAME += parts[i] + " ";
                }
                NAME = NAME.trim();
                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "lower(DISPLAY_NAME) = lower('" + NAME + "')", null, null);
                if (cursor.moveToFirst()) {
                    String ci = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor ph = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + ci, null, null);
                    while (ph.moveToNext()) {
                        String num = ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ar.add(num);
                    }
                    call_choose.xc = ar;
                    ph.close();
                }
                cursor.close();
                Intent cc = new Intent(context, call_choose.class);
                context.startActivity(cc);
            }
        } else {
            String number = "";
            for (int i = 1; i < parts.length; i++) {
                number += parts[i];
            }
            Matcher m = p.matcher(number);
            if (m.find()) {
                t = "Calling " + number;
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                call(number);
            } else {
                ar = new ArrayList<>();
                String NAME = "";
                for (int i = 1; i < parts.length; i++) {
                    NAME += parts[i] + " ";
                }
                NAME = NAME.trim();
                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "lower(DISPLAY_NAME) = lower('" + NAME + "')", null, null);
                if (cursor.moveToFirst()) {
                    String ci = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor ph = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + ci, null, null);
                    while (ph.moveToNext()) {
                        String num = ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ar.add(num);
                    }
                    call_choose.xc = ar;
                    ph.close();
                }
                cursor.close();
                Intent cc = new Intent(context, call_choose.class);
                context.startActivity(cc);
            }
        }

    }

    private void call(String s) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        context.startActivity(i);

    }

    public void alarm(Date d) {
        user = context.getResources().getString(R.string.user);
        u = context.getResources().getString(R.string.url);
        q = context.getSharedPreferences(user, Context.MODE_PRIVATE);
        int hr = d.getHours();
        int min = d.getMinutes();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        i.putExtra(AlarmClock.EXTRA_HOUR, hr);
        i.putExtra(AlarmClock.EXTRA_MINUTES, min);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "");
        t = "Alarm set for " + hr + ":" + min;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String cd = s.format(c.getTime());
        context.startActivity(i);
        try {
            JSONObject jo = null;
            String arr[][] = {
                    {"id", q.getString("id", "-1")},
                    {"alarm_id", cd},
                    {"alarm_time", hr + ":" + min},
                    {"alarm_text", "Wake up Time!"}
            };
            Helper pa = new Helper(u + "alarm", 2, arr);
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get();
            if (jo.isNull("error")) {
                Log.i("Alarm_sync", "Alarm Synced Successfully");
            } else {
                Toast.makeText(context, "Unable to Sync Alarm with server currently", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("alarm_post", e.getMessage());
        }
        MainActivity.t = t;
        MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
    }

}
