package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    ImageButton ibUploadProf;
    ImageView ivEditImage;
    ImageView ivImage;
    TextView tvUsername;
//    TextView tvUploadProf;
//    ImageButton ibUploadProf;
//    ImageView ivEditImage;
//    ImageView ivImage;

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etUsername;

    ImageButton btSaveChanges;
    ImageButton ibLogout;

//    Boolean changedProfilePhoto;

   // Context context;

    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1 ;
    final int ACTIVITY_START_CAMERA = 1100;
    final int ACTIVITY_SELECT_FILE = 2200;

    GalleryPhoto galleryPhoto;
    String selectedPhoto;

    ParseFile file;

    final ParseUser user = ParseUser.getCurrentUser();

    boolean changedName;
    boolean changedPhone;
    boolean changedEmail;
    boolean changedUsername;
    boolean changedPassword;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");
        galleryPhoto = new GalleryPhoto(getApplicationContext());

        context = this;

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);
        ivEditImage = (ImageView) findViewById(R.id.ivEditImage);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
//        tvUploadProf = (TextView) findViewById(R.id.tvUploadProf);
//        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);
//        ivEditImage = (ImageView) findViewById(R.id.ivEditImage);
//        ivImage = (ImageView) findViewById(R.id.ivImage);


        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);

        btSaveChanges = (ImageButton) findViewById(R.id.btSaveChanges);
        ibLogout = (ImageButton) findViewById(R.id.ibLogOut);

        changedName = true;
        changedPhone = true;
        changedEmail = true;

//       changedProfilePhoto = true;
        changedUsername = true;
        changedPassword = true;


       // final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            etName.setText(user.getString("name"), TextView.BufferType.EDITABLE);
            etPhone.setText(String.valueOf(user.getLong("phone")), TextView.BufferType.EDITABLE);
            etEmail.setText(user.getString("email"), TextView.BufferType.EDITABLE);
            etUsername.setText(user.getUsername(), TextView.BufferType.EDITABLE);
        }

        // log out if power button is clicked
        ibLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
            }
        });

        btSaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString() == "") {
                    changedName = false;
                }
                if ((etPhone.getText().toString()).isEmpty()) {
                    changedPhone = false;
                }
                if ((etEmail.getText().toString()) == "") {
                    changedEmail = false;
                }

//                changedProfilePhoto = false;


                if (true) {
                    if (changedName) {
                        String name = etName.getText().toString();
                        user.put("name", name);
                        user.saveInBackground();

                    }
                    if (changedPhone) {
                        String phone = etPhone.getText().toString();
                        user.put("phone", Long.parseLong(phone));
                        user.saveInBackground();
                    }
                    if (changedEmail) {
                        String email = etEmail.getText().toString();
                        user.put("email", email);
                        user.put("publicEmail", email);
                        user.saveInBackground();
                    }
//                    if (changedProfilePhoto) {
//                    }
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivityForResult(i, 1);
                    Toast.makeText(getApplicationContext(), "Information Updated", Toast.LENGTH_LONG).show();
                }

            }
        });

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

        queryImagesFromParse();
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


                            Glide.with(getApplicationContext()).load(imgUrl).into(ivImage);
                        }
                        else
                        {

                            Glide.with(getApplicationContext()).load(R.drawable.ic_profile_tab).into(ivImage);
                        }

                        //imageUploadPassed.pinInBackground();

                        // profileImageId = imageUploadPassed.getObjectId();
                        //Log.d(TAG, "The object id is: " + profileImageId);
                    }

                }else{
                    Toast.makeText(SettingsActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void addProfilePhoto(View view) {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);
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

    public void editProfilePhoto(View view) {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder= new AlertDialog.Builder(SettingsActivity.this);
        builder.setTitle("Change Image");
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        if(resultCode == Activity.RESULT_OK)
        {
            if(requestCode == ACTIVITY_START_CAMERA)
            {
                //Toast.makeText(this, "picture was taken", Toast.LENGTH_SHORT).show();
                Bundle extras = data.getExtras();
                Bitmap photoCaptured = (Bitmap) extras.get("data");
                ivImage.setImageBitmap(photoCaptured);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photoCaptured.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] image = stream.toByteArray();
                file = new ParseFile("itemimage.png", image);
                file.saveInBackground();
                user.put("image", image);
                user.saveInBackground();
                user.notify();
                Toast.makeText(SettingsActivity.this, "Image Uploaded",
                        Toast.LENGTH_SHORT).show();

                ivEditImage.setVisibility(View.VISIBLE);
                ivEditImage.setClickable(true);

                ibUploadProf.setVisibility(View.INVISIBLE);
                ibUploadProf.setClickable(false);
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
                    ivImage.setImageBitmap(rotateBitmapOrientation(photoPath));
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                    byte[] image = stream.toByteArray();
                    file = new ParseFile("itemimage.png", image);
                    file.saveInBackground();
                    Toast.makeText(SettingsActivity.this, "Image Uploaded",
                            Toast.LENGTH_SHORT).show();

                    ivEditImage.setVisibility(View.VISIBLE);
                    ivEditImage.setClickable(true);

                    ibUploadProf.setVisibility(View.INVISIBLE);
                    ibUploadProf.setClickable(false);
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


