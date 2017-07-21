package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.List;

public class AppNotificationsActivity extends AppCompatActivity {

    AppNotificationAdapter appNotificationAdapter;
    ArrayList<AppNotification> appNotifications;
    RecyclerView rvNotifications;

    BottomNavigationView bottomNavigationView;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;

    ParseLiveQueryClient parseLiveQueryClient;
    ParseQuery<AppNotification> parseQuery;
    SubscriptionHandling<AppNotification> subscriptionHandling;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Notifications");

        rvNotifications = (RecyclerView) findViewById(R.id.rvNotifications);
        appNotifications = new ArrayList<>();
        appNotificationAdapter = new AppNotificationAdapter(appNotifications, getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvNotifications.setLayoutManager(linearLayoutManager);
        rvNotifications.setAdapter(appNotificationAdapter);
        rvNotifications.setHasFixedSize(true);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(2);
        menuitem.setChecked(true);

        mContext = ItemAdapter.getContext();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        //Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_home = new Intent(AppNotificationsActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        break;

                    case R.id.action_add:
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_add = new Intent(AppNotificationsActivity.this, AddItemActivity.class);
                        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
                        break;

                    case R.id.action_notifications:
                        Toast.makeText(AppNotificationsActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(AppNotificationsActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        //Toast.makeText(AppNotificationsActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        // create new ParseQuery and subscribe to it
        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        parseQuery = ParseQuery.getQuery(AppNotification.class);

        subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<AppNotification>() {
            @Override
            public void onEvent(ParseQuery<AppNotification> query, AppNotification appNotification) {
                // HANDLING create event
                Log.e("AppNotifications", "OMG IT WORKS");
                Toast.makeText(getApplicationContext(), appNotification.getObjectId(), Toast.LENGTH_LONG).show();
            }
        });

//        // make the query
//        parseQuery.include("owner");
//        parseQuery.include("buyer");
//        parseQuery.include("item");
//        parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
//        parseQuery.orderByDescending("_created_at");
//        parseQuery.findInBackground(new FindCallback<AppNotification>() {
//            public void done(List<AppNotification> notificationsList, ParseException e) {
//                if (e == null) {
//                    if (notificationsList != null && !notificationsList.isEmpty()) {
//                        for (int i = 0; i < notificationsList.size(); i++) {
//                            appNotifications.add(notificationsList.get(i));
//                            appNotificationAdapter.notifyItemInserted(appNotifications.size()-1);
//                        }
//                    }
//                } else {
//                    Log.d("AppNotifications", e.getMessage());
//                }
//            }
//        });
    }

    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // make the query
        parseQuery.include("owner");
        parseQuery.include("buyer");
        parseQuery.include("item");
        parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        parseQuery.orderByDescending("_created_at");
        parseQuery.findInBackground(new FindCallback<AppNotification>() {
            public void done(List<AppNotification> notificationsList, ParseException e) {
                if (e == null) {
                    if (notificationsList != null && !notificationsList.isEmpty()) {
                        appNotifications.clear();
                        appNotifications.addAll(notificationsList);
                        appNotificationAdapter.notifyDataSetChanged(); // update adapter
                    }
                } else {
                    Log.d("AppNotifications", e.getMessage());
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(AppNotificationsActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }
}
