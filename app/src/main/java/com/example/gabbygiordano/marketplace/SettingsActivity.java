package com.example.gabbygiordano.marketplace;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
//    ImageButton ibUploadProf;
//    ImageView ivEditImage;
//    ImageView ivImage;

    EditText etName;
    EditText etPhone;
    EditText etEmail;

    Button btSaveChanges;
    ImageButton ibLogout;

    Boolean changedName;
    Boolean changedPhone;
    Boolean changedEmail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
//        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);
//        ivEditImage = (ImageView) findViewById(R.id.ivEditImage);
//        ivImage = (ImageView) findViewById(R.id.ivImage);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btSaveChanges = (Button) findViewById(R.id.btSaveChanges);
        ibLogout = (ImageButton) findViewById(R.id.ibLogOut);

        changedName = true;
        changedPhone = true;
        changedEmail = true;

        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            etName.setText(user.getString("name"), TextView.BufferType.EDITABLE);
            etPhone.setText(String.valueOf(user.getLong("phone")), TextView.BufferType.EDITABLE);
            etEmail.setText(user.getString("email"), TextView.BufferType.EDITABLE);
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
                    Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivityForResult(i, 1);
                    Toast.makeText(getApplicationContext(), "Information Updated", Toast.LENGTH_LONG).show();
                }

            }
        });

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


