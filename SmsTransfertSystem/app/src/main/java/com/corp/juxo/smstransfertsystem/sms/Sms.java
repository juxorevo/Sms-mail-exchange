package com.corp.juxo.smstransfertsystem.sms;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juxo on 10/11/2015.
 */
public class Sms {
    private static final String SEPARATEURTEL = "txtmsg--";
    private String msg;
    private String phoneNumber;
    private Context mContext;
    private static String SENT = "SMS_SENT";
    private static String DELIVERED = "SMS_DELIVERED";
    private static int MAX_SMS_MESSAGE_LENGTH = 69;

    private static List<Sms> smsAEnvoyer = new ArrayList<>();


    public Sms(Context c, String m, String pNumber){
        mContext = c;
        msg = m;
        phoneNumber = pNumber;
        smsAEnvoyer.add(this);
    }

    public void sendMsg(){

        PendingIntent piDelivered = PendingIntent.getBroadcast(mContext, 0, new Intent(DELIVERED), 0);

        SmsManager smsManager = SmsManager.getDefault();

        Intent intentSent2 = new Intent(SENT);
        intentSent2.putExtra("msg", SEPARATEURTEL + phoneNumber +" " + msg);
        PendingIntent piSent = PendingIntent.getBroadcast(mContext, 0, intentSent2, 0);

        int length = msg.length();
        if(length > MAX_SMS_MESSAGE_LENGTH) {
            ArrayList<String> messageList = smsManager.divideMessage(msg);
            ArrayList<PendingIntent> piSentList = new ArrayList<>();
            piSentList.add(piSent);
            smsManager.sendMultipartTextMessage(phoneNumber, null, messageList, piSentList, null);
        }else {
            smsManager.sendTextMessage(phoneNumber, null, msg, piSent, null);
        }
    }

    public synchronized static List<Sms> getSmsAEnvoyer(){
        return smsAEnvoyer;
    }

}
