package com.arbitrator.Activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.arbitrator.Background.RSA;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.Middleware.JsonHandler2;
import com.arbitrator.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


public class Login extends AppCompatActivity {


    Button login, c;
    EditText em, pwd;
    TextView reg, fgtpwd;
    SignInButton sib;
    CheckBox re;
    ImageButton qr_scan;
    String arr[][], ud[][];
    int ud_len, frsa = 0;
    String u, dev_id, dev_name, emrsa[], pwdrsa;
    public static String det[] = new String[5];
    public static int goog = -1;
    public static String barcode = "";


    private FirebaseAuth mAuth;


    GoogleSignInClient mGoogleSignInClient;
    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    String rem;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;

    RSA rs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login = (Button) findViewById(R.id.btn_login);
        em = (EditText) findViewById(R.id.input_email);
        pwd = (EditText) findViewById(R.id.input_password);
        reg = (TextView) findViewById(R.id.link_signup);
        sib = (SignInButton) findViewById(R.id.gsio);
        re = (CheckBox) findViewById(R.id.rem_me);
        fgtpwd = (TextView) findViewById(R.id.link_frgt);
        qr_scan = (ImageButton) findViewById(R.id.qr_scan);

        sib.setColorScheme(SignInButton.COLOR_LIGHT);

        dev_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        dev_name = Build.MODEL;

        u = getResources().getString(R.string.url);
        rem = getResources().getString(R.string.rem);
        user = getResources().getString(R.string.user);

        sp = getSharedPreferences(rem, getApplicationContext().MODE_PRIVATE);
        spe = sp.edit();

        spu = getSharedPreferences(user, getApplicationContext().MODE_PRIVATE);
        spue = spu.edit();

        rs = new RSA(getApplicationContext());

        //gotomain();

        if (Integer.parseInt(spu.getString("id", "-1")) > -1) {
            getdev();
            int q = 0;
            for (int i = 0; i < ud_len; i++) {
                if (ud[i][1].equalsIgnoreCase(dev_id))
                    q = 1;
            }
            if (q == 1) {
                as.run();
            } else {
                Toast.makeText(getApplicationContext(), "Device Removed from ID", Toast.LENGTH_LONG).show();
                spue.remove("id");
                spue.commit();
            }
        }

        if (spu.getInt("rem", 0) == 1) {
            re.setChecked(true);
            em.setText(sp.getString("em", ""));
            pwd.setText(sp.getString("pwd", ""));
        }

        //SETTING ON CLICK LISTENER ON LOGIN BUTTON
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
            }
        });

        //SETTING ON CLICK LISTENER ON REGISTER BUTTON
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regis();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();

        //SETTING ON CLICK LISTENER ON GOOGLE SIGN IN BUTTON
        sib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gsin();
            }
        });

        //SETTING ON CLICK LISTENER ON FORGOT PASSWORD
        fgtpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (em.getText().toString().trim().length() > 0)
                    frgt_pwd();
                else
                    em.setError("This field cannot be Blank");
            }
        });

        //SETTING ON CLICK LISTENER ON QR SCANNER
        qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkcamera()) {
                    frsa = 0;
                    Intent cam = new Intent(getApplicationContext(), QrScan.class);
                    cam.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(cam);
                }
            }
        });

    }

    Runnable as = new Runnable() {
        @Override
        public void run() {

            getdet(spu.getString("em", ""));
            getval();
            gotomain();

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (barcode.length() > 0 && frsa == 0) {
            try {
                frsa = 1;
                String s = barcode.substring(0, 6);
                if (s.equalsIgnoreCase("inarco")) {
                    barcode = barcode.substring(6);
                    emrsa = barcode.split(":");
                    pwdrsa = rs.Decode(emrsa[1], 77, 221);
                    em.setText(emrsa[0]);
                    pwd.setText(pwdrsa);
                    barcode = "";
                    check();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //CHECKING FOR CAMERA HARDWARE
    private boolean checkcamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    //MAIN CHECKER FUNCTION
    private void check() {
        try {

            JSONObject jo = null;

            arr = new String[][]{
                    {"email", em.getText().toString()},
                    {"password", pwd.getText().toString()},
                    {"device_id", dev_id}
            };

            jo = Json(u, "login", 2, arr);

            if (jo.has("error")) {
                if (jo.getString("error").equalsIgnoreCase("email not found")) {
                    em.setError("Email not Found");
                } else if (jo.getString("error").equalsIgnoreCase("passwords not matched")) {
                    pwd.setError("Incorrect Password");
                } else if (jo.getString("error").equalsIgnoreCase("device not registered")) {
                    JSONObject Jt;
                    String Ta[][] = new String[][]{
                            {"type", "android"},
                            {"email", em.getText().toString()},
                            {"device_id", dev_id},
                            {"device_name", dev_name + "-" + dev_id.substring(4, 9)}
                    };
                    Jt = Json(u, "userdevices", 2, Ta);
                    if (Jt.isNull("error")) {
                        getdet(em.getText().toString());
                        //Toast.makeText(getApplicationContext(), "Ho Gaya", Toast.LENGTH_LONG).show();
                        if (re.isChecked()) {
                            spe.putString("em", em.getText().toString());
                            spe.putString("pwd", pwd.getText().toString());
                            spe.commit();
                            spue.putInt("rem", 1);
                            spue.commit();
                        } else {
                            spue.putInt("rem", 0);
                            spue.commit();
                        }
                        gotomain();
                    } else
                        Toast.makeText(getApplicationContext(), "Device already registered!", Toast.LENGTH_LONG).show();
                }
            } else {
                spue.putString("id", jo.getString("id"));
                spue.putString("un", jo.getString("username"));
                spue.putString("fn", jo.getString("fullname"));
                spue.putString("dob", jo.getString("dob"));
                spue.putString("em", jo.getString("email"));
                spue.putString("gen", jo.getString("gender"));
                spue.putString("sync", jo.getString("sync"));
                spue.commit();
                if (re.isChecked()) {
                    spe.putString("em", em.getText().toString());
                    spe.putString("pwd", pwd.getText().toString());
                    spe.commit();
                    spue.putInt("rem", 1);
                    spue.commit();
                } else {
                    spue.putInt("rem", 0);
                    spue.commit();
                }
                gotomain();
            }
        } catch (Exception e) {
            Log.d("nrml check", "down");
            e.printStackTrace();
        }
    }

    //COMBINED JSON FUNCTION
    public JSONObject Json(String url, String func, int w, String ar[][]) {
        Helper pa = new Helper(url + func, w, ar, getApplicationContext());
        JsonHandler jh = new JsonHandler();
        JSONObject jq = null;
        try {
            jq = jh.execute(pa).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return jq;
    }

    //ACTIVATING GOOGLE SINGIN
    private void Gsin() {
        Intent sii = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(sii, RC_SIGN_IN);
    }

    //TRANSFERRING TO MAIN ACTIVITY
    private void gotomain() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("mode", "normal");
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser account = mAuth.getCurrentUser();
        start(account);
    }

    //CHECKING USER REGISTRATION
    public void start(FirebaseUser a) {
        if (a != null) {
            //sendmail(a);

            try {
                JSONObject jo = null;
                Helper pa = new Helper(u + "emailcheck/" + a.getEmail(), 1, arr, getApplicationContext());
                JsonHandler jh = new JsonHandler();
                try {
                    jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (jo.isNull("error")) {
                    Toast.makeText(getApplicationContext(), "Unregistered Email Entered", Toast.LENGTH_SHORT).show();
                    GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
                    det[0] = acct.getDisplayName();
                    det[1] = acct.getGivenName();
                    det[2] = acct.getFamilyName();
                    det[3] = acct.getEmail();
                    det[4] = acct.getId();
                    goog = 99;
                    FirebaseAuth.getInstance().signOut();
                    Intent i = new Intent(getApplicationContext(), Register.class);
                    startActivity(i);
                } else {
                    JSONObject Jt;
                    String Ta[][] = new String[][]{
                            {"type", "android"},
                            {"email", a.getEmail()},
                            {"device_id", dev_id},
                            {"device_name", dev_name + "-" + dev_id.substring(4, 9)}
                    };
                    Jt = Json(u, "userdevices", 2, Ta);
                    if (Jt.isNull("error")) {
                        getdet(em.getText().toString());
                        //Toast.makeText(getApplicationContext(), "Ho Gaya", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.putExtra("mode", "normal");
                        startActivity(i);
                        signIn();
                        getdet(a.getEmail());
                        loggoog(a.getEmail());
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Device already registered!", Toast.LENGTH_LONG).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            } catch (Exception e) {
                Log.d("google", "down");
                e.printStackTrace();
            }

        }
    }

    //SIGNING IN USER
    private void signIn() {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personGivenName = acct.getGivenName();
            String personFamilyName = acct.getFamilyName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    //AUTHORISING USER FIREBASE
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            start(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                        }
                    }
                });
    }

    //SENDING EMAIL THROUGH FIREBASE
    public void sendmail(FirebaseUser a) {
        a.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("sjdfh", "email sent");
                        }
                    }
                });
    }

    //OPENING REGISTRATION FORM
    public void regis() {
        Intent bi = new Intent(getApplicationContext(), Register.class);
        startActivity(bi);
    }

    //GETTING SYNC VALUE
    private void getval() {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "synctoggle/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                spue.putString("sync", jo.getString("success"));
                spue.commit();
            }
        } catch (Exception e) {
            Log.e("stogget", "down");
            e.printStackTrace();
        }
    }

    //GETTING USER DETAILS
    public void getdet(String em) {
        try {
            JSONObject ob = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "user/" + em, 1, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            ob = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (ob != null) {
                spue.putString("id", ob.getString("id"));
                spue.putString("un", ob.getString("username"));
                spue.putString("fn", ob.getString("fullname"));
                spue.putString("dob", ob.getString("dob"));
                spue.putString("em", ob.getString("email"));
                spue.putString("gen", ob.getString("gender"));
                spue.putString("sync", ob.getString("sync"));
                spue.commit();
            }
        } catch (Exception e) {
            Log.e("getdata", "down");
            e.printStackTrace();
        }
    }


    //USER REGISTRATION CHECK USING FIREBASE
    public void loggoog(String em) {
        try {
            JSONObject ob = null;
            String arr[][] = new String[][]{
                    {"email", em},
                    {"device_id", dev_id}
            };
            Helper pa = new Helper(u + "logingmail", 2, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            ob = jh.execute(pa).get(10, TimeUnit.SECONDS);
        } catch (Exception e) {
            Log.e("logingmail", "down");
            e.printStackTrace();
        }
    }

    //GETTING USER DEVICES
    private void getdev() {
        try {
            String arr[][] = null;
            Helper pa = new Helper(u + "userdevices/" + spu.getString("id", "-1"), 1, arr, getApplicationContext());
            JsonHandler2 jh = new JsonHandler2();
            JSONArray jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            ud = new String[jo.length()][4];
            ud_len = jo.length();
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

    //FORGOT PASSWORD
    private void frgt_pwd() {
        try {
            JSONObject jo = null;
            String arr[][] = null;
            Helper pa = new Helper(u + "forgotpassword/" + em.getText().toString(), 1, arr, getApplicationContext());
            JsonHandler jh = new JsonHandler();
            jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
            if (jo.isNull("error")) {
                Intent ot = new Intent(getApplicationContext(), Otp.class);
                startActivity(ot);
                Otp.em = em.getText().toString();
                Otp.phr = Calendar.getInstance().getTime().getHours();
                Otp.pmi = Calendar.getInstance().getTime().getMinutes();
                Otp.flag = 0;
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Unable to process your request right now!", Toast.LENGTH_LONG).show();
                Log.e("login_frgtpwd", jo.getString("error"));
            }
        } catch (Exception e) {
            Log.e("login_frgtpwd", "down");
            e.printStackTrace();
        }
    }

}
