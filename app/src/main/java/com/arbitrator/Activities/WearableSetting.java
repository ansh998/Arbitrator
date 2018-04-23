package com.arbitrator.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.arbitrator.Manifest;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;
import com.arbitrator.Services.NotificationService;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.Duration;

public class WearableSetting extends AppCompatActivity {

    Spinner a;
    ArrayList<String> cat;
    String u, ud[][];
    Button asd, asdf;
    Switch b, c;

    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_setting);

        u = getResources().getString(R.string.url);
        user = getResources().getString(R.string.user);
        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();
        asd = findViewById(R.id.asdfgh);
        a = findViewById(R.id.qweasd);
        asdf = findViewById(R.id.asdfg);
        b = findViewById(R.id.b);
        c = findViewById(R.id.c);

        if (spu.getInt("call", 0) == 1)
            b.setChecked(true);

        if (spu.getInt("notif", 0) == 1)
            if (NotificationService.isOn) {
                c.setChecked(true);
            } else {
                spue.putInt("notif", 0);
                spue.commit();
            }

        getalarms();

        cat = new ArrayList<String>();
        for (int i = 0; i < ud.length; i++) {
            cat.add(ud[i][2]);
        }

        ArrayAdapter<String> da = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);

        da.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        a.setAdapter(da);

        asd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setalarm();
            }
        });

        asdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.asd("^");
            }
        });

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    int PC = ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.READ_PHONE_STATE);
                    if (PC != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(WearableSetting.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 131);
                    }
                    spue.putInt("call", 1);
                } else
                    spue.putInt("call", 0);
                spue.commit();
            }
        });

        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!NotificationService.isOn) {
                        Intent in = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }
                    spue.putInt("notif", 1);
                } else
                    spue.putInt("notif", 0);
                spue.commit();
            }
        });

    }

    private void getalarms() {
        try {
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

    private void setalarm() {
        Date Ct = Calendar.getInstance().getTime();
        String val = a.getSelectedItem().toString();
        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
        Date St = null;
        try {
            St = sdf.parse(val);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int Chr, Cmin, Shr, Smin, dif = -99;

        Chr = Ct.getHours();
        Cmin = Ct.getMinutes();
        Shr = St.getHours();
        Smin = St.getMinutes();

        if (Shr > Chr) {
            dif = Shr - Chr;
            dif *= 60;

            if (Smin > Cmin) {
                dif += (Smin - Cmin);
            } else {
                dif -= 60;
                dif += 60 - (Cmin - Smin);
            }
        } else if (Shr == Chr) {
            dif = 0;
            if (Smin > Cmin) {
                dif += (Smin - Cmin);
            } else {
                dif -= 60;
                dif += 60 - (Cmin - Smin);
            }
        } else {
            dif = 24 - (Chr - Shr);
            dif *= 60;

            if (Smin > Cmin) {
                dif += (Smin - Cmin);
            } else {
                dif -= 60;
                dif += 60 - (Cmin - Smin);
            }
        }
        dif *= 60;

        MainActivity.asd("@" + dif);
    }

}
