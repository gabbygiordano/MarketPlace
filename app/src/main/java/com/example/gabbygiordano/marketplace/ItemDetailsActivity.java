package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import static com.example.gabbygiordano.marketplace.R.color.colorGold;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView tvItemName;
    TextView tvItemDescription;
    TextView tvItemPrice;
    RatingBar rbItemCondition;
    ImageView ivItemImage;
    FloatingActionButton fabBuy;
    TextView tvItemOwner;

    MarketPlaceClient client;
    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemDescription = (TextView) findViewById(R.id.tvItemDescription);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        rbItemCondition = (RatingBar) findViewById(R.id.rbItemCondition);
        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        fabBuy = (FloatingActionButton) findViewById(R.id.fabBuy);
        tvItemOwner = (TextView) findViewById(R.id.tvItemOwner);

        LayerDrawable stars = (LayerDrawable) rbItemCondition.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(colorGold), PorterDuff.Mode.SRC_ATOP);

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
                    // now we have an item object, need to define fields
                    tvItemName.setText(item.getItemName());
                    tvItemDescription.setText(item.getDescription());
                    tvItemPrice.setText(item.getPrice());
                    rbItemCondition.setRating(item.getCondition());
                    tvItemOwner.setText(item.getOwner().getUsername());

                } else {
                    // something went wrong
                }
            }
        });




    }


}