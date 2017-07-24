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
    TextView tvUploadProf;
    ImageButton ibUploadProf;

    EditText etName;
    EditText etPhone;

    Button btSaveChanges;

    Boolean changedName;
    Boolean changedPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Settings");

        tvName = (TextView) findViewById(R.id.tvName);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvUploadProf = (TextView) findViewById(R.id.tvUploadProf);
        ibUploadProf = (ImageButton) findViewById(R.id.ibUploadProf);

        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);

        btSaveChanges = (Button) findViewById(R.id.btSaveChanges);

        changedName = true;
        changedPhone = true;

        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            tvName.setText(user.getString("name"));
            tvPhone.setText(String.valueOf(user.getLong("phone")));

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
                if(true){
                    if(changedName) {
                        String name = etName.getText().toString();
                        user.put("name", name);
                        user.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Name Updated", Toast.LENGTH_LONG).show();
                    }
                    if(changedPhone) {
                        String phone = etPhone.getText().toString();
                        user.put("phone", Long.parseLong(phone));
                        user.saveInBackground();
                        Toast.makeText(getApplicationContext(), "Phone Updated", Toast.LENGTH_LONG).show();
                    }
                }

                Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivityForResult(i, 1);
            }
        });


    }
}
