package com.corp.juxo.smstransfertsystem.services;

import android.os.RemoteException;

/**
 * Created by Juxo on 25/12/2015.
 */
public class CheckMailBinder extends IRemoteCheckMail.Stub {
    private CheckMail service = null;

    public CheckMailBinder(CheckMail service) {
        super();
        this.service = service;
    }

    public void setUsername(String u) throws RemoteException {
        CheckMail.setUserName(u);
    }

    public void setPassword(String p) throws RemoteException {
        CheckMail.setPassword(p);
    }

    public void startSystem(){
        service.startSystem();
    }

    public void stopSystem(){
        service.stopSystem();
    }

    public boolean isOnline(){
        return service.isOnline();
    }
}
