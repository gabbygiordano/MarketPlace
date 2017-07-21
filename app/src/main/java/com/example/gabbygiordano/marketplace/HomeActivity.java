package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gabbygiordano.marketplace.fragments.ItemsListFragment;
import com.example.gabbygiordano.marketplace.fragments.ItemsPagerAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    ViewPager viewPager;
    ItemsPagerAdapter adapter;
    ImageView ivItemImage;

    ItemAdapter itemAdapter;
    ArrayList<Item> items;
    SearchView searchView;

    int ADD_ITEM_REQUEST = 10;

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
                query.whereContains("item_name", text);
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
        // return super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miInbox) {
            Intent i = new Intent(this, InboxActivity.class);
            startActivityForResult(i, 1);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
