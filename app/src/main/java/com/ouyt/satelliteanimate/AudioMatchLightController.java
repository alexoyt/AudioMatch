package com.ouyt.satelliteanimate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class AudioMatchLightController {

    private Handler handler;

    private static final String TAG = "AudioMatchLightController";

    private View mRootView;

    private ImageView mLightView;
    private ImageView mUfoView;
    private ImageView mRippleInsideView;
    private ImageView mRippleOutside1;
    private ImageView mRippleOutside2;
    private ImageView mRippleOutside3;
    private int mCurrentOutside;

    private Runnable mRippleOutsideRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrentOutside = mCurrentOutside % 3;
            if(mCurrentOutside == 0){
                startOutsideRippleAnima(mRippleOutside1);
            } else if(mCurrentOutside == 1){
                startOutsideRippleAnima(mRippleOutside2);
            } else {
                startOutsideRippleAnima(mRippleOutside3);
            }
            mCurrentOutside++;
            //ThreadUtils.runOnUiThread(this, 520);
            handler.postDelayed(this, 520);
        }
    };

    public AudioMatchLightController(View rootView, Handler handler){
        mRootView = rootView;
        this.handler = handler;
        initView();
    }

    private void initView(){
        mLightView = mRootView.findViewById(R.id.audio_match_light);
        mUfoView = mRootView.findViewById(R.id.audio_match_ufo);
        mRippleInsideView = mRootView.findViewById(R.id.audio_match_ripple_inside);
        mRippleOutside1 = mRootView.findViewById(R.id.audio_match_ripple_outside_1);
        mRippleOutside2 = mRootView.findViewById(R.id.audio_match_ripple_outside_2);
        mRippleOutside3 = mRootView.findViewById(R.id.audio_match_ripple_outside_3);
    }

    public void startAnima(){
        startUFOAnima();
        startBeforeMacthLightAnima();
        startRippleAnima();
    }

    private void startUFOAnima(){
        UFOEvaluator ufoEvaluator = new UFOEvaluator();
        ValueAnimator ufoAnimator = ValueAnimator.ofObject(ufoEvaluator, new PointF());
        ufoAnimator.setDuration(2000);
        final float initUFOX = mUfoView.getX();
        final float initUFOY = mUfoView.getY();
        final float initLightX = mLightView.getX();
        final float initLightY = mLightView.getY();
        ufoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF position = (PointF)valueAnimator.getAnimatedValue();
                mUfoView.setX(initUFOX + position.x);
                mLightView.setX(initLightX + position.x);
                mUfoView.setY(initUFOY + position.y);
                mLightView.setY(initLightY + position.y);
                mUfoView.invalidate();
                mLightView.invalidate();
            }
        });
        ufoAnimator.setInterpolator(new LinearInterpolator());
        ufoAnimator.setRepeatCount(-1);
        ufoAnimator.start();
    }

    private void startBeforeMacthLightAnima(){
        BeforeMatchLightEvaluator lightEvaluator = new BeforeMatchLightEvaluator();
        ValueAnimator lightAlphaAnimator = ValueAnimator.ofObject(lightEvaluator, 0f);
        lightAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                mLightView.setAlpha(alpha);
            }
        });
        lightAlphaAnimator.setDuration(1400);
        lightAlphaAnimator.setRepeatCount(-1);
        lightAlphaAnimator.setInterpolator(new LinearInterpolator());
        lightAlphaAnimator.start();

    }

    private class UFOEvaluator implements TypeEvaluator<PointF> {

        @Override
        public PointF evaluate(float fraction, PointF aFloat, PointF t1) {
            fraction = fraction * 2000;
            PointF p = new PointF();
            p.x = 4.5f + (float)(4.5f * Math.sin(Math.PI * fraction / 1000 - Math.PI / 2));
            p.y = 11f + (float)(11f * Math.sin(Math.PI * fraction / 1000 - Math.PI / 2));
            return p;
        }
    }

    private class BeforeMatchLightEvaluator implements TypeEvaluator<Float> {


        @Override
        public Float evaluate(float fraction, Float aFloat, Float t1) {
            fraction = fraction * 1400;
            if(fraction <= 400){
                return 0.1f / 400 * fraction;
            } else if(fraction <= 800){
                return - 0.2f / 800 * fraction + 0.2f;
            } else {
                return 0f;
            }
        }
    }

    public void startRippleAnima(){
        startMatchLightAnima();
        startInsideRippleAnima();
        //ThreadUtils.runOnUiThread(mRippleOutsideRunnable);
        handler.post(mRippleOutsideRunnable);
    }

    private void startMatchLightAnima(){
        mLightView.setAlpha(0f);
        MatchLightEvaluator matchLightEvaluator = new MatchLightEvaluator();
        final ValueAnimator valueAnimator = ValueAnimator.ofObject(matchLightEvaluator, 0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                mLightView.setAlpha(alpha);
                mLightView.invalidate();
            }
        });
        valueAnimator.setDuration(1200);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        mLightView.animate()
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation, boolean isReverse) {
                        valueAnimator.start();
                    }
                });
    }

    private void startInsideRippleAnima(){
        mRippleInsideView.setVisibility(View.VISIBLE);
        mRippleInsideView.setAlpha(0f);
        mRippleInsideView.animate().alpha(1f).setDuration(1360).setInterpolator(new LinearInterpolator()).start();
    }

    private void startOutsideRippleAnima(final View view){
        view.setVisibility(View.VISIBLE);
        view.setScaleX(0f);
        view.setScaleY(0f);
        view.animate().scaleX(1.2f).scaleY(1.2f).setDuration(1360).setInterpolator(new LinearInterpolator()).start();

        OutsideRippleEvaluator outsideRippleEvaluator = new OutsideRippleEvaluator();
        ValueAnimator valueAnimator = ValueAnimator.ofObject(outsideRippleEvaluator, 0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                view.setAlpha(alpha);
                view.invalidate();
            }
        });
        valueAnimator.setDuration(1360);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private class MatchLightEvaluator implements TypeEvaluator<Float> {

        @Override
        public Float evaluate(float fraction, Float aFloat, Float t1) {
            fraction = fraction * 1200;
            if(fraction <= 600){
                return - 1f / 1200f * fraction + 1;
            } else {
                return  1f / 1200f * fraction;
            }
        }
    }

    private class OutsideRippleEvaluator implements TypeEvaluator<Float> {

        @Override
        public Float evaluate(float fraction, Float aFloat, Float t1) {
            fraction = fraction * 1360;
            if(fraction <= 480){
                return 0.8f * fraction / 480f;
            } else {
                return - 0.8f / 880 * fraction + 68f/55f;
            }
        }

    }

}
