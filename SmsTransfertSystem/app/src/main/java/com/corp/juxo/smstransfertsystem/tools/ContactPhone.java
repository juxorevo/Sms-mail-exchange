package com.corp.juxo.smstransfertsystem.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

/**
 * Created by Juxo on 10/11/2015.
 */
public class ContactPhone {

    private Context context;

    public ContactPhone(Context c){
        context = c;
    }

    public String getContactNameByPhoneNumber(String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        String contactName = "";

        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        try {
            if(!phoneNumber.isEmpty()) {
                Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
                if (cursor == null) {
                    return null;
                }

                if (cursor.moveToFirst()) {
                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                }

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }catch(IllegalArgumentException e){
            e.printStackTrace();
        }

        return contactName;
    }

}
