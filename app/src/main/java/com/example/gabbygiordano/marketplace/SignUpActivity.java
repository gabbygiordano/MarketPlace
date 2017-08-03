package com.example.gabbygiordano.marketplace;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class SignUpActivity extends AppCompatActivity {

    List<String> schools = new ArrayList<String>();
    int size;
    int records;
    int page;

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
    TextView tvAccount;

    AutoCompleteTextView tvAutocompleteCollege;

    ParseUser parseUser;

    private FusedLocationProviderClient mFusedLocationClient;
    double latitude;
    double longitude;
    Geocoder geocoder;
    List<Address> addresses;
    String postalCode;
    final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOC = 1;
    boolean useLoc;
    String m_Text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Sign Up for Marketplace!");

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
        tvAccount = (TextView) findViewById(R.id.tvAccount);

        String login = "Already have an account? Login.";

        SpannableStringBuilder ssb_a = new SpannableStringBuilder(login);

        ForegroundColorSpan redForegroundColorSpan = new ForegroundColorSpan(Color.rgb(255, 87, 34));
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);

        ssb_a.setSpan(redForegroundColorSpan, 25, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssb_a.setSpan(bold, 25, ssb_a.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvAccount.setText(ssb_a, TextView.BufferType.EDITABLE);

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
                ArrayList<String> favoriteItems = new ArrayList<String>();
                ArrayList<String> interestedItems = new ArrayList<String>();
//                ArrayList<ParseUser> ProfilePhoto = new ArrayList<ParseUser>();

                if (email.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter email", Toast.LENGTH_LONG).show();
                    flag = true;
                } else if(!email.contains(".edu")){
                    Toast.makeText(getApplicationContext(), "Email needs to be a .edu email", Toast.LENGTH_LONG).show();
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
                    parseUser.put("publicEmail", email);
                    parseUser.put("favoriteItems", favoriteItems);
                    parseUser.put("interestedItems", interestedItems);
//                    parseUser.put("profilePhoto", ProfilePhoto);

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

        // get current location
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        int finePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (finePermission != PackageManager.PERMISSION_GRANTED) {
            Log.e("ZIP", "Location access denied");

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOC);
        } else {
            getLastLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOC: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    getLastLocation();
                } else {
                    useLoc = false;

                    if (!useLoc) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Unable to access location, enter zip");

                        // Set up the input
                        final EditText input = new EditText(this);
                        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                postalCode = input.getText().toString();
                                useLoc = true;
                            }
                        });

                        builder.show();
                    }

                    // get list of US universities
                    records = 0;
                    page = 0;
                    getSchools(false);
                }
            }
        }
    }

    public void getLastLocation() {
        //noinspection MissingPermission
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            // get zip
                            geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                            try {
                                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            postalCode = addresses.get(0).getPostalCode();
                            Log.e("ZIP", postalCode);

                            useLoc = true;

                            // get list of US universities
                            records = 0;
                            page = 0;
                            getSchools(false);
                        } else {
                            useLoc = false;

                            if (!useLoc) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                                builder.setMessage("Unable to access location, enter zip");

                                // Set up the input
                                final EditText input = new EditText(getApplicationContext());
                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                builder.setView(input);

                                // Set up the buttons
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        postalCode = input.getText().toString();

                                        useLoc = true;
                                    }
                                });

                                builder.show();
                            }

                            // get list of US universities
                            records = 0;
                            page = 0;
                            getSchools(false);
                        }
                    }
                });
    }

    public void goToLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void getSchools(boolean finished) {
        Log.e("Client", postalCode);
        Log.e("Client", "Getting schools...");

        // make network request to get university list using API
        MarketPlaceClient.getSchoolList(page, postalCode, useLoc, new JsonHttpResponseHandler() {
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
                Log.e("Client", "FAILURE 1");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                try {
                    Log.e("Client", "FAILURE 2 = " + statusCode + " " + errorResponse.getJSONArray("errors").getJSONObject(0).getString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Log.e("Client", "FAILURE 3");
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
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        });
    }
}
