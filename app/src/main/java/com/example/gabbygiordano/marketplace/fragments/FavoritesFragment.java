package com.example.gabbygiordano.marketplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.example.gabbygiordano.marketplace.Item;
import com.example.gabbygiordano.marketplace.ItemAdapter;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends ItemsListFragment {

    RecyclerView rvFavorites;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    String id;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        // populateTimeline();
    }
    @Override
    public void populateTimeline(){
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> favs = (ArrayList<String>) user.get("favoriteItems");

        for (int i = 0; i < favs.size(); i++) {
            ParseQuery<Item> query = ParseQuery.getQuery("Item");
            query.include("owner");
            query.include("image");
            query.orderByDescending("_created_at");
            query.setLimit(limit); // 20 items per page
            query.setSkip(page * limit); // skip first (page * 20) items
            query.getInBackground(favs.get(i), new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        items.add(item);
                        itemAdapter.notifyItemInserted(items.size()-1);
                    } else {
                        scrollListener.resetState();
                        // something went wrong
                    }
                }
            });
        }

    }




    public void addItems(List<Item> list) {
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
            try {
                (list.get(i)).fetchIfNeeded();
                list.get(i).getOwner().fetchIfNeeded();
                list.get(i).fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemAdapter.notifyItemInserted(items.size() - 1);
        }

    }
}
