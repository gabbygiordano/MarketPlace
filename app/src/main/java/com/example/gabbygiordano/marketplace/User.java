package com.example.gabbygiordano.marketplace;

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
    public String phone;

    public User() {
    }

    public static User fromInput(String name, String username, String email, String password, String college, String phone) {
        User user = new User();

        user.name = name;
        user.username = username;
        user.email = email;
        user.password = password;
        user.college = college;
        user.phone = phone;

        return user;
    }
}
