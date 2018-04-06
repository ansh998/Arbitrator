package com.arbitrator;

import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.RecognizerResultsIntent;
import android.support.annotation.Nullable;
import android.text.NoCopySpan;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResults;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class MicWidget extends AppWidgetProvider {

    Context c;

    ImageButton ib;

    final int req = 100;

    Activity a;
    SharedPreferences sp;
    SharedPreferences.Editor spe;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        sp = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
        spe = sp.edit();

        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];

            Intent in = new Intent(context, MainActivity.class);
            PendingIntent pi = PendingIntent.getActivity(context, 0, in, 0);

            RemoteViews vi = new RemoteViews(context.getPackageName(), R.layout.mic_widget);
            spe.putString("widget", "1");
            spe.commit();
            vi.setOnClickPendingIntent(R.id.mic_btn, pi);

            appWidgetManager.updateAppWidget(widgetId, vi);

        }
    }



    /*
    public class a extends Activity {

        TextView res;
        final int req = 100;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            res = (TextView) findViewById(R.id.result_widget);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            switch (requestCode) {
                case req: {
                    if (resultCode == RESULT_OK && null != data) {
                        ArrayList<String> rslt = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                        res.setText(rslt.get(0));
                    }
                }
                break;
                case 1:

                    if (resultCode == Activity.RESULT_OK) {
                        Log.i("DeviceAdminSample", "Admin enabled!");
                    } else {
                        Log.i("DeviceAdminSample", "Admin enable FAILED!");
                    }
            }
        }

    }


    try {
                Intent in = new Intent(context, class);
                PendingIntent pi = PendingIntent.getActivity(context, 0, in, 0);

                RemoteViews vi = new RemoteViews(context.getPackageName(), R.layout.mic_widget);


                Intent ri = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                ri.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                ri.putExtra(RecognizerIntent.EXTRA_PROMPT, context.getString(R.string.speech_prompt));


                PendingIntent pivr = PendingIntent.getActivity(context, 0, ri, 0);

                vi.setOnClickPendingIntent(R.id.mic_btn, pivr);

                context.startActivity(ri);

                appWidgetManager.updateAppWidget(widgetId, vi);

            } catch (Exception e) {
                Toast.makeText(context.getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

    */
}




