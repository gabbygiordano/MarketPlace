package com.example.gabbygiordano.marketplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.example.gabbygiordano.marketplace.Item;
import com.example.gabbygiordano.marketplace.R;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

//import static com.example.gabbygiordano.marketplace.ItemAdapter.context;

/**
 * Created by tanvigupta on 7/12/17.
 */

public class AllTimelineFragment extends ItemsListFragment {

    MenuItem miActionProgressItem;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setRetainInstance(true);

        // populateTimeline();
    }

    @Override
    public void populateTimeline() {
        ((ProgressListener) getActivity()).showProgressBar();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include("owner");
        query.include("image");
        query.orderByDescending("_created_at");
        query.setLimit(limit); // 20 items per page
        query.setSkip(page * limit); // skip first (page * 20) items
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                ((ProgressListener) getActivity()).hideProgressBar();
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
    public void fetchTimelineAsync(int page) {
        ((ProgressListener) getActivity()).showProgressBar();
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.include("owner");
        query.include("image");
        query.orderByDescending("_created_at");
        query.setLimit(limit); // 20 items per page
        query.setSkip(page * limit); // skip first (page * 20) items
        query.findInBackground(new FindCallback<Item>() {
            public void done(List<Item> itemsList, ParseException e) {
                ((ProgressListener) getActivity()).hideProgressBar();
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v = (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);
        // Return to finish
    }



}
