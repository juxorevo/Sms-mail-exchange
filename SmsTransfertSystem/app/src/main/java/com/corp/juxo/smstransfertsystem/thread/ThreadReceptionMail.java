package com.corp.juxo.smstransfertsystem.thread;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.gmail.GMailReceiver;

/**
 * Created by Juxo on 30/10/2015.
 */
public class ThreadReceptionMail extends Thread {
    private static int TEMPS_REFRESH = 10000;
    public static boolean execute;
    private Context mContext;
    private Intent intentSent;

    public ThreadReceptionMail(Context c, Intent i){
        mContext = c;
        intentSent = i;
        execute = true;
    }

    public void run() {

        MainActivity.activityPrincipal.getHandler().post(new Runnable() {
            public void run() {
                MainActivity.activityPrincipal.gettReceptionMail().setTextColor(Color.GREEN);
            }
        });

        GMailReceiver rM = new GMailReceiver(mContext, intentSent);
        try {
            while (execute) {
                Thread.sleep(TEMPS_REFRESH);
                ConnectivityManager cm = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                if(isConnected && !rM.ENCOURS){
                    rM.readMails();
                }
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }finally {
            MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                public void run() {
                    MainActivity.activityPrincipal.gettReceptionMail().setTextColor(Color.BLACK);
                }
            });
        }
    }
}
