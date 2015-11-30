package com.corp.juxo.smstransfertsystem.listener;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

/**
 * Created by Juxo on 20/11/2015.
 */
public class GpsListener implements LocationListener {

    public static Location loc;

    @Override
    public void onLocationChanged(Location location) {
        
        loc = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
