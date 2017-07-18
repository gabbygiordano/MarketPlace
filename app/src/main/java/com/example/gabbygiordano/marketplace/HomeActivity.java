package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.gabbygiordano.marketplace.fragments.ItemsListFragment;
import com.example.gabbygiordano.marketplace.fragments.ItemsPagerAdapter;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ViewPager viewPager;
    ItemsPagerAdapter adapter;

    int ADD_ITEM_REQUEST = 10;
    int NOTIF_REQUEST = 20;

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

        adapter = new ItemsPagerAdapter(getSupportFragmentManager(), this);

        // set up the adapter for the pager
        viewPager.setAdapter(adapter);

        // setup the Tab Layout to use the view pager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

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

                    case R.id.action_search:
                        // Toast.makeText(HomeActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_search = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(i_search);
                        break;

                    case R.id.action_add:
                        Intent i_add = new Intent(HomeActivity.this, AddItemActivity.class);
                        startActivityForResult(i_add, ADD_ITEM_REQUEST);
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(HomeActivity.this, NotificationsActivity.class);
                        startActivity(i_notifications);
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        // Toast.makeText(HomeActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });
    }

    public void seeDetails(View view) {
        Intent i = new Intent(HomeActivity.this, ItemDetailsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String type = data.getStringExtra("type");

        // ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(type);
        ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        fragment.activityResult(requestCode, resultCode, data);
    }


}
