package com.example.alex.aufgabe3_neu;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Timer;
import java.util.TimerTask;

public class LoggedInActivity  extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
    private String ActivityName = MainActivity.class.getSimpleName();

    private String activityName = GpsActivity.class.getSimpleName();
    private LocationManager locationManager;
    private boolean IsNetworkListenerAttached = false;

    private SensorManager accelerometerManager;
    private Sensor accelerometer;

    private SensorManager lightManager;
    private Sensor brightnessSensor;


    private double latitude=0;
    private double longitude=0;
    private double velX = 0;
    private double velY =0;
    private double velZ = 0;
    private double brightness;

    private GoogleSignInAccount account;
    private String projectId = "87344848365";
    private FirebaseMessaging fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);


        //request the permission for using gps
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        //Init accelerometer sensor and connect it with the manager
        accelerometerManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register tha activity as sensor event listener
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);


        //Init accelerometer sensor and connect it with the manager
        lightManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        brightnessSensor = lightManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //register tha activity as sensor event listener
        lightManager.registerListener(this, brightnessSensor, SensorManager.SENSOR_DELAY_FASTEST);

        // Get account data
        Bundle extra = getIntent().getExtras();
        account = extra.getParcelable("user");


        // Try to register at the server
        fm = FirebaseMessaging.getInstance();
        Log.d(TAG, "Try to send a register message to Server: "+projectId);

        fm.send(new RemoteMessage.Builder( projectId + "@gcm.googleapis.com")
                .setMessageId("1")
                .addData("action", "REGISTER")
                .addData("firstName",""+account.getGivenName())
                .addData("secondName",""+account.getFamilyName())
                .addData("mail",""+account.getEmail())
                .addData("googleId",""+account.getId())
                .addData("timestamp",""+System.currentTimeMillis())
                .build());


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = "123456";
            String channelName = "News";
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]


        Button logTokenButton = findViewById(R.id.logTokenButton);
        Button sendMessageButton = findViewById(R.id.buttonSendMessage);
        logTokenButton.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {

                Log.d(TAG,"on click. Get Token!");

                //Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();


                // Get token
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "getInstanceId failed", task.getException());
                                    return;
                                }

                                // Get new Instance ID token
                                String token = task.getResult().getToken();

                                // Log and toast

                                Log.d(TAG, token);
                                Toast.makeText(LoggedInActivity.this, token, Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });


        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToServer();
            }
        });

        // Send data each minute
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                sendDataToServer();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, 10000, 20000);
    }


    /**
     * Handle clicks on menu items
     * Each item navigates the user to a separate activity
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        Intent intent = null;

        //set Intent according to the clicked menu item

        if (id == R.id.menu_gps) {

            Log.d(ActivityName, "User navigates to the gps activity.");

            intent = new Intent(this, GpsActivity.class);
        } else if (id == R.id.menu_brightness) {

            Log.d(ActivityName, "User navigates to the brightness activity.");

            intent = new Intent(this, BrightnessActivity.class);
        } else if (id == R.id.menu_accelerometer) {

            Log.d(ActivityName, "User navigates to the accelerometer activity.");

            intent = new Intent(this, AccelerometerActivity.class);
        }

        //navigate to the selected activity
        if(intent != null) {
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Init menu in header
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Listener for the GPS signal
     */
    LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            Log.d(activityName, "Location value received from GPS provider.");

            // Remove the network listener if attached as soon as we got a finer GPS signal
            if(IsNetworkListenerAttached){
                locationManager.removeUpdates(locationListenerNetwork);
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

        this.latitude = lat;
        this.longitude = lon;

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
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    // Lookup for LastKnownLocation
                    Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (l != null) {
                        setLocationData(l, LocationType.LASTKNOWN);
                    }

                    boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    boolean network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if(!gps_enabled && !network_enabled) {
                        Toast.makeText(this,
                                "GPS and network provider is disabled.", Toast.LENGTH_LONG).show();
                    }

                    if(gps_enabled) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
                        Log.d(activityName, "GPS provider is enabled and the listener has been added.");
                    } else {
                        Log.d(activityName, "GPS provider is disabled");
                    }

                    if(network_enabled) {
                        IsNetworkListenerAttached = true;
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
                        Log.d(activityName, "Network provider is enabled and the listener has been added.");
                    } else {
                        Log.d(activityName, "Network provider is disabled");
                    }
                } else {
                    // permission denied, boo! Disable the functionality
                    Toast.makeText(this,
                            "Permission denied to read your gps data. This activity will stay without functionality.", Toast.LENGTH_LONG).show();

                    Log.d(activityName, "Location permission has been denied.");
                }

                return;
            }
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        //check if the event come from an accelerometer sensor
        if(sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            //get instance to the labels in the UI
            TextView tvx = findViewById(R.id.tv_x);
            TextView tvy = findViewById(R.id.tv_y);
            TextView tvz = findViewById(R.id.tv_z);

            //read the movement values for x,y and z direction
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            this.velX = x;
            this.velY = y;
            this.velZ = z;

            Log.d(activityName, "value x:" + x);
            Log.d(activityName, "value y:" + y);
            Log.d(activityName, "value z:" + z);

            //Display the values in the ui labels
            tvx.setText("x :" + x);
            tvy.setText("y :" + y);
            tvz.setText("z :" + z);
        }

        if(sensor.getType() == Sensor.TYPE_LIGHT){
            //get instance to the labels in the UI
            TextView tvBrightness = findViewById(R.id.tv_brightness);


            //read the lux value of the brightness sensor
            float brightness = event.values[0];

            this.brightness = brightness;

            Log.d(activityName, "Brightness:" + brightness + "lux");

            //Display the values in the ui labels
            tvBrightness.setText("Brightness:" + brightness + "lux");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void sendDataToServer(){
        Log.d(TAG, "Try to send messages to Server: "+projectId);

        fm.send(new RemoteMessage.Builder( projectId + "@gcm.googleapis.com")
                .setMessageId("1")
                .addData("action", "GPS")
                .addData("latitude",""+latitude)
                .addData("longitude",""+longitude)
                .addData("timestamp",""+System.currentTimeMillis())
                .build());

        fm.send(new RemoteMessage.Builder( projectId + "@gcm.googleapis.com")
                .setMessageId("2")
                .addData("action", "ACCELERATOR")
                .addData("velX",""+velX)
                .addData("velY",""+velY)
                .addData("velZ",""+velZ)
                .addData("timestamp",""+System.currentTimeMillis())
                .build());

        fm.send(new RemoteMessage.Builder( projectId + "@gcm.googleapis.com")
                .setMessageId("3")
                .addData("action", "LIGHT")
                .addData("light",""+brightness)
                .addData("timestamp",""+System.currentTimeMillis())
                .build());
    }


    protected void onPause() {
        super.onPause();
        //unregister listener if the activity switches to pause state
        accelerometerManager.unregisterListener(this);
        lightManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        //register the listener again if the activity is active again
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        lightManager.registerListener(this, brightnessSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
