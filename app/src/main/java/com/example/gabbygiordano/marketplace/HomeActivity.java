package com.example.gabbygiordano.marketplace;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final BottomNavigationView bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Toast.makeText(HomeActivity.this, "Home Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_search:
                        Toast.makeText(HomeActivity.this, "Search Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_add:
                        Toast.makeText(HomeActivity.this, "Add Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_notifications:
                        Toast.makeText(HomeActivity.this, "Notifications Tab Selected", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_profile:
                        Toast.makeText(HomeActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                        break;
                }

                return true;
            }
        });

    }
}
