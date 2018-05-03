package com.arbitrator.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;
import com.arbitrator.Services.NotificationService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SyncSetting extends AppCompatActivity {

    Switch b, qq;
    TextView c, e, f, g, h, i, j, k, l;
    Spinner d;
    Button rem, ala, note;
    Boolean check;
    String u, dev_id, curr_id;
    String ud[][];


    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;


    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_setting);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolb);
        setSupportActionBar(toolbar);

        user = getResources().getString(R.string.user);
        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();


        int s = Integer.parseInt(spu.getString("sync", "0"));
        u = getResources().getString(R.string.url);
        dev_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        mAuth = FirebaseAuth.getInstance();


        b = (Switch) findViewById(R.id.b);
        d = (Spinner) findViewById(R.id.d);
        c = (TextView) findViewById(R.id.c);
        e = (TextView) findViewById(R.id.e);
        f = (TextView) findViewById(R.id.f);
        g = (TextView) findViewById(R.id.g);
        h = (TextView) findViewById(R.id.h);
        i = (TextView) findViewById(R.id.i);
        j = (TextView) findViewById(R.id.j);
        k = (TextView) findViewById(R.id.k);
        l = (TextView) findViewById(R.id.l);
        rem = (Button) findViewById(R.id.btn_remdev);
        ala = (Button) findViewById(R.id.btn_syncalarm);
        note = (Button) findViewById(R.id.btn_syncnote);
        qq = (Switch) findViewById(R.id.alert_flag);


        getdev();
        getval();

        if (s == 1) {
            check = true;
        } else
            check = false;

        if (MainActivity.f_s == 1)
            qq.setChecked(true);
        else
            qq.setChecked(false);

        b.setChecked(check);
        if (check) {
            b.setTextColor(Color.GREEN);
            ala.setVisibility(View.VISIBLE);
            note.setVisibility(View.VISIBLE);
            qq.setVisibility(View.VISIBLE);
        }
        //changer(check);

        List<String> cat = new ArrayList<String>();
        for (int i = 0; i < ud.length; i++) {
            cat.add(ud[i][2]);
        }

        ArrayAdapter<String> da = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cat);

        da.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        d.setAdapter(da);

        d.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                f.setText(ud[position][2]);
                h.setText(ud[position][0]);
                if (ud[position][1].length() > 16)
                    j.setText(ud[position][1].substring(0, 16) + "...");
                else
                    j.setText(ud[position][1]);
                String n = ud[position][3];
                if (n.equalsIgnoreCase("1")) {
                    l.setText("Online");
                    l.setTextColor(Color.GREEN);
                } else if (n.equalsIgnoreCase("0")) {
                    l.setText("Offline");
                    l.setTextColor(Color.RED);
                }
                curr_id = ud[position][1];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        qq.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    MainActivity.f_s = 1;
                    if (!NotificationService.isOn) {
                        Intent in = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(in);
                    }
                } else {
                    MainActivity.f_s = 0;
                }
            }
        });

        b.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        JSONObject jo = null;
                        String arr[][] = new String[][]{
                                {"id", spu.getString("id", "-1")},
                                {"sync", "1"}
                        };
                        Helper pa = new Helper(u + "synctoggle", 2, arr, getApplicationContext());
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
                        if (jo.isNull("error")) {
                            Toast.makeText(getApplicationContext(), "Sync Started", Toast.LENGTH_SHORT).show();
                            //changer(true);
                            b.setTextColor(Color.GREEN);
                            ala.setVisibility(View.VISIBLE);
                            note.setVisibility(View.VISIBLE);
                            qq.setVisibility(View.VISIBLE);
                            spue.putString("sync", "1");
                            spue.commit();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to start Sync! Retry later", Toast.LENGTH_SHORT).show();
                            b.setChecked(false);
                            b.setTextColor(Color.RED);
                            ala.setVisibility(View.INVISIBLE);
                            note.setVisibility(View.INVISIBLE);
                            qq.setVisibility(View.INVISIBLE);
                            //changer(false);
                        }
                    } catch (Exception e) {
                        Log.e("Synctoggle", "down");
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jo = null;
                        String arr[][] = new String[][]{
                                {"id", spu.getString("id", "-1")},
                                {"sync", "0"}
                        };
                        Helper pa = new Helper(u + "synctoggle", 2, arr, getApplicationContext());
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
                        if (jo.isNull("error")) {
                            Toast.makeText(getApplicationContext(), "Sync Closed", Toast.LENGTH_SHORT).show();
                            //changer(false);
                            b.setTextColor(Color.RED);
                            ala.setVisibility(View.INVISIBLE);
                            note.setVisibility(View.INVISIBLE);
                            qq.setVisibility(View.INVISIBLE);
                            spue.putString("sync", "0");
                            spue.commit();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to close Sync! Retry later", Toast.LENGTH_SHORT).show();
                            b.setChecked(true);
                            b.setTextColor(Color.GREEN);
                            ala.setVisibility(View.VISIBLE);
                            note.setVisibility(View.VISIBLE);
                            qq.setVisibility(View.VISIBLE);
                            //changer(true);
                        }
                    } catch (Exception e) {
                        Log.e("Synctoggle", "down");
                        e.printStackTrace();
                    }
                }
            }
        });

        rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remdev(curr_id);
                if (curr_id.equalsIgnoreCase(dev_id)) {
                    logout();
                } else {
                    finish();
                    startActivity(getIntent());
                }
            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent no = new Intent(getApplicationContext(), NoteList.class);
                no.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(no);
            }
        });

        ala.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent no = new Intent(getApplicationContext(), AlarmList.class);
                no.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(no);
            }
        });

    }

    //GET USER DEVICES
    private void getdev() {
        try {
            String arr[][] = null;
            Helper pa = new Helper(u + "userdevices/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler2 jh = new JsonHandler2();
            JSONArray jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
            ud = new String[jo.length()][4];
            for (int i = 0; i < jo.length(); i++) {
                ud[i][0] = (jo.getJSONObject(i).getString("type"));
                ud[i][1] = (jo.getJSONObject(i).getString("device_id"));
                ud[i][2] = (jo.getJSONObject(i).getString("device_name"));
                ud[i][3] = (jo.getJSONObject(i).getString("status"));
            }
        } catch (Exception e) {
            Log.e("userdevget", "down");
            e.printStackTrace();
        }
    }

    //LOGOUT
    private void logout() {
        FirebaseUser account = mAuth.getCurrentUser();
        if (account != null)
            FirebaseAuth.getInstance().signOut();

        Intent li = new Intent(getApplicationContext(), Login.class);
        startActivity(li);
        spue.remove("id");
        spue.commit();
        finish();
    }

    //REMOVE DEVICE
    private void remdev(String id) {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "userdevices/" + id, 3, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                Toast.makeText(getApplicationContext(), "Device Removed Successfully", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Sorry Unable to Remove Device Currently \n Retry Later", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("rem_dev", "down");
            e.printStackTrace();
        }
    }

    //GET SYNC VALUE
    private void getval() {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "synctoggle/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                spue.putString("sync", jo.getString("success"));
                spue.commit();
            }
        } catch (Exception e) {
            Log.e("stogget", "down");
            e.printStackTrace();
        }
    }

}
