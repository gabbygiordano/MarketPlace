package com.example.gabbygiordano.marketplace.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gabbygiordano.marketplace.Item;
import com.example.gabbygiordano.marketplace.ItemAdapter;
import com.example.gabbygiordano.marketplace.R;

import org.parceler.Parcels;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ItemsListFragment extends Fragment {

    ArrayList<Item> items;
    ItemAdapter itemAdapter;

    RecyclerView rvItems;

    int ADD_ITEM_REQUEST = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_items_list, container, false);

        // perform find view by id lookups
        rvItems = (RecyclerView) v.findViewById(R.id.rvItems);

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvItems.setLayoutManager(linearLayoutManager);
        rvItems.setAdapter(itemAdapter);
        rvItems.setHasFixedSize(true);

        return v;
    }

    public void addItem(Item item) {
        items.add(item);
        itemAdapter.notifyItemInserted(items.size() - 1);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK) {
            Item item = (Item) Parcels.unwrap(data.getParcelableExtra("item"));
            items.add(item);
            itemAdapter.notifyDataSetChanged();
            //rvItems.scrollToPosition(0);
        }
    }

    public void populate() {}
}
