package com.arbitrator.Background;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.arbitrator.Activities.App_Chooser;
import com.arbitrator.Activities.MainActivity;
import com.arbitrator.Activities.NoteList;
import com.arbitrator.Activities.ReminderSet;
import com.arbitrator.Manifest;
import com.arbitrator.Middleware.Helper;
import com.arbitrator.Middleware.JsonHandler;
import com.arbitrator.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class Parser {


    private final Context context;


    private Set set = null;
    public static Appopen ao = null;
    private Systemser ss = null;
    private Calc ca = null;

    String user;
    SharedPreferences spu;


    String parts[], t = "", u;
    ArrayList<String> appList, temp1, temp2;


    public Parser(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Parser(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void setter(Set a, Appopen b, Systemser c, Calc d) {
        set = a;
        ao = b;
        ss = c;
        ca = d;
    }

    public void parse1(String y) {


        parts = y.split(" ");

        user = context.getResources().getString(R.string.user);
        spu = context.getSharedPreferences(user, Context.MODE_PRIVATE);

        if (y.toLowerCase().contains("what is the time")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("h:mm a");
            String cd = s.format(c.getTime());
            t = "The time is " + cd;
            MainActivity.t = t;
            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
        } else if (y.toLowerCase().contains("what is date") || y.toLowerCase().contains("what is today's date")) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat s = new SimpleDateFormat("EEE, MMM d yyyy");
            String cd = s.format(c.getTime());
            t = "Today is " + cd;
            MainActivity.t = t;
            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
        } else {

            u = context.getResources().getString(R.string.url);

            switch (parts[0].toLowerCase()) {
                case "open":
                case "start": {
                    openCase(y);
                }
                break;
                case "close":
                case "turn": {
                    settingcase();
                }
                break;
                case "call": {
                    ss.caller(parts);
                }
                break;
//            case "lock": {
//                if (MainActivity.DPM.isAdminActive(MainActivity.CN))
//                    MainActivity.DPM.lockNow();
//            }
//            break;
                case "show": {
                    if (y.contains("note")) {
                        if (spu.getString("sync", "0").equalsIgnoreCase("1")) {
                            Intent no = new Intent(context.getApplicationContext(), NoteList.class);
                            no.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(no);
                        } else {
                            t = "Sync is Disabled\nEnable it to view notes";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
                break;
                case "create": {
                    if (y.contains("note")) {
                        if (spu.getString("sync", "0").equalsIgnoreCase("1")) {
                            Intent no = new Intent(context.getApplicationContext(), NoteList.class);
                            no.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(no);
                        } else {
                            t = "Sync is Disabled\nEnable it to create notes";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                }
                break;
                case "set": {
                    if (y.contains("alarm")) {
                        alarmset(y);
                    } else if (y.contains("reminder")) {
                        reminderset();
                    }
                }
                break;
                case "calculate": {
                    try {
                        y = y.replace("X", "*");
                        y = y.replace("into", "*");
                        y = y.replace("x", "*");
                        t = String.valueOf(ca.evaluate(y.substring(y.indexOf(" ") + 1)));
                        MainActivity.t = t;
                        MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
                default: {
                    try {
                        JSONObject jo = null;
                        String arr[][] = {
                                {"question", "android:" + y}
                        };
                        Helper pa = new Helper(u + "aiquestion", 2, arr, context);
                        JsonHandler jh = new JsonHandler();
                        jo = jh.execute(pa).get(10, TimeUnit.SECONDS);
                        if (jo.isNull("error")) {
                            t = jo.getString("answer");
                            aiparser(t);
                        }
                    } catch (Exception e) {
                        Log.e("aiserverques_catch", "below");
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }

    private void reminderset() {
        String time = "";
        Intent rem = new Intent(context, ReminderSet.class);
        rem.putExtra("time", time);

    }

    public void openCase(String q) {
        //ao.startApp();

        if (parts.length > 1 && !parts[1].equalsIgnoreCase("arbitrator")) {
            //wifi
            if (parts[1].equalsIgnoreCase("wifi") || parts[1].equalsIgnoreCase("wi-fi")) {
                t = "Turning on WiFi";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                set.wifi("open");
            }
            //bluetooth
            else if (parts[1].equalsIgnoreCase("bluetooth")) {
                t = "Turning on Bluetooth";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                set.bt("open");
            }
            //flashlight
            else if (parts[1].equalsIgnoreCase("torch") || parts[1].equalsIgnoreCase("flashlight")) {
                t = "Turning on Flashlight";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                set.flash("open");
            } /*else if (parts.length == 2) {
                t = parts[1];
                ao.startApp(ao.appNameList.indexOf(t.toLowerCase()));
            }*/
            //silent-vibrate
            else if (parts[1].equalsIgnoreCase("silent")) {
                t = "Switching to Silent Mode";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
            //general
            else if (parts[1].equalsIgnoreCase("general") || parts[1].equalsIgnoreCase("normal")) {
                t = "Switching to General Mode";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }
//            //mobile data
//            else if (q.toLowerCase().contains("mobile") && q.toLowerCase().contains("data")) {
//                set.mobdata("open");
//                t = "Switching on Mobile Data";
//                MainActivity.t = t;
//                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//            }
            //app
            else {
                int hits[] = new int[ao.appNameList.size()];
                int max = 0, in = -1;
                appList = new ArrayList<>();
                temp1 = new ArrayList<>();
                temp2 = new ArrayList<>();
                for (int i = 0; i < ao.appNameList.size(); i++) {
                    String ww = ao.appNameList.get(i);
                    int tt = 0;
                    for (int j = 1; j < parts.length; j++) {
                        if (parts[j].length() > 1)
                            if (ww.contains(parts[j].toLowerCase())) {
                                tt++;
                            }
                        //Log.i("hits:", ww + "\t\t\t\t\t" + tt + "\ti=" + i + "\tj=" + j);
                    }
                    hits[i] = tt;
                    if (tt > max) {
                        max = tt;
                        in = i;
                    }
                    if (tt > 0) {
                        temp1.add(ao.appPackageList.get(i));
                        temp2.add(tt + "");
                    }
                }

                for (int i = 0; i < temp1.size(); i++) {
                    if (temp2.get(i).equalsIgnoreCase(max + "")) {
                        appList.add(temp1.get(i));
                    }
                }

                if (appList.size() > 1) {
                    Intent aci = new Intent(context, App_Chooser.class);
                    aci.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    App_Chooser.App_List = appList;
                    context.startActivity(aci);
                } else {
                    if (in != -1) {
                        t = "opening " + ao.appNameList.get(in);
                    } else {
                        String na = "";
                        for (int i = 1; i < parts.length; i++)
                            na = na + parts[i] + " ";
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=" + na));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                        t = "Requested app is not installed !";
                    }
                    MainActivity.t = t;
                    MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                    ao.startApp(in);
                }
            }
        }
    }

    public void settingcase() {
        if (parts[0].equalsIgnoreCase("close")) {
            switch (parts[1].toLowerCase()) {
                case "wi-fi":
                case "wifi":
                    t = "Turning off Wifi";
                    MainActivity.t = t;
                    MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                    set.wifi("close");
                    break;
                case "bluetooth":
                    t = "Turning off Bluetooth";
                    MainActivity.t = t;
                    MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                    set.bt("close");
                    break;
                case "torch":
                case "flashlight":
                    t = "Turning off Flashlight";
                    MainActivity.t = t;
                    MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                    set.flash("close");
                    break;
//                case "mobile":
//                    if (parts[3].equalsIgnoreCase("data")) {
//                        set.mobdata("close");
//                        t = "Switching on Mobile Data";
//                        MainActivity.t = t;
//                        MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                    }
//                    break;
//                case "data":
//                    set.mobdata("close");
//                    t = "Switching on Mobile Data";
//                    MainActivity.t = t;
//                    MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                    break;
            }
        } else if (parts[0].equalsIgnoreCase("turn")) {
            switch (parts[1].toLowerCase()) {
                case "on":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            t = "Turning on WiFi";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.wifi("open");
                            break;
                        case "bluetooth":
                            t = "Turning on Bluetooth";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.bt("open");
                            break;
                        case "torch":
                        case "flashlight":
                            t = "Turn on Flashlight";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.flash("open");
                            break;
                        case "silent":
                            t = "Switching to Silent Mode";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                            break;
                        case "general":
                        case "normal":
                            t = "Switching to General Mode";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            break;
//                        case "mobile":
//                            if (parts[3].equalsIgnoreCase("data")) {
//                                set.mobdata("open");
//                                t = "Switching on Mobile Data";
//                                MainActivity.t = t;
//                                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                            }
//                            break;
//                        case "data":
//                            set.mobdata("open");
//                            t = "Switching on Mobile Data";
//                            MainActivity.t = t;
//                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                            break;
                    }
                    break;
                case "off":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            t = "Turning off WiFi";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.wifi("close");
                            break;
                        case "bluetooth":
                            t = "turning off Bluetooth";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.bt("close");
                            break;
                        case "torch":
                        case "flashlight":
                            t = "Turning off Flashlight";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            set.flash("close");
                            break;
                        case "silent":
                            t = "Switching to General Mode";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            break;
//                        case "mobile":
//                            if (parts[3].equalsIgnoreCase("data")) {
//                                set.mobdata("close");
//                                t = "Switching on Mobile Data";
//                                MainActivity.t = t;
//                                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                            }
//                            break;
//                        case "data":
//                            set.mobdata("close");
//                            t = "Switching on Mobile Data";
//                            MainActivity.t = t;
//                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
//                            break;
                    }
                    break;
            }
        }
    }

    public void aiparser(String t) {

        switch (t.charAt(0)) {
            case '#': {
                t = t.substring(1);
                t = "open " + t;
                parts = t.split(" ");
                openCase(" ");
            }
            break;
            case '@': {
                t = t.substring(1);
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(t));
                context.startActivity(i);
            }
            break;
            default: {
                t = "Sorry can't help with this right now!";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    }

    private void alarmset(String y) {
        int t = -1;
        for (int i = 0; i < parts.length; i++) {
            if (parts[i].contains(":")) {
                t = i;
            }
        }
        if (t != -1) {
            SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
            Date d = null;
            try {
                d = sdf.parse(parts[t]);
                if (parts.length > t + 1) {
                    String da = "" + d.getHours();
                    int n = Integer.parseInt(da);
                    y = y.toLowerCase();
                    if ((y.contains("pm") || y.contains("p.m") || y.contains("p.m.")) && n < 13) {
                        n += 12;
                        if (n >= 24)
                            n -= 12;
                    } else if ((y.contains("am") || y.contains("a.m") || y.contains("a.m.")) && n > 11) {
                        n -= 12;
                    }
                    da = n + ":" + d.getMinutes();
                    d = sdf.parse(da);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ss.alarm(d);
        }
    }

}
