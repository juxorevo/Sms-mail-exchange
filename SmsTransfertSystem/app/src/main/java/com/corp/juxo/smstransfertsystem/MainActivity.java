package com.corp.juxo.smstransfertsystem;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.corp.juxo.smstransfertsystem.listener.GeneralListener;
import com.corp.juxo.smstransfertsystem.listener.GpsListener;
import com.corp.juxo.smstransfertsystem.services.CheckMail;
import com.corp.juxo.smstransfertsystem.services.CheckMailConnexion;

/**
 * S0F1
 */
public class MainActivity extends AppCompatActivity {

    public static Context me;
    public static MainActivity activityPrincipal;

    private Button buttonStop;
    private TextView connexionService;
    private TextView tReceptionMail;
    private TextView tEnvoieMail;

    private EditText tUser;
    private EditText tPassword;

    private Handler handler;
    private ServiceConnection remoteConnection;

    public static LocationManager locationManager;
    public static GpsListener myLocationListener;
    private Intent intentService;

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

        //Accès à l'inteface graphique
        buttonStop = (Button)findViewById(R.id.arret);
        buttonStop.setOnClickListener(new GeneralListener());

        connexionService = (TextView) findViewById(R.id.ConnecServ);
        tReceptionMail = (TextView) findViewById(R.id.ThreadReceptionMail);
        tEnvoieMail = (TextView) findViewById(R.id.ThreadEnvoieMail);

        SharedPreferences settings = getSharedPreferences("Global", Context.MODE_PRIVATE);
        tUser = (EditText) findViewById(R.id.loginGoogle);
        tPassword = (EditText) findViewById(R.id.passGoogle);

        tUser.setText(settings.getString("user", ""));
        tPassword.setText(settings.getString("pass", ""));

        handler = new Handler();
        activityPrincipal = this;
        me = getBaseContext();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new GpsListener();

        intentService = new Intent();
        intentService.setClassName("com.corp.juxo.smstransfertsystem", "com.corp.juxo.smstransfertsystem.services.CheckMail");
        if(!isMyServiceRunning(CheckMail.class)){
            startService(intentService);
        }
        openAccess();
    }

    @Override
    protected void onResume() {
        super.onResume();
        intentService = new Intent();
        intentService.setClassName("com.corp.juxo.smstransfertsystem", "com.corp.juxo.smstransfertsystem.services.CheckMail");
        if(!isMyServiceRunning(CheckMail.class)){
            startService(intentService);
        }

        openAccess();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutDownAccess();

    }

    public void openAccess(){
        //Connection au service
        if(isMyServiceRunning(CheckMail.class) && remoteConnection == null){
            remoteConnection =  new CheckMailConnexion();
            bindService(intentService, remoteConnection, Context.BIND_AUTO_CREATE);
        }

        checkServiceOnline();
        connexionService.setTextColor(Color.GREEN);
    }

    public void shutDownAccess(){
        if(isMyServiceRunning(CheckMail.class)) {
            if (remoteConnection != null) {
                unbindService(remoteConnection);
                remoteConnection = null;
            }
        }
        checkServiceOnline();
        buttonStop.setText("Accès au service coupé");
        connexionService.setTextColor(Color.BLACK);
    }

    public void checkServiceOnline(){
        //Test si le service est en route
        try {
            if(CheckMailConnexion.remoteService!=null && CheckMailConnexion.remoteService.isOnline()){
                buttonStop.setText("System lancé");
            }else{
                buttonStop.setText("System stoppé");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



    public synchronized Button getButtonStop(){
        return buttonStop;
    }

    public synchronized Handler getHandler(){
        return handler;
    }

    public synchronized TextView getConnexionService() {
        return connexionService;
    }

    public void setConnexionService(TextView connexionService) {
        this.connexionService = connexionService;
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

    public void setGpsNormalMode(){
        locationManager.removeUpdates(myLocationListener);
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000,10, myLocationListener);
    }

    public void stopGpsNormalMode(){
        locationManager.removeUpdates(myLocationListener);
    }

    public EditText gettUser() {
        return tUser;
    }

    public void settUser(EditText tUser) {
        this.tUser = tUser;
    }

    public EditText gettPassword() {
        return tPassword;
    }

    public void settPassword(EditText tPassword) {
        this.tPassword = tPassword;
    }

    public ServiceConnection getRemoteConnection() {
        return remoteConnection;
    }

    public void setRemoteConnection(ServiceConnection remoteConnection) {
        this.remoteConnection = remoteConnection;
    }
}
