package com.corp.juxo.smstransfertsystem.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.corp.juxo.smstransfertsystem.breceiver.MMSReceiver;
import com.corp.juxo.smstransfertsystem.breceiver.SmsReceiver;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieMail;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieSms;
import com.corp.juxo.smstransfertsystem.thread.ThreadReceptionMail;
import com.corp.juxo.smstransfertsystem.tools.MmsTools;

public class CheckMail extends Service {

    private static final String SMSSENT = "android.provider.Telephony.SMS_RECEIVED";
    private static final String ACTION_MMS_RECEIVED = "android.provider.Telephony.WAP_PUSH_RECEIVED";
    private static final String MMS_DATA_TYPE = "application/vnd.wap.mms-message";
    public static CheckMail me;
    private Intent intentSent = null;
    private CheckMailBinder binder;
    private ThreadReceptionMail rM;
    private ThreadEnvoieSms tE;
    private ThreadEnvoieMail tM;
    private SmsReceiver receiverSms;
    private MMSReceiver receiverMms;
    private static String username;
    private static String password;
    private boolean online = false;
    public IntentFilter mmsIntent;
    public static int MMS_NUMBER;


    public CheckMail() {
        System.out.println("Constructeur()");
        binder = new CheckMailBinder(this);
        me = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

        try {
            this.unregisterReceiver(receiverMms);
            receiverMms = null;
        }catch(IllegalArgumentException e){
        }
        receiverMms = new MMSReceiver();

        try {
            mmsIntent = new IntentFilter(ACTION_MMS_RECEIVED);
            mmsIntent.addDataType(MMS_DATA_TYPE);

        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        this.registerReceiver(receiverMms,mmsIntent);

        online = true;
        MMS_NUMBER = MmsTools.getLastIdMms(getContentResolver());
        System.out.println(" ------------------ Nombre MMS : " + MMS_NUMBER);
    }


    public void stopSystem() {
        System.out.println("System STOP");
        this.unregisterReceiver(receiverSms);
        this.unregisterReceiver(receiverMms);
        receiverSms = null;
        receiverMms = null;

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
