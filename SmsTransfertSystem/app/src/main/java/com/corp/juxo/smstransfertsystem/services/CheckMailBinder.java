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

    public boolean getEnabled() throws RemoteException{
        return service.isExecute();
    }

    public void setEnabled(boolean b) throws RemoteException {
        service.setExecute(b);
    }

    public void setUsername(String u) throws RemoteException {
        service.setUserName(u);
    }

    public void setPassword(String p) throws RemoteException {
        service.setPassword(p);
    }

    public void reload(){
        service.reload();
    }
}
