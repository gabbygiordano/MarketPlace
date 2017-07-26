package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    Button btLogIn;
    EditText etUserName;
    EditText etPassword;
    TextView tvSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login to Marketplace!");

        btLogIn = (Button) findViewById(R.id.btLogIn);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvSignUp = (TextView) findViewById(R.id.tvSignUp);

        String signup = "Don't have an account? Sign up.";

        SpannableStringBuilder ssb_a = new SpannableStringBuilder(signup);

        ForegroundColorSpan redForegroundColorSpan = new ForegroundColorSpan(Color.rgb(255, 87, 34));
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        ssb_a.setSpan(redForegroundColorSpan, 23, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb_a.setSpan(bold, 23, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvSignUp.setText(ssb_a, TextView.BufferType.EDITABLE);

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(i);
            }
        });
    }

    public void onLoginSuccess(View view){
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                } else {
                    // Signup failed. Look at the ParseException to see what happened.
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
