package com.example.controler;

import androidx.appcompat.app.AppCompatActivity;
import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import static android.hardware.Sensor.TYPE_LIGHT;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LightPage extends AppCompatActivity  {

    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View ellipse_light;
    SpringAnimation lightX, lightY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_page);

        final View img = findViewById(R.id.ellipse_light);
        lightX = new SpringAnimation(img,DynamicAnimation.SCALE_X,0);
        lightY = new SpringAnimation(img,DynamicAnimation.SCALE_Y,0);

        lightX.setStartValue(0);
        lightX.setMinValue(0);
        lightX.setMaxValue(10000000000L);
        lightX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        lightX.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);

        lightY.setStartValue(0);
        lightY.setMinValue(0);
        lightY.setMaxValue(10000000000L);
        lightY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        lightY.getSpring().setStiffness(SpringForce.STIFFNESS_LOW);

        ellipse_light = findViewById(R.id.ellipse_light);
        sensorManager = (SensorManager) getSystemService(Service.SENSOR_SERVICE);
        lightSensor = sensorManager.getDefaultSensor(TYPE_LIGHT);

        if(lightSensor == null) {
            Toast.makeText(this, "No light sensor detected", Toast.LENGTH_SHORT).show();
            finish();
        }

        lightEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float value = event.values[0];
                // getSupportActionBar().setTitle("Lumiere: " + value + "lx");

                if(event.values[0] <= 300) {
                    lightX.animateToFinalPosition((float) value / 200);
                    lightY.animateToFinalPosition((float) value / 200);
                }
                else if(event.values[0] > 300) {
                    lightX.cancel();
                    lightY.cancel();
                },
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
    }

    @Override
    protected void onResume()  {
        super.onResume();
        sensorManager.registerListener(lightEventListener, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause()  {
        super.onPause();
        sensorManager.unregisterListener(lightEventListener);
    }

}

