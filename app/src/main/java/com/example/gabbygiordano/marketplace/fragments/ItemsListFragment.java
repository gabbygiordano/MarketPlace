package com.example.gabbygiordano.marketplace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.gabbygiordano.marketplace.EndlessRecyclerViewScrollListener;
import com.example.gabbygiordano.marketplace.Item;
import com.example.gabbygiordano.marketplace.ItemAdapter;
import com.example.gabbygiordano.marketplace.R;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ItemsListFragment extends Fragment {

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    ImageView ivItemImage;



    RecyclerView rvItems;
    SwipeRefreshLayout swipeContainer;

    int ADD_ITEM_REQUEST = 10;

    int page = 0;
    final int limit = 20;
    EndlessRecyclerViewScrollListener scrollListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items_list, container, false);

        // perform find view by id lookups
        rvItems = (RecyclerView) v.findViewById(R.id.rvItems);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(itemAdapter);
        rvItems.setHasFixedSize(true);

        // swipe to refresh setup

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // set up infinite pagination
        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                populateTimeline();
            }
        };
        // Adds the scroll listener to RecyclerView
        rvItems.addOnScrollListener(scrollListener);


        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        itemAdapter.clear();
        items.clear();

        page = 0;

        populateTimeline();
    }

    public void populateTimeline() {}

    public void fetchTimelineAsync(int page) {}

    public void addItems(List<Item> list){
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
            itemAdapter.notifyItemInserted(items.size()-1);
        }
        scrollListener.resetState();
        page += 1;
    }

    public void refreshItems(List<Item> list) {
        itemAdapter.clear();

        List<Item> new_items = new ArrayList<Item>();

        for (int i = 0; i < list.size(); i++) {
            new_items.add(list.get(i));
        }

        itemAdapter.addAll(new_items);

        scrollListener.resetState();

        swipeContainer.setRefreshing(false);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) {
            String id = data.getStringExtra("item_id");
            String type = data.getStringExtra("type");

            Log.e("item_id", id);

            // Execute the query to find the object with ID
            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            query.include("owner");
            query.include("image");
            query.getInBackground(id, new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        // item was found
                        items.add(0, item);
                        itemAdapter.notifyItemInserted(0);
                        rvItems.scrollToPosition(0);
                    } else {
                        Log.e("ItemsListFragment", e.getMessage());
                    }
                }
            });
        }
    }
}
