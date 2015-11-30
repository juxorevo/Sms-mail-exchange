package com.corp.juxo.smstransfertsystem.tools;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import com.corp.juxo.smstransfertsystem.MainActivity;
import com.corp.juxo.smstransfertsystem.listener.GpsListener;
import com.corp.juxo.smstransfertsystem.sms.Sms;

import java.io.IOException;
import java.util.List;

/**
 * Created by Juxo on 20/11/2015.
 */
public class Commands {

    public static final String POSITION = "position";

    public static void executeCommande(String cmd, String numero){

        switch (cmd){

            case POSITION :
                CreateSmsPosition(numero);
                break;

        }
    }

    public static void CreateSmsPosition(String telReponse){
        GpsListener.loc=null;

        Context context = MainActivity.me.getApplicationContext();
        MainActivity.activityPrincipal.getHandler().post(new Runnable() {
            public void run() {
                MainActivity.activityPrincipal.setGpsNormalMode();
            }
        });
        int i = 0;
        while(GpsListener.loc == null && i <60){
            try {
                Thread.sleep(1000);
                i++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Location loc = GpsListener.loc;
        if(loc!=null){
            Geocoder geocoder = new Geocoder(context);
            String addresse = getAdresse(loc.getLatitude(),
                                            loc.getLongitude(),
                                            geocoder,
                                            context
                                           );
            String pos = addresse;
            double speed = loc.getSpeed() * 3.6;
            pos += ", " + String.valueOf(speed) + " km/h";
            pos += ", direction : " + loc.getBearing();
            pos += ", precision : " + loc.getAccuracy();
            new Sms(context,pos,telReponse);
        }else{
            new Sms(context,"Je ne trouve pas la position de Benjamin Désolé ...",telReponse);
        }
        MainActivity.activityPrincipal.getHandler().post(new Runnable() {
            public void run() {
                MainActivity.activityPrincipal.stopGpsNormalMode();
            }
        });
    }

    public static String getAdresse(double latitude, double longitude, Geocoder geocoder, Context c) {
        List<Address> addresses = null;
        String address = null;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses!=null){
                address = addresses.get(0).getAddressLine(0);
            }
        }catch(IOException e){
            System.out.println(e);
        }catch (NullPointerException e){
            System.out.println(e);
        }

        return address;
    }

}
