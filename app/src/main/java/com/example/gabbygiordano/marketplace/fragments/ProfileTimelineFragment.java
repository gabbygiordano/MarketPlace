package com.example.gabbygiordano.marketplace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabbygiordano.marketplace.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by gabbygiordano on 7/27/17.
 */

public class ProfileTimelineFragment extends ItemsListFragment {

    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvCollege;
    TextView tvPhone;
    ImageButton ibLogOut;
    RecyclerView rvProfileItems;

    ViewPager viewPager;
    ItemsPagerAdapter adapter;
    ImageView ivItemImage;

    ImageButton ibFavoriteOff;
    ImageButton ibFavoriteOn;


    Intent mServiceIntent;

    Button btFavorites;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // populateTimeline();



        setRetainInstance(true);
    }


    @Override
    public void populateTimeline() {
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);

        query.whereEqualTo("owner", user);
        query.include("owner");
        query.include("image");
        query.orderByDescending("_created_at");
        query.setLimit(limit); // 20 items per page
        query.setSkip(page * limit); // skip first (page * 20) items
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    if(itemsList != null && !itemsList.isEmpty()) {
                        Log.d("items", "Retrieved" + itemsList.size() + "items");
                        Log.d("items", itemsList.get(0).getItemName());
                        addItems(itemsList);
                        Log.d("items", itemsList.get(0).getItemName());
                    }
                } else {
                    Log.d("ProfileFragment", e.getMessage());
                    scrollListener.resetState();
                }
            }
        });

    }

    @Override
    public void fetchTimelineAsync(int page){
        // set text to current user info
        ParseUser user = ParseUser.getCurrentUser();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);

        query.include("owner");
        query.include("image");
        query.whereEqualTo("owner", user);
        query.orderByDescending("_created_at");
        query.setLimit(limit); // 20 items per page
        query.setSkip(page * limit); // skip first (page * 20) items
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    refreshItems(itemsList);
                } else {
                    Log.d("AllFragment", e.getMessage());
                    scrollListener.resetState();
                    swipeContainer.setRefreshing(false);
                }
            }
        });
    }

    public static ProfileTimelineFragment newInstance(){
        ProfileTimelineFragment profileTimelineFragment = new ProfileTimelineFragment();
        return profileTimelineFragment;
    }

}
