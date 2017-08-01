package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvUsername;
    TextView tvUploadProf;
    ImageButton ibUploadProf;

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etUsername;

    ImageButton btSaveChanges;
    ImageButton ibLogout;

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

        context = this;

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvUploadProf = (TextView) findViewById(R.id.tvUploadProf);
        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);

        btSaveChanges = (ImageButton) findViewById(R.id.btSaveChanges);
        ibLogout = (ImageButton) findViewById(R.id.ibLogOut);

        changedName = true;
        changedPhone = true;
        changedEmail = true;
        changedUsername = true;
        changedPassword = true;

        final ParseUser user = ParseUser.getCurrentUser();
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
                if (true) {
                    if (changedName) {
                        String name = etName.getText().toString();
                        user.put("name", name);
                        user.saveInBackground();
                        //Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(i, 1);
                    }
                    if (changedPhone) {
                        String phone = etPhone.getText().toString();
                        user.put("phone", Long.parseLong(phone));
                        user.saveInBackground();
                        //Toast.makeText(getApplicationContext(), "Phone Updated", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(i, 1);
                    }
                    if (changedEmail) {
                        String email = etEmail.getText().toString();
                        user.put("email", email);
                        user.put("publicEmail", email);
                        user.saveInBackground();
                        //Toast.makeText(getApplicationContext(), "Email Updated", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(i, 1);
                    }

                    Toast.makeText(getApplicationContext(), "Information Updated", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}

