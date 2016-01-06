package com.corp.juxo.smstransfertsystem.breceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.corp.juxo.smstransfertsystem.gmail.GMailSender;
import com.corp.juxo.smstransfertsystem.services.CheckMail;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoiePosition;
import com.corp.juxo.smstransfertsystem.tools.ContactPhone;


public class SmsReceiver extends BroadcastReceiver {

    private final String ACTION_RECEIVE_SMS = "android.provider.Telephony.SMS_RECEIVED";
    public static boolean EXECUTE = true;
    private Context mContext;
    private Intent mIntent;
    private static final String RETURNCHAR = "(\\r|\\n)";
    private static final String BLANKCHAR = "";

    private String username;
    private String password;

    private final String CMD = "Cmd";

    @Override
    public void onReceive(Context context, Intent intent) {

        username = CheckMail.getUserName();
        password = CheckMail.getPassword();

        mContext = context;
        mIntent = intent;
        String action = intent.getAction();

        if (action.equals(ACTION_RECEIVE_SMS) && EXECUTE) {
            Bundle bundle;
            bundle = intent.getExtras();
            if (bundle != null) {
               extractMessage();
            }
        }
    }

    public void extractMessage(){
        String address = "";
        String str = "";
        String phoneNumber = "";
        int contactId = -1;

        SmsMessage[] msgs = getMessagesFromIntent(this.mIntent);
        if (msgs != null) {
            for (int i = 0; i < msgs.length; i++) {
                phoneNumber = msgs[i].getDisplayOriginatingAddress();
                address = msgs[i].getOriginatingAddress();
                str += msgs[i].getMessageBody().toString();
                str += "\n";
            }

            String contact = new ContactPhone(mContext).getContactNameByPhoneNumber(address);

            String[] commande = str.split("--");

            switch(commande[0]){
                case CMD :
                        new ThreadEnvoiePosition(commande[1].replaceAll(RETURNCHAR, BLANKCHAR),phoneNumber).start();
                    break;

                default:
                    new GMailSender(username, password,"txtmsg--" + phoneNumber, str + " de : " + contact,username, username);
            }

        }
    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

}
