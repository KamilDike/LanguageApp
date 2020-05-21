package com.example.controler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import static android.hardware.Sensor.TYPE_LIGHT;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LightPage extends AppCompatActivity  {
    private FirebaseDatabase database;
    private SensorManager sensorManager;
    private Sensor lightSensor;
    private SensorEventListener lightEventListener;
    private View ellipse_light;
    SpringAnimation lightX, lightY;
    private DatabaseReference slideRef;
    private Boolean firstRead = true;

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
                }
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

