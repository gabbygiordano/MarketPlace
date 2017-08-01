package com.example.gabbygiordano.marketplace;

import android.Manifest;
import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.gabbygiordano.marketplace.fragments.FavoritesFragment;
import com.example.gabbygiordano.marketplace.fragments.InterestedFragment;
import com.example.gabbygiordano.marketplace.fragments.ItemsListFragment;
import com.example.gabbygiordano.marketplace.fragments.ProfilePagerAdapter;
import com.example.gabbygiordano.marketplace.fragments.ProfileTimelineFragment;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.example.gabbygiordano.marketplace.ItemAdapter.getContext;

public class ProfileActivity extends AppCompatActivity {

    ParseUser user = ParseUser.getCurrentUser();
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1 ;
    final int ACTIVITY_START_CAMERA = 1100;
    final int ACTIVITY_SELECT_FILE = 2200;

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvCollege;
    TextView tvPhone;
    ImageButton ibEdit;
    ViewPager viewPager;
    ImageButton ibAdd;
//    ImageView ivItemImage;
//    ImageButton ibFavoriteOff;
//    ImageButton ibFavoriteOn;
 //   ProfilePagerAdapter adapter;

    TabLayout tabLayout;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    ProfilePagerAdapter adapter;
    String id;

    GalleryPhoto galleryPhoto;
    String selectedPhoto;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    Toolbar toolbar;

    ParseFile file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        galleryPhoto = new GalleryPhoto(getApplicationContext());

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Profile");

        ProfileTimelineFragment profileTimelineFragment = ProfileTimelineFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, profileTimelineFragment);
        ft.commit();

        context = this;

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        mContext = getContext();

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // perform find view by id lookups
       // ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvCollege = (TextView) findViewById(R.id.tvCollege);
        tvPhone = (TextView) findViewById(R.id.tvContact);
        ibEdit = (ImageButton) findViewById(R.id.ibEdit);
        ibAdd = (ImageButton) findViewById(R.id.ibAddProfileImage);

        ibEdit.setColorFilter(Color.rgb(255, 87, 34));

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SettingsActivity.class);
                startActivityForResult(i, 1);
            }
        });

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(2);
        menuitem.setChecked(true);

        adapter = new ProfilePagerAdapter(getSupportFragmentManager(), this);

        // set up the adapter for the pager
        viewPager.setAdapter(adapter);


        // setup the Tab Layout to use the view pager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    ProfileTimelineFragment profileTimelineFragment = ProfileTimelineFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, profileTimelineFragment);
                    ft.commit();
                }
                if(tab.getPosition() == 1){
                    FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, favoritesFragment);
                    ft.commit();
                }
                if(tab.getPosition() == 2){
                    InterestedFragment interestedFragment = InterestedFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, interestedFragment);
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        finish();
                        break;


                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(ProfileActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        finish();
                        break;

                    case R.id.action_profile:
                        if (!tvUsername.getText().toString().equals(ParseUser.getCurrentUser().getUsername())) {
                            Intent i_profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(i_profile);
                            finish();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }

                return false;
            }
        });

        populateUserHeadline();
        queryImagesFromParse();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_MEDIA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }


    }

            public void populateUserHeadline () {
                if (getIntent().hasExtra("itemId")) {
                    id = getIntent().getStringExtra("itemId");
                            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
                            query.include("owner");
                            query.include("favoritesList");
                            query.whereContains("itemId", id);
                            query.orderByDescending("_created_at");
                            query.getInBackground(id, new GetCallback<Item>() {
                                public void done(Item item, ParseException e) {
                                    if (e == null) {
                                        // item was found
                                        tvName.setText(item.getOwner().getString("name"));
                                        tvUsername.setText(item.getOwner().getUsername());
                                        tvCollege.setText(item.getOwner().getString("college"));
                                        tvPhone.setText(" ");
                                        // populateTimeline(item.getOwner());

                                    } else {
                                        Log.e("ItemsListFragment", e.getMessage());
                                    }
                                }
                            });
                        } else {
                            // set text to current user info
                            //ParseUser user = ParseUser.getCurrentUser();
                            if (user != null) {
                                tvName.setText(user.getString("name"));
                                tvUsername.setText(user.getUsername());
                                tvCollege.setText(user.getString("college"));
                                String formattedNumber = PhoneNumberUtils.formatNumber(String.valueOf(user.getLong("phone")));
                                String email = user.getEmail();
                                tvPhone.setText(email + ", " + formattedNumber);

                            } else {

                            }

                        }
                    }


    private void queryImagesFromParse(){
        ParseQuery<ParseObject> imagesQuery = new ParseQuery<>("User");
        imagesQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> imagesItems, ParseException e) {
                if(e == null){

                    ParseUser userCurrentOfParse = ParseUser.getCurrentUser();
                    final String imgUrl = userCurrentOfParse.getParseFile("image").getUrl();
                    if(userCurrentOfParse != null) {
                        if(userCurrentOfParse.getParseFile("image") != null) {

                            ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
                            Glide.with(context).load(imgUrl).into(ivProfileImage);
                        }
                        else
                        {
                            ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
                            Glide.with(context).load(R.drawable.ic_profile_tab).into(ivProfileImage);
                        }

                        //imageUploadPassed.pinInBackground();

                        // profileImageId = imageUploadPassed.getObjectId();
                        //Log.d(TAG, "The object id is: " + profileImageId);
                    }

                }else{
                    Toast.makeText(ProfileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }



    public void populateProfileTimeline(ParseUser user){
    }

    public void addItems(List<Item> list){
        for(int i=0; i< list.size(); i++){
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.miSettings) {
//            Intent i = new Intent(this, SettingsActivity.class);
//            startActivityForResult(i, 1);
//        }
//        return true;
//    }



    public void addItem(View view) {
        Intent i_add = new Intent(context, AddItemActivity.class);
        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }

    public void addProfileImage(View view) {

        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder= new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                if(items[i].equals("Camera"))
                {
                    Intent callCamera = new Intent();
                    callCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(callCamera, ACTIVITY_START_CAMERA);
                }
                else if(items[i].equals("Gallery"))
                {
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("image/*");
                    startActivityForResult(galleryPhoto.openGalleryIntent(), ACTIVITY_SELECT_FILE);
                }
                else if (items[i].equals("Cancel"))
                {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(type);
        ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        fragment.activityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == ACTIVITY_START_CAMERA)
            {
                //Toast.makeText(this, "picture was taken", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap photoCaptured = (Bitmap) extras.get("data");
                ivProfileImage.setImageBitmap(photoCaptured);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoCaptured.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] image = stream.toByteArray();
                file = new ParseFile("itemimage.png", image);
                //file.saveInBackground();
                user.put("image", file);
                user.saveInBackground();
                Toast.makeText(ProfileActivity.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();


//                ivEditImage.setVisibility(View.VISIBLE);
//                ivEditImage.setClickable(true);
//
//                ivAddImage.setVisibility(View.INVISIBLE);
//                ivAddImage.setClickable(false);
            }
            else if(requestCode == ACTIVITY_SELECT_FILE)
            {
                Uri uri = data.getData();
                galleryPhoto.setPhotoUri(uri);

                String photoPath = galleryPhoto.getPath();
                selectedPhoto = photoPath;
                try
                {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(300,300).getBitmap();
                    ivProfileImage.setImageBitmap(rotateBitmapOrientation(photoPath));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] image = stream.toByteArray();
                    file = new ParseFile("itemimage.png", image);
                    file.saveInBackground();
                    user.put("image", file);
                    user.saveInBackground();
                    Toast.makeText(ProfileActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();
//
//                    ivEditImage.setVisibility(View.VISIBLE);
//                    ivEditImage.setClickable(true);
//
//                    ivAddImage.setVisibility(View.INVISIBLE);
//                    ivAddImage.setClickable(false);
                }
                catch (FileNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(), "Something went wrong while uploading photo", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }



    public Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }
}
