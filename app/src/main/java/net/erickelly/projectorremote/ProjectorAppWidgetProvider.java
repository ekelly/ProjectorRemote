package net.erickelly.projectorremote;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.widget.RemoteViews;
import android.widget.Toast;


public class ProjectorAppWidgetProvider extends AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
//            Intent intent = new Intent(context, ProjectorAppWidgetProvider.class);

//            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
//                    PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent(context, PowerIntentService.class);
            PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.projector_widget);
            views.setOnClickPendingIntent(R.id.power, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private void toast(Context c, String message) {
        Toast t = Toast.makeText(c, message, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.BOTTOM, 0, 0);
        t.show();
    }

    // Called when the BroadcastReceiver receives an Intent broadcast.
    // Checks to see whether the intent's action is TOAST_ACTION. If it is, the app widget
    // displays a Toast message for the current item.
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("receiver", "got intent");
        toast(context, "Toggling projector power");
        Intent i = new Intent(context, PowerIntentService.class);
        context.startService(i);
        super.onReceive(context, intent);
    }


}
