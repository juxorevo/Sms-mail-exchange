package com.corp.juxo.smstransfertsystem.breceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import com.corp.juxo.smstransfertsystem.gmail.GMailSender;

/**
 * Created by Juxo on 04/11/2015.
 */
public class SmsControl extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // We need to make all the parts succeed before we say we have succeeded.
        String msg = "";
        switch (getResultCode()) {
            case Activity.RESULT_OK:
                msg = "Message bien envoyé";
                break;
            case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                msg = "Error - Generic failure";
                break;
            case SmsManager.RESULT_ERROR_NO_SERVICE:
                msg = "Error - No Service";
                break;
            case SmsManager.RESULT_ERROR_NULL_PDU:
                msg = "Error - Null PDU";
                break;
            case SmsManager.RESULT_ERROR_RADIO_OFF:
                msg = "Error - Radio off";
                break;
        }
        String mymsg = intent.getStringExtra("msg");
        new GMailSender("be.aimard@gmail.com", "jdojriiqdrjfzabm", "return--" + msg, mymsg, "be.aimard@gmail.com", "juxorevo@gmail.com");
    }
}
