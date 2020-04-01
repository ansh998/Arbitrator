package com.arbitrator.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arbitrator.R;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class call_choose extends AppCompatActivity {

    public static ArrayList<String> xc;
    LinearLayout ll;
    String t = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_choose);

        ll = (LinearLayout) findViewById(R.id.LL_cll);

        for (int i = 0; i < xc.size(); i++) {
            TextView a = new TextView(this);
            a.setText(xc.get(i));
            a.setId(i);
            a.setTextSize(22);

            //SETTING ON CLICK LISTENER ON TEXT VIEW
            a.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView r = (TextView) findViewById(v.getId());
                    String y = r.getText().toString();
                    String parts[] = y.split(" ");
                    Pattern p = Pattern.compile("\\d+");
                    if (parts.length == 1) {
                        Matcher m = p.matcher(parts[0]);
                        if (m.find()) {
                            t = "Calling " + parts[0];
                            MainActivity.f_t = 1;
                            MainActivity.t = t;
                            call(parts[0]);
                        }
                    } else {
                        String number = "";
                        for (int i = 0; i < parts.length; i++) {
                            number += parts[i];
                        }
                        Matcher m = p.matcher(number);
                        if (m.find()) {
                            t = "Calling " + number;
                            MainActivity.f_t = 1;
                            MainActivity.t = t;
                            call(number);
                        }
                    }
                }
            });
            a.setPadding(10, 10, 10, 10);
            ll.addView(a);
        }

    }

    //CALLING CALL INTENT
    private void call(String s) {

        Intent i = new Intent(Intent.ACTION_CALL);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setData(Uri.parse("tel:" + s));
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(i);
        finish();

    }

}
