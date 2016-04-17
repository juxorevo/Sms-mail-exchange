package com.corp.juxo.smstransfertsystem.listener;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.services.CheckMailConnexion;

/**
 * Created by Juxo on 03/11/2015.
 */
public class GeneralListener extends Activity implements View.OnClickListener {


    public GeneralListener() {
    }

    @Override
    public void onClick(View v) {
        try {
            if (CheckMailConnexion.remoteService!=null && CheckMailConnexion.remoteService.isOnline()) {
                try {
                        CheckMailConnexion.remoteService.stopSystem();
                        MainActivity.activityPrincipal.checkServiceOnline();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }else{
                MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                    public void run() {
                        SharedPreferences settings = MainActivity.activityPrincipal
                                .getApplicationContext()
                                .getSharedPreferences("Global",
                                        Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        EditText user = MainActivity.activityPrincipal.gettUser();
                        EditText pass = MainActivity.activityPrincipal.gettPassword();
                        editor.putString("user", user.getText().toString());
                        editor.putString("pass", pass.getText().toString());
                        editor.commit();

                        try {
                            if(CheckMailConnexion.remoteService!=null && !CheckMailConnexion.remoteService.isOnline()){
                                CheckMailConnexion.remoteService.startSystem();
                                MainActivity.activityPrincipal.checkServiceOnline();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
