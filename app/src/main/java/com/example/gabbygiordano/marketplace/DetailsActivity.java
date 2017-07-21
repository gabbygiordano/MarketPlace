package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import static com.example.gabbygiordano.marketplace.R.color.colorGold;

public class DetailsActivity extends AppCompatActivity {

    TextView tvItemName;
    TextView tvItemDescription;
    TextView tvItemPrice;
    RatingBar rbItemCondition;
    ImageView ivItemImage;
    TextView tvItemOwner;

    Button btSeller;

    MarketPlaceClient client;
    private Item parseItem;

    Button btnInterested;

    BottomNavigationView bottomNavigationView;

    int ADD_ITEM_REQUEST = 10;

    Item mItem;
    Notification notification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setTitle("Item Details");

        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        rbItemCondition = (RatingBar) findViewById(R.id.rbItemCondition);
        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemOwner = (TextView) findViewById(R.id.tvItemOwner);

        btSeller = (Button) findViewById(R.id.btSeller);

        btnInterested = (Button) findViewById(R.id.btnInterested);


        LayerDrawable stars = (LayerDrawable) rbItemCondition.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(colorGold), PorterDuff.Mode.SRC_ATOP);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        //BottomNavigationViewHelper.removeTextLabel(bottomNavigationView, );

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent i_home = new Intent(DetailsActivity.this, HomeActivity.class);
                        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i_home);
                        break;

                    case R.id.action_search:
                        // Toast.makeText(HomeActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_search = new Intent(DetailsActivity.this, SearchActivity.class);
                        startActivity(i_search);
                        break;

                    case R.id.action_add:
                        Intent i_add = new Intent(DetailsActivity.this, AddItemActivity.class);
                        startActivityForResult(i_add, ADD_ITEM_REQUEST);
                        // Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(DetailsActivity.this, NotificationsActivity.class);
                        startActivity(i_notifications);
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(DetailsActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        // Toast.makeText(HomeActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });

        tvItemOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(i);
            }
        });

        //get item ID from Intent
        String itemId = getIntent().getStringExtra("ID");

        // create Item
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include("owner");
        query.getInBackground(itemId, new GetCallback<Item>() {
            public void done(Item item, ParseException e) {
                if (e == null) {
                    mItem = item;
                    // now we have an item object, need to define fields
                    tvItemName.setText(item.getItemName());
                    tvItemDescription.setText(item.getDescription());
                    tvItemPrice.setText(item.getPrice());
                    rbItemCondition.setRating(item.getCondition());
                    tvItemOwner.setText(item.getOwner().getUsername());
                    parseItem = item;


                } else {
                    // something went wrong
                }
            }
        });

        btSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                i.putExtra("itemId", parseItem.getObjectId());
                startActivity(i);
            }
        });
    }

    public void onInterestedClick(View view) {
        ParseUser buyer = ParseUser.getCurrentUser();
        ParseUser owner = mItem.getOwner();
        notification = new Notification(owner, buyer, mItem);

        // make the query
        ParseQuery<Notification> parseQuery = ParseQuery.getQuery(Notification.class);
        parseQuery.whereEqualTo("buyer", buyer);
        parseQuery.whereEqualTo("item", mItem);
        parseQuery.findInBackground(new FindCallback<Notification>() {
            public void done(List<Notification> notificationsList, ParseException e) {
                if (e == null) {
                    if (notificationsList == null || notificationsList.isEmpty()) {
                        // save the notification
                        notification.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                // make toast
                                Toast.makeText(getApplicationContext(), "Request sent!", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        // already sent request!
                        Toast.makeText(getApplicationContext(), "Item already requested", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("NotificationsActivity", e.getMessage());
                }
            }
        });
    }
}