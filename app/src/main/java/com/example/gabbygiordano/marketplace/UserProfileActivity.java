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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.gabbygiordano.marketplace.ItemAdapter.getContext;

public class UserProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvCollege;
    TextView tvPhone;
    RecyclerView rvProfileItems;

    Button btFavorites;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    String id;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;
    ParseFile file;

    MenuItem miActionProgressItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setTitle("Profile");

        context = this;

        // perform find view by id lookups
        rvProfileItems = (RecyclerView) findViewById(R.id.rvProfileItems);

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        mContext = getContext();

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvProfileItems.setLayoutManager(linearLayoutManager);
        rvProfileItems.setAdapter(itemAdapter);
        rvProfileItems.setHasFixedSize(true);

        // perform find view by id lookups
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvCollege = (TextView) findViewById(R.id.tvCollege);
        tvPhone = (TextView) findViewById(R.id.tvPhone);

        fetchTimelineAsync();
        

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(2);
        menuitem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent i_home = new Intent(UserProfileActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        finish();
                        break;

                    case R.id.action_maps:
                        Intent i_maps = new Intent(UserProfileActivity.this, MapsActivity.class);
                        startActivity(i_maps);
                        finish();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(UserProfileActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        finish();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(UserProfileActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        finish();
                        break;
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

    public void fetchTimelineAsync(){
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

                        if (item.getOwner().getParseFile("image") != null) {
                            String imgUri = item.getOwner().getParseFile("image").getUrl();
                            Glide
                                    .with(context)
                                    .load(imgUri)
                                    .bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, 20, 0))
                                    .into(ivProfileImage);
                        }

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

            }

        }
    }

    public void addItems(List<Item> list){
        for(int i=0; i< list.size(); i++){
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }

    }

    public void addItem(View view) {
        Intent i_add = new Intent(context, AddItemActivity.class);
        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(UserProfileActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }


}