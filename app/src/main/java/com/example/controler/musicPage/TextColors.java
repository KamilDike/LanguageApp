package com.example.controler.musicPage;

import android.graphics.Color;

public class TextColors {
    private int background;
    private int foreground;
    private int[] shaders;
    private int[] colors;

    public TextColors(int foreground, int background) {
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
