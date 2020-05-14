package com.example.controler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Math.asin;
import static java.lang.Math.tan;

public class WaterPage extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor sensor;
    private SpringAnimation springAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout myLayout = findViewById(R.id.WaterPage);


        setContentView(R.layout.activity_water_page);

        final View img = findViewById(R.id.rectangle_2);
// Setting up a spring animation to animate the view’s translationY property with the final
// spring position at 0.
         springAnim = new SpringAnimation(img, DynamicAnimation.ROTATION, 0);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR);
        sensorManager.registerListener( WaterPage.this,sensor,SensorManager.SENSOR_DELAY_GAME);


        springAnim.setStartValue(0);
        springAnim.setMinValue(-180);
        springAnim.setMaxValue((float)180); // in radians
        springAnim.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY);
        springAnim.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);




    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i){

    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent){
            springAnim.animateToFinalPosition((float)Math.toDegrees(2 * asin(sensorEvent.values[2])));
        }
}
