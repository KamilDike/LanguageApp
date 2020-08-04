package com.example.controler.musicPage;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.Message;

/**
 * Class responsible for reading input from microphone.
 */
public class MicrophoneInput {
    private static final int SAMPLE_RATE = 44100;
    public static boolean recording;
    private AudioRecord record;
    private short[] data;
    Handler backgroundAnimationHandler;

    public MicrophoneInput(Handler backgroundAnimationHandler) {
        this.backgroundAnimationHandler = backgroundAnimationHandler;
        recording = false;
    }

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

    public static void stopRecording() { recording = false; }

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
}
