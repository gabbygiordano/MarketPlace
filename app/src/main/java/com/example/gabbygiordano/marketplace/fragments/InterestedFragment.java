package com.example.gabbygiordano.marketplace.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by gabbygiordano on 7/27/17.
 */

public class InterestedFragment extends ItemsListFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static InterestedFragment newInstance(){
        InterestedFragment interestedFragment = new InterestedFragment();
        return interestedFragment;
    }
}
