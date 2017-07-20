package com.example.gabbygiordano.marketplace;

import android.graphics.Bitmap;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by gabbygiordano on 7/12/17.
 */

@ParseClassName("Item")
public class Item extends ParseObject {

    // public default constructor for parse
    public Item() {
        super();
    }

    // constructor
    public Item(String name, String description, String price, int condition, ParseUser user, String type) {
        setItemName(name);
        setDescription(description);
        setPrice("$" + price);
        setCondition(condition);
        setOwner(user);
        setType(type);
    }

    public String getItemName() {
        return getString("item_name");
    }

    public void setItemName(String itemName) {
        put("item_name", itemName);
    }

    public String getDescription() {
        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getPrice() {
        return getString("price");
    }

    public void setPrice(String price) {
        put("price", price);
    }

    public int getCondition() {
        return getInt("condition");
    }

    public void setCondition(int condition) {
        put("condition", condition);
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public String getType() {
        return getString("type");
    }

    public void setType(String type) {
        put("type", type);
    }

    public Bitmap getResource() {
        return (Bitmap) get("resource");
    }

    public void setResource(Bitmap resource) {
        put("resource", resource);
    }
}
