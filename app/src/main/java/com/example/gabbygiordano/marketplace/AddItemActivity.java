package com.example.gabbygiordano.marketplace;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.solver.SolverVariable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;

import static com.example.gabbygiordano.marketplace.R.id.view;

public class AddItemActivity extends AppCompatActivity {

    private static final int ACTIVITY_START_CAMERA = 1;
    private static final int ACTIVITY_SELECT_FILE = 0;

    public EditText etItemName;
    public EditText etItemDescription;
    public EditText etItemPrice;
    public ImageButton ibAddImage;
    public ImageButton ibPostItem;
    public ImageView imageLocation;

    BottomNavigationView bottomNavigationView;

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;
    Spinner itemType;
    ArrayAdapter<CharSequence> adaptertwo;

    final ArrayList<ImageView> addImage = new ArrayList<>();

    String condition;
    String type;
    Bitmap resource;


    Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        // find view by id lookups
        etItemName = (EditText) findViewById(R.id.tvItemName);
        etItemDescription = (EditText) findViewById(R.id.tvItemDescription);
        etItemPrice = (EditText) findViewById(R.id.tvItemPrice);
        ibAddImage = (ImageButton) findViewById(R.id.ibAddImage);
        ibPostItem = (ImageButton) findViewById(R.id.ibPostItem);
        imageLocation = (ImageView) findViewById(R.id.ivItemPhoto);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        ibPostItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = false;

                String name = etItemName.getText().toString();
                String description = etItemDescription.getText().toString();
                String price = etItemPrice.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter item name", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter item description", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (price.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter item price", Toast.LENGTH_LONG).show();
                    flag = true;
                } else {
                    int con = Integer.parseInt(condition);
                    ParseUser currentUser = ParseUser.getCurrentUser();

                    item = new Item(name, description, price, con, currentUser, type, resource);
                    item.setOwner(ParseUser.getCurrentUser());
                }

                if (!flag) {
                    onPostSuccess();
                }
            }
        });

        spinner = (Spinner) findViewById(R.id.conditionOptions);
        adapter = ArrayAdapter.createFromResource(this, R.array.condition_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence cs = (CharSequence) spinner.getSelectedItem();
                condition = (String) cs;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        itemType = (Spinner) findViewById(R.id.spItemType);
        adaptertwo = ArrayAdapter.createFromResource(this, R.array.itemType_array, android.R.layout.simple_spinner_item);
        adaptertwo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemType.setAdapter(adaptertwo);
        itemType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence cs = (CharSequence) itemType.getSelectedItem();
                type = (String) cs;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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


    public void onPostSuccess() {
        // save the item
        item.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Intent intent = new Intent();
                String id = item.getObjectId();
                intent.putExtra("item_id", id);
                String type = item.getType();
                intent.putExtra("type", type);
                intent.putExtra("resource", item.getResource());

                // return to required activity
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }


    private void SelectImage()
    {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder= new AlertDialog.Builder(AddItemActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent intent = new Intent();
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, ACTIVITY_START_CAMERA);
                }
                else if(items[i].equals("Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(Intent.createChooser(intent,"Select File"), ACTIVITY_SELECT_FILE);
                }
                else if (items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    public void takeItemPhoto(View view)
    {
        SelectImage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == ACTIVITY_START_CAMERA)
            {
                //Toast.makeText(this, "picture was taken", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap photoCaptured = (Bitmap) extras.get("data");
                imageLocation.setImageBitmap(photoCaptured);
                resource = photoCaptured;

            }
            else if(requestCode == ACTIVITY_SELECT_FILE)
            {
                Uri selectedImageUri = data.getData();
                imageLocation.setImageURI(selectedImageUri);

            }

        }
    }

}
