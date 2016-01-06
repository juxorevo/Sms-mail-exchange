package com.corp.juxo.smstransfertsystem.thread;

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
            }
    }

    public static boolean isExecute() {
        return execute;
    }

    public synchronized  static void setExecute(boolean execute) {
        ThreadEnvoieMail.execute = execute;
    }
}
