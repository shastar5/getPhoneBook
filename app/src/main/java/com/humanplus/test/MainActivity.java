package com.humanplus.test;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.FacebookSdk;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this);
        setContentView(R.layout.activity_main);
    }

    public void onClickButton(View v) {
        if(v.getId() == R.id.button2) {
            PhoneBook[] phoneBook = new PhoneBook[1000];
            ReadContacts readContacts = new ReadContacts();

            readContacts.requestPermission(this);
            phoneBook = readContacts.getContact(this);
        }
    }

    public static Bitmap getFacebookProfilePicture(String userID){
        Bitmap bitmap = null;

        try {
            URL imageURL = new URL("https://graph.facebook.com/" + userID + "/picture?type=large");
            Bitmap bitmap2 = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
        } catch(Exception e) {
            Log.i("FacebookSdk Error: ", e.getMessage().toString());
        }
        return bitmap;
    }
}
