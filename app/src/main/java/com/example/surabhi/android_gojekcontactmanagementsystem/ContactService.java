package com.example.surabhi.android_gojekcontactmanagementsystem;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Surabhi on 2/9/2017.
 */

public interface ContactService {
    @GET("contacts.json")
    Call<List<Contact>> contacts();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://gojek-contacts-app.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
