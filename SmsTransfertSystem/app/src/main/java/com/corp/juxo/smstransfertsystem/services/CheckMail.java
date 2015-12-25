package com.corp.juxo.smstransfertsystem.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.corp.juxo.smstransfertsystem.thread.ThreadReceptionMail;

public class CheckMail extends Service {
    private final int TEMPS_REFRESH = 2000;
    private static String SENT = "SMS_SENT";
    private Intent intentSent;
    private CheckMailBinder binder;
    private ThreadReceptionMail rM;

    public CheckMail(){
        binder = new CheckMailBinder(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        rM = new ThreadReceptionMail(this,
                intentSent);

        rM.start();
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
        return binder;
    }

    public boolean isExecute() {
        return execute;
    }

    public void setExecute(boolean execute) {
        rM.execute = execute;
    }

    public void reload(){
        if(rM.isInterrupted()) {
            rM = new ThreadReceptionMail(this, intentSent);
            rM.start();
        }
    }

    public void setUserName(String userName) {
        rM.setUserName(userName);
    }

    public void setPassword(String password) {
        rM.setPassword(password);
    }

    private boolean execute;

}
