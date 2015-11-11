package com.corp.juxo.smstransfertsystem.thread;

import android.graphics.Color;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.gmail.GMailSender;

/**
 * Created by Juxo on 30/10/2015.
 */
public class ThreadEnvoieMail extends Thread {
    private static final int LASTMAIL = 0;

    private static boolean execute;

    public ThreadEnvoieMail(){
        execute = true;
    }

    public void run(){
        MainActivity.activityPrincipal.getHandler().post(new Runnable() {
            public void run() {
                MainActivity.activityPrincipal.gettEnvoieMail().setTextColor(Color.GREEN);
            }
        });
        try {
                while(execute){
                    Thread.sleep(1000);
                    if(GMailSender.getMailAEnvoyer() != null && !GMailSender.getMailAEnvoyer().isEmpty()){
                        GMailSender g = GMailSender.getMailAEnvoyer().get(LASTMAIL);
                        g.sendMail();
                        GMailSender.getMailAEnvoyer().remove(g);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                    public void run() {
                        MainActivity.activityPrincipal.gettEnvoieMail().setTextColor(Color.BLACK);
                    }
                });
            }
    }

    public static boolean isExecute() {
        return execute;
    }

    public synchronized  static void setExecute(boolean execute) {
        ThreadEnvoieMail.execute = execute;
    }
}
