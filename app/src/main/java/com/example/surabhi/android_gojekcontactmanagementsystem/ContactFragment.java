package com.example.surabhi.android_gojekcontactmanagementsystem;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by surabhiagrawal on 15/02/17.
 */

public class ContactFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ListView contactList;
    ArrayAdapter<Contact> adapter;
    Handler handler;
    List<Contact> contacts;
    List<Contact> result = new ArrayList<>();
    String TAG = "ContactFragment";
    SwipeRefreshLayout swipeLayout;



    public ContactFragment()
    {
        handler = new Handler();
        contacts = new ArrayList<Contact>();

    }

    public static Fragment newInstance(){
        ContactFragment pf= new ContactFragment();
        return  pf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstance){
        View v = inflater.inflate(R.layout.contact_listview,container,false);

        contactList = (ListView)v.findViewById(R.id.contact_list);
        swipeLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);

        swipeLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_red_dark,
                android.R.color.holo_blue_dark,
                android.R.color.holo_orange_dark);

        contactList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contactItem = adapter.getItem(position);

                // Log.d(TAG,"ITEMPOSITION:" + position + "");
                //Log.d(TAG, "POST OBJECT" + post.commentsUrl + "");
                String contactID= contactItem.getId();

                ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (contactID == null || !isConnected) {
                    if(contactID == null ) {
                        Toast.makeText(getActivity(), "No Contact", Toast.LENGTH_LONG).show();
                    }
                    else if(!isConnected){
                        Toast.makeText(getActivity(), "Please check the internet connection and try again!", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d(TAG,"ContactID"+ contactID);
                    Intent i;
                    i = new Intent(getActivity(), ContactDetail.class);
                    // sending data to new activity
                    i.putExtra("id", contactID);
                    startActivity(i);
                }
            }
        });

        return v;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstance){
        super.onActivityCreated(savedInstance);

        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            Log.d(TAG,"Initialize Called");
            initialize();

        }
        else
            Toast.makeText(getActivity(), "Please check the internet connection and try again!", Toast.LENGTH_LONG).show();

    }

    private void initialize(){
        if(contacts.size() == 0)
        {
            new Thread(){
                public void run(){
                    try {

                        contacts.addAll(getContactsFromServer());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createAdapter();
                        }
                    });
                }
            }.start();
        }else{
            new Thread(){
                public void run(){
                    try {
                        int size = contacts.size();
                        contacts.addAll(getContactsFromServer());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createAdapter();
                        }
                    });
                }
            }.start();
        }
    }

    private void createAdapter(){
        if(getActivity() == null) return;

        adapter = new ArrayAdapter<Contact>(getActivity(),R.layout.contact_listitems,contacts){
            @Override
            public View getView(int position,View convertView, ViewGroup parent)
            {
                if(convertView == null) {
                    convertView = getActivity().getLayoutInflater().inflate(R.layout.contact_listitems, null);
                }

                TextView contactUser;
                contactUser = (TextView) convertView.findViewById(R.id.first_last);

                SimpleDraweeView draweeView;
                draweeView = (SimpleDraweeView) convertView.findViewById(R.id.profile_pic);

//

                String url = contacts.get(position).getProfile_pic();

                if(url.contains("/images/missing.png")) {
                    Log.d(TAG,"url: " + url);
                    draweeView.setBackgroundResource(R.drawable.ic_account_circle_black_24dp);

                }
                else {
                    Uri uri = Uri.parse(url);
                    draweeView.setImageURI(uri);
                }

                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                draweeView.getHierarchy().setRoundingParams(roundingParams);

                contactUser.setText( contacts.get(position).getFirst_name() + " " + contacts.get(position).getLast_name());

                return convertView;
            }

            @Override
            public void notifyDataSetChanged() {
                super.notifyDataSetChanged();
            }

            @Override
            public Contact getItem(int position) {
                return super.getItem(position);
            }

        };

        contactList.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        swipeLayout.setRefreshing(false);
        ConnectivityManager connMgr = (ConnectivityManager) getActivity()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(isConnected) {
            new Thread() {
                public void run() {


                    try {
                        contacts.addAll(getContactsFromServer());


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            createAdapter();

                            Toast.makeText(getActivity(), "Please go to the bottom", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }.start();
        }
        else
        {
            Toast.makeText(getActivity(), "Please check the internet connection and try again!", Toast.LENGTH_LONG).show();
        }
    }

    private List<Contact> getContactsFromServer() {
        ContactService contactService = ContactService.retrofit.create(ContactService.class);
        Call<List<Contact>> call = contactService.contacts();

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                result = response.body();
                for (int i = 0; i < result.size(); i++) {
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
        return result;
    }


}
