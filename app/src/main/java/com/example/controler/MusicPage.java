package com.example.controler;

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
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class MusicPage extends AppCompatActivity {

    /**
     * Class responsible for reading input from microphone.
     */
    public class MicrophoneInput {
        private static final int SAMPLE_RATE = 44100;
        private boolean recording;
        private AudioRecord record;
        private short[] data;
        void recordAudio()
        {
            record.startRecording();
            while(recording)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int result = record.read(data, 0, SAMPLE_RATE/8); // read 0.25 second at a time

                if(result == AudioRecord.ERROR_INVALID_OPERATION || result == AudioRecord.ERROR_BAD_VALUE)
                {
                    System.out.println("Recording error");
                    return;
                }
                else
                {
                    Message message = backgroundAnimationHandler.obtainMessage(findMax(data));
                    message.sendToTarget();
                }
            }
            record.stop();
        }

        void run() {
            if(!recording)
            {
                System.out.println("Recording Started");
                int bufferSize = AudioRecord.getMinBufferSize(  SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT);

                record = new AudioRecord(   MediaRecorder.AudioSource.MIC,
                        SAMPLE_RATE,
                        AudioFormat.CHANNEL_IN_MONO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize * 2);

                data = new short[SAMPLE_RATE];

                new Thread()
                {
                    @Override
                    public void run()
                    {
                        recordAudio();
                    }

                }.start();

                recording = true;
            }
            else
            {
                recording = false;
            }
        }

        void stopRecording() { recording = false; }
    }

    /**
     * Class responsible for animating text.
     */
    class TextAnimation {
        boolean running;

        void run() {
            running = true;
            animPosition = 0;

            new Thread() {
                @Override
                public void run() {
                    startAnimation();
                }
            }.start();
        }

        private void startAnimation() {
            sleep(songAnimationDelay[songPosition]);
            int type; // 1 - fadeIn, 0 - fadeOut.
            while (running) {
                System.out.println("SongPos: " + songPosition + "\nAnimPos: " + animPosition);
                if (animPosition < 110 && songPosition < 14) {
                    type = 1;
                    animPosition++;
                    Message message = textAnimationHandler.obtainMessage(type);
                    message.sendToTarget();
                    sleep(10);
                } else if (songPosition < 14) {
                    songPosition++;
                    animPosition = 0;
                    sleep(songAnimationDelay[songPosition]);
                } else {
                    animPosition = 0;
                    type = 0;
                    microphoneInput.stopRecording();
                    //mediaPlayer.release();
                    //mediaPlayer = null;
                    Message message2 = backgroundAnimationHandler.obtainMessage(0);
                    message2.sendToTarget();
                    running = false;

                    for (int i = 0; i < 110; i++) {
                        animPosition++;
                        Message message = textAnimationHandler.obtainMessage(type);
                        message.sendToTarget();
                        sleep(20);
                    }
                }
            }
        }

        private void sleep(int time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        void stop() {
            running = false;
        }
    }

    // UI objects.
    LinearLayout linearLayout;
    TextView textView;
    Button btnPlaySong;

    int animPosition = 0;
    int songPosition = 0;
    int[] songAnimationDelay;
    String[] songText;
    public TextColors colorsSlideIN;
    public TextColors colorsFadeOut;
    MicrophoneInput microphoneInput;

    MediaPlayer mediaPlayer;
    AssetFileDescriptor assetFileDescriptor;

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

        loadBackgroundAnim(backgroundView);
        songText = new String[20];
        mediaPlayer = new MediaPlayer();
        loadSong(1);
        btnPlaySong = findViewById(R.id.btnPlaySong);

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
                        mediaPlayer.start();
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

        textAnimation = new TextAnimation();
        microphoneInput = new MicrophoneInput();

        colorsSlideIN = new TextColors(Color.parseColor("#FFFFFFFF"), Color.parseColor("#00D2003C"));
        colorsFadeOut = new TextColors(Color.parseColor("#00D2003C"), Color.parseColor("#FFFFFFFF"));

        linearLayout = findViewById(R.id.linearLayoutText);

        linearLayout.bringToFront();

        listingPermissions();

        //Using handler to manage UI, within main thread.
        backgroundAnimationHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                animateBackground(message.what);
            }
        };

        textAnimationHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message message) {
                if (message.what == 1)
                    paintText(1);
                else if (message.what == 0) {
                    paintText(0);
                    if (animPosition == 110)
                    {
                        btnPlaySong.setVisibility(View.VISIBLE);
                        songPosition = 0;
                        textAnimation.stop();
                    }
                }
            }
        };

        btnPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlaySong.setVisibility(View.GONE);
                try {
                    microphoneInput.run();
                    mediaPlayer.start();
                    textAnimation.run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
            colorsSlideIN.setSlideColors(animPosition);
        else colorsFadeOut.setFadeColors(animPosition);

        textView = new TextView(MusicPage.this);
        textView.setText(songText[songPosition]);
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
    public void listingPermissions() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] permissions = info.requestedPermissions;
            for (String permission : permissions) {
                System.out.println(permission);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method responsible for finding max value in array of shorts.
     * @param arr Array of shorts.
     * @return Returning maximum value in array.
     */
    public short findMax(final short[] arr) {
        short o_intMax = arr[0];
        for ( int p_intI = 1; p_intI < arr.length; p_intI++)
            if ( arr[p_intI] > o_intMax )
                o_intMax = arr[p_intI];
        return o_intMax;
    } // end method

    /**
     * Setting delays, text for chosen song.
     * @param z choosing song from library.
     */
    public void loadSong(int z) {
        try {
            assetFileDescriptor = getAssets().openFd("Audio.mp3");
            mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (z == 1) {
            songAnimationDelay = new int[20];

            songText = new String[20];
            String line1 = "On va au bal";
            String line2 = "On va du ciel";
            for (int i = 0; i < 15; i++) {
                songAnimationDelay[i] = 400;
                if (i < 4) {
                    songText[i] = line1;
                }
                else if (i < 8) {
                    songText[i] = line2;
                }
                else {
                    songText[i] = line1;
                }
            }
            songAnimationDelay[0] = 6000;
            songAnimationDelay[9] = 2000;
            songAnimationDelay[10] = 2000;
        }
    }

}
