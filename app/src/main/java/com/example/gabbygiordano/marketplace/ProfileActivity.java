package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gabbygiordano.marketplace.fragments.FavoritesFragment;
import com.example.gabbygiordano.marketplace.fragments.InterestedFragment;
import com.example.gabbygiordano.marketplace.fragments.ItemsListFragment;
import com.example.gabbygiordano.marketplace.fragments.ProfilePagerAdapter;
import com.example.gabbygiordano.marketplace.fragments.ProfileTimelineFragment;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.gabbygiordano.marketplace.ItemAdapter.getContext;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvCollege;
    TextView tvPhone;
    ImageButton ibEdit;

    ViewPager viewPager;
    ProfilePagerAdapter adapter;

    TabLayout tabLayout;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    String id;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // toolbar = (Toolbar) findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Profile");

        ProfileTimelineFragment profileTimelineFragment = ProfileTimelineFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flContainer, profileTimelineFragment);
        ft.commit();

        context = this;

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        mContext = getContext();

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // perform find view by id lookups
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvCollege = (TextView) findViewById(R.id.tvCollege);
        tvPhone = (TextView) findViewById(R.id.tvContact);
        ibEdit = (ImageButton) findViewById(R.id.ibEdit);

        ibEdit.setColorFilter(Color.rgb(255, 87, 34));

        ibEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, SettingsActivity.class);
                startActivityForResult(i, 1);
            }
        });

        BottomNavigationView bottomNavigationView;

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuitem = menu.getItem(2);
        menuitem.setChecked(true);

        adapter = new ProfilePagerAdapter(getSupportFragmentManager(), this);

        // set up the adapter for the pager
        viewPager.setAdapter(adapter);


        // setup the Tab Layout to use the view pager
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);


        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    ProfileTimelineFragment profileTimelineFragment = ProfileTimelineFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, profileTimelineFragment);
                    ft.commit();
                }
                if(tab.getPosition() == 1){
                    FavoritesFragment favoritesFragment = FavoritesFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, favoritesFragment);
                    ft.commit();
                }
                if(tab.getPosition() == 2){
                    InterestedFragment interestedFragment = InterestedFragment.newInstance();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flContainer, interestedFragment);
                    ft.commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.action_home:
                        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(i_home);
                        finish();
                        break;


                    case R.id.action_notifications:
                        Intent i_notifications = new Intent(ProfileActivity.this, AppNotificationsActivity.class);
                        startActivity(i_notifications);
                        finish();
                        break;

                    case R.id.action_profile:
                        if (!tvUsername.getText().toString().equals(ParseUser.getCurrentUser().getUsername())) {
                            Intent i_profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                            startActivity(i_profile);
                            finish();
                        }
                        else {
                            Toast.makeText(ProfileActivity.this, "Profile Tab Selected", Toast.LENGTH_SHORT).show();
                            break;
                        }
                }

                return false;
            }
        });

        populateUserHeadline();
    }


    public void populateUserHeadline() {

        if(getIntent().hasExtra("itemId")){

            id = getIntent().getStringExtra("itemId");

            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            query.include("owner");
            query.whereContains("itemId", id);
            query.orderByDescending("_created_at");
            query.getInBackground(id, new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        // item was found
                        tvName.setText(item.getOwner().getString("name"));
                        tvUsername.setText(item.getOwner().getUsername());
                        tvCollege.setText(item.getOwner().getString("college"));
                        tvPhone.setText(" ");
                    } else {
                        Log.e("ItemsListFragment", e.getMessage());
                    }
                }
            });
        }
        else{
            // set text to current user info
            ParseUser user = ParseUser.getCurrentUser();
            if (user != null) {
                tvName.setText(user.getString("name"));
                tvUsername.setText(user.getUsername());
                tvCollege.setText(user.getString("college"));
                String formattedNumber = PhoneNumberUtils.formatNumber(String.valueOf(user.getLong("phone")));
                String email = user.getEmail();
                tvPhone.setText(email + ", " + formattedNumber);

                // populateTimeline(user);

            } else {

            }

        }
    }

    public void populateProfileTimeline(ParseUser user){
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(type);
        ItemsListFragment fragment = (ItemsListFragment) adapter.getRegisteredFragment(viewPager.getCurrentItem());
        fragment.activityResult(requestCode, resultCode, data);
    }

    public void addItems(List<Item> list){
        for(int i=0; i< list.size(); i++){
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        getMenuInflater().inflate(R.menu.menu_profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.miSettings) {
//            Intent i = new Intent(this, SettingsActivity.class);
//            startActivityForResult(i, 1);
//        }
//        return true;
//    }



    public void addItem(View view) {
        Intent i_add = new Intent(context, AddItemActivity.class);
        ((HomeActivity) mContext).startActivityForResult(i_add, ADD_ITEM_REQUEST);
    }

    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(ProfileActivity.this, HomeActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }


}
