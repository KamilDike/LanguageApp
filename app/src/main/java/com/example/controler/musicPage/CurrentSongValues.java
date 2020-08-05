package com.example.controler.musicPage;

public class CurrentSongValues {
    int animPosition = 0;
    int songPosition = 0;
    int[] songAnimationDelay = new int[20];
    String[] songText = new String[20];

    public CurrentSongValues(int song) {
        loadSong(song);
    }

    private void loadSong(int song) {
        if (song == 1) {
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
