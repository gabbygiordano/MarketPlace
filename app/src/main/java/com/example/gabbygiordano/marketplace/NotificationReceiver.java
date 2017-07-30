package com.example.gabbygiordano.marketplace;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import java.util.ArrayList;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by tanvigupta on 7/24/17.
 */

public class NotificationReceiver extends BroadcastReceiver {

    ArrayList<Notification> notifications;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("NotifReceiver", "Received intent");

        // Handle intents
        notifications = (ArrayList<Notification>) intent.getSerializableExtra("notifications");

        for (int i = 0; i < notifications.size(); i++) {
            // make push notification here
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.home_icon_logo)
                            .setContentTitle("New item request!")
                            .setAutoCancel(true)
                            .setContentText("Tap to view");

            Intent resultIntent = new Intent(context, AppNotificationsActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            // Adds the back stack
            stackBuilder.addParentStack(AppNotificationsActivity.class);
            // Adds the Intent to the top of the stack
            stackBuilder.addNextIntent(resultIntent);

            mBuilder.setContentIntent(resultPendingIntent);

            mBuilder.setAutoCancel(true);

            // Sets an ID for the notification
            int mNotificationId = 1;
            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }
    }
}
