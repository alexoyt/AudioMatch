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

    private static final String TAG = "LightController";

    private View mRootView;

    private ImageView mLightView;
    private ImageView mUfoView;
    private ImageView mRippleLightView;
    private ImageView mRippleInsideView;
    private ImageView mRippleOutside1;
    private ImageView mRippleOutside2;
    private ImageView mRippleOutside3;
    private ImageView mBackgroundLightView;
    private int mCurrentOutside;

    private ValueAnimator mUfoAnimator;
    private ValueAnimator mBeforeMatchLightAnimator;
    private ValueAnimator mMatchingLightAnimator;
    private LinearInterpolator mLinearInterpolator;

    private boolean mRippleOutsideRunning;

    private Runnable mRippleOutsideRunnable = new Runnable() {
        @Override
        public void run() {
            mCurrentOutside = mCurrentOutside % 4;
            if(mCurrentOutside == 0){
                startOutsideRippleAnima(mRippleOutside1);
            } else if(mCurrentOutside == 1){
                startOutsideRippleAnima(mRippleOutside2);
            } else if(mCurrentOutside == 2) {
                startOutsideRippleAnima(mRippleOutside3);
            }
            mCurrentOutside++;
            //ThreadUtils.runOnUiThread(this, 520);
            if(mRippleOutsideRunning){
                handler.postDelayed(this, 520);
            }
        }
    };

    public AudioMatchLightController(View rootView, Handler handler){
        mRootView = rootView;
        this.handler = handler;
        mLinearInterpolator = new LinearInterpolator();
        initView();
    }

    private void initView(){
        mLightView = mRootView.findViewById(R.id.audio_match_light);
        mUfoView = mRootView.findViewById(R.id.audio_match_ufo);
        mRippleLightView = mRootView.findViewById(R.id.audio_match_ripple_light);
        mRippleInsideView = mRootView.findViewById(R.id.audio_match_ripple_inside);
        mBackgroundLightView = mRootView.findViewById(R.id.audio_match_bg_light);
        mRippleOutside1 = mRootView.findViewById(R.id.audio_match_ripple_outside_1);
        mRippleOutside2 = mRootView.findViewById(R.id.audio_match_ripple_outside_2);
        mRippleOutside3 = mRootView.findViewById(R.id.audio_match_ripple_outside_3);
    }

    public void startBeforMatchAnima(){
        startUFOAnima();
        startBeforeMatchLightAnima();
    }

    public void startMatchingAnima(){
        mBeforeMatchLightAnimator.cancel();
        startMatchingLightAnima();
        startInsideRippleAnima();
        //ThreadUtils.runOnUiThread(mRippleOutsideRunnable);
        mRippleOutsideRunning = true;
        handler.post(mRippleOutsideRunnable);
    }

    public void stopAllAnimaAndHide(){
        mUfoAnimator.cancel();
        mBeforeMatchLightAnimator.cancel();
        mMatchingLightAnimator.cancel();
        mRippleOutsideRunning = false;

        mRippleLightView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mRippleInsideView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mRippleOutside1.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mRippleOutside2.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mRippleOutside3.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        startUFOLeaveAnima();
    }

    private void startUFOLeaveAnima(){
        UFOEvaluator ufoLeaveEvaluator = new UFOEvaluator(78, 155f, 78, 155f, 640, 0.5f);
        ValueAnimator ufoLeaveAnimator = ValueAnimator.ofObject(ufoLeaveEvaluator, new PointF());
        ufoLeaveAnimator.setDuration(320);
        final float initUFOX = mUfoView.getX();
        final float initUFOY = mUfoView.getY();
        final float initLightX = mLightView.getX();
        final float initLightY = mLightView.getY();
        ufoLeaveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF position = (PointF)valueAnimator.getAnimatedValue();
                mUfoView.setX(initUFOX + position.x);
                mLightView.setX(initLightX + position.x);
                mUfoView.setY(initUFOY - position.y);
                mLightView.setY(initLightY - position.y);
                mUfoView.setAlpha(1f - valueAnimator.getAnimatedFraction());
                mLightView.setAlpha(1f - valueAnimator.getAnimatedFraction());
                mUfoView.invalidate();
                mLightView.invalidate();
            }
        });
        ufoLeaveAnimator.setInterpolator(mLinearInterpolator);
        ufoLeaveAnimator.start();
    }

    private void startUFOAnima(){
        UFOEvaluator ufoEvaluator = new UFOEvaluator(4.5f, 11f, 4.5f, 11f, 2000, 1f);
        mUfoAnimator = ValueAnimator.ofObject(ufoEvaluator, new PointF());
        mUfoAnimator.setDuration(2000);
        final float initUFOX = mUfoView.getX();
        final float initUFOY = mUfoView.getY();
        final float initLightX = mLightView.getX();
        final float initLightY = mLightView.getY();
        mUfoAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF position = (PointF)valueAnimator.getAnimatedValue();
                mUfoView.setX(initUFOX + position.x);
                mLightView.setX(initLightX + position.x);
                mUfoView.setY(initUFOY - position.y);
                mLightView.setY(initLightY - position.y);
                mUfoView.invalidate();
                mLightView.invalidate();
            }
        });
        mUfoAnimator.setInterpolator(mLinearInterpolator);
        mUfoAnimator.setRepeatCount(-1);
        mUfoAnimator.start();
    }

    private void startBeforeMatchLightAnima(){
        BeforeMatchLightEvaluator lightEvaluator = new BeforeMatchLightEvaluator();
        mBeforeMatchLightAnimator = ValueAnimator.ofObject(lightEvaluator, 0f);
        mBeforeMatchLightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                mLightView.setAlpha(alpha);
                mRippleLightView.setAlpha(1.5f * alpha + 0.25f);
            }
        });
        mBeforeMatchLightAnimator.setDuration(1400);
        mBeforeMatchLightAnimator.setRepeatCount(-1);
        mBeforeMatchLightAnimator.setInterpolator(mLinearInterpolator);
        mBeforeMatchLightAnimator.start();
    }

    private void startMatchingLightAnima(){
        MatchLightEvaluator matchLightEvaluator = new MatchLightEvaluator();
        mMatchingLightAnimator = ValueAnimator.ofObject(matchLightEvaluator, 0f);
        mMatchingLightAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float alpha = (float)valueAnimator.getAnimatedValue();
                mLightView.setAlpha(alpha);
                mLightView.invalidate();
            }
        });
        mMatchingLightAnimator.setDuration(1200);
        mMatchingLightAnimator.setRepeatCount(-1);
        mMatchingLightAnimator.setInterpolator(mLinearInterpolator);
        mLightView.animate()
                .alpha(1f)
                .setDuration(800)
                .setInterpolator(mLinearInterpolator)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        mMatchingLightAnimator.start();
                    }
                });
        mRippleLightView.animate().alpha(0.8f).setDuration(800).setInterpolator(mLinearInterpolator).start();
        mBackgroundLightView.animate().alpha(0.8f).setDuration(800).setInterpolator(mLinearInterpolator).start();
    }

    private void startInsideRippleAnima(){
        mRippleInsideView.setVisibility(View.VISIBLE);
        mRippleInsideView.setAlpha(0f);
        mRippleInsideView.animate().alpha(1f).setDuration(1360).setInterpolator(mLinearInterpolator).start();
    }

    private void startOutsideRippleAnima(final View view){
        view.setVisibility(View.VISIBLE);
        OutsideRippleEvaluator outsideRippleEvaluator = new OutsideRippleEvaluator();
        ValueAnimator valueAnimator = ValueAnimator.ofObject(outsideRippleEvaluator, 0f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                if(!mRippleOutsideRunning){
                    return;
                }
                float alpha = (float)valueAnimator.getAnimatedValue();
                view.setAlpha(alpha);
                view.setScaleX(1.2f * valueAnimator.getAnimatedFraction());
                view.setScaleY(1.2f * valueAnimator.getAnimatedFraction());
                view.invalidate();
            }
        });
        valueAnimator.setDuration(1360);
        valueAnimator.setInterpolator(mLinearInterpolator);
        valueAnimator.start();
    }


    private class UFOEvaluator implements TypeEvaluator<PointF> {
        private float Ax;
        private float Ay;
        private float Kx;
        private float Ky;
        private float time;
        private float rate;
        /**
         *  UFO做正弦函数运动，表达式 K * A * sin(2 * PI * x / time - PI / 2)
         * @param Ax        x正弦函数的振幅
         * @param Ay        y正弦函数的振幅
         * @param Kx        x正弦函数的偏距
         * @param Ky        y正弦函数的偏距
         * @param time      弦函数的周期
         * @param rate      运行周期
         */
        private UFOEvaluator(float Ax, float Ay, float Kx, float Ky, float time, float rate){
            this.Ax = Ax;
            this.Ay = Ay;
            this.Kx = Kx;
            this.Ky = Ky;
            this.time = time;
            this.rate = rate;
        }

        @Override
        public PointF evaluate(float fraction, PointF aFloat, PointF t1) {
            fraction = fraction * time * rate;
            PointF p = new PointF();
            p.x = Kx + (float)(Ax * Math.sin(2 * Math.PI * fraction / time - Math.PI / 2));
            p.y = Ky + (float)(Ay * Math.sin(2 * Math.PI * fraction / time - Math.PI / 2));
            return p;
        }
    }

    private class BeforeMatchLightEvaluator implements TypeEvaluator<Float> {

        /**
         *  开始匹配前探照灯透明曲线：
         *  y = 0.1 * x / 400               x <= 400
         *  y = - 0.2 * x / 800 + 0.2      400 < x <= 800
         *  y = 0                          800 <= x <= 1400
         */
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

    private class MatchLightEvaluator implements TypeEvaluator<Float> {

        /**
         *  匹配过程探照灯透明度曲线：
         *  y = -1 * x / 1200 + 1              x <= 600
         *  y = 1 * x / 1200                   600 < x <= 1200
         */
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
        /**
         *  搜索波纹单圈放大曲线：
         *  y = 0.8 * x / 480              x <= 480
         *  y = -0.8 * x / 880 + 68 / 55                  480 < x <= 1360
         */
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
