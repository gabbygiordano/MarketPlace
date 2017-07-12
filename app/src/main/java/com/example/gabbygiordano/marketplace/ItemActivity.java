package com.example.gabbygiordano.marketplace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemActivity extends AppCompatActivity {

    public ImageView ivItemImage;
    public TextView tvItemName;
    public TextView tvSeller;
    public TextView tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        ivItemImage = (ImageView) findViewById(R.id.ivItemImage);
        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvSeller = (TextView) findViewById(R.id.tvSeller);
        tvPrice = (TextView) findViewById(R.id.tvPrice);



    }
}
