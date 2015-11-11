package com.corp.juxo.smstransfertsystem.thread;

import android.graphics.Color;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.sms.Sms;

/**
 * Created by Juxo on 10/11/2015.
 */
public class ThreadEnvoieSms extends Thread{

    private static final int LASTSMS = 0;
    private static boolean execute;

    public ThreadEnvoieSms(){
        execute = true;
    }


    public void run(){
        MainActivity.activityPrincipal.getHandler().post(new Runnable() {
            public void run() {
                MainActivity.activityPrincipal.gettEnvoieSms().setTextColor(Color.GREEN);
            }
        });
        try {
            while(execute){

                    Thread.sleep(1000);
                    if(Sms.getSmsAEnvoyer() != null && !Sms.getSmsAEnvoyer().isEmpty()){
                        Sms s = Sms.getSmsAEnvoyer().get(LASTSMS);
                        s.sendMsg();
                        Sms.getSmsAEnvoyer().remove(s);
                    }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                public void run() {
                    MainActivity.activityPrincipal.gettEnvoieSms().setTextColor(Color.BLACK);
                }
            });
        }
    }

    public static boolean isExecute() {
        return execute;
    }

    public static void setExecute(boolean execute) {
        ThreadEnvoieSms.execute = execute;
    }
}
