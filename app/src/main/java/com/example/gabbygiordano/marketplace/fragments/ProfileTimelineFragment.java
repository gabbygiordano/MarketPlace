package com.example.gabbygiordano.marketplace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gabbygiordano.marketplace.Item;
import com.example.gabbygiordano.marketplace.ItemAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
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

    ArrayList<Item> items;
    ItemAdapter itemAdapter;

    String id;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id = getArguments().getString("itemId");

        setRetainInstance(true);
    }


    @Override
    public void populateTimeline() {
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
                    if (itemsList != null && !itemsList.isEmpty()) {
                        addItems(itemsList);
                    }
                } else {
                    Log.d("AllFragment", e.getMessage());
                    scrollListener.resetState();
                }
            }
        });

    }

    @Override
    public void fetchTimelineAsync(int page){
        if(id != ""){

            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            query.include("owner");
            query.include("favoritesList");
            query.whereEqualTo("itemId", id);
            query.orderByDescending("_created_at");
            query.setLimit(limit); // 20 items per page
            query.setSkip(page * limit); // skip first (page * 20) items
            query.findInBackground(new FindCallback<Item>() {
                public void done(List<Item> itemsList, ParseException e) {
                    if (e == null) {
                        if (itemsList != null && !itemsList.isEmpty()) {
                            addItems(itemsList);
                        }
                    } else {
                        Log.d("AllFragment", e.getMessage());
                        scrollListener.resetState();
                    }
                }
            });
        }
        else{
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
                        if (itemsList != null && !itemsList.isEmpty()) {
                            addItems(itemsList);
                        }
                    } else {
                        Log.d("AllFragment", e.getMessage());
                        scrollListener.resetState();
                    }
                }
            });
        }

    }
}
