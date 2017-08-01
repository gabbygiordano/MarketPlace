package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.gabbygiordano.marketplace.R.color.colorGold;

public class DetailsActivity extends AppCompatActivity {

    TextView tvItemName;
    TextView tvItemDescription;
    TextView tvItemPrice;
    RatingBar rbItemCondition;
    ImageView ivImage;
    TextView tvItemOwner;
    Button btnInterested;
    BottomNavigationView bottomNavigationView;
    TextView tvTimeAgo;
    LikeButton likeButton;

    private Item parseItem;

    int ADD_ITEM_REQUEST = 10;

    Item mItem;
    AppNotification appNotification;

    Context context;

    ParseUser buyer;
    ParseUser owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setTitle("Item Details");

        context = this;

        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        rbItemCondition = (RatingBar) findViewById(R.id.rbItemCondition);
        ivImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemOwner = (TextView) findViewById(R.id.tvItemOwner);
        btnInterested = (Button) findViewById(R.id.btnInterested);
        tvTimeAgo = (TextView) findViewById(R.id.tvTimeAgo);
        likeButton = (LikeButton) findViewById(R.id.likeBtn);

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

                    String owner = "Owner: " + item.getOwner().getString("name");

                    SpannableStringBuilder ssb_a = new SpannableStringBuilder(owner);

                    ForegroundColorSpan redForegroundColorSpan = new ForegroundColorSpan(Color.rgb(255, 87, 34));
                    StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

                    ssb_a.setSpan(redForegroundColorSpan, 7, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ssb_a.setSpan(bold, 7, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tvItemOwner.setText(ssb_a, TextView.BufferType.EDITABLE);

                    tvTimeAgo.setText(getRelativeTimeAgo(item.getCreatedAt()));

                    ParseUser user = ParseUser.getCurrentUser();
                    ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");
                    if (favoriteItems.contains(item.getObjectId())) {
                        // favorited
                        likeButton.setLiked(true);
                    } else {
                        // unfavorited
                        likeButton.setLiked(false);
                    }

                    likeButton.setOnLikeListener(new OnLikeListener() {
                        @Override
                        public void liked(LikeButton likeButton) {
                            // save in favorites list
                            ParseUser user = ParseUser.getCurrentUser();
                            ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");

                            favoriteItems.add(mItem.getObjectId());
                            user.put("favoriteItems", favoriteItems);
                            user.saveInBackground();
                        }

                        @Override
                        public void unLiked(LikeButton likeButton) {
                            // remove from favorites list
                            ParseUser user = ParseUser.getCurrentUser();
                            ArrayList<String> favoriteItems = (ArrayList) user.get("favoriteItems");

                            favoriteItems.remove(mItem.getObjectId());
                            user.put("favoriteItems", favoriteItems);
                            user.saveInBackground();
                        }
                    });

                    parseItem = item;

                    getSupportActionBar().setTitle(item.getItemName());

                    if (item.getImage() != null) {
                        String imageUri = item.getImage().getUrl();
                        Picasso
                                .with(context)
                                .load(imageUri)
                                .into(ivImage);
                    }
                } else
                    {
                    // something went wrong
                }


            }
        });

        tvItemOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser owner = parseItem.getOwner();
                ParseUser current = ParseUser.getCurrentUser();
                if (owner.getObjectId().equals(current.getObjectId())) {
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    i.putExtra("itemId", parseItem.getObjectId());
                    startActivity(i);
                    finish();
                } else {
                    Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                    i.putExtra("itemId", parseItem.getObjectId());
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    // getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
    public String getRelativeTimeAgo(Date date) {
        String relativeDate = "";

        long createdDate = date.getTime();
        relativeDate = DateUtils.getRelativeTimeSpanString(createdDate,
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();

        return relativeDate;
    }

    public void onInterestedClick(View view) {
        buyer = ParseUser.getCurrentUser();
        owner = mItem.getOwner();
        Date created = new Date();
        appNotification = new AppNotification(owner, buyer, mItem, created);

        final ArrayList<String> interestedItems = (ArrayList<String>) buyer.get("interestedItems");

        // send notification if necessary
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
                                // add to buyer's interestedItems
                                interestedItems.add(mItem.getObjectId());
                                buyer.put("interestedItems", interestedItems);
                                buyer.saveInBackground();
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