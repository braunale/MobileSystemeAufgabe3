package com.example.alex.aufgabe3_neu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    String channelId  = "123456";
    String channelName = "News";


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotificationManager notificationManager =  getSystemService(NotificationManager.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                    new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW));
        }




    }


}
