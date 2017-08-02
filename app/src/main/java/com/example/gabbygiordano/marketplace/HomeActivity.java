package com.example.gabbygiordano.marketplace;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gabbygiordano.marketplace.fragments.ItemsListFragment;
import com.example.gabbygiordano.marketplace.fragments.ItemsPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements ItemsListFragment.ProgressListener {

    BottomNavigationView bottomNavigationView;

    ViewPager viewPager;
    ItemsPagerAdapter adapter;

    int ADD_ITEM_REQUEST = 10;

    Date lastNotif = new Date();

    Intent mServiceIntent;

    MenuItem miActionProgressItem;


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
        setContentView(R.layout.activity_home);



        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        //BottomNavigationViewHelper.removeTextLabel(bottomNavigationView, );

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(0);
        menuitem.setChecked(true);

        miActionProgressItem = (MenuItem) findViewById(R.id.miActionProgress);

        adapter = new ItemsPagerAdapter(getSupportFragmentManager(), this);

        // set up the adapter for the pager
        viewPager.setAdapter(adapter);


        // setup the Tab Layout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        final int[] unselectedImgs = {R.drawable.ic_action_undashboard, R.drawable.ic_action_unbook,
                R.drawable.ic_action_unmac, R.drawable.ic_action_unshirt, R.drawable.ic_action_unmore};
        final int[] selectedImgs = {R.drawable.ic_action_dashboard, R.drawable.ic_action_book,
                R.drawable.ic_action_laptop_mac, R.drawable.ic_action_shirt, R.drawable.ic_action_more};

        tabLayout.getTabAt(0).setIcon(selectedImgs[0]);
        for (int i = 1; i < unselectedImgs.length; i++) {
            tabLayout.getTabAt(i).setIcon(unselectedImgs[i]);
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.setIcon(selectedImgs[tab.getPosition()]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setIcon(unselectedImgs[tab.getPosition()]);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        break;


                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(HomeActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        break;
                }

                return false;
            }
        });




        // myHandler.postDelayed(mRefreshMessagesRunnable, POLL_INTERVAL);

        Log.e("NotifService", lastNotif.toString());
        // Using background services and broadcast receivers
        mServiceIntent = new Intent(this, NotificationService.class);
        mServiceIntent.putExtra("last", lastNotif);
        startService(mServiceIntent);

        // Instantiates a new DownloadStateReceiver
        NotificationReceiver mDownloadStateReceiver = new NotificationReceiver();

        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
        //miActionProgressItem.setVisible(false);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(type);
        ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        fragment.activityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_home, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String text) {
                // perform query here
                ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
                query.include("owner");
                query.include("favoritesList");
                query.whereMatches("item_name", "("+text+")", "i");
                query.orderByDescending("_created_at");
                query.findInBackground(new FindCallback<Item>() {
                    public void done(List<Item> itemsList, ParseException e) {
                        if (e == null) {
                            Intent i = new Intent(getApplicationContext(), SearchActivity.class);
                            i.putExtra("query", text);
                            startActivity(i);
                        } else {
                            Log.e("ItemsListFragment", e.getMessage());
                        }
                    }
                });


                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }

            // TODO: implement so the seach query changes as you type
            // @Override
            // public boolean onQueryTextChange(String newText) {
            //   fetchBooks(newText);
            //    return false;
            //}

        });

        MenuItem mapItem = menu.findItem(R.id.miActionMap);
        mapItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent i = new Intent(HomeActivity.this, MapsActivity.class);
                startActivity(i);

                return true;
            }
        });


        // return super.onCreateOptionsMenu(menu);
        return true;
    }

    public void addItem(View view) {
        Intent i_add = new Intent(HomeActivity.this, AddItemActivity.class);
        startActivityForResult(i_add, ADD_ITEM_REQUEST);
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);

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
                        for (int i = 0; i < notificationsList.size(); i++) {
                            if (((Date) notificationsList.get(i).get("date")).after(lastNotif)) {
                                AppNotification appNotification = notificationsList.get(i);
                                String name = appNotification.getBuyer().getString("name");

                                // make push notification here
                                NotificationCompat.Builder mBuilder =
                                        new NotificationCompat.Builder(getApplicationContext())
                                                .setSmallIcon(R.drawable.homeicon)
                                                .setContentTitle("New item request from " + name + "!")
                                                .setAutoCancel(true)
                                                .setContentText("Tap to view");

                                Intent resultIntent = new Intent(getApplicationContext(), AppNotificationsActivity.class);
                                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                                // Adds the back stack
                                stackBuilder.addParentStack(AppNotificationsActivity.class);
                                // Adds the Intent to the top of the stack
                                stackBuilder.addNextIntent(resultIntent);

                                mBuilder.setContentIntent(resultPendingIntent);

                                // Sets an ID for the notification
                                int mNotificationId = 1;
                                // Gets an instance of the NotificationManager service
                                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                // Builds the notification and issues it.
                                mNotifyMgr.notify(mNotificationId, mBuilder.build());
                            }
                        }
                        lastNotif = notificationsList.get(0).getCreatedAt();
                        Log.e("AppNotifications", lastNotif.toString());
                    }
                } else {
                    Log.d("AppNotifications", e.getMessage());
                }
            }
        });

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v = (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
        miActionProgressItem.setVisible(true);
        return true;
    }


    @Override
    public void showProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(true);
        }
    }

    @Override
    public void hideProgressBar() {
        if (miActionProgressItem != null) {
            miActionProgressItem.setVisible(false);
        }
    }
}
