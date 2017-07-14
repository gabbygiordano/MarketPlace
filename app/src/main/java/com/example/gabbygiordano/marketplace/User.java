package com.example.gabbygiordano.marketplace;

import com.parse.ParseUser;

import org.parceler.Parcel;

/**
 * Created by gabbygiordano on 7/12/17.
 */

@Parcel
public class User {

    public String name;
    public String username;
    public String email;
    public String password;
    public String college;
    public long phone;
    public String contactMethod;

    public User() {
    }

    public static User fromInput(String name, String username, String email, String password, String college, long phone, String contact) {
        User user = new User();

        user.name = name;
        user.username = username;
        user.email = email;
        user.password = password;
        user.college = college;
        user.phone = phone;
        user.contactMethod = contact;

        return user;
    }

    public static User fromParseUser(ParseUser parseUser) {
        User user = new User();

        user.name = parseUser.getString("name");
        user.username = parseUser.getUsername();
        user.email = parseUser.getEmail();
        user.password = parseUser.getString("password");
        user.college = parseUser.getString("college");
        user.phone = parseUser.getLong("phone");
        user.contactMethod = parseUser.getString("contact");

        return user;
    }
}
