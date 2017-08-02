package com.example.gabbygiordano.marketplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.gabbygiordano.marketplace.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends ItemsListFragment {

    RecyclerView rvFavorites;

    ArrayList<Item> tempItems;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tempItems = new ArrayList<>();

        setRetainInstance(true);
    }

    @Override
    public void populateTimeline(){
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> favs = (ArrayList<String>) user.get("favoriteItems");
        if(items != null && items.size() == favs.size()){
            //Log.d("itemsize", items.size() + " " + favs.size());
        }
        else {
            ParseQuery<Item> query = ParseQuery.getQuery("Item");
            query.include("owner");
            query.include("image");
            query.whereContainedIn("objectId", favs);
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
        ArrayList<String> favs = (ArrayList<String>) user.get("favoriteItems");


        ParseQuery<Item> query = ParseQuery.getQuery("Item");
        query.include("owner");
        query.include("image");
        query.whereContainedIn("objectId", favs);
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

    public static FavoritesFragment newInstance(){
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        return favoritesFragment;
    }
}
