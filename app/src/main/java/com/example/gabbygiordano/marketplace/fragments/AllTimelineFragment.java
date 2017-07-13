package com.example.gabbygiordano.marketplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.example.gabbygiordano.marketplace.Item;

/**
 * Created by tanvigupta on 7/12/17.
 */

public class AllTimelineFragment extends ItemsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //populate();
    }

    @Override
    public void populate() {
        Item item = Item.fromInput("name", "desc", "pr", 0, null, "all");
        addItem(item);
    }
}
