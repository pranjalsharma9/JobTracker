package com.pranjals.nsit.jobtracker;

import android.graphics.Bitmap;

/**
 * Created by Pranjal on 12-03-2016.
 */
public class Customer {

    private String name, mobile, email, address;
    private Bitmap profilePic;
    private long _id;

    Customer(long _id, String name, String mobile, String email, String address,Bitmap profilePic){
        this._id = _id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.profilePic = profilePic;
    }

    public long get_id() { return _id; }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public Bitmap getProfilePic() {
        return profilePic;
    }
}
