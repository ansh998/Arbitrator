package com.arbitrator.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class ProfileSetting extends AppCompatActivity {

    EditText un, fn, dob;
    AppCompatButton up;
    RadioButton ma, fe;

    String use, gen, u;

    SharedPreferences su;
    SharedPreferences.Editor se;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        un = (EditText) findViewById(R.id.profile_un);
        fn = (EditText) findViewById(R.id.profile_fn);
        dob = (EditText) findViewById(R.id.profile_dob);
        up = (AppCompatButton) findViewById(R.id.profie_upd);
        ma = (RadioButton) findViewById(R.id.profile_male);
        fe = (RadioButton) findViewById(R.id.profile_female);
        use = getResources().getString(R.string.user);
        u = getResources().getString(R.string.url);

        un.clearFocus();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        su = getSharedPreferences(use, MODE_PRIVATE);
        se = su.edit();

        un.setText(su.getString("un", ""));
        fn.setText(su.getString("fn", ""));
        dob.setText(su.getString("dob", ""));

        if (su.getString("gen", "").equalsIgnoreCase("male")) {
            ma.setChecked(true);
            fe.setChecked(false);
        } else if (su.getString("gen", "").equalsIgnoreCase("female")) {
            ma.setChecked(false);
            fe.setChecked(true);
        }

        gen = su.getString("gen", "");


        un.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                up.setEnabled(true);
            }
        });

        fn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                up.setEnabled(true);
            }
        });

        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                up.setEnabled(true);
            }
        });

        ma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up.setEnabled(true);
            }
        });

        fe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                up.setEnabled(true);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });

    }

    //UPDATING PROFILE
    private void update() {
        try {
            if (ma.isChecked())
                gen = "male";
            else if (fe.isChecked())
                gen = "female";

            JSONObject jo = null;
            String arr[][] = {
                    {"username", un.getText().toString().toUpperCase()},
                    {"fullname", fn.getText().toString()},
                    {"dob", dob.getText().toString()},
                    {"gender", gen},
                    {"email", su.getString("em", "")},
                    {"id", su.getString("id", "-1")}
            };
            Helper pa = new Helper(u + "user", 2, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(20, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                se.putString("un", un.getText().toString().toUpperCase());
                se.putString("fn", fn.getText().toString());
                se.putString("dob", dob.getText().toString());
                se.putString("gen", gen);
                se.commit();
                Toast.makeText(getApplicationContext(), "User Profile Updated Successfully!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(getApplicationContext(), "Unable to Update currently! \nRetry Later", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("user_update", "down");
            e.printStackTrace();
        }
    }
}
