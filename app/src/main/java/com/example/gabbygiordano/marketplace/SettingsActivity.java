package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class SettingsActivity extends AppCompatActivity {

    TextView tvName;
    TextView tvPhone;
    TextView tvEmail;
    TextView tvUploadProf;;
    ImageButton ibUploadProf;

    EditText etName;
    EditText etPhone;
    EditText etEmail;

    Button btSaveChanges;

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
        tvUploadProf = (TextView) findViewById(R.id.tvUploadProf);
        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);

        btSaveChanges = (Button) findViewById(R.id.btSaveChanges);

        changedName = true;
        changedPhone = true;
        changedEmail = true;

        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            tvName.setText(user.getString("name"));
            tvPhone.setText(String.valueOf(user.getLong("phone")));
            tvEmail.setText(user.getString("email"));

        }

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
                        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
                    }
                    if (changedPhone) {
                        String phone = etPhone.getText().toString();
                        user.put("phone", Long.parseLong(phone));
                        user.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Phone Updated", Toast.LENGTH_LONG).show();
                    }
                    if (changedEmail) {
                        String email = etEmail.getText().toString();
                        user.put("email", email);
                        user.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Email Updated", Toast.LENGTH_LONG).show();
                    }

                }

                btSaveChanges.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.saveInBackground();
                        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivityForResult(i, 1);
                    }
                });
            }
        });
    }
}

