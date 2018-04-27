package com.arbitrator.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.arbitrator.Activities.MainActivity;
import com.arbitrator.R;

public class MicWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;
        for (int i = 0; i < count; i++) {
            int widgetId = appWidgetIds[i];
            Intent in = new Intent(context, MainActivity.class);
            in.putExtra("mode", "widget");
            PendingIntent pi = PendingIntent.getActivity(context, 0, in, 0);
            RemoteViews vi = new RemoteViews(context.getPackageName(), R.layout.mic_widget);
            vi.setOnClickPendingIntent(R.id.mic_btn, pi);
            appWidgetManager.updateAppWidget(widgetId, vi);
        }
    }
}