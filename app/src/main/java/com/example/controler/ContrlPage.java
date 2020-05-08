package com.example.controler;


import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.math.BigDecimal;
import android.os.Bundle;

import android.view.View;

import java.lang.Math;
import java.util.ArrayList;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import processing.core.PApplet;

import static java.lang.Math.asin;
import static java.lang.Math.tan;


public class ContrlPage extends AppCompatActivity implements SensorEventListener {
    private PApplet sketch;
    private SensorManager sensorManager;
    private Sensor sensor;
    private double intialX;
    private int doResetSensorIfThisSmallerThanTen = 0 ;
    private ArrayList<Double> xi = new ArrayList<>();
    private ArrayList<Double> yi = new ArrayList<>();
    private ArrayList<Double> zi = new ArrayList<>();
    private Double X;
    private Double Y;
    private Double Z;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrl_page);

        X=0.0;
        Y=0.0;
        Z=0.0;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener( ContrlPage.this,sensor,SensorManager.SENSOR_DELAY_GAME);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        if(doResetSensorIfThisSmallerThanTen >11) {
            DatabaseReference myRef = database.getReference("X");
           // y =
            myRef.setValue(( ( (720 - 0) * (70 - (50 +  100 * 0.5 * tan(2 * asin(sensorEvent.values[2] - X)))) ) / (70 - 40 ) ) + 0);
            myRef = database.getReference("Y");
            myRef.setValue(( ( (500 - 0) * (64 - (50 +  100 * 0.5 * tan(2 * asin(sensorEvent.values[0] - Y)))) ) / (64 - 39 ) ) + 0);;
            myRef = database.getReference("Z");
            myRef.setValue(50 +  100 * 0.5 * tan(2 * asin(sensorEvent.values[1] - Z) ));
        } else if (doResetSensorIfThisSmallerThanTen <11){
           xi.add((double) sensorEvent.values[2]);
           yi.add((double) sensorEvent.values[0]);
           zi.add((double) sensorEvent.values[1]);
           doResetSensorIfThisSmallerThanTen = doResetSensorIfThisSmallerThanTen + 1;
        }else{
            X = getMean(xi);
            Y = getMean(yi);
            Z = getMean(zi);
            xi.clear();
            yi.clear();
            zi.clear();
            doResetSensorIfThisSmallerThanTen = doResetSensorIfThisSmallerThanTen + 1;
        }


    }

    private Double getMean(ArrayList<Double> a){
        Double sum = 0.0;
        for( int i=0 ; i < a.size() ; i++ ){
            sum = sum + a.get(i);
        }
        return sum / a.size();
    }

    public void resetMousePointer(View view) {


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("X");
        myRef.setValue(360);
        myRef = database.getReference("Y");
        myRef.setValue(250);
        myRef = database.getReference("Z");
        myRef.setValue(49);
        doResetSensorIfThisSmallerThanTen = 0;
    }

    public void accept(View view){
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("accept");
        myRef.setValue("1");
    }
}
