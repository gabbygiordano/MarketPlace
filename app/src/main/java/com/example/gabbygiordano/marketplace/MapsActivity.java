package com.example.gabbygiordano.marketplace;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.BounceInterpolator;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.HashMap;
import java.util.List;

import static com.example.gabbygiordano.marketplace.R.id.map;

public class MapsActivity extends AppCompatActivity implements GoogleMap.OnMapLongClickListener, GoogleMap.OnInfoWindowClickListener{

    MapFragment mapFragment;
    FusedLocationProviderClient mFusedLocationClient;
    BottomNavigationView bottomNavigationView;

    int ADD_ITEM_REQUEST = 10;

    GoogleMap mMap;

    private HashMap<String, Item> markers= new HashMap<String, Item>();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        context = this;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(1);
        menuitem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent i_home = new Intent(MapsActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        finish();
                        break;

                    case R.id.action_maps:
                        Toast.makeText(MapsActivity.this, "Maps Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(MapsActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        finish();
                        break;

                    case R.id.action_profile:
                        Intent i_profile = new Intent(MapsActivity.this, ProfileActivity.class);
                        startActivity(i_profile);
                        finish();
                        break;
                }

                return false;
            }
        });


        // Do a null check to confirm that we have not already instantiated the map.
        if (mapFragment == null) {
            mapFragment = (MapFragment) getFragmentManager().findFragmentById(map);
            // Check if we were successful in obtaining the map.
            if (mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap map) {
                        loadMap(map);
                    }
                });
            }
        }
    }

    // The Map is verified. It is now safe to manipulate the map.
    protected void loadMap(GoogleMap googleMap) {
        if (googleMap != null) {
            // ... use map here
            mMap = googleMap;

            Log.e("MapsActivity", "Map loaded successfully!");

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setPadding(0, 0, 0, 175);

            mMap.setMyLocationEnabled(true); // false to disable
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setTiltGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);

            mMap.setOnMapLongClickListener(this);
            mMap.setOnInfoWindowClickListener(this);

            getCurrentLocation();
        }
    }

    public void getCurrentLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        //noinspection MissingPermission
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            mMap.animateCamera(cameraUpdate);

                            mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                @Override
                                public void onCameraIdle() {
                                    loadData();
                                    mMap.setOnCameraIdleListener(null);
                                }
                            });

                        } else {
                            loadData();
                        }
                    }
                });
    }

    public void loadData() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.whereExists("lat");
        query.whereExists("long");
        //query.whereNotEqualTo("lat", Double.NaN);
        //query.whereNotEqualTo("long", Double.NaN);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> objects, ParseException e) {
                if (objects != null && !objects.isEmpty()) {
                    for (int i = 0; i < objects.size(); i++) {
                        // listingPosition is a LatLng point
                        LatLng listingPosition = new LatLng(objects.get(i).getLatitute(), objects.get(i).getLongitude());
                        // Create the marker on the fragment
                        Marker marker = mMap.addMarker(new MarkerOptions().position(listingPosition));
                        marker.setTitle(objects.get(i).getItemName());
                        // marker.setSnippet(objects.get(i).getDescription());
                        dropPinEffect(marker);
                        markers.put(marker.getId(), objects.get(i));
                    }
                }
                Toast.makeText(context, "Long click to add item", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void dropPinEffect(final Marker marker) {
        // Handler allows us to repeat a code block after a specified delay
        final android.os.Handler handler = new android.os.Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 1500;

        // Use the bounce interpolator
        final android.view.animation.Interpolator interpolator =
                new BounceInterpolator();

        // Animate marker with a bounce updating its position every 15ms
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                // Calculate t for bounce based on elapsed time
                float t = Math.max(
                        1 - interpolator.getInterpolation((float) elapsed
                                / duration), 0);
                // Set the anchor
                marker.setAnchor(0.5f, 1.0f + 14 * t);

                if (t > 0.0) {
                    // Post this event again 15ms from now.
                    handler.postDelayed(this, 15);
                } else { // done elapsing, show window
                    marker.showInfoWindow();
                }
            }
        });
    }

    // Fires when info window for a marker is clicked
    @Override
    public void onInfoWindowClick(Marker marker) {
        Item mapItem = (Item) markers.get(marker.getId());
        Intent i = new Intent(MapsActivity.this, DetailsActivity.class);
        i.putExtra("ID", mapItem.getObjectId());
        startActivity(i);
    }

    // Fires when a long press happens on the map
    @Override
    public void onMapLongClick(final LatLng point) {
        showAlertDialogForPoint(point);
    }

    // Display the alert that adds the marker
    private void showAlertDialogForPoint(final LatLng point) {
        // Create alert dialog builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Add item here?");

        // Create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();

        // Configure dialog button (OK)
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Go to add item with these coordinates
                        Intent i_add = new Intent(MapsActivity.this, AddItemActivity.class);
                        i_add.putExtra("lat", point.latitude);
                        i_add.putExtra("long", point.longitude);
                        startActivityForResult(i_add, ADD_ITEM_REQUEST);
                    }
                });

        // Configure dialog button (Cancel)
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
                });

        // Display the dialog
        alertDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) {
            // drop new marker
            String id = data.getStringExtra("item_id");

            // Execute the query to find the object with ID
            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            query.include("owner");
            query.include("image");
            query.getInBackground(id, new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        Log.e("ItemsListFragment", "Query successful");
                        // listingPosition is a LatLng point
                        LatLng listingPosition = new LatLng(item.getLatitute(), item.getLongitude());
                        // Create the marker on the fragment
                        Marker marker = mMap.addMarker(new MarkerOptions().position(listingPosition));
                        marker.setTitle(item.getItemName());
                        marker.setSnippet(item.getDescription());
                        dropPinEffect(marker);
                    } else {
                        Log.e("ItemsListFragment", e.getMessage());
                    }
                }
            });
        }
    }
}
