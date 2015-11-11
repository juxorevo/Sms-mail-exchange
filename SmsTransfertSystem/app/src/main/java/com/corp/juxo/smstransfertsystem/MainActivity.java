package com.corp.juxo.smstransfertsystem;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.corp.juxo.smstransfertsystem.listener.GeneralListener;

/**
 * S0F1
 */
public class MainActivity extends AppCompatActivity {

    public static Context me;
    public static MainActivity activityPrincipal;
    private static String SENT = "SMS_SENT";
    private Button buttonStop;
    private TextView tEnvoieSms;
    private Handler handler;
    private TextView tReceptionMail;
    private TextView tEnvoieMail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        handler = new Handler();
        activityPrincipal = this;
        me = getBaseContext();

        Intent intentSent = new Intent(SENT);

        buttonStop = (Button)findViewById(R.id.arret);
        buttonStop.setOnClickListener(new GeneralListener(getApplicationContext(), intentSent));

        tEnvoieSms = (TextView) findViewById(R.id.ThreadSms);
        tReceptionMail = (TextView) findViewById(R.id.ThreadReceptionMail);
        tEnvoieMail = (TextView) findViewById(R.id.ThreadEnvoieMail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public synchronized Button getButtonStop(){
        return buttonStop;
    }

    public synchronized Handler getHandler(){
        return handler;
    }

    public synchronized TextView gettEnvoieSms() {
        return tEnvoieSms;
    }

    public void settEnvoieSms(TextView tEnvoieSms) {
        this.tEnvoieSms = tEnvoieSms;
    }

    public synchronized TextView gettReceptionMail() {
        return tReceptionMail;
    }

    public void settReceptionMail(TextView tReceptionMail) {
        this.tReceptionMail = tReceptionMail;
    }

    public synchronized TextView gettEnvoieMail() {
        return tEnvoieMail;
    }

    public void settEnvoieMail(TextView tEnvoieMail) {
        this.tEnvoieMail = tEnvoieMail;
    }
}
