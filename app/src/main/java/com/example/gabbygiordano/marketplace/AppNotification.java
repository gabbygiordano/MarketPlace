package com.example.gabbygiordano.marketplace;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

/**
 * Created by tanvigupta on 7/20/17.
 */

@ParseClassName("AppNotification")
public class AppNotification extends ParseObject {

    // public default constructor for parse
    public AppNotification() {
        super();
    }

    // constructor
    public AppNotification(ParseUser owner, ParseUser buyer, Item item, Date date) {
        setOwner(owner);
        setBuyer(buyer);
        setItem(item);
        setDate(date);
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

    public void setDate(Date date) {
        put("date", date);
    }

    public Date getDate() {
        return (Date) get("date");
    }
}
