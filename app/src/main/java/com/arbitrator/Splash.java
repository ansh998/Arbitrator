package com.arbitrator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Splash extends AppCompatActivity {


    String pms[] = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.INTERNET,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.SET_ALARM,
            Manifest.permission.WRITE_SETTINGS
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //ao = new Appopen(this);

        if (!haspms(this, pms)) {
            ActivityCompat.requestPermissions(this, pms, 131);
        } else {
            sta();
        }
    }

    private void sta() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
                finish();
            }
        }, 300);
    }

    private boolean haspms(Context context, String pm[]) {
        boolean allpm = true;
        for (int i = 0; i < pm.length; i++) {
            int a = context.checkCallingOrSelfPermission(pm[i]);
            if (a != PackageManager.PERMISSION_GRANTED) {
                allpm = false;
            }
        }
        return allpm;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 131:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED
                        && grantResults[3] == PackageManager.PERMISSION_GRANTED
                        && grantResults[4] == PackageManager.PERMISSION_GRANTED
                        && grantResults[5] == PackageManager.PERMISSION_GRANTED
                        && grantResults[6] == PackageManager.PERMISSION_GRANTED) {
                    sta();
                } else {
                    Toast.makeText(this, "Permission dede!!", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Toast.makeText(this, "Wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

}