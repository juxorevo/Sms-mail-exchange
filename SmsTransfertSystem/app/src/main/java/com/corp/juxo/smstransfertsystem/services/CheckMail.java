package com.corp.juxo.smstransfertsystem.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.corp.juxo.smstransfertsystem.breceiver.SmsReceiver;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieMail;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieSms;
import com.corp.juxo.smstransfertsystem.thread.ThreadReceptionMail;

public class CheckMail extends Service {

    private final int TEMPS_REFRESH = 2000;
    private static String SENT = "SMS_SENT";
    private static String SMSSENT = "android.provider.Telephony.SMS_RECEIVED";
    public static CheckMail me;
    private Intent intentSent;
    private CheckMailBinder binder;
    private ThreadReceptionMail rM;
    private ThreadEnvoieSms tE;
    private ThreadEnvoieMail tM;
    private SmsReceiver receiverSms;
    private static String username;
    private static String password;
    private boolean online = false;


    public CheckMail() {
        System.out.println("Constructeur()");
        binder = new CheckMailBinder(this);
        me = this;
    }

    @Override
    public void onCreate() {
        System.out.println("onCreate()");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("onDestroy()");
        super.onDestroy();
        //stopSystem();
    }

    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("onBind()");
        return binder;
    }


    public void startSystem() {

        System.out.println("System GO");

        if (rM == null) {
            rM = new ThreadReceptionMail(this, intentSent);
            rM.start();
        }

        if (tE == null) {
            tE = new ThreadEnvoieSms();
            tE.start();
        }

        if (tM == null) {
            tM = new ThreadEnvoieMail();
            tM.start();
        }

        try {
            this.unregisterReceiver(receiverSms);
            receiverSms = null;
        }catch(IllegalArgumentException e){

        }
        receiverSms = new SmsReceiver();
        this.registerReceiver(receiverSms, new IntentFilter(SMSSENT));

        online = true;
    }


    public void stopSystem() {
        System.out.println("System STOP");
        this.unregisterReceiver(receiverSms);
        receiverSms = null;
        //STOP THREAD
        ThreadEnvoieSms.setExecute(false);
        ThreadEnvoieMail.setExecute(false);
        ThreadReceptionMail.execute = false;
        rM=null;
        tM=null;
        tE=null;

        online = false;
    }

    public static void setUserName(String userName) {
        CheckMail.username=userName;
    }

    public static void setPassword(String password) {
        CheckMail.password=password;
    }

    public static String getUserName(){
        return CheckMail.username;
    }

    public static String getPassword(){
        return CheckMail.password;
    }

    public boolean isOnline(){
        return online;
    }

}
