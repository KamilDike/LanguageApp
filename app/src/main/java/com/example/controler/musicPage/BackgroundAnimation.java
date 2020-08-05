package com.example.controler.musicPage;

import android.view.View;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;

public class BackgroundAnimation {
    SpringAnimation springAnimScaleX;
    SpringAnimation springAnimScaleY;

    public BackgroundAnimation(View view) {
        loadBackgroundAnim(view);
    }

    /**
     * Function loads SpringAnimation Objects.
     * @param view current View.
     */
    public void loadBackgroundAnim(View view) {
        springAnimScaleX = new SpringAnimation(view, DynamicAnimation.SCALE_X,0);
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
}
