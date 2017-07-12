package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;

import org.parceler.Parcels;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA = 0;

    public EditText etItemName;
    public EditText etItemDescription;
    public EditText etItemPrice;
    public RatingBar rbCondition;
    public ImageButton ibAddImage;
    public ImageButton ibPostItem;
    public ImageView imageLocation;

    final ArrayList<ImageView> addImage = new ArrayList<>();

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
        imageLocation = (ImageView) findViewById(R.id.ivItemPhoto);
    }

    public void onPostSuccess(View view) {
        String name = etItemName.getText().toString();
        String description = etItemDescription.getText().toString();
        String price = etItemPrice.getText().toString();
        int condition = 0;

        User user = User.fromInput("name", "username", "email", "password", "college", "phone");

        Item item = Item.fromInput(name, description, price, condition, user);

        Intent intent = new Intent();
        intent.putExtra("item", Parcels.wrap(item));

        // return to required activity
        setResult(RESULT_OK, intent);
        finish();
    }


    public void takeItemPhoto(View view)
    {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, ACTIVITY_START_CAMERA);
    }

    protected void onActivityResult(int requestcode, int resultCode, Intent data)
    {
        if(requestcode == ACTIVITY_START_CAMERA && resultCode == RESULT_OK)
        {
            //Toast.makeText(this, "picture was taken", Toast.LENGTH_SHORT).show();
            Bundle extras = data.getExtras();
            Bitmap photoCaptured = (Bitmap) extras.get("data");
            imageLocation.setImageBitmap(photoCaptured);

        }
    }
}
