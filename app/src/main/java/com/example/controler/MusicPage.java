package com.example.controler;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import static java.lang.Math.asin;

public class MusicPage extends AppCompatActivity {

    //Microphone
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
                    // TODO Auto-generated catch block
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
                    //System.out.println("Read: " + Arrays.toString(data));

                    //currently fixing animation
                    Message message = mHandler.obtainMessage(findMax(data));
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

                data = new short[SAMPLE_RATE]; // Records up to 10 seconds

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

        public void stopRecording() { recording = false; }
    }

    //Colors for text
    class MyColors {

        private int background;
        private int foreground;
        private int[] shaders;
        private int[] colors;

        public MyColors(int foreground, int background) {
            this.background = background;
            this.foreground = foreground;

            shaders = new int[10];
            colors = new int[100];

            shaders[9] = Color.parseColor("#FFd93f6d");
            shaders[8] = Color.parseColor("#00e35680");
            shaders[7] = Color.parseColor("#e6678d");
            shaders[6] = Color.parseColor("#ed7e9f");
            shaders[5] = Color.parseColor("#f09cb5");
            shaders[4] = Color.parseColor("#edafc2");
            shaders[3] = Color.parseColor("#edc2d0");
            shaders[2] = Color.parseColor("#edd3dc");
            shaders[1] = Color.parseColor("#f0dfe5");
            shaders[0] = Color.parseColor("#f2e9ec");
        }

        //MANUALLY SETTING SHADER
        public void setShader(int i, int color) {
            shaders[i] = color;
        }

        public int getBackground() {
            return background;
        }

        public int getForeground() {
            return foreground;
        }

        //SETTING COLOR
        public void setSlideColors(int animPosition) throws ArrayIndexOutOfBoundsException {
            for (int i = 0; i < animPosition - 10; i++) {
                colors[i] = foreground;
            }

            for (int i = animPosition; i < 100; i++) {
                colors[i] = background;
            }

            int z = 0;

            for (int i = animPosition-10; i < animPosition; i++) {
                if (i >= 0 && i < 100)
                    colors[i] = shaders[z++];
            }
        }

        public void setFadeColors(int animPosition) {
            switch (animPosition) {
                case 0:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = background;
                    }
                    break;
                case 10:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#edd1d9");
                    }
                    break;
                case 20:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#dba4b4");
                    }
                    break;
                case 30:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#cf7a93");
                    }
                    break;
                case 40:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#c75b7b");
                    }
                    break;
                case 50:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#c2486c");
                    }
                    break;
                case 60:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#BBba3a60");
                    }
                    break;
                case 70:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#99b32d54");
                    }
                    break;
                case 80:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#77ab244b");
                    }
                    break;
                case 90:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = Color.parseColor("#77D2003C");
                    }
                    break;
                case 99:
                    for (int i = 0; i < colors.length; i++) {
                        colors[i] = foreground;
                    }
                    break;
            }
        }

        public int[] getColors() {
            return colors;
        }
    }

    LinearLayout linearLayout;
    TextView textView;
    Button btnPlaySong;

    int animPosition = 0;
    int songPosition = 0;
    String[] songText;
    public MyColors colorsSlideIN;
    public MyColors colorsFadeOut;
    MicrophoneInput microphoneInput;

    //MediaPlayer
    MediaPlayer mediaPlayer;
    AssetFileDescriptor assetFileDescriptor;

    //Database configuration
    private FirebaseDatabase database;
    private DatabaseReference slideRef;
    private DatabaseReference playSongRef;
    private Boolean firstRead = true;

    //Background animation
    private SpringAnimation springAnim;
    private SpringAnimation springAnim2;
    private SpringAnimation springAnim3;
    private SpringAnimation springAnim4;
    private SpringAnimation springAnim5;
    private SpringAnimation springAnim6;
    private SpringAnimation springAnim7;

    //Handler for ui, main thread
    Handler mHandler;

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

        playSongRef = database.getReference("playSong");
        playSongRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long playSong = dataSnapshot.getValue(Long.class);
                Log.i("FROM FIREBASE", "slide");

                if(firstRead==false) {
                    if (playSong == 1) {
                        fadeIN();
                        playSong = (long) 0;
                    }
                }
                firstRead= false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
            };
        });

        microphoneInput = new MicrophoneInput();

        colorsSlideIN = new MyColors(Color.parseColor("#FFFFFFFF"), Color.parseColor("#00D2003C"));
        colorsFadeOut = new MyColors(Color.parseColor("#00D2003C"), Color.parseColor("#FFFFFFFF"));

        colorsSlideIN.setSlideColors(0);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutText);

        textView = new TextView(this);
        textView.setText(songText[0]);
        textView.setTextSize(40);

        Shader textShader;
        textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                colorsSlideIN.getColors(), null, Shader.TileMode.REPEAT);

        textView.getPaint().setShader(textShader);

        linearLayout.addView(textView);
        linearLayout.bringToFront();

        listingPermissions();

        //Using handler to manage UI, within main thread.
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
                System.out.println(message.toString());
                animateBackground(message.what);
            }
        };

        //Play song button
        btnPlaySong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                microphoneInput.run();
                fadeIN();
                btnPlaySong.setVisibility(View.GONE);
                try {
                    assetFileDescriptor = getAssets().openFd("Audio.mp3");
                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                            assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
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

        //System.out.println("Animation position = " + animPosition);

        Shader textShader;
        if (choice == 1)
            textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                    colorsSlideIN.getColors(), null, Shader.TileMode.REPEAT);
        else
            textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                    colorsFadeOut.getColors(), null, Shader.TileMode.REPEAT);

        textView.getPaint().setShader(textShader);

        linearLayout.removeAllViews();
        linearLayout.addView(textView);
    }

    //Appear text
    public void fadeIN() {
        animPosition = 0;
        paintText(1);

        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 1000f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animPosition < 110) {
                    animPosition++;
                    paintText(1);
                    //System.out.println("Animator value = " + (float)animator.getAnimatedValue());
                }
                else if (songPosition < 14) {
                    animator.cancel();
                    changeLine();
                }
                else {
                    animator.cancel();
                    microphoneInput.stopRecording();
                    btnPlaySong.setVisibility(View.VISIBLE);
                    fadeOut();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
            }
        });
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);
        animator.start();
    }

    //Switch text line
    public void changeLine() {
        fadeIN();
        songPosition++;
    }

    //Fade out text
    public void fadeOut() {
        animPosition = 0;
        paintText(2);

        final ValueAnimator animator = ValueAnimator.ofFloat(0f, 2000f);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (animPosition < 110) {
                    animPosition++;
                    paintText(2);
                    //System.out.println("Animator value = " + (float)animator.getAnimatedValue());
                }
                else {
                    animator.cancel();
                    songPosition = 0;
                }
            }
        });
        animator.setDuration(5000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    //Load background animation object
    public void loadBackgroundAnim(View view) {
        springAnim = new SpringAnimation(view, DynamicAnimation.TRANSLATION_X, 200);
        springAnim2 = new SpringAnimation(view,DynamicAnimation.TRANSLATION_Y,200);
        springAnim3 = new SpringAnimation(view,DynamicAnimation.ROTATION,0);
        springAnim4 = new SpringAnimation(view,DynamicAnimation.ROTATION_X,0);
        springAnim5 = new SpringAnimation(view,DynamicAnimation.ROTATION_Y,0);
        springAnim6 = new SpringAnimation(view,DynamicAnimation.SCALE_X,0);
        springAnim7 = new SpringAnimation(view,DynamicAnimation.SCALE_Y,0);

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

        springAnim6.setStartValue(0);
        springAnim6.setMinValue(-200);
        springAnim6.setMaxValue((float)200); // in radians
        springAnim6.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim6.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);

        springAnim7.setStartValue(0);
        springAnim7.setMinValue(-200);
        springAnim7.setMaxValue((float)200); // in radians
        springAnim7.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY);
        springAnim7.getSpring().setStiffness(SpringForce.STIFFNESS_HIGH);
    }

    //Background animations
    public void animateBackground(float value) {
        if (value > 5000)
            value = (float) 0.75;
        else if (value > 500)
            value = (float) (0.75*(value/5000));
        else value = 0;

//        springAnim.animateToFinalPosition(1 + value);
//        springAnim2.animateToFinalPosition(1 + value);
//        springAnim3.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
//        springAnim4.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
//        springAnim5.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim6.animateToFinalPosition((float) (1 + value*0.5));
        springAnim7.animateToFinalPosition((float) (1 + value));
    }

    //Listing app permissions
    public void listingPermissions() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String[] permissions = info.requestedPermissions;//This array contains the requested permissions.

        for (int i = 0; i < permissions.length; i++) {
            System.out.println(permissions[i]);
        }
    }

    //Finding max value in shorts array
    public short findMax(final short[] arr) {
        short o_intMax = arr[0];
        for ( int p_intI = 1; p_intI < arr.length; p_intI++)
            if ( arr[p_intI] > o_intMax )
                o_intMax = arr[p_intI];
        return o_intMax;
    } // end method

    //Loading song, 1 - On Va Au Bal
    public void loadSong(int z) {
        if (z == 1) {
            String line1 = "On va au bal";
            String line2 = "On va du ciel";
            for (int i = 0; i < 14; i++) {
                if (i < 4)
                    songText[i] = line1;
                else if (i < 8)
                    songText[i] = line2;
                else songText[i] = line1;
            }
        }
    }
}
