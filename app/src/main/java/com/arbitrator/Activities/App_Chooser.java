package com.arbitrator.Activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.arbitrator.Background.Appopen;
import com.arbitrator.Background.Parser;
import com.arbitrator.R;

import java.util.ArrayList;

public class App_Chooser extends AppCompatActivity {

    public static ArrayList<String> App_List;

    private Appopen ao = null;

    LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app__chooser);

        ao = Parser.ao;

        ll = (LinearLayout) findViewById(R.id.LL_all);

        try {

            if (App_List.size() > 0) {
                for (int i = 0; i < App_List.size(); i++) {

                    //CREATING NEW LINEAR LAYOUT
                    LinearLayout l = new LinearLayout(this);
                    l.setOrientation(LinearLayout.HORIZONTAL);
                    l.setId(i);

                    //CREATING NEW IMAGE VIEW
                    ImageView iv = new ImageView(this);
                    iv.setImageDrawable(getApplicationContext().getPackageManager().getApplicationIcon(App_List.get(i)));
                    iv.setId(i);

                    //CREATING NEW TEXT VIEW
                    TextView tv = new TextView(this);
                    tv.setText(
                            getApplicationContext()
                                    .getPackageManager()
                                    .getApplicationLabel(
                                            getApplicationContext()
                                                    .getPackageManager()
                                                    .getApplicationInfo(App_List.get(i), PackageManager.GET_META_DATA)
                                    )
                    );
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                    tv.setGravity(Gravity.CENTER_VERTICAL);
                    tv.setId(i);

                    //SETTING ON CLICK LISTENER ON LINEAR LAYOUT
                    l.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = v.getId();
                            int f = ao.appPackageList.indexOf(App_List.get(i));
                            String t = "Opening " + ao.appNameList.get(ao.appPackageList.indexOf(App_List.get(i)));
                            MainActivity.f_t = 1;
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            ao.startApp(f);
                            finish();

                        }
                    });

                    //SETTING ON CLICK LISTENER ON IMAGE VIEW
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = v.getId();
                            int f = ao.appPackageList.indexOf(App_List.get(i));
                            String t = "Opening " + ao.appNameList.get(ao.appPackageList.indexOf(App_List.get(i)));
                            MainActivity.f_t = 1;
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            ao.startApp(f);
                            finish();

                        }
                    });

                    //SETTING ON CLICK LISTENER ON TEXT VIEW
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int i = v.getId();
                            int f = ao.appPackageList.indexOf(App_List.get(i));
                            String t = "Opening " + ao.appNameList.get(ao.appPackageList.indexOf(App_List.get(i)));
                            MainActivity.f_t = 1;
                            MainActivity.t = t;
                            MainActivity.tt.speak(t, TextToSpeech.QUEUE_FLUSH, null);
                            ao.startApp(f);
                            finish();

                        }
                    });

                    l.addView(iv);
                    l.addView(tv);
                    ll.addView(l);

                }
            }
        } catch (Exception e) {
            Log.e("app_chooser", "down");
            e.printStackTrace();
        }
    }
}
