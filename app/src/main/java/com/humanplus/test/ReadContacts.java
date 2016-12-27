package com.humanplus.test;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;


import static android.widget.Toast.LENGTH_SHORT;

public class ReadContacts {
    private int index;
    private int requestCode = 1000;
    private PhoneBook[] phoneBook;
    public boolean check = false;

    public ReadContacts() {
        index = 0;
        phoneBook = new PhoneBook[1000];
    }


    public void requestPermission(Activity activity) {
        final Activity currentActivity = activity;
        int permissionCheck = ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.READ_CONTACTS);

        // The case we do not need to check permissions.
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.M || permissionCheck == PackageManager.PERMISSION_GRANTED) {
            check = true;
        }

        //  When denied
        if(permissionCheck == PackageManager.PERMISSION_DENIED) {
            ContextCompat.checkSelfPermission(currentActivity, Manifest.permission.READ_CONTACTS);

            // If we need additional explanation for using permission.
            if(ActivityCompat.shouldShowRequestPermissionRationale(currentActivity, Manifest.permission.READ_CONTACTS)) {

                // Pop up dialog to grant permission from user.
                AlertDialog.Builder dialog = new AlertDialog.Builder(currentActivity);
                dialog.setTitle("권한 요청")
                        .setMessage("전화번호부를 읽습니다.")
                        .setPositiveButton("수락하기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(currentActivity,
                                        new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
                                check = true;
                                getContact(currentActivity);
                            }
                        })
                        .setNegativeButton("거절", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Toast.makeText(currentActivity, "요청이 거절되었습니다.", LENGTH_SHORT).show();
                                return;
                            }
                        }).create().show();
                return;
            } else {
                // Request READ_CONTACT to android system
                // 최초 실행시 권한 요청
                ActivityCompat.requestPermissions(currentActivity,
                        new String[]{Manifest.permission.READ_CONTACTS}, requestCode);
                //getContact(currentActivity);
            }
        }
    }


    public PhoneBook[] getContact(Activity context) {
        if(!check)
            return phoneBook;

        // 여기에 데이터가 투영될 것
        String[] arrNameProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String[] arrPhoneProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        Cursor clsCursor = context.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, arrNameProjection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1", null, null
        );

        while (clsCursor.moveToNext()) {
            phoneBook[index] = new PhoneBook();
            String strContactId = clsCursor.getString(0);
            phoneBook[index].setDigit("0");

            // name
            phoneBook[index].setName(clsCursor.getString(1));
            Log.i("ACT: ", "name: " + phoneBook[index].getName());

            // phone number
            Cursor clsPhoneCursor = context.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrPhoneProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
                    null, null
            );

            while (clsPhoneCursor.moveToNext()) {
                phoneBook[index].setDigit(clsPhoneCursor.getString(0));
                Log.i("ACT: ", "phone number: " + phoneBook[index++].getDigit());
            }
            clsPhoneCursor.close();
        }
        clsCursor.close();

        return phoneBook;
    }
}