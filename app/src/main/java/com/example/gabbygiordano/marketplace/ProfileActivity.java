package com.example.gabbygiordano.marketplace;

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
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.gabbygiordano.marketplace.ItemAdapter.getContext;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvCollege;
    TextView tvPhone;
    ImageButton ibLogOut;
    RecyclerView rvProfileItems;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("Profile");

        // perform find view by id lookups
        rvProfileItems = (RecyclerView) findViewById(R.id.rvProfileItems);

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProfileItems.setLayoutManager(linearLayoutManager);
        rvProfileItems.setAdapter(itemAdapter);
        rvProfileItems.setHasFixedSize(true);


        // perform find view by id lookups
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvCollege = (TextView) findViewById(R.id.tvCollege);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        ibLogOut = (ImageButton) findViewById(R.id.ibLogOut);

        if( getIntent().hasExtra("itemId")){

            id = getIntent().getStringExtra("itemId");

            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            query.include("owner");
            query.whereContains("itemId", id);
            query.orderByDescending("_created_at");
            query.getInBackground(id, new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        // item was found
                        tvName.setText(item.getOwner().getString("name"));
                        tvUsername.setText(item.getOwner().getUsername());
                        tvCollege.setText(item.getOwner().getString("college"));
                        tvPhone.setText(" ");

                        populateProfileTimeline(item.getOwner());

                    } else {
                        Log.e("ItemsListFragment", e.getMessage());
                    }
                }
            });


        }
        else{
            // set text to current user info
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {
                tvName.setText(user.getString("name"));
                tvUsername.setText(user.getUsername());
                tvCollege.setText(user.getString("college"));
                tvPhone.setText(String.valueOf(user.getLong("phone")));

                populateProfileTimeline(user);

            } else {
                // show the signup or login screen
            }

        }


        // log out if power button is clicked
        ibLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(3);
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
                        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        break;

                    case R.id.action_add:
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_add = new Intent(ProfileActivity.this, AddItemActivity.class);
                        startActivity(i_add);
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(ProfileActivity.this, NotificationsActivity.class);
                        startActivity(i_notifications);
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        if(tvUsername.getText() != ParseUser.getCurrentUser().getUsername()){
                            Intent i_profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(i_profile);
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }

                return false;
            }
        });

    }

    public void populateProfileTimeline(ParseUser user){
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereEqualTo("owner", user);
        query.include("owner");
        query.setLimit(200);
        query.orderByDescending("_created_at");
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    Log.d("items", "Retrieved " + itemsList.size() + " items");
                    addItems(itemsList);
                } else {
                    Log.d("items", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void addItems(List<Item> list){
        for(int i=0; i< list.size(); i++){
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miSettings) {
            Intent i = new Intent(this, SettingsActivity.class);
            startActivityForResult(i, 1);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }
}
