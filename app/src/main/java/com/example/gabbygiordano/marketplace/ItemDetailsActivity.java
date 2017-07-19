package com.example.gabbygiordano.marketplace;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView tvItemName;
    TextView tvItemDescription;
    TextView tvItemPrice;
    RatingBar rbItemCondition;
    ImageView ivItemImage;
    FloatingActionButton fabBuy;

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

                } else {
                    // something went wrong
                }
            }
        });




    }


}