package com.arbitrator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SyncSetting extends AppCompatActivity {

    Switch b;
    TextView c, e, f, g, h, i, j, k, l;
    Spinner d;
    Boolean check;
    String u;
    String ud[][];


    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_setting);

        user = getResources().getString(R.string.user);
        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();


        int s = Integer.parseInt(spu.getString("sync", "0"));
        u = getResources().getString(R.string.url);

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


        getdev();

        if (s == 1) {
            check = true;
        } else
            check = false;

        b.setChecked(check);
        changer(check);

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
                l.setText(ud[position][3]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                        Helper pa = new Helper(u + "synctoggle", 2, arr);
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get();
                        if (jo.isNull("error")) {
                            Toast.makeText(getApplicationContext(), "Sync Started", Toast.LENGTH_SHORT).show();
                            changer(true);
                            spue.putString("sync", "1");
                            spue.commit();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to start Sync! Retry later", Toast.LENGTH_SHORT).show();
                            b.setChecked(false);
                            changer(false);
                        }
                    } catch (Exception e) {
                        Log.e("Synctoggle", e.getMessage());
                    }
                } else {
                    try {
                        JSONObject jo = null;
                        String arr[][] = new String[][]{
                                {"id", spu.getString("id", "-1")},
                                {"sync", "0"}
                        };
                        Helper pa = new Helper(u + "synctoggle", 2, arr);
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get();
                        if (jo.isNull("error")) {
                            Toast.makeText(getApplicationContext(), "Sync Closed", Toast.LENGTH_SHORT).show();
                            changer(false);
                            spue.putString("sync", "0");
                            spue.commit();
                        } else {
                            Toast.makeText(getApplicationContext(), "Unable to close Sync! Retry later", Toast.LENGTH_SHORT).show();
                            b.setChecked(true);
                            changer(true);
                        }
                    } catch (Exception e) {
                        Log.e("Synctoggle", e.getMessage());
                    }
                }
            }
        });
    }

    private void changer(Boolean a) {
        if (a) {
            c.setEnabled(a);
            d.setEnabled(a);
            e.setEnabled(a);
            f.setEnabled(a);
            g.setEnabled(a);
            h.setEnabled(a);
            i.setEnabled(a);
            j.setEnabled(a);
            k.setEnabled(a);
            l.setEnabled(a);
        } else {
            c.setEnabled(a);
            d.setEnabled(a);
            e.setEnabled(a);
            f.setEnabled(a);
            g.setEnabled(a);
            h.setEnabled(a);
            i.setEnabled(a);
            j.setEnabled(a);
            k.setEnabled(a);
            l.setEnabled(a);
        }
    }

    private void getdev() {
        try {
            String arr[][] = null;
            Helper pa = new Helper(u + "userdevices/" + spu.getString("id", "-1"), 1, arr);
            JsonHandler2 jh = new JsonHandler2();
            JSONArray jo = jh.execute(pa).get();
            ud = new String[jo.length()][4];
            for (int i = 0; i < jo.length(); i++) {
                ud[i][0] = (jo.getJSONObject(i).getString("type"));
                ud[i][1] = (jo.getJSONObject(i).getString("device_id"));
                ud[i][2] = (jo.getJSONObject(i).getString("device_name"));
                ud[i][3] = (jo.getJSONObject(i).getString("status"));
            }
        } catch (Exception e) {
            Log.e("userdevget", e.getMessage());
        }
    }

}
