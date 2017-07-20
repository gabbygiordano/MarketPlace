package com.example.gabbygiordano.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    List<String> schools = new ArrayList<String>();
    int size;
    int records;
    int page;

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

    AutoCompleteTextView tvAutocompleteCollege;

    ParseUser parseUser;

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
        // schoolSpinner = (Spinner) findViewById(R.id.schoolOptions);
        etSchoolEmail = (EditText) findViewById(R.id.etSchoolEmail);
        etFullName = (EditText) findViewById(R.id.etFullName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        etPassword = (EditText) findViewById(R.id.etPassword);
        registerButton = (Button) findViewById(R.id.registerButton);
        tvGotoLogin = (TextView) findViewById(R.id.tvAccount);
        tvAutocompleteCollege = (AutoCompleteTextView) findViewById(R.id.tvAutocompleteCollege);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean flag = false;

                String email = etSchoolEmail.getText().toString();
                String name = etFullName.getText().toString();
                String username = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                String phone = etPhoneNumber.getText().toString();
                String school = tvAutocompleteCollege.getText().toString();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter name", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (phone.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter phone number", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if (school.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter school", Toast.LENGTH_LONG).show();
                    flag = true;
                } else {
                    // Create the ParseUser
                    parseUser = new ParseUser();

                    // Set core properties
                    parseUser.setUsername(username);
                    parseUser.setPassword(password);
                    parseUser.setEmail(email);

                    // Set custom properties
                    parseUser.put("name", name);
                    parseUser.put("phone", Long.parseLong(phone));
                    parseUser.put("college", school);
                    parseUser.put("contact", preference);

                    onRegisterClicked();
                }
            }
        });

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
        records = 0;
        page = 0;
        getSchools(false);
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getSchools(boolean finished) {
        // make network request to get university list using API
        MarketPlaceClient.getSchoolList(page, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (page == 0) {
                        size = response.getJSONObject("metadata").getInt("total");
                        Log.e("Client", "Size = " + String.valueOf(size));
                    }

                    records += response.getJSONArray("results").length();
                    Log.e("Client", "Records = " + String.valueOf(records));

                    // add each school to list
                    for (int i = 0; i < response.getJSONArray("results").length(); i++) {
                        schools.add(response.getJSONArray("results").getJSONObject(i).getString("school.name"));
                        Log.e("Client", response.getJSONArray("results").getJSONObject(i).getString("school.name"));
                    }

                    Log.e("Client", "heree");

                    if (records < size) {
                        page += 1;
                        Log.e("Client", String.valueOf(page));
                        getSchools(false);
                    } else {
                        Log.e("Client", "here");
                        setupAdapter();
                    }
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
        //setupAdapter();
    }

    public void setupSpinnerAdapter() {
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

    public void setupAdapter() {
        Log.e("Client", "Setting up adapter");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schools);
        tvAutocompleteCollege.setAdapter(adapter);
    }

    public void onRegisterClicked() {
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
