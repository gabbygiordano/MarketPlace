package com.example.gabbygiordano.marketplace;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class NotificationsActivity extends AppCompatActivity {


    ItemAdapter itemAdapter;
    ArrayList<Notification> notifications;
    RecyclerView rvNotifications;

    BottomNavigationView bottomNavigationView;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(3);
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

                    case R.id.action_search:
                        // Toast.makeText(HomeActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_search = new Intent(NotificationsActivity.this, SearchActivity.class);
                        startActivity(i_search);
                        break;

                    case R.id.action_add:
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_add = new Intent(NotificationsActivity.this, AddItemActivity.class);
                        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
                        break;

                    case R.id.action_notifications:
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
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

    }


}
