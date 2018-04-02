package com.arbitrator;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.Settings;

import java.time.Instant;

public class Set {


    private final Context context;


    public Set(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Set(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void wifi(String a) {
        WifiManager w = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        if (a.equalsIgnoreCase("close")) {
            w.setWifiEnabled(false);
        } else if (a.equalsIgnoreCase("open")) {
            w.setWifiEnabled(true);
        }
    }

    public void bt(String a) {
        BluetoothAdapter bt = BluetoothAdapter.getDefaultAdapter();
        if (a.equalsIgnoreCase("close")) {
            bt.disable();
        } else if (a.equalsIgnoreCase("open")) {
            bt.enable();
        }

    }

    public void flash(String a) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            android.hardware.Camera cam = android.hardware.Camera.open();
            android.hardware.Camera.Parameters p = cam.getParameters();
            p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);

            switch (a.toLowerCase()) {
                case "open":
                    cam.setParameters(p);
                    cam.startPreview();
                    break;
                case "close":
                    cam.stopPreview();
                    cam.release();
            }
        }
    }

}
    /*
    public void arplmd(String a) {
        int c = -1;
        switch (a.toLowerCase()) {
            case "open":
                Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
                c = 1;
                break;
            case "close":
                Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 0);
                c = 0;
                break;
        }
        Intent i = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        i.putExtra("state", c);
        context.sendBroadcast(i);
    }
    */

