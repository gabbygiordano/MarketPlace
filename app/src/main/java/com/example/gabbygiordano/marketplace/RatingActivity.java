package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import static com.example.gabbygiordano.marketplace.R.color.colorGold;

public class RatingActivity extends AppCompatActivity {

    RatingBar ratingBar;
    Button btSubmit;



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

        int con = (int) ratingBar.getRating();
    }

    public void onSubmitClicked(View view){
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
    }
}
