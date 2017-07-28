package com.example.gabbygiordano.marketplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.gabbygiordano.marketplace.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabbygiordano on 7/27/17.
 */

public class InterestedFragment extends ItemsListFragment {

    ArrayList<Item> tempItems;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Override
    public void populateTimeline(){
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> interests = (ArrayList<String>) user.get("interestedItems");
        if(items != null && items.size() == interests.size()){
            // Log.d("itemsize", items.size() + " " + interests.size());
        }
        else {
            ParseQuery<Item> query = ParseQuery.getQuery("Item");
            query.include("owner");
            query.include("image");
            query.whereContainedIn("objectId", interests);
            query.setLimit(limit); // 20 items per page
            query.setSkip(page * limit); // skip first (page * 20) items
            query.findInBackground(new FindCallback<Item>() {
                public void done(List<Item> itemsList, ParseException e) {
                    if (e == null) {
                        if(itemsList != null && !itemsList.isEmpty()) {
                            addItems(itemsList);
                            // itemAdapter.notifyDataSetChanged();
                        }
                    } else {
                        scrollListener.resetState();
                        // something went wrong
                    }
                }
            });
        }
    }

    @Override
    public void fetchTimelineAsync(int page) {
        ParseUser user = ParseUser.getCurrentUser();
        ArrayList<String> interests = (ArrayList<String>) user.get("interestedItems");

        ParseQuery<Item> query = ParseQuery.getQuery("Item");
        query.include("owner");
        query.include("image");
        query.whereContainedIn("objectId", interests);
        query.setLimit(limit); // 20 items per page
        query.setSkip(page * limit); // skip first (page * 20) items
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                if (e == null) {
                    if (itemsList != null && !itemsList.isEmpty()) {
                        refreshItems(itemsList);
                    }
                } else {
                    Log.d("AllFragment", e.getMessage());
                    swipeContainer.setRefreshing(false);
                    scrollListener.resetState();
                }
            }
        });
    }

    public static InterestedFragment newInstance(){
        InterestedFragment interestedFragment = new InterestedFragment();
        return interestedFragment;
    }
}
