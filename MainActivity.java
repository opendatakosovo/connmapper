package com.opendatakosovo.connmapper.connmapper;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // Instead of try and catch, could created method and make use of following annotation?
            // @RequiresPermission(anyOf = {Manifest.permission.ACCESS_FINE_LOCATION})
            try{
                // Request location update with 1 second minimum time interval and 1 metre minimum distance between location updates.
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1, this);
            }catch(SecurityException e){

                // Inform use location permission has not been set
                Toast.makeText(getApplicationContext(), "Location not permitted.", Toast.LENGTH_SHORT).show();
            }

        }else{
            // If location is not enabled, forward user to settings screen to enable location
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onLocationChanged(Location location) {
        // Get lat, lng, and altitude
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double alt = location.getAltitude();

        // Get network class
        String networkType = getNetworkType(getApplicationContext());

        // Eventually change this with an API POST request saving the information in a database in some server.
        Toast.makeText(getApplicationContext(), lat + ", " + lng + ": " + networkType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(getApplicationContext(), "Location provider enabled.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
    }

    /**
     * Get the network type. Can either be 2G, 3G, 4G, or Unknown.
     * @param context
     * @return
     */
    public String getNetworkType(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return "2G";
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return "3G";
            case TelephonyManager.NETWORK_TYPE_LTE:
                return "4G";
            default:
                return "Unknown";
        }
    }
}
