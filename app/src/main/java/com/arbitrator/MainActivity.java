package com.arbitrator;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Camera;
import android.inputmethodservice.Keyboard;
import android.media.AudioManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.service.autofill.RegexValidator;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.util.*;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    TextView tin, op, dr_name, dr_em;
    EditText tinp;
    ImageButton bspk;
    final int req = 100;
    static String t = "";
    String y = "";
    public static String[] parts;
    ImageButton ok;
    ImageView asd;
    static int in = 0;
    int flag = 0;

    FirebaseAuth mAuth;
    String u;
    String idd, dev_id;

    public static DevicePolicyManager DPM;
    public static ActivityManager AM;
    public static ComponentName CN;


    private Set set = null;
    private Appopen ao = null;
    private Systemser ss = null;
    private Parser pp = null;
    private Calc ca = null;


    public static TextToSpeech tt;
    public static AudioManager am;

    String user;
    SharedPreferences spu;
    SharedPreferences.Editor spue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ui);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dev_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        u = getResources().getString(R.string.url);
        mAuth = FirebaseAuth.getInstance();
        user = getResources().getString(R.string.user);
        am = (AudioManager) getSystemService(getApplicationContext().AUDIO_SERVICE);

        DPM = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        AM = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        CN = new ComponentName(getApplicationContext(), Admin.class);

        set = new Set(getApplicationContext());
        ao = new Appopen(getApplicationContext());
        ss = new Systemser(getApplicationContext());
        pp = new Parser(getApplicationContext());
        ca = new Calc(getApplicationContext());

        pp.setter(set, ao, ss, ca);

        ao.startApp();

        //per();

        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();

        idd = spu.getString("id", "-1");


        tinp = findViewById(R.id.txtinp1);
        bspk = findViewById(R.id.btnSpeak);
        ok = findViewById(R.id.okbtn);
        asd = findViewById(R.id.menubtn);
        op = findViewById(R.id.optv);

        op.setMovementMethod(new ScrollingMovementMethod());


        tinp.clearFocus();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        asd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menupop();
            }
        });

        bspk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog();
            }
        });


        /*tinp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinp.setText("");
            }
        });*/


        tt = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    tt.setLanguage(Locale.getDefault());
                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ao.startApp();
                y = tinp.getText().toString();
                pp.parse1(y);

                op.setText(t);
                tinp.setText("");
                y = "";
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headv = navigationView.getHeaderView(0);

        dr_name = (TextView) headv.findViewById(R.id.nav_name);
        dr_em = (TextView) headv.findViewById(R.id.nav_em);

        dr_name.setText(spu.getString("un", "").toLowerCase());
        dr_em.setText(spu.getString("em", ""));

        dr_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileSetting.class);
                startActivity(i);
            }
        });

        dr_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileSetting.class);
                startActivity(i);
            }
        });


    }

    private void menupop() {
        PopupMenu pop = new PopupMenu(this, asd);
        pop.getMenuInflater().inflate(R.menu.main_activity_actions, pop.getMenu());
        pop.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_btn_abtus:
                        //Code for option 1
                        break;

                    case R.id.menu_btn_cnglg:
                        Intent i = new Intent(getApplicationContext(), changelog.class);
                        startActivity(i);
                        break;

                    case R.id.menu_btn_lgout:
                        FirebaseUser account = mAuth.getCurrentUser();
                        if (account != null)
                            FirebaseAuth.getInstance().signOut();
                        try {
                            JSONObject jo = null;
                            String[][] arr = new String[][]{
                                    {"id", idd},
                                    {"device_id", dev_id}
                            };
                            Helper pa = new Helper(u + "Logout", 2, arr, getApplicationContext());
                            JsonHandler jh = new JsonHandler();
                            jo = jh.execute(pa).get();
                            if (jo.getString("success").equalsIgnoreCase("Successfully Logged Out")) {
                                Intent li = new Intent(getApplicationContext(), Login.class);
                                startActivity(li);
                                spue.remove("id");
                                spue.commit();
                                finish();
                            }
                        } catch (Exception e) {
                            Log.i("logout", e.getMessage());
                        }
                        break;

                    case R.id.menu_btn_sync:
                        Intent si = new Intent(getApplicationContext(), SyncSetting.class);
                        startActivity(si);
                        break;

                    default:
                        break;
                }
                return false;
            }
        });
        pop.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag == 1) {
            pp.parse1(y);
            op.setText(t);
            tinp.setText("");
            y = "";
            flag = 0;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case req: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> rslt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    y = rslt.get(0);
                    flag = 1;
                }
            }
            break;
            case 1: {
                if (resultCode == Activity.RESULT_OK) {
                    Log.i("DeviceAdminSample", "Admin enabled!");
                } else {
                    Log.i("DeviceAdminSample", "Admin enable FAILED!");
                }
            }
            break;
            case 0: {
                Log.e("fsd", "sda");
            }
        }
    }

    public void voiceRecog() {

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        i.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));

        try {
            startActivityForResult(i, req);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getApplicationContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    public void onPause() {
        super.onPause();
        spue.putString("widget", "0");
        spue.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tt != null) {
            tt.stop();
            tt.shutdown();
        }
    }

    private void per() {
        Intent intent = new Intent(DevicePolicyManager
                .ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, MainActivity.CN);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added.");

        startActivityForResult(intent, 1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_btn_sync) {
            int i = 0;
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            Intent i = new Intent(getApplicationContext(), SyncSetting.class);
            startActivity(i);
        } else if (id == R.id.nav_abt_us) {


        } else if (id == R.id.nav_cnglog) {
            Intent i = new Intent(getApplicationContext(), changelog.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
            FirebaseUser account = mAuth.getCurrentUser();
            if (account != null)
                FirebaseAuth.getInstance().signOut();
            try {
                JSONObject jo = null;
                String[][] arr = new String[][]{
                        {"id", idd},
                        {"device_id", dev_id}
                };
                Helper pa = new Helper(u + "Logout", 2, arr, getApplicationContext());
                JsonHandler jh = new JsonHandler();
                jo = jh.execute(pa).get();
                if (jo.getString("success").equalsIgnoreCase("Successfully Logged Out")) {
                    Intent li = new Intent(getApplicationContext(), Login.class);
                    startActivity(li);
                    spue.remove("id");
                    spue.commit();
                    finish();
                }
            } catch (Exception e) {
                Log.i("logout", e.getMessage());
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}