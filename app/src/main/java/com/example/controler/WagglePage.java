package com.example.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.TriggerEvent;
import android.hardware.TriggerEventListener;
import android.os.Bundle;
import android.util.Log;

import com.example.controler.musicPage.MusicPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WagglePage extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference slideRef;
    private DatabaseReference myRef;
    private SensorManager sensorManager;
    private TriggerEventListener triggerEventListener;
    private Sensor sensor;
    private Boolean firstRead = true;
    private int triggerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waggle_page);

        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_SIGNIFICANT_MOTION);

        database = FirebaseDatabase.getInstance();
        slideRef = database.getReference("slide");

        slideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long slide = dataSnapshot.getValue(Long.class);
                Log.i("FROM FIREBASE", "slide");

                if(firstRead==false) {
                    if (slide == 1) {
                        Intent myIntent = new Intent(getBaseContext(), ContrlPage.class);
                        startActivity(myIntent);
                    } else if (slide == 2) {
                        Intent myIntent = new Intent(getBaseContext(), GrabPage.class);
                        startActivity(myIntent);
                    } else if (slide == 3) {
                        Intent myIntent = new Intent(getBaseContext(), LightPage.class);
                        startActivity(myIntent);
                    } else if (slide == 4) {
                        Intent myIntent = new Intent(getBaseContext(), MusicPage.class);
                        startActivity(myIntent);
                    } else if (slide == 5) {
                        Intent myIntent = new Intent(getBaseContext(), WagglePage.class);
                        startActivity(myIntent);
                    } else if (slide == 6) {
                        Intent myIntent = new Intent(getBaseContext(), WaterPage.class);
                        startActivity(myIntent);
                    }
                }
                firstRead= false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
            };
        });
        triggerEventListener = new TriggerEventListener() {
            @Override
            public void onTrigger(TriggerEvent event) {
                //getSupportActionBar().setTitle("Trigger: " + event.values[0]);
                myRef = database.getReference("trigger");
                if(event.values[0] == 1) {
                    triggerCount++;
                    myRef.setValue(triggerCount);
                    sensorManager.requestTriggerSensor(triggerEventListener, sensor);
                }
            }
        };
    }

    protected void onResume() {
        super.onResume();
        sensorManager.requestTriggerSensor(triggerEventListener, sensor);
    }

    protected void onPause() {
        super.onPause();
        sensorManager.cancelTriggerSensor(triggerEventListener, sensor);
        myRef.setValue(0);
    }
}
