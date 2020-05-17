// Sketch.java
package com.example.controler;

import processing.core.PApplet;
import processing.core.PImage;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEvent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static java.lang.Math.asin;
import static java.lang.Math.tan;

public class Sketch extends PApplet  {
    Context context;
    SensorManager manager;
    Sensor sensor;
    AccelerometerListener listener;
    float ax, ay, az;
    float nx,ny,nz;
    float alpha;
    PImage jar;
    float waterFlow;
    float checkIfTheWaterShouldFlow;

    public void settings() {
        size(width, height);
    }

    public void setup() {
        context = getActivity();
        manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        listener = new AccelerometerListener();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);
        waterFlow=0;


    }

    public void draw() {
        background(220);


        fill(50,50,240);
        translate(width/2,height/2);
        rotate(alpha);
        rect(-width,0 + waterFlow,2*width,height);

        rotate(-alpha);
        translate(-width/2,-height/2);

        strokeWeight(0);
        fill(255);

        rect(0,0,width,height/4);
        rect(0,height/4*3,width,height);
        rotate((float)-0.1);
        translate(-width/5,0);
        rect(0,0,width/3,height);
        translate(width/5,0);
        rotate((float)0.2);
        translate(width/5,0);
        rect(width/3*2,0,width,height);
        translate(-width/5,0);
        rotate((float)-0.1);

        stroke(255);
        fill(0);
        text(alpha,50,50);
        text(ay,50,80);
        text(az,50,110);

        if(alpha < -0.940 ){
            fill(0,0,255);
            rect(width/3*2,0,width,height/4);
        }
        if(alpha >0.940){
            fill(0,0,255);
            rect(0,0,width/3,height/4);
        }


    }

    class AccelerometerListener implements SensorEventListener {
        private int doResetSensorIfThisSmallerThanTen = 0 ;
        private ArrayList<Double> xi = new ArrayList<>();
        private ArrayList<Double> yi = new ArrayList<>();
        private ArrayList<Double> zi = new ArrayList<>();
        private Double X;
        private Double Y;
        private Double Z;
        @Override
        public void onSensorChanged(SensorEvent sensorEvent){
            if(doResetSensorIfThisSmallerThanTen >11) {
               alpha = (2*asin((float)(sensorEvent.values[2] - X )));


                ax = (float)(sensorEvent.values[2] - X);
               ay = (float)(sensorEvent.values[0] - Y);
               az = (float)(sensorEvent.values[1] - Z);

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

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    }


}