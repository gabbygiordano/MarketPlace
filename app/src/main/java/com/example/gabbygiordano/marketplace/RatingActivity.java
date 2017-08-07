package com.example.gabbygiordano.marketplace;

import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.gabbygiordano.marketplace.R.color.colorGold;

public class RatingActivity extends AppCompatActivity {

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    RatingBar ratingBar;
    Button btSubmit;
    String itemId;
    Item mItem;
    ParseUser mUser;
    int con;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        getSupportActionBar().setTitle("Rate This Seller");

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btSubmit = (Button) findViewById(R.id.btSubmit);



        //  ivAddImage.bringToFront();
        //  ivEditImage.bringToFront();

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(getResources().getColor(colorGold), PorterDuff.Mode.SRC_ATOP);


    }

    public void onSubmitClicked(View view){
        Toast.makeText(this, "button clicked", Toast.LENGTH_LONG).show();
        /* itemId = getIntent().getStringExtra("itemId");
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include("owner");
        query.whereEqualTo("itemId", itemId);
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    if (itemsList != null && !itemsList.isEmpty()) {
                        addItems(itemsList);
                        mItem = itemsList.get(0);
                    }
                } else {
                    Log.d("AllFragment", e.getMessage());
                }
            }
        });

        mUser = mItem.getOwner();
        con = (int) ratingBar.getRating();
        mUser.put("rating_list", con);
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i); */
    }

    public void addItems(List<Item> list){
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }
    }
}
