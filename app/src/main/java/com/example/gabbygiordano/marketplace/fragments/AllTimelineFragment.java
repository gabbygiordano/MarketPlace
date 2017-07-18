package com.example.gabbygiordano.marketplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gabbygiordano.marketplace.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by tanvigupta on 7/12/17.
 */

public class AllTimelineFragment extends ItemsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateAllTimeline();

    }

    public void populateAllTimeline(){
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        // query.whereEqualTo("playerName", "Dan Stemkoski");
        query.include("owner");
        query.setLimit(200);
        query.orderByDescending("_created_at");
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    Log.d("items", "Retrieved " + itemsList.size() + " items");
                    addItems(itemsList);
                } else {
                    Log.d("items", "Error: " + e.getMessage());
                }
            }
        });
    }

}
