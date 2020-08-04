package com.example.controler.musicPage;

import android.os.Handler;
import android.os.Message;

/**
 * Class responsible for animating text.
 */
class TextAnimation {
    boolean running = false;
    CurrentSongValues currentSongValues;

    //Handler for ui, main thread.
    Handler backgroundAnimationHandler;
    Handler textAnimationHandler;

    public TextAnimation(Handler backgroundAnimationHandler, Handler textAnimationHandler,
                         CurrentSongValues currentSongValues) {
        this.backgroundAnimationHandler = backgroundAnimationHandler;
        this.textAnimationHandler = textAnimationHandler;
        this.currentSongValues = currentSongValues;
    }

    void run() {
        running = true;
        currentSongValues.animPosition = 0;

        new Thread() {
            @Override
            public void run() {
                startAnimation();
            }
        }.start();
    }

    private void startAnimation() {
        sleep(currentSongValues.songAnimationDelay[currentSongValues.songPosition]);
        int type; // 1 - fadeIn, 0 - fadeOut.
        while (running) {
            System.out.println("SongPos: " + currentSongValues.songPosition + "\nAnimPos: " +
                    currentSongValues.animPosition);
            if (currentSongValues.animPosition < 110 && currentSongValues.songPosition < 14) {
                type = 1;
                currentSongValues.animPosition++;
                Message message = textAnimationHandler.obtainMessage(type);
                message.sendToTarget();
                sleep(10);
            } else if (currentSongValues.songPosition < 14) {
                currentSongValues.songPosition++;
                currentSongValues.animPosition = 0;
                sleep(currentSongValues.songAnimationDelay[currentSongValues.songPosition]);
            } else {
                currentSongValues.animPosition = 0;
                type = 0;
                MicrophoneInput.stopRecording();
                //mediaPlayer.release();
                //mediaPlayer = null;
                Message message2 = backgroundAnimationHandler.obtainMessage(0);
                message2.sendToTarget();
                running = false;

                for (int i = 0; i < 110; i++) {
                    currentSongValues.animPosition++;
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

    public void stop() {
        running = false;
    }
}
