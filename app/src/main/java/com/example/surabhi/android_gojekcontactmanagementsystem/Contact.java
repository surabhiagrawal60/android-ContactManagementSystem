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
    String url;

    public String getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getEmail() {
        return email;
    }
}
