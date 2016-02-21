package com.corp.juxo.smstransfertsystem.tools;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juxo on 20/02/2016.
 */
public class MmsTools {


    public static int getCountMms(ContentResolver r) {
        final String[] projection = new String[]{"*"};
        Uri uri = Uri.parse("content://mms/part");
        Cursor query = r.query(uri, projection, null, null, "_id DESC");
        int count = query.getCount();
        query.close();
        return count;
    }

    public static int getLastIdMms(ContentResolver r){
        final String[] projection = new String[] {"*"};
        Uri uri = Uri.parse("content://mms/part");
        Cursor query = r.query(uri, projection, null, null, "_id DESC");
        query.moveToFirst();
        int lastId = Integer.parseInt(query.getString(query.getColumnIndex("_id")));
        return lastId;
    }

    public static List<String> saveLastMmsBitmap(ContentResolver r, int lastKnowid){
        final String[] projection = new String[] {"*"};
        List<String> listFileDetected  = new ArrayList<>();
        Uri uri = Uri.parse("content://mms/part");
        Cursor query = r.query(uri, projection, null, null, "_id DESC");
        int id = 0;
        if (query.moveToFirst()) {
            do {
                try {
                    id = Integer.parseInt(query.getString(query.getColumnIndex("_id")));
                    String type = query.getString(query.getColumnIndex("ct"));
                    if ("image/jpeg".equals(type) || "image/bmp".equals(type) ||
                            "image/gif".equals(type) || "image/jpg".equals(type) ||
                            "image/png".equals(type)) {
                        Bitmap bitmap = getMmsImage(query.getString(query.getColumnIndex("_id")),r);
                        listFileDetected.add(saveBitmap(query.getString(query.getColumnIndex("_id")), bitmap));
                    }

                }catch(NullPointerException e){
                    System.out.println("null");
                }
            } while (query.moveToNext() && lastKnowid+1 < id);

        }
        query.close();

        return listFileDetected;
    }

    public static String getMessageMms(String mmsId, ContentResolver r){
        String selectionPart = "mid=" + mmsId;
        Uri uri = Uri.parse("content://mms/part");
        Cursor cursor = r.query(uri, null,selectionPart, null, null);
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
                        body = getMmsText(partId, r);
                    } else {
                        body = cursor.getString(cursor.getColumnIndex("text"));
                    }
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return body;
    }

    public static String getMmsText(String id, ContentResolver r) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        StringBuilder sb = new StringBuilder();
        try {
            is = r.openInputStream(partURI);
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



    public static Bitmap getMmsImage(String id, ContentResolver r) {
        Uri partURI = Uri.parse("content://mms/part/" + id);
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            is = r.openInputStream(partURI);
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



    public static String getAddressNumber(String id, ContentResolver r) {
        String selectionAdd = new String("msg_id=" + id);
        String uriStr = MessageFormat.format("content://mms/{0}/addr", id);
        Uri uriAddress = Uri.parse(uriStr);
        Cursor cAdd = r.query(uriAddress, null,
                selectionAdd, null, null);
        String name = null;
        if (cAdd.moveToFirst()) {
            do {
                String number = cAdd.getString(cAdd.getColumnIndex("address"));
                if (number != null) {
                    try {
                        Long.parseLong(number.replace("-", ""));
                        name = number;
                    } catch (NumberFormatException nfe) {
                        if (name == null) {
                            name = number;
                        }
                    }
                }
            } while (cAdd.moveToNext());
        }
        if (cAdd != null) {
            cAdd.close();
        }
        return name;
    }

    public static String saveBitmap(String fileName, Bitmap bmp){
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


}
