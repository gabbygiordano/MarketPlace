package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

public class NotificationsActivity extends AppCompatActivity {

    NotificationAdapter notificationAdapter;
    ArrayList<Notification> notifications;
    RecyclerView rvNotifications;

    BottomNavigationView bottomNavigationView;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;

    ParseLiveQueryClient parseLiveQueryClient;
    ParseQuery<Notification> parseQuery;
    SubscriptionHandling<Notification> subscriptionHandling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        getSupportActionBar().setTitle("Notifications");

        rvNotifications = (RecyclerView) findViewById(R.id.rvNotifications);
        notifications = new ArrayList<>();
        notificationAdapter = new NotificationAdapter(notifications, getApplicationContext());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvNotifications.setLayoutManager(linearLayoutManager);
        rvNotifications.setAdapter(notificationAdapter);
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
                        Intent i_home = new Intent(NotificationsActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        break;

                    case R.id.action_add:
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_add = new Intent(NotificationsActivity.this, AddItemActivity.class);
                        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
                        break;

                    case R.id.action_notifications:
                        Toast.makeText(NotificationsActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(NotificationsActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        //Toast.makeText(NotificationsActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        // create new ParseQuery and subscribe to it

        parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        parseQuery = ParseQuery.getQuery(Notification.class);

        subscriptionHandling = parseLiveQueryClient.subscribe(parseQuery);

        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, new SubscriptionHandling.HandleEventCallback<Notification>() {
            @Override
            public void onEvent(ParseQuery<Notification> query, Notification notification) {
                // HANDLING create event
                Log.e("NotificationsActivity", "OMG IT WORKS");
                Toast.makeText(getApplicationContext(), notification.getObjectId(), Toast.LENGTH_LONG).show();
            }
        });

        // make the query
        parseQuery.include("owner");
        parseQuery.include("buyer");
        parseQuery.include("item");
        parseQuery.whereEqualTo("owner", ParseUser.getCurrentUser());
        parseQuery.orderByDescending("_created_at");
        parseQuery.findInBackground(new FindCallback<Notification>() {
            public void done(List<Notification> notificationsList, ParseException e) {
                if (e == null) {
                    if (notificationsList != null && !notificationsList.isEmpty()) {
                        for (int i = 0; i < notificationsList.size(); i++) {
                            notifications.add(notificationsList.get(i));
                            notificationAdapter.notifyItemInserted(notifications.size()-1);
                        }
                    }
                } else {
                    Log.d("NotificationsActivity", e.getMessage());
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(NotificationsActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);    }
}
