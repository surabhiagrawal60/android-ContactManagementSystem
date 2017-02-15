package com.example.surabhi.android_gojekcontactmanagementsystem;

import java.util.Date;

/**
 * Created by Surabhi on 2/9/2017.
 */

public class Contact {
    String id;
    String first_name;
    String last_name;
    String phone_number;
    String email;
    String profile_pic;
    boolean favourite = false;
    Date created_at;
    Date updated_at;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
