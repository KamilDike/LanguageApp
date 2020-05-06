package com.example.controler;


import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.lang.Math;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import processing.core.PApplet;

import static java.lang.Math.tan;


public class ContrlPage extends AppCompatActivity implements SensorEventListener {
    private PApplet sketch;
    private SensorManager sensorManager;
    private Sensor sensor;
    private double intialX;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contrl_page);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener( ContrlPage.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("X");

        myRef.setValue(sensorEvent.values[0] );
        myRef = database.getReference("Y");
        myRef.setValue(sensorEvent.values[1] );
        myRef = database.getReference("Z");
        myRef.setValue(sensorEvent.values[2]);


    }


    public void resetMousePointer(View view) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("X");
        myRef.setValue(0);
        myRef = database.getReference("Y");
        myRef.setValue(0);
        myRef = database.getReference("Z");
        myRef.setValue(0);
    }

    public void accept(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("accept");
        myRef.setValue("1");
    }
}
