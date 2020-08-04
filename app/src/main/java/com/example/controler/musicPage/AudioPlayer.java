package com.example.controler.musicPage;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;

public class AudioPlayer {
    MediaPlayer mediaPlayer = new MediaPlayer();
    AssetFileDescriptor assetFileDescriptor;
    Context context;

    public AudioPlayer(Context context) {
        this.context = context;
        loadSong(1);
    }

    public void loadSong(int i) {
        if (i == 1) {
            try {
                assetFileDescriptor = context.getAssets().openFd("Audio.mp3");
                mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                        assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startSong() {
        mediaPlayer.start();
    }
}
