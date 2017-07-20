package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvSearch;
    ItemAdapter itemAdapter;
    MarketPlaceClient client;
    ArrayList<Item> aitems;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        rvSearch = (RecyclerView) findViewById(R.id.rvSearch);
        aitems = new ArrayList<>();

        // initialize adapter
        itemAdapter = new ItemAdapter(aitems, this);

        // attatch adapter to recycler view
        rvSearch.setAdapter(itemAdapter);

        // set layout manager to position items
        rvSearch.setLayoutManager(new LinearLayoutManager(this));




        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(1);
        menuitem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        //Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_home = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        break;

                    case R.id.action_search:
                        Toast.makeText(SearchActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_add:
                        Intent i_add = new Intent(SearchActivity.this, AddItemActivity.class);
                        startActivity(i_add);
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(SearchActivity.this, NotificationsActivity.class);
                        startActivity(i_notifications);
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(SearchActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        //Toast.makeText(SearchActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

    }


}
