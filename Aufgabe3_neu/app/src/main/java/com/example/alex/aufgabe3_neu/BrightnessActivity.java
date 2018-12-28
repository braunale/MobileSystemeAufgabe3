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
 * Activity that shows the usage of the smartphone brightness sensor
 * that record the intensity of the environmental light
 */
public class BrightnessActivity extends AppCompatActivity implements SensorEventListener {

    private String activityName = BrightnessActivity.class.getSimpleName();
    private SensorManager lightManager;
    private Sensor brightness;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        //Init accelerometer sensor and connect it with the manager
        lightManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        brightness = lightManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //register tha activity as sensor event listener
        lightManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST);
    }


    /**
     * Activity listens to all sensor events
     * For every event this method is triggered
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        //check if the event come from a brightness sensor
        if(sensor.getType() == Sensor.TYPE_LIGHT){
            //get instance to the labels in the UI
            TextView tvBrightness = findViewById(R.id.tv_brightness);


            //read the lux value of the brightness sensor
            float brightness = event.values[0];


            Log.d(activityName, "Brightness:" + brightness + "lux");

            //Display the values in the ui labels
            tvBrightness.setText("Brightness:" + brightness + "lux");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onPause() {
        super.onPause();
        //unregister listener if the activity switches to pause state
        lightManager.unregisterListener(this);
    }

    protected void onResume() {
        super.onResume();
        //register the listener again if the activity is active again
        lightManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_FASTEST);
    }
}
