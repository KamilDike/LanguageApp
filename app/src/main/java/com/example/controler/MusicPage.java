package com.example.controler;

import android.Manifest;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.util.Arrays;

import static java.lang.Math.asin;

public class MusicPage extends AppCompatActivity {
    //Microphone usage permission
    private int MICROPHONE_PERMISSION_CODE = 1;
    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MusicPage.this,
                Manifest.permission.RECORD_AUDIO)) {
            new AlertDialog.Builder(MusicPage.this)
                    .setTitle("Wymagane pozwolenie użycia mikrofonu")
                    .setMessage("Wybierz \"ok\"")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MusicPage.this,
                                    new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(MusicPage.this,
                    new String[]{Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }

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
                    Thread.sleep(0);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                int result = record.read(data, 0, SAMPLE_RATE/4); // read 0.25 second at a time
                if(result == AudioRecord.ERROR_INVALID_OPERATION || result == AudioRecord.ERROR_BAD_VALUE)
                {
                    System.out.println("Recording error");
                    return;
                }
                else
                {
                    System.out.println("Read: " + Arrays.toString(data));
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

    int animPosition = 0;
    int songPosition = 0;
    final String[] sText = {"Song", "About", "Sheep"};
    public MyColors colorsSlideIN;
    public MyColors colorsFadeOut;
    MicrophoneInput microphoneInput;

    //Database configuration
    private FirebaseDatabase database;
    private DatabaseReference slideRef;
    private Boolean firstRead = true;

    //Background animation
    private SpringAnimation springAnim;
    private SpringAnimation springAnim2;
    private SpringAnimation springAnim3;
    private SpringAnimation springAnim4;
    private SpringAnimation springAnim5;
    private SpringAnimation springAnim6;
    private SpringAnimation springAnim7;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_page);

        final View backgroundView = findViewById(R.id.ellipse_5);
        loadBackgroundAnim(backgroundView);

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

        microphoneInput = new MicrophoneInput();
        requestMicrophonePermission();

        colorsSlideIN = new MyColors(Color.parseColor("#FFFFFFFF"), Color.parseColor("#00D2003C"));
        colorsFadeOut = new MyColors(Color.parseColor("#00D2003C"), Color.parseColor("#FFFFFFFF"));

        colorsSlideIN.setSlideColors(0);

        linearLayout = (LinearLayout)findViewById(R.id.linearLayoutText);

        textView = new TextView(this);
        textView.setText(sText[0]);
        textView.setTextSize(40);

        Shader textShader;
        textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                colorsSlideIN.getColors(), null, Shader.TileMode.REPEAT);

        textView.getPaint().setShader(textShader);

        linearLayout.addView(textView);
        linearLayout.bringToFront();

        //microphoneInput.run();

        fadeIN();
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
        textView.setText(sText[songPosition]);
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
                else animator.cancel();
            }
        });
        animator.setDuration(1000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setRepeatCount(-1);
        animator.start();
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
                else animator.cancel();
            }
        });
        animator.setDuration(5000);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    //Switch text line
    public void changeLine(int z) {
        if (sText.length > songPosition + 1 && z == 1)
            songPosition++;
        else if (z == -1 && songPosition > 0)
            songPosition--;
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

    public void animateBackground(short values[]) {
        values[0] = (short) (Math.random()*5);
        springAnim.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim2.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim3.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim4.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim5.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim6.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
        springAnim7.animateToFinalPosition(-(float)Math.toDegrees(2 * asin(values[0]))*2);
    }
}
