package com.example.gabbygiordano.marketplace;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private Button btLogIn;
    public EditText etUserName;
    public EditText etPassword;
    public TextView tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btLogIn = (Button) findViewById(R.id.btLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgot = (TextView) findViewById(R.id.tvForgot);

    }

    public void onLoginSuccess(){
        // if username and password are accepted, start new activity
        // note: Intent is currently commented out until we create the HomeActivity class

        // Intent i = new Intent(this, HomeActivity.class);
        // startActivity(i);
    }
}
