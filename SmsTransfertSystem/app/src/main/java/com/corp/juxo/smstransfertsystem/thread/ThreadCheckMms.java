package com.corp.juxo.smstransfertsystem.thread;

import android.content.Context;

import com.corp.juxo.smstransfertsystem.gmail.GMailConnexion;
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

    public Context mContext;

    public ThreadCheckMms(Context c){
        this.mContext= c;
        execute = true;
    }

    public void run(){
        try {
                new GMailConnexion(mContext, CheckMail.getUserName(), CheckMail.getPassword());
                System.out.println("Thread Check mms GO");
                Thread.sleep(5000);
                List<MmsInformation> fileDetected = MmsTools.saveLastMmsBitmap(mContext, CheckMail.MMS_NUMBER);
                CheckMail.MMS_NUMBER = MmsTools.getLastIdMms(mContext.getContentResolver());
                for(MmsInformation s : fileDetected){
                    new GMailSender(GMailConnexion.myConnexion
                            ,"txtmsg--"+ s.phoneNumber + "-- "+s.contactName
                            ,s.message
                            ,GMailConnexion.myConnexion.getUserName()
                            , s.image);
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        execute = false;
    }
}
