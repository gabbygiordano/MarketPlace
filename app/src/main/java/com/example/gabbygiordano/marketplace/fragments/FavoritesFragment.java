package com.example.gabbygiordano.marketplace.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.gabbygiordano.marketplace.Item;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class FavoritesFragment extends ItemsListFragment {

    RecyclerView rvFavorites;

    

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setRetainInstance(true);

        populateTimeline();

        // populateTimeline();
    }
    @Override
    public void populateTimeline(){
        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> favs = (ArrayList<String>) user.get("favoriteItems");
        Log.d("favs", favs.get(0));

        for (int i = 0; i < favs.size(); i++) {
            ParseQuery<Item> query = ParseQuery.getQuery("Item");
            query.include("owner");
            query.include("image");
            query.getInBackground(favs.get(i), new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        items.add(0, item);
                        Log.d("item added", item.getItemName());
                        itemAdapter.notifyItemInserted(0);
                    } else {
                        scrollListener.resetState();
                        // something went wrong
                    }
                }
            });
        }

    }




    /* public void addItems(List<Item> list) {
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

    } */

    public static FavoritesFragment newInstance(){
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        return favoritesFragment;
    }
}
