package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    RecyclerView rvItems;
    ItemAdapter itemAdapter;
    MarketPlaceClient client;
    ArrayList<Item> items;
    SearchView searchView;
    TextView tvIntro;
    String search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setTitle("Search");

        tvIntro = (TextView) findViewById(R.id.tvIntro);

        rvItems = (RecyclerView) findViewById(R.id.rvItems);
        items = new ArrayList<>();

        // initialize adapter
        itemAdapter = new ItemAdapter(items, this);

        // attatch adapter to recycler view
        rvItems.setAdapter(itemAdapter);

        // set layout manager to position items
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        if(items.size() != 0){
            tvIntro.setText(" ");
        }

        search = getIntent().getStringExtra("query");

        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include("owner");
        query.whereContains("item_name", search);
        query.orderByDescending("_created_at");
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    // item was found
                    if (itemsList != null && !itemsList.isEmpty()) {
                        addItems(itemsList);
                        tvIntro.setText(" ");
                    }
                    else{
                        tvIntro.setText("No items matched what you searched for. Try again.");
                    }
                } else {
                    Log.e("ItemsListFragment", e.getMessage());
                }
            }
        });


        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(1);
        menuitem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        //Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_home = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(i_home);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                // perform query here
                ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
                query.include("owner");
                query.whereContains("item_name", text);
                query.orderByDescending("_created_at");
                query.findInBackground(new FindCallback<Item>() {
                    public void done(List<Item> itemsList, ParseException e) {
                        if (e == null) {
                            // item was found
                            if (itemsList != null && !itemsList.isEmpty()) {
                                addItems(itemsList);
                                tvIntro.setText(" ");
                            }
                            else{
                                tvIntro.setText("No items matched what you searched for. Try again.");
                            }
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
                items.clear();
                return false;
            }

            // TODO: implement so the seach query changes as you type
            // @Override
            // public boolean onQueryTextChange(String newText) {
             //   fetchBooks(newText);
            //    return false;
            //}

        });
        return super.onCreateOptionsMenu(menu);


    }

    public void addItems(List<Item> list){
        for(int i=0; i< list.size(); i++){
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(SearchActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);    }
}
