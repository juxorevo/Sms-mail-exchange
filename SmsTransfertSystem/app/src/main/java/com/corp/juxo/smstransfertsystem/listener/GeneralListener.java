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
                CheckMailConnexion.remoteService.stopSystem();
                MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.activityPrincipal.getButtonStop().setText("Système à l'arrêt");
                    }
                });
            } else{
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

                        if(!user.getText().toString().equals("")){
                            MainActivity.activityPrincipal.lancerSerice();
                        }
                        try {
                            if(CheckMailConnexion.remoteService!=null && !CheckMailConnexion.remoteService.isOnline()){
                                CheckMailConnexion.remoteService.startSystem();
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
