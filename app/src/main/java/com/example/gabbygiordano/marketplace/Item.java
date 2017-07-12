package com.example.gabbygiordano.marketplace;

import android.graphics.Bitmap;

import org.parceler.Parcel;

/**
 * Created by gabbygiordano on 7/12/17.
 */

@Parcel
public class Item {

    public String itemName;
    public String description;
    public String price;
    public int condition;
    public User user;

    public Bitmap resource;

    public Item() {
    }

    public static Item fromInput(String name, String description, String price, int condition, User user) {
        Item item = new Item();

        item.itemName = name;
        item.description = description;
        item.price = price;
        item.condition = condition;
        item.user = user;

        return item;
    }
}
