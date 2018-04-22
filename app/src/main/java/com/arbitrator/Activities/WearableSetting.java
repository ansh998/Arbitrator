package com.arbitrator.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;

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
    Button asd;

    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wearable_setting);

        u=getResources().getString(R.string.url);
        user = getResources().getString(R.string.user);
        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();
        asd=findViewById(R.id.asdfgh);
        a=findViewById(R.id.qweasd);

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
                String tyu = null;
                Date cc = Calendar.getInstance().getTime();
                String aq=a.getSelectedItem().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
                Date tt = null;
                try {
                    tt=dateFormat.parse(aq);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long mills = tt.getTime()-cc.getTime();
                System.out.print(mills);

              //  MainActivity.asd(tyu);
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

        } catch (Exception e){

        }
    }
}
