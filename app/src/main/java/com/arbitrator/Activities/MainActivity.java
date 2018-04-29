package com.arbitrator.Activities;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.admin.DevicePolicyManager;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.arbitrator.Arduino.DeviceList;
import com.arbitrator.Background.Appopen;
import com.arbitrator.Background.Calc;
import com.arbitrator.Background.Parser;
import com.arbitrator.Background.Set;
import com.arbitrator.Background.Systemser;
import com.arbitrator.ChatDisplayHelper.ChatAdapter;
import com.arbitrator.ChatDisplayHelper.ChatMessage;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;
import com.arbitrator.Services.Admin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    TextView tin, dr_name, dr_em;
    ImageView dr_im;
    EditText tinp;
    ImageButton bspk;
    final int req = 100;
    public static String t = "";
    String y = "";
    public static String[] parts;
    ImageButton ok;
    ImageView asd;
    public static int in = 0;
    public static int flag = 0, f_bt = 0;
    public static String address = null;
    public ListView op;
    //    TextView op;
    private ChatAdapter adapter;


    FirebaseAuth mAuth;
    String u, iVal = "";
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


    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    static BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


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

//        per();

        spu = getSharedPreferences(user, Context.MODE_PRIVATE);
        spue = spu.edit();

        idd = spu.getString("id", "-1");


        tinp = findViewById(R.id.txtinp1);
        bspk = findViewById(R.id.btnSpeak);
        ok = findViewById(R.id.okbtn);
        op = findViewById(R.id.optv);
//        op.setMovementMethod(new ScrollingMovementMethod());


        tinp.clearFocus();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        Intent intent = getIntent();
        iVal = intent.getStringExtra("mode");
        if (iVal.equalsIgnoreCase("widget")) {
            if (Integer.parseInt(spu.getString("id", "-1")) > -1) {
                voiceRecog();
            } else {
                Intent log = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(log);
                finish();
            }
        }

        bspk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecog();
            }
        });


//        tinp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tinp.setText("");
//            }
//        });


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
                setmessage(false, y);
                pp.parse1(y);

//                op.setText(t);
                setmessage(true, t);
                tinp.setText("");
                y = "";
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headv = navigationView.getHeaderView(0);

        dr_name = (TextView) headv.findViewById(R.id.nav_name);
        dr_em = (TextView) headv.findViewById(R.id.nav_em);
        dr_im = (ImageView) headv.findViewById(R.id.nav_im);


        dr_name.setText(spu.getString("un", "").toLowerCase());
        dr_em.setText(spu.getString("em", ""));
        String c = spu.getString("fn", "").charAt(0) + "";
        c = c.toLowerCase();
        String cc = "@drawable/" + c;
        int ccc = getResources().getIdentifier(cc, null, getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dr_im.setImageDrawable(getDrawable(ccc));
        }

        dr_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileSetting.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        dr_em.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileSetting.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        dr_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProfileSetting.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

        loadWelcome();

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (flag == 1) {
            setmessage(false, y);
            pp.parse1(y);
//            op.setText(t);
            setmessage(true, t);
            tinp.setText("");
            y = "";
            flag = 0;
        }

        if (address != null && f_bt == 0) {
            new ConnectBT().execute();
            f_bt = 1;
        }

//        if (iVal.equalsIgnoreCase("widget")) {
//            iVal = "";
//            finish();
//        }
    }

    private void setmessage(Boolean val1, String val2) {
        ChatMessage a = new ChatMessage();
        a.setId(122);
        a.setMe(val1);
        a.setMessage(val2);
        a.setDate("");
        displayMessage(a);
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

    //activating voice recognition
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
        Disconnect();
    }

//    private void per() {
//        Intent intent = new Intent(DevicePolicyManager
//                .ACTION_ADD_DEVICE_ADMIN);
//        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, MainActivity.CN);
//        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
//                "Additional text explaining why this needs to be added.");
//
//        startActivityForResult(intent, 1);
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.menu_btn_sync) {
//            int i = 0;
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
            Intent i = new Intent(getApplicationContext(), SyncSetting.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.nav_abt_us) {
            Intent i = new Intent(getApplicationContext(), AboutUs.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.nav_arduino) {
            Intent i = new Intent(getApplicationContext(), DeviceList.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        } else if (id == R.id.nav_cnglog) {
            Intent i = new Intent(getApplicationContext(), changelog.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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
                jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
                if (jo.getString("success").equalsIgnoreCase("Successfully Logged Out")) {
                    Intent li = new Intent(getApplicationContext(), Login.class);
                    li.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(li);
                    spue.remove("id");
                    spue.commit();
                    finish();
                }
            } catch (Exception e) {
                Log.i("logout", "down");
                e.printStackTrace();
            }
        } else if (id == R.id.nav_jack) {
            Intent ws = new Intent(getApplicationContext(), WearableSetting.class);
            ws.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(ws);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    //sending data to wearable
    public static void asd(String a) {
        if (btSocket != null) {
            try {
                btSocket.getOutputStream().write(a.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //establishing bluetooth connection to wearable
    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try {
                if (btSocket == null || !isBtConnected) {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            } catch (IOException e) {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                Toast.makeText(getApplicationContext(), "Connection Failed. Is it a SPP Bluetooth? Try again.", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Connected.", Toast.LENGTH_LONG).show();
                try {
                    String q = spu.getString("un", "").toLowerCase() + ":" + spu.getString("em", "");
                    btSocket.getOutputStream().write(q.getBytes());
//                    Calendar cc = Calendar.getInstance();
//                    SimpleDateFormat ss = new SimpleDateFormat("dd/MM");
//                    String qw = "%" + ss.format(cc.getTime());
//                    btSocket.getOutputStream().write(qw.getBytes());
                } catch (Exception e) {
                    Log.e("cnctbt_async", "down");
                    e.printStackTrace();
                }
                isBtConnected = true;
            }
        }
    }

    private void Disconnect() {
        if (btSocket != null) //If the btSocket is busy
        {
            try {
                btSocket.close(); //close connection
            } catch (IOException e) {
                Log.e("Main_disconnect", "Error");
            }
        }
        finish(); //return to the first layout

    }

    private void loadWelcome() {
        ChatMessage a = new ChatMessage();
        a.setId(1);
        a.setMe(true);
        a.setMessage("Welcome To Arbitrator");
        a.setDate("");
        adapter = new ChatAdapter(MainActivity.this, new ArrayList<ChatMessage>());
        op.setAdapter(adapter);
        displayMessage(a);

    }

    public void displayMessage(ChatMessage message) {
        adapter.add(message);
        adapter.notifyDataSetChanged();
        scroll();
    }

    private void scroll() {
        op.setSelection(op.getCount() - 1);
    }

}

