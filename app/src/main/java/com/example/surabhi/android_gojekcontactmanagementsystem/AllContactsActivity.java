package com.example.surabhi.android_gojekcontactmanagementsystem;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static java.lang.System.in;

public class AllContactsActivity extends AppCompatActivity {
    String TAG  = "AllContactsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_contacts);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getContactsFromServer();
    }

    private void getContactsFromServer()
    {
        ContactService contactService = ContactService.retrofit.create(ContactService.class);
        Call<List<Contact>> call = contactService.contacts();
        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                List <Contact> result = response.body();
                for (int i = 0; i < result.size(); i++)
                {
                    Log.d(TAG, "id:" + result.get(i).id);
                    Log.d(TAG, "first_name:" + result.get(i).first_name);
                    Log.d(TAG, "last_name:" + result.get(i).last_name);
                    Log.d(TAG, "profile_pic:" + result.get(i).profile_pic);

                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });


//     for asyncronous calls
//     call.enqueue(new Callback<List<Contributor>>() {
//            @Override
//            public void onResponse(Response<List<Contributor>> response, Retrofit retrofit) {
//                // handle success
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                // handle failure
//            }
//        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_all_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
