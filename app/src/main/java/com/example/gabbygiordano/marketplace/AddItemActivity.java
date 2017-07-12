package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;

public class AddItemActivity extends AppCompatActivity {

    public EditText etItemName;
    public EditText etItemDescription;
    public EditText etItemPrice;
    public RatingBar rbCondition;
    public ImageButton ibAddImage;
    public ImageButton ibPostItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemDescription = (EditText) findViewById(R.id.etItemDescription);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        rbCondition = (RatingBar) findViewById(R.id.rbCondition);
        ibAddImage = (ImageButton) findViewById(R.id.ibAddImage);
        ibPostItem = (ImageButton) findViewById(R.id.ibPostItem);

    }

    public void onPostSuccess(View view){
        Intent i = new Intent(this, HomeActivity.class);
        startActivity(i);
    }
}
