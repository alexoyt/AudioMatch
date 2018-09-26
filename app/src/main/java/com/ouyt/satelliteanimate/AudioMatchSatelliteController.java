package com.ouyt.satelliteanimate;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class AudioMatchSatelliteController {

    private static final String TAG = "AudioMatchSatelliteController";
    private static final double LEAN_ANGLE = 17 * Math.PI / 180;

    private View mRootView;

    private ImageView mSatellite1;
    private ImageView mSatellite2;
    private ImageView mSatellite3;
    private ImageView mSatellite4;
    private ImageView mSatellite5;
    private ImageView mSatellite6;
    private ImageView mSatellite7;
    private ImageView mSatellite8;

    private List<ValueAnimator> valueAnimatorList;

    public AudioMatchSatelliteController(View rootView){
        mRootView = rootView;
        valueAnimatorList = new ArrayList<>();
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
        startTrackAnim(mSatellite1,a * 1.0f,b * 1.0f, 36000, -123);
        startTrackAnim(mSatellite2,a * 0.9f,b * 0.9f, 37000, -11);
        startTrackAnim(mSatellite3,a * 1.0f,b * 1.0f, 30000, 31);
        startTrackAnim(mSatellite4,a * 1.0f,b * 1.0f, 33000, 180);
        startTrackAnim(mSatellite5,a * 0.9f,b * 0.9f, 31000, -97);
        startTrackAnim(mSatellite6,a * 0.9f,b * 0.9f, 26000, 37);
        startTrackAnim(mSatellite7,a * 0.6f,b * 0.6f, 33000, 107);
        startTrackAnim(mSatellite8,a * 0.4f,b * 0.4f, 30000, -33);
    }

    public void stopAllTrackAnim(){
        for(ValueAnimator valueAnimator : valueAnimatorList){
            if(valueAnimator != null){
                valueAnimator.cancel();
            }
        }
    }

    private void startTrackAnim(final View view, float a, float b, int duration, float startAngle) {
        TypeEvaluator<PointF> evaluator = new OvalTypeEvaluator(a, b, startAngle);
        ValueAnimator anim = ValueAnimator.ofObject(evaluator, new PointF());
        anim.setDuration(duration);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //动画变化时，获取到当前小球的位置，重画界面
                PointF roundPoint = (PointF) animation.getAnimatedValue();
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
        valueAnimatorList.add(anim);
    }

    private class OvalTypeEvaluator implements TypeEvaluator<PointF> {

        private float a;//椭圆长半轴
        private float b;//椭圆短半轴
        private float startAngle;//椭圆初始偏移

        /**
         *
         * @param a 椭圆长半轴
         * @param b 椭圆短半轴
         */
        public OvalTypeEvaluator(float a,float b, float startAngle) {
            this.a = a;
            this.b = b;
            this.startAngle = startAngle;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //椭圆标准公式x^2/a^2+y^2/b^2=1,其中a为椭圆长轴，b为椭圆短轴；
            PointF p = new PointF();
            p.x = (float)(a * Math.cos(fraction * 2 * Math.PI + startAngle * Math.PI / 180));
            p.y = (float)(- b * Math.sin(fraction * 2 * Math.PI + startAngle * Math.PI / 180));
            return p;
        }
    }


}
