package com.example.surabhi.android_gojekcontactmanagementsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by surabhiagrawal on 15/02/17.
 */
import static android.Manifest.permission.CALL_PHONE;
public class ContactDetail extends AppCompatActivity {

    String TAG = "ContactDetail";
    Contact result = new Contact();
    static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_detail);
        Bundle bundle = getIntent().getExtras();

        String id = bundle.getString("id");
        Log.d(TAG, "id:" + id);

        getContactDetailFromServer(id);

    }


    private void getContactDetailFromServer(String id) {
        ContactService contactService = ContactService.retrofit.create(ContactService.class);
        Call<Contact> call = contactService.getUser(id);

        Log.d(TAG, "url: " + contactService.getUser(id).request().url().toString());

        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {

                result = response.body();

                Contact contact = result;

                Log.d(TAG, "id:" + result.id);
                Log.d(TAG, "first_name:" + result.first_name);
                Log.d(TAG, "last_name:" + result.last_name);
                Log.d(TAG, "profile_pic:" + result.profile_pic);
                Log.d(TAG, "phone:" + result.phone_number);
                Log.d(TAG, "email:" + result.email);


                ImageView imgFav;
                imgFav = (ImageView) findViewById(R.id.favorite_icon);

                TextView txtName;
                txtName = (TextView) findViewById(R.id.name);
                txtName.setText(contact.getFirst_name() + " " + contact.getLast_name());

                ImageView imgCall;
                imgCall = (ImageView) findViewById(R.id.call_icon);

                final TextView txtCall;
                txtCall = (TextView) findViewById(R.id.phone);
                txtCall.setText(contact.getPhone_number());
                if (!checkPermission())
                    requestPermission();

                txtCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:+" + txtCall.getText().toString().trim()));
                        callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if(checkPermission()) {
                            startActivity(callIntent);
                        }
                    else
                        {
                            requestPermission();
                        }
                    }
                });

                ImageView imgEmail;
                imgEmail = (ImageView) findViewById(R.id.email_icon);

                TextView txtEmail;
                txtEmail = (TextView) findViewById(R.id.email);
                txtEmail.setText(contact.getEmail());

                txtEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String[] TO = {""};
                        String[] CC = {""};
                        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.setData(Uri.parse("mailto:"));
                        emailIntent.setType("text/plain");
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                            Log.d(TAG,"Finished sending email");
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(ContactDetail.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                SimpleDraweeView draweeView;
                draweeView = (SimpleDraweeView) findViewById(R.id.profile_pic_detail);

                String profileImgUrl = contact.getProfile_pic();

                if(profileImgUrl.contains("/images/missing.png")) {
                    Log.d(TAG,"url: " + profileImgUrl);
                    draweeView.setBackgroundResource(R.drawable.ic_account_circle_black_24dp);

                }
                else {
                    Uri uri = Uri.parse(profileImgUrl);
                    draweeView.setImageURI(uri);
                }

                RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
                roundingParams.setRoundAsCircle(true);
                draweeView.getHierarchy().setRoundingParams(roundingParams);

            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {

            }

        });

    }


    public boolean checkPermission() {


        int perm_write = ContextCompat.checkSelfPermission(getApplicationContext(), CALL_PHONE);


        return (perm_write == PackageManager.PERMISSION_GRANTED);

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this,
                new String[]{
                        CALL_PHONE,
                },
                PERMISSION_REQUEST_CODE);
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ContactDetail.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length == 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted ) {
                        Toast.makeText(ContactDetail.this, "Permission Granted, Now you can access location data/camera/storage/accounts", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(ContactDetail.this, "Your app might not work without permissions. Please restart your app.", Toast.LENGTH_LONG).show();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(CALL_PHONE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ CALL_PHONE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        }
                                );
                                return;
                            }
                        }
                    }
                }

                break;
        }
    }
}
