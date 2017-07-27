package com.example.gabbygiordano.marketplace;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import static com.example.gabbygiordano.marketplace.ItemAdapter.getContext;

public class FavoritesActivity extends AppCompatActivity {

    RecyclerView rvFavorites;

    ArrayList<Item> items;
    ItemAdapter itemAdapter;
    String id;

    int ADD_ITEM_REQUEST = 10;

    Context mContext;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_test);
        getSupportActionBar().setTitle("Favorites");

        context = this;

        // perform find view by id lookups
        rvFavorites = (RecyclerView) findViewById(R.id.rvFavorites);

        // initialize arraylist
        items = new ArrayList<>();

        //construct the adapter from the array list
        itemAdapter = new ItemAdapter(items, getContext());

        mContext = getContext();

        // RecyclerView setup (layout manager, use adapter)
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvFavorites.setLayoutManager(linearLayoutManager);
        rvFavorites.setAdapter(itemAdapter);
        rvFavorites.setHasFixedSize(true);

        ParseUser user = ParseUser.getCurrentUser();

        ArrayList<String> favs = (ArrayList<String>) user.get("favoriteItems");

        for (int i = 0; i < favs.size(); i++) {
            ParseQuery<Item> query = ParseQuery.getQuery("Item");
            query.include("owner");
            query.include("image");
            query.getInBackground(favs.get(i), new GetCallback<Item>() {
                public void done(Item item, ParseException e) {
                    if (e == null) {
                        items.add(0, item);
                        itemAdapter.notifyItemInserted(0);
                    } else {
                        // something went wrong
                    }
                }
            });
        }

        // addItems((List<Item>) user.get("favoritesList"));
    }

    public void addItems(List<Item> list) {
        for (int i = 0; i < list.size(); i++) {
            items.add(list.get(i));
            try {
                (list.get(i)).fetchIfNeeded();
                list.get(i).getOwner().fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            itemAdapter.notifyItemInserted(items.size() - 1);
        }

    }


    @Override
    public void onBackPressed() {
        Intent i_home = new Intent(FavoritesActivity.this, ProfileActivity.class);
        i_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i_home);
    }
}
