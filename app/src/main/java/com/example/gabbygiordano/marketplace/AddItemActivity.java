package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;

public class AddItemActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA = 0;

    public EditText etItemName;
    public EditText etItemDescription;
    public EditText etItemPrice;
    public ImageButton ibAddImage;
    public ImageButton ibPostItem;
    public ImageView imageLocation;

    final ArrayList<ImageView> addImage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Spinner spinner = (Spinner) findViewById(R.id.conditionOptions);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.condition_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner itemType = (Spinner) findViewById(R.id.spItemType);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adaptertwo = ArrayAdapter.createFromResource(this,
                R.array.itemType_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adaptertwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        itemType.setAdapter(adaptertwo);

        etItemName = (EditText) findViewById(R.id.etItemName);
        etItemDescription = (EditText) findViewById(R.id.etItemDescription);
        etItemPrice = (EditText) findViewById(R.id.etItemPrice);
        ibAddImage = (ImageButton) findViewById(R.id.ibAddImage);
        ibPostItem = (ImageButton) findViewById(R.id.ibPostItem);
        imageLocation = (ImageView) findViewById(R.id.ivItemPhoto);

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(2);
        menuitem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        //Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_home = new Intent(AddItemActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        break;

                    case R.id.action_search:
                        // Toast.makeText(HomeActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        Intent i_search = new Intent(AddItemActivity.this, SearchActivity.class);
                        startActivity(i_search);
                        break;

                    case R.id.action_add:
                        Toast.makeText(AddItemActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(AddItemActivity.this, NotificationsActivity.class);
                        startActivity(i_notifications);
                        // Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(AddItemActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        //Toast.makeText(AddItemActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }
        });
    }

    public void onPostSuccess(View view) {
        String name = etItemName.getText().toString();
        String description = etItemDescription.getText().toString();
        String price = etItemPrice.getText().toString();
        int condition = 0;
        String type = "all";  // TODO: Ask user for type and update here

        //User user = null;

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
//            //user = User.fromParseUser(currentUser);
//            user = new User(currentUser.getString("name"), currentUser.getUsername(), currentUser.getEmail(),
//                    "", currentUser.getString("college"), currentUser.getLong("phone"),
//                    currentUser.getString("contact"));

            // idk
            Log.e("AddItem", "Current user is not null");
        } else {
            // TODO: Go to sign up or login if user null
            // This line should never actually execute
            // Because items cannot be added without being signed in
            Log.e("AddItem", "Current user null");
        }

        Item item = new Item(name, description, price, condition, currentUser, type);
        item.setOwner(ParseUser.getCurrentUser());

        // save the item
        item.saveInBackground();

        Intent intent = new Intent();
        intent.putExtra("item_id", item.getObjectId());

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

    @Override
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
