package com.corp.juxo.smstransfertsystem.breceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import com.corp.juxo.smstransfertsystem.thread.ThreadCheckMms;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MMSReceiver extends BroadcastReceiver {
    private final String DEBUG_TAG = getClass().getSimpleName().toString();
    private static final String ACTION_MMS_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String MMS_DATA_TYPE = "application/vnd.wap.mms-message";

    // Retrieve MMS
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        String type = intent.getType();
        System.out.println("MMS Recu traitement en cours");
        if(action.equals(ACTION_MMS_RECEIVED) && type.equals(MMS_DATA_TYPE)){
            if(!ThreadCheckMms.execute) {
                ThreadCheckMms tCheck = new ThreadCheckMms(context);
                tCheck.start();
            }
        }
    }


    public void writeFile(byte[] data, String fileName) throws IOException{
        File file = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);
        FileOutputStream out = new FileOutputStream(file, true);
        out.write(data);
        out.close();
    }

}