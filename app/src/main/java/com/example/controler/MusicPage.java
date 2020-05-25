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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        textView = new TextView(this);
        textView.setText(sText[0]);
        textView.setTextSize(40);

        Shader textShader;
        textShader = new LinearGradient(0,0,textView.getPaint().measureText((String)textView.getText()),textView.getTextSize(),
                colorsSlideIN.getColors(), null, Shader.TileMode.REPEAT);

        textView.getPaint().setShader(textShader);

        linearLayout.addView(textView);
        linearLayout.bringToFront();

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
}
