package com.corp.juxo.smstransfertsystem.thread;

import android.content.Context;

import com.corp.juxo.smstransfertsystem.gmail.GMailSender;
import com.corp.juxo.smstransfertsystem.services.CheckMail;
import com.corp.juxo.smstransfertsystem.tools.MmsInformation;
import com.corp.juxo.smstransfertsystem.tools.MmsTools;

import java.util.List;

/**
 * Created by Juxo on 21/02/2016.
 */
public class ThreadCheckMms extends Thread {
    public static boolean execute;

    public Context c;
    public String username;
    public String password;

    public ThreadCheckMms(Context c){
        this.c= c;
        username = CheckMail.getUserName();
        password = CheckMail.getPassword();
        execute = true;
    }

    public void run(){
        try {
                System.out.println("Thread Check mms GO");
                Thread.sleep(5000);
                List<MmsInformation> fileDetected = MmsTools.saveLastMmsBitmap(c, CheckMail.MMS_NUMBER);
                CheckMail.MMS_NUMBER = MmsTools.getLastIdMms(c.getContentResolver());
                for(MmsInformation s : fileDetected){
                    new GMailSender(username, password,"txtmsg--"+ s.phoneNumber + "-- "+s.contactName, s.message,username, username, s.image);
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        execute = false;
    }
}
