package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    String[] schools;
    int size;

    Spinner schoolSpinner;
    ArrayAdapter<String> schoolsAdapter;
    String school;

    Spinner contactOptions;
    ArrayAdapter<CharSequence> contactsAdapter;
    String preference;

    EditText etSchoolEmail;
    EditText etFullName;
    EditText etUserName;
    EditText etPhoneNumber;
    EditText etPassword;
    Button registerButton;
    TextView tvGotoLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_sign_up);

        // perform find view by id lookups
        schoolSpinner = (Spinner) findViewById(R.id.schoolOptions);
        etSchoolEmail = (EditText) findViewById(R.id.etSchoolEmail);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registerButton = (Button) findViewById(R.id.registerButton);
        tvGotoLogin = (TextView) findViewById(R.id.tvGotoLogin);

        contactOptions = (Spinner) findViewById(R.id.contactOptions);
        // Create an ArrayAdapter using the string array and a default spinner layout
        contactsAdapter = ArrayAdapter.createFromResource(this, R.array.contact_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        contactsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        contactOptions.setAdapter(contactsAdapter);
        contactOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                CharSequence cs = (CharSequence) contactOptions.getSelectedItem();
                preference = (String) cs;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // get list of US universities
        getSchools();
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getSchools() {
        // make network request to get university list using API
        MarketPlaceClient.getSchoolList(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    size = response.getJSONObject("metadata").getInt("total");
                    Log.e("SIZE", String.valueOf(size));

                    schools = new String[20];

                    // add each school to list
                    for (int i = 0; i < 20; i++) {
                        schools[i] = response.getJSONArray("results").getJSONObject(i).getString("school.name");
                    }

                    setupAdapter();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    public void setupAdapter() {
        // create an ArrayAdapter using the string array and a default spinner layout
        schoolsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, schools);

        // specify the layout to use when the list of choices appears
        schoolsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply the adapter to the spinner
        schoolSpinner.setAdapter(schoolsAdapter);

        schoolSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                school = (String) schoolSpinner.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }


    public void onRegisterClicked(View view) {
        String email = etSchoolEmail.getText().toString();
        String name = etFullName.getText().toString();
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        long phone = Long.parseLong(etPhoneNumber.getText().toString());

        //User user = User.fromInput(name, username, email, password, school, phone, preference);

        // Create the ParseUser
        ParseUser parseUser = new ParseUser();

        // Set core properties
        parseUser.setUsername(username);
        parseUser.setPassword(password);
        parseUser.setEmail(email);

        // Set custom properties
        parseUser.put("name", name);
        parseUser.put("phone", phone);
        parseUser.put("college", school);
        parseUser.put("contact", preference);

        // Invoke signUpInBackground
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
