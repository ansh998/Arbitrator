package com.arbitrator;

import android.Manifest;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.AlarmClock;
import android.support.v4.app.ActivityCompat;
import android.text.format.Time;

import java.util.Date;

import static com.arbitrator.MainActivity.parts;

public class Systemser {


    private final Context context;


    public Systemser(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Systemser(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void caller() {
        if (parts.length == 2) {
            //if (Pattern.matches("[0-9]",parts[1])) {
            call(parts[1]);
            //}
        } else {
            String number = "";
            for (int i = 1; i < parts.length; i++) {
                number += parts[i];
            }
            call(number);
        }
        /*
        else {
            String NAME="";
            ContentResolver cr = getContentResolver();
            Cursor cursor= cr.query(ContactsContract.Contacts.CONTENT_URI,null,"DISPLAY_NAME = "+NAME+"'",null,null);
            if (cursor.moveToFirst()){
                //String
            }
        }
        */
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
        i.putExtra(AlarmClock.EXTRA_MESSAGE, "Uth JA Mohan Pyare \n Uth JA Lal Dulare");
        context.startActivity(i);
    }

}
