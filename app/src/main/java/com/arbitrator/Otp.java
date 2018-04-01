package com.arbitrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

public class Otp extends AppCompatActivity {

    EditText k;
    Button snd, rsnd;

    String u = null;

    public static String un, fn, em, pwd, cpwd, dob, gen;
    public static int phr, pmi;

    int chr, cmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        u = getResources().getString(R.string.url);

        k = (EditText) findViewById(R.id.input_otp);
        snd = (Button) findViewById(R.id.btn_otpsnd);
        rsnd = (Button) findViewById(R.id.btn_otprsnd);

        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chr = Calendar.getInstance().getTime().getHours();
                cmi = Calendar.getInstance().getTime().getMinutes();
                if ((cmi - phr) < 10) {
                    if (k.getText().toString().length() == 8) {
                        ottp();
                        if (Register.otpc) {
                            if (process()) {
                                Toast.makeText(getApplicationContext(), "User Created Successfully", Toast.LENGTH_LONG);
                                fwd();

                            } else {
                                Toast.makeText(getApplicationContext(), "Unable to register currently! Please try later.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } else {
                            Log.e("jkdfhs", "otp galat");
                        }
                    }
                } else
                    k.setError("OTP has expired! Resend OTP");
            }
        });

        rsnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phr = Calendar.getInstance().getTime().getHours();
                pmi = Calendar.getInstance().getTime().getMinutes();
                try {
                    JSONObject jo = null;
                    String arr[][] = new String[][]{
                            {"email", ""},
                            {"otp", ""}
                    };
                    Helper pa = new Helper(u + "emailverify", 4, arr);
                    JsonHandler jh = new JsonHandler();
                    jo = jh.execute(pa).get();
                    if (jo.isNull("error"))
                        Log.e("otpsuccess", "dsf");
                } catch (Exception e) {
                    Log.e("otp", e.getMessage());
                }
            }
        });

    }

    private void ottp() {
        try {
            JSONObject jo = null;
            String arr[][] = new String[][]{
                    {"email", em},
                    {"otp", k.getText().toString()}
            };
            Helper pa = new Helper(u + "emailverify", 2, arr);
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get();
            if (jo.isNull("error"))
                Register.otpc = true;
        } catch (Exception e) {
            Log.e("otp", e.getMessage());
        }
    }

    private Boolean process() {
        try {
            JSONObject jo = null;
            String arr[][] = new String[][]{
                    {"username", un},
                    {"fullname", fn},
                    {"password", pwd},
                    {"dob", dob},
                    {"email", em},
                    {"gender", gen}
            };
            Helper pa = new Helper(u + "Register", 2, arr);
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get();
            if (jo.isNull("error"))
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e("RegProcess", e.getMessage());
            return false;
        }
    }

    public void fwd() {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }

}
