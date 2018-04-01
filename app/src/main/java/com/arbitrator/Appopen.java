package com.arbitrator;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Appopen {


    public ArrayList<String> appNameList = new ArrayList<>();
    public ArrayList<String> appPackageList = new ArrayList<>();


    private final Context context;


    public Appopen(Context context) {
        this(context, PreferenceManager.getDefaultSharedPreferences(context));
    }

    public Appopen(Context context, SharedPreferences sp) {
        this.context = context;
        //tts = new Speaker(context);
    }

    public void startApp() {

        PackageManager pm = context.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> packages = pm.queryIntentActivities(i, 0);
        for (ResolveInfo ri : packages) {
            try {
                String pn = ri.activityInfo.packageName;
                String an = (String) pm.getApplicationLabel(pm.getApplicationInfo(pn, PackageManager.GET_META_DATA));
                boolean s = false;
                for (int j = 0; j < appNameList.size(); j++) {
                    if (pn.equals(appPackageList.get(j)))
                        s = true;
                }
                if (!s) {
                    appNameList.add(an.toLowerCase());
                    appPackageList.add(pn);
                }
                //Log.i("Check","package = <"+pn+"> name = <"+an+">");
            } catch (Exception e) {
            }
        }
    }

    public void startApp(int f) {
        if (f != -1) {
            activityStarter(context, appPackageList.get(f));
        } else {
            Toast.makeText(context, "App not Installed !", Toast.LENGTH_SHORT).show();
        }
    }

    public void activityStarter(Context c, String pn) {
        Intent i = c.getPackageManager().getLaunchIntentForPackage(pn);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(i);
    }

}
