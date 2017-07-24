package com.example.gabbygiordano.marketplace;

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
    TextView tvChangeName;
    TextView tvChangePhone;
    TextView tvUploadProf;
    ImageButton ibUploadProf;

    EditText etName;
    EditText etPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvChangeName = (TextView) findViewById(R.id.tvChangeName);
        tvChangePhone = (TextView) findViewById(R.id.tvChangePhone);
        tvUploadProf = (TextView) findViewById(R.id.tvUploadProf);
        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);

        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            tvName.setText(user.getString("name"));
            tvPhone.setText(String.valueOf(user.getLong("phone")));

        }


        tvChangeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etName.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "Enter new name!", Toast.LENGTH_LONG).show();
                }
                else{
                    String name = etName.getText().toString();
                    user.put("name", name);
                    user.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        tvChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPhone.getText().toString() == "") {
                    Toast.makeText(getApplicationContext(), "Enter new phone!", Toast.LENGTH_LONG).show();
                }
                else{
                    String phone = etPhone.getText().toString();
                    user.put("phone", Long.parseLong(phone));
                    user.saveInBackground();
                    Toast.makeText(getApplicationContext(), "Phone Updated", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
