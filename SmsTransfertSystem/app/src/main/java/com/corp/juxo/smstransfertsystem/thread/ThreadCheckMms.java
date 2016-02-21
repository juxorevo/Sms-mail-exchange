package com.corp.juxo.smstransfertsystem.thread;

import android.content.ContentResolver;

import com.corp.juxo.smstransfertsystem.gmail.GMailSender;
import com.corp.juxo.smstransfertsystem.services.CheckMail;
import com.corp.juxo.smstransfertsystem.tools.MmsTools;

import java.util.List;

/**
 * Created by Juxo on 21/02/2016.
 */
public class ThreadCheckMms extends Thread {
    public static boolean execute;

    public ContentResolver r;
    public String username;
    public String password;

    public ThreadCheckMms(ContentResolver resolver){
        r= resolver;
        username = CheckMail.getUserName();
        password = CheckMail.getPassword();
        execute = true;
    }

    public void run(){
        try {
                System.out.println("Thread Check mms GO");
                Thread.sleep(5000);
                List<String> fileDetected = MmsTools.saveLastMmsBitmap(r, CheckMail.MMS_NUMBER);
                CheckMail.MMS_NUMBER = MmsTools.getLastIdMms(r);
                for(String s : fileDetected){
                    new GMailSender(username, password,"txtmsg--mms" + "-- ", "mms",username, username, s);
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        execute = false;
    }
}
