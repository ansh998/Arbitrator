package com.arbitrator;

import android.content.Context;
import android.content.SharedPreferences;
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

    String parts[], t = "";

    public Parser(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Parser(Context context, SharedPreferences sp) {
        this.context = context;
    }

    public void setter(Set a, Appopen b, Systemser c) {
        set = a;
        ao = b;
        ss = c;
    }

    public void parse1(String y) {


        parts = y.split(" ");


        switch (parts[0].toLowerCase()) {
            case "open":
            case "start":
                openCase();
                break;
            case "close":
            case "turn":
                settingcase();
                break;
            case "call":
                ss.caller();
                break;
            case "set":
                if (y.contains("alarm")) {
                    int t = -1;
                    for (int i = 0; i < parts.length; i++) {
                        if (parts[i].contains(":")) {
                            t = i;
                        }
                    }
                    if (t != -1) {
                        SimpleDateFormat sdf = new SimpleDateFormat("h:mm");
                        Date d = null;
                        try {
                            d = sdf.parse(parts[t]);
                            if (parts.length > t + 1) {
                                String da = "" + d.getHours();
                                int n = Integer.parseInt(da);
                                if ((parts[t + 1].equalsIgnoreCase("pm") || parts[t + 1].equalsIgnoreCase("p.m") || parts[t + 1].equalsIgnoreCase("p.m.")) && n < 13) {
                                    n += 12;
                                    if (n > 24)
                                        n -= 24;
                                } else if ((parts[t + 1].equalsIgnoreCase("am") || parts[t + 1].equalsIgnoreCase("a.m") || parts[t + 1].equalsIgnoreCase("a.m.")) && n > 11) {
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
    }

    public void openCase() {
        //ao.startApp();

        if (parts.length > 1 && !parts[1].equalsIgnoreCase("arbitrator")) {
            if (parts[1].equalsIgnoreCase("wifi") || parts[1].equalsIgnoreCase("wi-fi")) {
                set.wifi("open");
            } else if (parts[1].equalsIgnoreCase("bluetooth")) {
                set.bt("open");
            } else if (parts[1].equalsIgnoreCase("torch") || parts[1].equalsIgnoreCase("flashlight")) {
                set.flash("open");
            } /*else if (parts.length == 2) {
                t = parts[1];
                ao.startApp(ao.appNameList.indexOf(t.toLowerCase()));
            }*/ else if (parts[1].equalsIgnoreCase("airplane")) {
                //s
            } else {
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
                    set.wifi("close");
                    break;
                case "bluetooth":
                    set.bt("close");
                    break;
                case "torch":
                case "flashlight":
                    set.flash("close");
                    break;
            }
        } else if (parts[0].equalsIgnoreCase("turn")) {
            switch (parts[1].toLowerCase()) {
                case "on":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            set.wifi("open");
                            break;
                        case "bluetooth":
                            set.bt("open");
                            break;
                        case "torch":
                        case "flashlight":
                            set.flash("close");
                            break;
                    }
                    break;
                case "off":
                    switch (parts[2].toLowerCase()) {
                        case "wi-fi":
                        case "wifi":
                            set.wifi("close");
                            break;
                        case "bluetooth":
                            set.bt("close");
                            break;
                        case "torch":
                        case "flashlight":
                            set.flash("close");
                            break;
                    }
                    break;
            }
        }
    }

}
