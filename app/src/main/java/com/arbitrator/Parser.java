package com.arbitrator;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Parser {


    private final Context context;


    private Set set = null;
    private Appopen ao = null;
    private Systemser ss = null;
    private Calc ca = null;


    String parts[], t = "";


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


        switch (parts[0].toLowerCase()) {
            case "open":
            case "start": {
                openCase();
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
            case "set": {
                if (y.contains("alarm")) {
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
                t = "Sorry can't help with this right now!";
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
            }
            break;
        }
    }

    public void openCase() {
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
            //app
            else {
                int hits[] = new int[ao.appNameList.size()];
                int max = 0, in = -1;
                for (int i = 0; i < ao.appNameList.size(); i++) {
                    String ww = ao.appNameList.get(i);
                    int tt = 0;
                    for (int j = 1; j < parts.length; j++) {
                        if (parts[j].length() > 1)
                            if (ww.contains(parts[j].toLowerCase())) {
                                tt++;
                            }
                        Log.i("hits:", ww + " " + tt);
                    }
                    hits[i] = tt;
                    if (tt > max) {
                        max = tt;
                        in = i;
                    }
                }
                if (in != -1) {
                    t = "opening " + ao.appNameList.get(in);
                } else {
                    t = "Requested app is not installed !";
                }
                MainActivity.t = t;
                MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                ao.startApp(in);
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
                            t = "Stchinh to General Mode";
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            MainActivity.am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                            break;
                    }
                    break;
            }
        }
    }

}
