package com.corp.juxo.smstransfertsystem.thread;

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
        }
    }

    public static boolean isExecute() {
        return execute;
    }

    public static void setExecute(boolean execute) {
        ThreadEnvoieSms.execute = execute;
    }
}
