package com.example.alex.aufgabe3_neu;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity that shows the usage of the smartphone gps system that measure the location of the device
 */
public class GpsActivity extends AppCompatActivity {

    private String activityName = GpsActivity.class.getSimpleName();
    private LocationManager manager;
    private boolean IsNetworkListenerAttached = false;
    /**
     * init activity and set layout
     * Get permission for using gps
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        //request the permission for using gps
        ActivityCompat.requestPermissions(GpsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
    }

    /**
     * Listener for the GPS signal
     */
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(activityName, "Location value received from GPS provider.");

            // Remove the network listener if attached as soon as we got a finer GPS signal
            if(IsNetworkListenerAttached){
                manager.removeUpdates(locationListenerNetwork);
                IsNetworkListenerAttached = false;
                Log.d(activityName, "Network provider listener has been removed.");
            }

            setLocationData(location, LocationType.FINE);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    /**
     * Listener for the network signal
     */
    LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(activityName, "Location received from network provider");
            setLocationData(location, LocationType.COARSE);
        }

        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    /**
     * Helper function to display the location values of the listeners on the UI
     */
    private void setLocationData(Location location, LocationType type){
        TextView tvlon = findViewById(R.id.tv_lon);
        TextView tvlat = findViewById(R.id.tv_lat);
        TextView tvgps = findViewById(R.id.tv_gps);

        //read the devices latitude and longitude
        double lat = location.getLatitude();
        double lon = location.getLongitude();

        switch(type) {
            case FINE:
                tvgps.setText("Fine location found!");
                break;
            case COARSE:
                tvgps.setText("Coarse location found!");
                break;
            case LASTKNOWN:
                tvgps.setText("Last known location found!");
                break;
            default:
                throw new IllegalArgumentException("No case found for type " + type);
        }

        Log.d(activityName, "value longitude: " + lon);
        Log.d(activityName, "value latitude: " + lat);

        tvlon.setText("longitude: " + lon);
        tvlat.setText("latitude: " + lat);
    }

    /**
     * React to the users choice to grand or refuse the permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    // init sensor manager
                    manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    // Lookup for LastKnownLocation
                    Location l = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (l != null) {
                        setLocationData(l, LocationType.LASTKNOWN);
                    }

                    boolean gps_enabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean network_enabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if(!gps_enabled && !network_enabled) {
                        Toast.makeText(GpsActivity.this,
                                "GPS and network provider is disabled.", Toast.LENGTH_LONG).show();
                    }

                    if(gps_enabled) {
                        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
                        Log.d(activityName, "GPS provider is enabled and the listener has been added.");
                    } else {
                        Log.d(activityName, "GPS provider is disabled");
                    }

                    if(network_enabled) {
                        IsNetworkListenerAttached = true;
                        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                        Log.d(activityName, "Network provider is enabled and the listener has been added.");
                    } else {
                        Log.d(activityName, "Network provider is disabled");
                    }
                } else {
                    // permission denied, boo! Disable the functionality
                    Toast.makeText(GpsActivity.this,
                            "Permission denied to read your gps data. This activity will stay without functionality.", Toast.LENGTH_LONG).show();

                    Log.d(activityName, "Location permission has been denied.");
                }

                return;
            }
        }
    }
}
