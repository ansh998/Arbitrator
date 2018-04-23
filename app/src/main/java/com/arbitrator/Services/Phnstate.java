package com.arbitrator.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.arbitrator.Activities.MainActivity;
import com.arbitrator.R;

public class Phnstate extends BroadcastReceiver {

    String user;
    SharedPreferences spu;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {

                user = context.getResources().getString(R.string.user);
                spu = context.getSharedPreferences(user, Context.MODE_PRIVATE);

                if (spu.getInt("notif", 0) == 1) {

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
                    Thread.sleep(5000);
                    MainActivity.asd("f");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
