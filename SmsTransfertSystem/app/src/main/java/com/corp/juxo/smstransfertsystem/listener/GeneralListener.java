package com.corp.juxo.smstransfertsystem.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.View;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.breceiver.SmsControl;
import com.corp.juxo.smstransfertsystem.breceiver.SmsReceiver;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieMail;
import com.corp.juxo.smstransfertsystem.thread.ThreadEnvoieSms;
import com.corp.juxo.smstransfertsystem.thread.ThreadReceptionMail;

/**
 * Created by Juxo on 03/11/2015.
 */
public class GeneralListener extends Activity implements View.OnClickListener {

    private Context mContext;
    private Intent intentSent;
    private static String SENT = "SMS_SENT";
    private SmsControl receiverPend;
    private ThreadEnvoieSms tE;
    private ThreadEnvoieMail tM;
    private ThreadReceptionMail rM;

    public GeneralListener(Context c, Intent i){
        mContext = c;
        intentSent = i;
    }

    @Override
    public void onClick(View v) {
        if(ThreadReceptionMail.execute){
            MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                public void run() {
                    MainActivity.activityPrincipal.getButtonStop().setText("Système à l'arrêt");
                }
            });
            SmsReceiver.EXECUTE=false;
            mContext.unregisterReceiver(receiverPend);

            //STOP THREAD
            ThreadEnvoieSms.setExecute(false);
            ThreadEnvoieMail.setExecute(false);
            ThreadReceptionMail.execute =false;
        }else{

            SmsReceiver.EXECUTE=true;

            rM = new ThreadReceptionMail(mContext, intentSent);
            rM.start();

            tE = new ThreadEnvoieSms();
            tE.start();

            tM = new ThreadEnvoieMail();
            tM.start();

            receiverPend = new SmsControl();
            mContext.registerReceiver(receiverPend, new IntentFilter(SENT));
            MainActivity.activityPrincipal.getHandler().post(new Runnable() {
                public void run() {
                    MainActivity.activityPrincipal.getButtonStop().setText("Système en marche");
                }
            });
        }

    }
}
