package com.arbitrator.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.arbitrator.Background.Systemser;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class AlarmList extends AppCompatActivity {

    ListView alarmlist;

    String u, user, ud[][];
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    Systemser ss = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_list);

        alarmlist = (ListView) findViewById(R.id.alarmlist);

        ss = new Systemser(getApplicationContext());

        getalarms();

        //ALARM LIST ON CLICK LISTENER
        alarmlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (alarmlist.getItemAtPosition(0).toString().equalsIgnoreCase("No alarms yet!")) {
                        Toast.makeText(getApplicationContext(), "Set an Alarm First!", Toast.LENGTH_LONG);
                    } else {
                        SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

                        try {
                            Date d = sdf.parse(ud[position][2]);
                            ss.alarm(d);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    SimpleDateFormat sdf = new SimpleDateFormat("H:mm");

                    try {
                        Date d = sdf.parse(ud[position][2]);
                        ss.alarm(d);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    //GETTING ALARMS FROM MIDDLEWARE API
    private void getalarms() {
        ArrayList li = new ArrayList();
        try {
            u = getResources().getString(R.string.url);
            user = getResources().getString(R.string.user);
            spu = getSharedPreferences(user, Context.MODE_PRIVATE);
            spue = spu.edit();
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

            if (ud.length > 0) {
                for (int i = 0; i < ud.length; i++) {
                    li.add("Alarm --> " + ud[i][2]);
                }
            } else
                li.add("No Alarms yet!");

            final ArrayAdapter ada = new ArrayAdapter(this, android.R.layout.simple_list_item_1, li);

            alarmlist.setAdapter(ada);

        } catch (Exception e) {
            Log.e("getalarm", "down");
            e.printStackTrace();
        }
    }
}
