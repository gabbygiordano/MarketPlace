package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.List;

import static com.example.gabbygiordano.marketplace.ItemAdapter.context;
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
    AppNotification appNotification;



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
                        finish();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(DetailsActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        finish();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(DetailsActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        finish();
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
                    tvItemOwner.setText(item.getOwner().getString("name"));

                    if(item.getImage() != null)
                    {
                        String imageUri = item.getImage().getUrl();

                        Picasso
                                .with(context)
                                .load(imageUri)
                                .into(ivItemImage);
                    }

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
                finish();
            }
        });
    }

    public void onInterestedClick(View view) {
        ParseUser buyer = ParseUser.getCurrentUser();
        ParseUser owner = mItem.getOwner();
        Date created = new Date();
        appNotification = new AppNotification(owner, buyer, mItem, created);

        // make the query
        ParseQuery<AppNotification> parseQuery = ParseQuery.getQuery(AppNotification.class);
        parseQuery.whereEqualTo("buyer", buyer);
        parseQuery.whereEqualTo("item", mItem);
        parseQuery.findInBackground(new FindCallback<AppNotification>() {
            public void done(List<AppNotification> notificationsList, ParseException e) {
                if (e == null) {
                    if (notificationsList == null || notificationsList.isEmpty()) {
                        // save the appNotification
                        appNotification.saveInBackground(new SaveCallback() {
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
                    Log.d("AppNotifications", e.getMessage());
                }
            }
        });
    }
}