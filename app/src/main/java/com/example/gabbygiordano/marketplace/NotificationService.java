package com.example.gabbygiordano.marketplace;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tanvigupta on 7/24/17.
 */

public class NotificationService extends IntentService {

    Context context;
    Date lastNotif;

    // Create a handler which can run code periodically
    static final int POLL_INTERVAL = 1000; // milliseconds
    Handler myHandler = new Handler();  // android.os.Handler
    Runnable mRefreshMessagesRunnable = new Runnable() {
        @Override
        public void run() {
            refreshMessages();
            myHandler.postDelayed(this, POLL_INTERVAL);
        }
    };

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("NotifService", "Initialized");

        context = getApplicationContext();
        // Handle intent data here
        lastNotif = (Date) intent.getExtras().get("last");

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);
    }

    void refreshMessages() {
        // make the query
        ParseQuery<AppNotification> parseQuery = ParseQuery.getQuery(AppNotification.class);;

        parseQuery.include("owner");
        parseQuery.include("buyer");
        parseQuery.include("item");
        parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        parseQuery.whereGreaterThan("date", lastNotif);
        parseQuery.orderByDescending("_created_at");
        parseQuery.findInBackground(new FindCallback<AppNotification>() {
            public void done(List<AppNotification> notificationsList, ParseException e) {
                if (e == null) {
                    if (notificationsList != null && !notificationsList.isEmpty()) {
                        Log.e("NotifService", "Made query");

                        Intent localIntent = new Intent(Constants.BROADCAST_ACTION);
                        localIntent.putExtra("notifications", (Serializable) notificationsList);

                        Log.e("NotifService", "Intent created");

                        // Broadcasts the Intent to receivers in this app.
                        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);

                        lastNotif = notificationsList.get(0).getCreatedAt();
                        Log.e("NotifService", lastNotif.toString());
                    }
                } else {
                    lastNotif = notificationsList.get(0).getCreatedAt();
                    Log.d("AppNotifications", e.getMessage());
                }
            }
        });
    }
}
