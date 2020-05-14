// Sketch.java
package com.example.controler;

import processing.core.PApplet;
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

    public void settings() {
        size(width, height);
    }

    public void setup() {
        context = getActivity();
        manager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        sensor = manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        listener = new AccelerometerListener();
        manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_GAME);

    }

    public void draw() {
        background(255);
        if (mousePressed) {


            ellipse(mouseX, mouseY, 50, 50);
        }
        fill(0);
        text("X: " + ax + "\nY: " + ay + "\nZ: " + az, 0, 0, width, height);

        strokeWeight(50);
        stroke(50,50,240);
        fill(50,50,240);

        translate(width/2,height/2);
        rotate(alpha);
        rect(-width,0,2*width,height);

        rotate(-alpha);
        translate(-width/2,-height/2);
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
               alpha = (float)(2*asin((float)(sensorEvent.values[2] - X )));


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