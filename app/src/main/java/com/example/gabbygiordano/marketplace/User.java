package com.example.gabbygiordano.marketplace;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by gabbygiordano on 7/12/17.
 */

public class User implements Parcelable {

    public String name;
    public String username;
    public String email;
    public String password;
    public String college;
    public String phone;

    protected User(Parcel in) {
        name = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        college = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(username);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(college);
        parcel.writeString(phone);
    }
}
