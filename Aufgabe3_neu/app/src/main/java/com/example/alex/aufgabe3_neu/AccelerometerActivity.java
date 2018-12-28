package com.example.alex.aufgabe3_neu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

/**
 * Activity that shows the usage of the smartphone accelerometer
 * that record movement of the device in x, y and z direction
 */
public class AccelerometerActivity extends AppCompatActivity implements SensorEventListener{

    private String activityName = AccelerometerActivity.class.getSimpleName();
    private SensorManager accelerometerManager;
    private Sensor accelerometer;


    /**
     * Init activity and the sensor
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

        //Init accelerometer sensor and connect it with the manager
        accelerometerManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = accelerometerManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //register tha activity as sensor event listener
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }


    /**
     * Activity listens to all sensor events
     * For every event this method is triggered
     * @param event
     */
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

            Log.d(activityName, "value x:" + x);
            Log.d(activityName, "value y:" + y);
            Log.d(activityName, "value z:" + z);

            //Display the values in the ui labels
            tvx.setText("x :" + x);
            tvy.setText("y :" + y);
            tvz.setText("z :" + z);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        //unregister listener if the activity switches to pause state
        accelerometerManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        //register the listener again if the activity is active again
        accelerometerManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
