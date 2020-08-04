package com.example.controler.musicPage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.example.controler.ContrlPage;
import com.example.controler.GrabPage;
import com.example.controler.LightPage;
import com.example.controler.MainActivity;
import com.example.controler.R;
import com.example.controler.WagglePage;
import com.example.controler.WaterPage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MusicPage extends AppCompatActivity {

    CurrentSongValues currentSongValues;

    // UI objects.
    LinearLayout linearLayout;
    TextView textView;
    Button btnPlaySong;

    public TextColors colorsSlideIN;
    public TextColors colorsFadeOut;
    MicrophoneInput microphoneInput;

    AudioPlayer audioPlayer;

    private Boolean firstRead = true;

    // Spring Animation.
    private SpringAnimation springAnimScaleX;
    private SpringAnimation springAnimScaleY;

    //Handler for ui, main thread.
    Handler backgroundAnimationHandler;
    Handler textAnimationHandler;

    //Text animation object.
    TextAnimation textAnimation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_page);

        final View backgroundView = findViewById(R.id.ellipse_5);

        permissionsCheck();

        currentSongValues = new CurrentSongValues(1);
        loadBackgroundAnim(backgroundView);
        audioPlayer = new AudioPlayer(this);
        btnPlaySong = findViewById(R.id.btnPlaySong);
        initializeHandlers();

        // Database config.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference slideRef = database.getReference("slide");
        slideRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long slide = dataSnapshot.getValue(Long.class);
                Log.i("FROM FIREBASE", "slide");

                if(!firstRead) {
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
            }
        });

        DatabaseReference playSongRef = database.getReference("playSong");
        playSongRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Long playSong;
                try {
                    playSong = dataSnapshot.getValue(Long.class);
                    if(!firstRead && playSong == 1) {
                        microphoneInput.run();
                        audioPlayer.startSong();
                        textAnimation.run();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.i("FROM FIREBASE", "slide");
                firstRead= false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
            }
        });

        textAnimation = new TextAnimation(backgroundAnimationHandler, textAnimationHandler, currentSongValues);
        microphoneInput = new MicrophoneInput(backgroundAnimationHandler);

        colorsSlideIN = new TextColors(Color.parseColor("#FFFFFFFF"), Color.parseColor("#00D2003C"));
        colorsFadeOut = new TextColors(Color.parseColor("#00D2003C"), Color.parseColor("#FFFFFFFF"));

        linearLayout = findViewById(R.id.linearLayoutText);

        linearLayout.bringToFront();

        btnPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlaySong.setVisibility(View.GONE);
                try {
                    microphoneInput.run();
                    audioPlayer.startSong();
                    textAnimation.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void initializeHandlers() {
        textAnimationHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                if (message.what == 1)
                    paintText(1);
                else if (message.what == 0) {
                    paintText(0);
                    if (currentSongValues.animPosition == 110)
                    {
                        btnPlaySong.setVisibility(View.VISIBLE);
                        currentSongValues.songPosition = 0;
                        textAnimation.stop();
                    }
                }
            }
        };

        //Using handler to manage UI, within main thread.
        backgroundAnimationHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                animateBackground(message.what);
            }
        };
    }

    public void resetMousePointer(View view) {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("X");
        myRef.setValue("0");
        myRef = database.getReference("Y");
        myRef.setValue("0");
        myRef = database.getReference("Z");
        myRef.setValue("0");
    }

    //Coloring TextView
    public void paintText(int choice) {

        //choice 1 = slideIN , 0 = fadeOut
        if (choice == 1)
            colorsSlideIN.setSlideColors(currentSongValues.animPosition);
        else colorsFadeOut.setFadeColors(currentSongValues.animPosition);

        textView = new TextView(MusicPage.this);
        textView.setText(currentSongValues.songText[currentSongValues.songPosition]);
        textView.setTextSize(40);

        Shader textShader;
        if (choice == 1) {
            textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                    colorsSlideIN.getColors(), null, Shader.TileMode.REPEAT);
        }
        else {
            textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                    colorsFadeOut.getColors(), null, Shader.TileMode.REPEAT);
        }

        textView.getPaint().setShader(textShader);

        linearLayout.removeAllViews();
        linearLayout.addView(textView);
    }

    /**
     * Function loads SpringAnimation Objects.
     * @param view current View.
     */
    public void loadBackgroundAnim(View view) {
        
        springAnimScaleX = new SpringAnimation(view,DynamicAnimation.SCALE_X,0);
        springAnimScaleY = new SpringAnimation(view,DynamicAnimation.SCALE_Y,0);

        springAnimScaleX.setStartValue(0);
        springAnimScaleX.setMinValue(-200);
        springAnimScaleX.setMaxValue((float)200); // in radians
        springAnimScaleX.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimScaleX.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnimScaleY.setStartValue(0);
        springAnimScaleY.setMinValue(-200);
        springAnimScaleY.setMaxValue((float)200); // in radians
        springAnimScaleY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnimScaleY.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);
    }

    /**
     * Function animates background.
     * @param value describes current microphone reading.
     */
    public void animateBackground(float value) {
        if (value > 5000)
            value = (float) 0.75;
        else if (value > 500)
            value = (float) (0.75*(value/5000));
        else value = 0;

        springAnimScaleX.animateToFinalPosition((float) (1.0 + value*0.5));
        springAnimScaleY.animateToFinalPosition((float) (1.0 + value));
    }

    /**
     * Method responsible for listing currently granted permissions.
     */
    public void permissionsCheck() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 100);
        }
    }
}
