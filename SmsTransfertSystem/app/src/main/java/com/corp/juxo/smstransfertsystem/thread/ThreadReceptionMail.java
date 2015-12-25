package com.corp.juxo.smstransfertsystem.thread;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.corp.juxo.smstransfertsystem.gmail.GMailReceiver;
import com.corp.juxo.smstransfertsystem.services.CheckMail;

/**
 * Created by Juxo on 30/10/2015.
 */
public class ThreadReceptionMail extends Thread {
    private static int TEMPS_REFRESH = 5000;
    public static boolean execute;
    private Context mContext;
    private Intent intentSent;

    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private String userName;
    private String password;


    public ThreadReceptionMail(Context c, Intent i){
        mContext = c;
        intentSent = i;
        execute = true;
    }

    public void run() {

        GMailReceiver rM=null;

        try {
            while (execute) {
                userName = CheckMail.getUserName();
                password = CheckMail.getPassword();
                Thread.sleep(TEMPS_REFRESH);
                if(userName != null && password !=null && rM == null) {
                    rM = new GMailReceiver(mContext, intentSent, userName, password);
                }

                if(rM!=null) {
                    ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
                    if (isConnected && !rM.ENCOURS) {
                        rM.readMails();
                    }
                }
            }
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}
