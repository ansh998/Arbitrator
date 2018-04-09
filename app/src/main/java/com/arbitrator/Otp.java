package com.arbitrator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Calendar;

public class Otp extends AppCompatActivity {

    EditText k, np, ncp;
    Button snd, rsnd, cp;
    View v;
    LinearLayout ll;

    String u = null;

    public static String un, fn, em, pwd, cpwd, dob, gen;
    public static int phr, pmi, flag;

    int chr, cmi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        u = getResources().getString(R.string.url);

        k = (EditText) findViewById(R.id.input_otp);
        snd = (Button) findViewById(R.id.btn_otpsnd);
        rsnd = (Button) findViewById(R.id.btn_otprsnd);
        np = (EditText) findViewById(R.id.new_pwd);
        ncp = (EditText) findViewById(R.id.new_cnfrm_pwd);
        cp = (Button) findViewById(R.id.btn_pwdchange);
        v = (View) findViewById(R.id.v_otp);
        ll = (LinearLayout) findViewById(R.id.ll_pwd);

        snd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chr = Calendar.getInstance().getTime().getHours();
                cmi = Calendar.getInstance().getTime().getMinutes();
                if ((cmi - pmi) < 10) {
                    if (k.getText().toString().length() == 8 && flag != 0) {
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
                    } else {
                        v.setVisibility(View.VISIBLE);
                        ll.setVisibility(View.VISIBLE);
                    }
                } else
                    k.setError("OTP has expired! Resend OTP");
            }
        });

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (np.getText().toString().length() < 6)
                    np.setError("Password should be of min 6 characters");
                else if (ncp.getText().toString().length() < 6)
                    ncp.setError("Password should be of min 6 characters");
                else if (np.getText().equals(ncp.getText()))
                    ncp.setError("Confirm Password does not match Password");
                else {
                    try {
                        JSONObject jo = null;
                        String arr[][] = {
                                {"email", em},
                                {"password", np.getText().toString()}
                        };
                        Helper pa = new Helper(u + "user/1234", 4, arr, getApplicationContext());
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get();
                        if (jo.isNull("error")) {
                            Log.e("cngpwd_success", "ho gaya");
                            Intent lo = new Intent(getApplicationContext(), Login.class);
                            startActivity(lo);
                            finish();
                        } else {
                            Log.e("cngpwd_err", jo.getString("error"));
                            Toast.makeText(getApplicationContext(), "Sorry Unable to Change Password Currently!", Toast.LENGTH_LONG).show();
                            Intent lo = new Intent(getApplicationContext(), Login.class);
                            startActivity(lo);
                            finish();
                        }
                    } catch (Exception e) {
                        Log.e("cngpwd_catch", e.getMessage());
                    }
                }
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
                            {"email", em},
                            {"otp", ""}
                    };
                    Helper pa = new Helper(u + "emailverify/" + em, 4, arr, getApplicationContext());
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
            Helper pa = new Helper(u + "emailverify", 2, arr, getApplicationContext());
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
            Helper pa = new Helper(u + "Register", 2, arr, getApplicationContext());
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
        otpd();
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }

    private void otpd() {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "emailverify/" + em, 3, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get();
            if (jo.isNull("error"))
                Log.e("otpdel_success", "ho gaya");
            else
                Log.e("otpdel_success", jo.getString("error"));
        } catch (Exception e) {
            Log.e("otpdel_catch", e.getMessage());
        }
    }

}
