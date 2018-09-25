package com.ouyt.satelliteanimate;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;


public class AudioMatchSatelliteController {

    private static final String TAG = "AudioMatchSatelliteController";
    public static final double LEAN_ANGLE = 17 * Math.PI / 180;

    private View mRootView;

    private ImageView mSatellite1;
    private ImageView mSatellite2;
    private ImageView mSatellite3;
    private ImageView mSatellite4;
    private ImageView mSatellite5;
    private ImageView mSatellite6;
    private ImageView mSatellite7;
    private ImageView mSatellite8;

    public AudioMatchSatelliteController(View rootView){
        mRootView = rootView;
        initView();
    }

    private void initView(){
        mSatellite1 = mRootView.findViewById(R.id.satellite_1);
        mSatellite2 = mRootView.findViewById(R.id.satellite_2);
        mSatellite3 = mRootView.findViewById(R.id.satellite_3);
        mSatellite4 = mRootView.findViewById(R.id.satellite_4);
        mSatellite5 = mRootView.findViewById(R.id.satellite_5);
        mSatellite6 = mRootView.findViewById(R.id.satellite_6);
        mSatellite7 = mRootView.findViewById(R.id.satellite_7);
        mSatellite8 = mRootView.findViewById(R.id.satellite_8);
    }

    public void startAllTrackAnim(){
        int a = mRootView.getWidth() / 2;
        int b = mRootView.getHeight() / 2;
        startTrackAnim(mSatellite1, a, b, 36000, 0.7f );
        startTrackAnim(mSatellite2,a*0.9f, b*0.9f, 37000, 0.95f );
        startTrackAnim(mSatellite3, a, b, 30000, 0.125f);
        startTrackAnim(mSatellite4, a, b, 33000, 0.5f );
        startTrackAnim(mSatellite5, a*0.9f, b*0.9f, 31000, 0.75f);
        startTrackAnim(mSatellite6, a*0.9f, b*0.9f, 26000, 0.15f);
        startTrackAnim(mSatellite7, a*0.6f, b*0.6f, 33000, 0.30f );
        startTrackAnim(mSatellite8, a*0.4f, b*0.4f, 30000, 0.85f );
    }

    public void startTrackAnim(final View view, float a, float b, int duration, float startFraction) {
        TypeEvaluator<PointF> evaluator = new OvalTypeEvaluator(a, b, startFraction);
        ValueAnimator anim = ValueAnimator.ofObject(evaluator, new PointF());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //动画变化时，获取到当前小球的位置，重画界面
                PointF roundPoint = (PointF) animation.getAnimatedValue();
                //Log.i(TAG,"roundPoint="+roundPoint);
                float originX = roundPoint.x;
                float originY = roundPoint.y;
                view.setX((float)((originX * Math.cos(LEAN_ANGLE) - originY * Math.sin(LEAN_ANGLE)) + mRootView.getX() + mRootView.getWidth() / 2) - view.getWidth() / 2);
                view.setY((float)((originX * Math.sin(LEAN_ANGLE) + originY * Math.cos(LEAN_ANGLE)) + mRootView.getY() + mRootView.getHeight() / 2) - view.getHeight() / 2);
                view.invalidate();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(-1);
        anim.start();
    }


    private class OvalTypeEvaluator implements TypeEvaluator<PointF> {

        private float a;//椭圆长半轴
        private float b;//椭圆短半轴
        private float startFraction;//椭圆初始偏移

        /**
         *
         * @param a 椭圆长半轴
         * @param b 椭圆短半轴
         */
        public OvalTypeEvaluator(float a,float b, float startFraction) {
            this.a = a;
            this.b = b;
            this.startFraction = startFraction;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //椭圆标准公式x^2/a^2+y^2/b^2=1,其中a为椭圆长轴，b为椭圆短轴；
            PointF p = new PointF();
            fraction = fraction + startFraction;
            if(fraction > 1){
                fraction = fraction - 1;
            }
            p.x = (float)(a * Math.cos(fraction * 2 * Math.PI));
            p.y = (float)(- b * Math.sin(fraction * 2 * Math.PI));
            return p;
        }
    }


}
