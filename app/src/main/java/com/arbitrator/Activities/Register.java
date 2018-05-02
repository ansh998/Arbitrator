package com.arbitrator.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText a, b, c, d, e, f, k;
    Button reg;
    RadioButton g, h;


    String un, fn, em, pwd, cpwd, dob, gen;

    String date, u;

    int otpf = -1, fun = 0, ffn = 0, fem = 0, fpw = 0, fdob = 0;

    public static Boolean otpc = false;

    public static Time st = null;
    public static Date dt = null;
    public static int hr, min;


    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        u = getResources().getString(R.string.url);

        //Date myDate;

        a = (EditText) findViewById(R.id.input_username);
        b = (EditText) findViewById(R.id.input_fullname);
        c = (EditText) findViewById(R.id.input_email);
        d = (EditText) findViewById(R.id.input_password);
        e = (EditText) findViewById(R.id.input_cnfrmpassword);
        f = (EditText) findViewById(R.id.input_dob);
        g = (RadioButton) findViewById(R.id.input_male);
        h = (RadioButton) findViewById(R.id.input_female);
        k = (EditText) findViewById(R.id.input_otp);
        //l = (ImageButton) findViewById(R.id.otp_btn);
        reg = (Button) findViewById(R.id.btn_register);

        a.clearFocus();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Login.goog == 99) {
            b.setText(Login.det[1] + " " + Login.det[2]);
            c.setText(Login.det[3]);
        }

        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                a.setError(null);
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                b.setError(null);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c.setError(null);
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.setError(null);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e.setError(null);
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                f.setError(null);
            }
        });

        a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("[^A-Za-z0-9_@.]");
                Matcher m = p.matcher(s);
                if (m.find()) {
                    a.setError("Invalid Username");
                    fun = 1;
                } else
                    fun = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        b.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Pattern p = Pattern.compile("[^A-Za-z ]");
                Matcher m = p.matcher(s);
                if (m.find()) {
                    b.setError("Invalid Fullname");
                    ffn = 1;
                } else {
                    ffn = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!Pattern.matches("([\\d\\w]+[.\\w\\d])+?([.\\w\\d])?@([\\w\\d]+[.\\w\\d]*)", s)) {
//                    Log.e("jasdkflj", m.toMatchResult().group());
                    c.setError("Invalid email");
                    fem = 1;
                } else
                    fem = 0;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

//        d.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!Pattern.matches("((?=.\\d)(?=.[a-z])(?=.[A-Z])(?=.[\\W]).{6,64})", s)) {
//                    d.setError("Invalid Password");
//                    fpw = 1;
//                } else
//                    fpw = 0;
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        f.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (!Pattern.matches("(0?[1-9]|1[0-2])[/](0?[1-9]|[12]\\d|3[01])[/](19|20)\\d{2}", s)) {
//                    f.setError("Invalid DOB");
//                    fdob = 1;
//                } else
//                    fdob = 0;
                get_date();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (verify_details()) {
                    if (Login.goog == 99) {
                        if (process())
                            fwd();
                    } else {
                        if (otp())
                            if (process())
                                fwd();
                    }
                }
            }
        });
    }

    //GETTING DATE
    private void get_date() {
        date = f.getText().toString();

        try {
            Date myDate = df.parse(date);
            String my = myDate.getDate() + "/" + (myDate.getMonth() + 1) + "/" + (1900 + myDate.getYear());
            Log.i("qwe", my);
        } catch (ParseException e) {
            f.setError("Invalid Date");
            Log.e("hgfdakj", "down");
            e.printStackTrace();
        }

        if (date.length() > 10)
            f.setError("Invalid Date");
    }

    //VERIFYING DETAILS
    private Boolean verify_details() {
        un = a.getText().toString();
        fn = b.getText().toString();
        em = c.getText().toString();
        pwd = d.getText().toString();
        cpwd = e.getText().toString();
        dob = f.getText().toString();

        if (g.isChecked())
            gen = "Male";
        else if (h.isChecked())
            gen = "Female";
        else {
            Toast.makeText(getApplicationContext(), "Please Select a Gender!", Toast.LENGTH_LONG).show();
            return false;
        }

        if (un.length() != 0) {
            try {
                JSONObject jo = null;
                String arr[][] = null;
                Helper pa = new Helper(u + "usernamecheck/" + un, 1, arr, getApplicationContext());
                JsonHandler jh = new JsonHandler();
                jo = jh.execute(pa).get(2, TimeUnit.SECONDS);
                if (!jo.isNull("error")) {
                    a.setError("Username Already Registered");
                    return false;
                }
            } catch (Exception e) {
                Log.e("uncheck", "down");
                e.printStackTrace();
            }
        } else {
            a.setError("This Field cannot be left Blank");
            return false;
        }

        if (fn.length() == 0) {
            b.setError("This Field cannot be left Blank");
            return false;
        }

        if (em.length() != 0) {
            try {
                JSONObject jo = null;
                String arr[][] = null;
                Helper pa = new Helper(u + "emailcheck/" + em, 1, arr, getApplicationContext());
                JsonHandler jh = new JsonHandler();
                jo = jh.execute(pa).get(2, TimeUnit.SECONDS);
                if (!jo.isNull("error")) {
                    c.setError("Email is Already Registered");
                    return false;
                }
            } catch (Exception e) {
                Log.e("emcheck", "down");
                e.printStackTrace();
            }
        } else {
            c.setError("This Field cannot be left Blank");
            return false;
        }

        if (pwd.length() == 0) {
            d.setError("This Field cannot be left Blank");
            return false;
        }

        if (cpwd.length() == 0) {
            e.setError("This Field cannot be left Blank");
            return false;
        }

        if (dob.length() == 0) {
            f.setError("This Field cannot be left Blank");
            return false;
        }

        if (!pwd.equals(cpwd)) {
            e.setError("Password and Confirm Password are not same");
            return false;
        }

        if (pwd.length() < 6) {
            d.setError("Password cannot be of less than 6 characters");
            return false;
        }

        if (fun == 1 || ffn == 1 || fpw == 1 || fem == 1 || fdob == 1)
            return false;

        return true;
    }

    //PROCESSING REGISTRATION
    private Boolean process() {
        try {
            JSONObject jo = null;
            String arr[][] = new String[][]{
                    {"username", un.toUpperCase()},
                    {"fullname", fn},
                    {"password", pwd},
                    {"dob", dob},
                    {"email", em},
                    {"gender", gen}
            };
            Helper pa = new Helper(u + "Register", 2, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error"))
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e("RegProcess", "down");
            e.printStackTrace();
            return false;
        }
    }

    //FORWARDING TO LOGIN PAGE
    public void fwd() {
        Intent i = new Intent(getApplicationContext(), Login.class);
        startActivity(i);
        finish();
    }

    //SENDING OTP
    private boolean otp() {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "emailverify/" + em, 1, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                otpf = 1;
                Intent qw = new Intent(getApplicationContext(), Otp.class);
                startActivity(qw);
                Otp.phr = Calendar.getInstance().getTime().getHours();
                Otp.pmi = Calendar.getInstance().getTime().getMinutes();
                Otp.cpwd = cpwd;
                Otp.dob = dob;
                Otp.em = em;
                Otp.fn = fn;
                Otp.gen = gen;
                Otp.pwd = pwd;
                Otp.un = un;
                Otp.flag = 1;
                finish();
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
