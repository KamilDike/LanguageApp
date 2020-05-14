package com.example.controler;


import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.icu.math.BigDecimal;
import android.os.Bundle;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

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
    private SpringAnimation springAnim;
    private SpringAnimation springAnim2;
    private SpringAnimation springAnim3;
    private SpringAnimation springAnim4;
    private SpringAnimation springAnim5;
    private SpringAnimation springAnim6;
    private SpringAnimation springAnim7;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrl_page);
        FrameLayout myLayout = findViewById(R.id.control_page);
        final View img = findViewById(R.id.virtualJ);
        final View img2 = findViewById(R.id.ellipse_4);
        springAnim = new SpringAnimation(img, DynamicAnimation.TRANSLATION_X, 200);
        springAnim2 = new SpringAnimation(img,DynamicAnimation.TRANSLATION_Y,200);
        springAnim3 = new SpringAnimation(img,DynamicAnimation.ROTATION,0);
        springAnim4 = new SpringAnimation(img,DynamicAnimation.ROTATION_X,0);
        springAnim5 = new SpringAnimation(img,DynamicAnimation.ROTATION_Y,0);
        springAnim6 = new SpringAnimation(img2,DynamicAnimation.SCALE_X,0);
        springAnim7 = new SpringAnimation(img2,DynamicAnimation.SCALE_Y,0);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener( ContrlPage.this,sensor,SensorManager.SENSOR_DELAY_GAME);


        springAnim.setStartValue(0);
        springAnim.setMinValue(-200);
        springAnim.setMaxValue((float)200); // in radians
        springAnim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim2.setStartValue(0);
        springAnim2.setMinValue(-200);
        springAnim2.setMaxValue((float)200); // in radians
        springAnim2.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim2.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim3.setStartValue(0);
        springAnim3.setMinValue(-200);
        springAnim3.setMaxValue((float)200); // in radians
        springAnim3.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim3.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim4.setStartValue(0);
        springAnim4.setMinValue(-200);
        springAnim4.setMaxValue((float)200); // in radians
        springAnim4.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim4.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim5.setStartValue(0);
        springAnim5.setMinValue(-200);
        springAnim5.setMaxValue((float)200); // in radians
        springAnim5.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim5.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim6.setStartValue(1);
        springAnim6.setMinValue(-200);
        springAnim6.setMaxValue((float)200); // in radians
        springAnim6.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim6.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim7.setStartValue(1);
        springAnim7.setMinValue(-200);
        springAnim7.setMaxValue((float)200); // in radians
        springAnim7.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim7.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);



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
            springAnim.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(sensorEvent.values[2] - X))*2);
            springAnim2.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(sensorEvent.values[0] - Y))*2);

            springAnim3.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(sensorEvent.values[2] - X)*0.75));
           springAnim4.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(sensorEvent.values[0] - Y)*0.75));
           // springAnim5.animateToFinalPosition((float)Math.toDegrees(2 * asin(sensorEvent.values[1] - Z)*0.75));

                if(sensorEvent.values[0] - Y < 0){
                    springAnim6.animateToFinalPosition((float)(2 * asin(sensorEvent.values[0] - Y)*0.25)+1);
                }
                else{
                    springAnim6.animateToFinalPosition(-(float)(2 * asin(sensorEvent.values[0] - Y)*0.25)+1);
                }

                if(sensorEvent.values[2] - X < 0 ){
                    springAnim7.animateToFinalPosition(+(float)(2 * asin(sensorEvent.values[2] - X)*0.25)+1);

                }else{
                    springAnim7.animateToFinalPosition(-(float)(2 * asin(sensorEvent.values[2] - X)*0.25)+1);
                }




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
