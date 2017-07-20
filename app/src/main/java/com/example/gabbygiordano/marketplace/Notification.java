package com.example.gabbygiordano.marketplace;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Created by tanvigupta on 7/20/17.
 */

@ParseClassName("Notification")
public class Notification extends ParseObject {

    // public default constructor for parse
    public Notification() {
        super();
    }

    // constructor
    public Notification(ParseUser owner, ParseUser buyer, Item item) {
        setOwner(owner);
        setBuyer(buyer);
        setItem(item);
    }

    public Item getItem() {
        return (Item) getParseObject("item");
    }

    public void setItem(Item item) {
        put("item", item);
    }

    public ParseUser getOwner() {
        return getParseUser("owner");
    }

    public void setOwner(ParseUser user) {
        put("owner", user);
    }

    public ParseUser getBuyer() {
        return getParseUser("buyer");
    }

    public void setBuyer(ParseUser buyer) {
        put("buyer", buyer);
    }
}
