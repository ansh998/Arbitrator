package com.arbitrator.Services;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.arbitrator.Activities.Splash;


public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, BackgroundService.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //context.startActivity(i);
    }

}
