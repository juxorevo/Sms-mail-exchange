package com.corp.juxo.test;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ContentResolver contentResolver = getContentResolver();
        final String[] projection = new String[] {"*"};
        Uri uri = Uri.parse("content://mms/part");

        Cursor query = contentResolver.query(uri, projection, null, null, "_id DESC");

        if (query.moveToFirst()) {
            do {
                try {

                    String type = query.getString(query.getColumnIndex("ct"));
                    System.out.println("Type niveau sup : " + type);

                    if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                            "image/gif".equals(type) || "image/jpg".equals(type) ||
                            "image/png".equals(type)) {
                        Bitmap bitmap = getMmsImage(query.getString(query.getColumnIndex("_id")));
                        String bitmapLink = saveBitmap(query.getString(query.getColumnIndex("_id")), bitmap);
                        String msgTxt = getMessageMms(query.getString(query.getColumnIndex("_id")));
                        String phoneNumer = getAddressNumber(query.getString(query.getColumnIndex("_id")));
                        //String contactName = new ContactPhone(c).getContactNameByPhoneNumber(phoneNumer);
                        System.out.println(query.getString(query.getColumnIndex("mid")));
                        System.out.println(saveBitmap("photo1", bitmap));
                    }
                    // saveLastMmsBitmap(query.getString(query.getColumnIndex("_id")), getContentResolver());
                    // trouvermms(query.getString(query.getColumnIndex("_id")));

                }catch(NullPointerException e){
                    System.out.println("null");
                }
            } while (query.moveToNext());

        }
        System.out.println(query.getCount());

        query.close();



    }

    private String getMessageMms(String mmsId){
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = getContentResolver().query(uri, null,selectionPart, null, null);
        String body ="";
        if (cursor.moveToFirst()) {
            do {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));
                System.out.println("type : " + type);
                if ("text/plain".equals(type)) {
                    String data = cursor.getString(cursor.getColumnIndex("_data"));

                    if (data != null) {
                        // implementation of this method below
                        body = getMmsText(partId);
                    } else {
                        body = cursor.getString(cursor.getColumnIndex("text"));
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return body;
    }

    private String getMmsText(String id) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = getContentResolver().openInputStream(partURI);
            if (is != null) {
                InputStreamReader isr = new InputStreamReader(is, "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                String temp = reader.readLine();
                while (temp != null) {
                    sb.append(temp);
                    temp = reader.readLine();
                }
            }
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return sb.toString();
    }

    public String saveBitmap(String fileName, Bitmap bmp){
        FileOutputStream out = null;
        File pathComplet = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName+".png");
        try {
            out = new FileOutputStream(pathComplet);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pathComplet.getPath();
    }

    public void saveLastMmsBitmap(String mmsId, ContentResolver r){

        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = getContentResolver().query(uri, null,selectionPart, null, null);
        if (cursor.moveToFirst()) {
            do {
                String partId = cursor.getString(cursor.getColumnIndex("_id"));
                String type = cursor.getString(cursor.getColumnIndex("ct"));
                if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                        "image/gif".equals(type) || "image/jpg".equals(type) ||
                        "image/png".equals(type)) {
                    Bitmap bitmap = getMmsImage(partId);
                    System.out.println(saveBitmap("photo1", bitmap));
                }
            } while (cursor.moveToNext());
        }
    }


    private Bitmap getMmsImage(String _id) {
        Uri partURI = Uri.parse("content://mms/part/" + _id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = getContentResolver().openInputStream(partURI);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {}
        finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }
        return bitmap;
    }
    private String getAddressNumber(String id) {
        String add = "";
        final String[] projection = new String[] {"address","contact_id","charset","type"};
        final String selection = "type=137 or type=151"; // PduHeaders
        Uri.Builder builder = Uri.parse("content://mms").buildUpon();
        builder.appendPath(String.valueOf(id)).appendPath("addr");

        Cursor cursor = getContentResolver().query(
                builder.build(),
                projection,
                selection,
                null, null);

        if (cursor.moveToFirst()) {
            do {
                 add = cursor.getString(cursor.getColumnIndex("address"));
                String typet =  cursor.getString(cursor.getColumnIndex("type"));
            } while(cursor.moveToNext());
        }
        // Outbound messages address type=137 and the value will be 'insert-address-token'
        // Outbound messages address type=151 and the value will be the address
        // Additional checking can be done here to return the correct address.
        return add;
    }

}
