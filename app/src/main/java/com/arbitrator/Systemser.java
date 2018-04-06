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

import java.util.Date;
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

    public void caller(String[] parts) {

        Pattern p = Pattern.compile("\\d+");
        if (parts.length == 2) {
            Matcher m = p.matcher(parts[1]);
            if (m.find()) {
                call(parts[1]);
                t = "Calling " + parts[1];
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
            } else {
                String NAME = parts[1];
                ContentResolver cr = context.getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, "DISPLAY_NAME = '" + NAME + "'", null, null);
                if (cursor.moveToFirst()) {
                    String ci = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    Cursor ph = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + ci, null, null);
                    while (ph.moveToNext()) {
                        String num = ph.getString(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        int type = ph.getInt(ph.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        switch (type) {
                            case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:{

                            }
                        }
                    }
                    ph.close();
                }
                cursor.close();
            }
        } else {
            String number = "";
            for (int i = 1; i < parts.length; i++) {
                number += parts[i];
            }
            Matcher m = p.matcher(number);
            if (m.find()) {
                call(number);
                t = "Calling " + number;
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
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
        int hr = d.getHours();
        int min = d.getMinutes();
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        i.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        i.putExtra(AlarmClock.EXTRA_HOUR, hr);
        i.putExtra(AlarmClock.EXTRA_MINUTES, min);
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "");
        t = "Alarm set for " + hr + ":" + min;
        MainActivity.t = t;
        MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
        context.startActivity(i);
    }

}
