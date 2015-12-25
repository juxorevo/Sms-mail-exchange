package com.corp.juxo.smstransfertsystem.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteException;

import com.corp.juxo.smstransfertsystem.MainActivity;

/**
 * Created by Juxo on 25/12/2015.
 */
public class CheckMailConnexion implements ServiceConnection {

    public static IRemoteCheckMail remoteService;

    public void onServiceConnected(ComponentName className, IBinder service) {
         remoteService = IRemoteCheckMail.Stub.asInterface(service);
        try {
            SharedPreferences settings = MainActivity.me.getApplicationContext()
                    .getSharedPreferences("Global", Context.MODE_PRIVATE);
            remoteService.setUsername(settings.getString("user", ""));
            remoteService.setPassword(settings.getString("pass", ""));
        }
        catch (RemoteException e) {
        }
    }

    public void onServiceDisconnected(ComponentName name) {

    }

}
