package com.example.gabbygiordano.marketplace;

import com.parse.ParseObject;

/**
 * Created by gabbygiordano on 7/12/17.
 */

public class User extends ParseObject {

    public String name;
    public String username;
    public String email;
    public String password;
    public String college;
    public long phone;
    public String contactMethod;

    // default constructor for parse
    public User() {
    }

    public User(String name, String username, String email, String password, String college, long phone, String contact) {
        setName(name);
        setUsername(username);
        setEmail(email);
        setPassword(password);
        setCollege(college);
        setPhone(phone);
        setContactMethod(contact);
    }

    public static User fromInput(String name, String username, String email, String password, String college, long phone, String contact) {
        User user = new User(name, username, email, password, college, phone, contact);

        return user;
    }

    public String getName() {
        return getString("name");
    }

    public void setName(String name) {
        put("name", name);
    }

    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        put("username", username);
    }

    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        put("email", email);
    }

    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getCollege() {
        return getString("college");
    }

    public void setCollege(String college) {
        put("college", college);
    }

    public long getPhone() {
        return getLong("phone");
    }

    public void setPhone(long phone) {
        put("phone", phone);
    }

    public String getContactMethod() {
        return getString("contact");
    }

    public void setContactMethod(String contactMethod) {
        put("contact", contactMethod);
    }
}
