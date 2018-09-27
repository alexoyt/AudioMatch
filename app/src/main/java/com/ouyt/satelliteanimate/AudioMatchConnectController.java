package com.ouyt.satelliteanimate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.PointF;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *  通话连接过程动画控制器
 */
public class AudioMatchConnectController {

    private static final String TAG = "ConnectingController";

    private View mRootView;
    private FrameLayout mSatelliteContainer;
    private ImageView mSatellite1;
    private ImageView mSatellite2;
    private ImageView mSatellite3;
    private ImageView mOtherAvatar;
    private TextView mCountTimeView;
    private TextView mConnectingTipsView;
    private ImageView mAvatarRippleView;
    private ImageView mBackgroundLightView;

    private Handler handler;
    private int mCountTime = 3;
    private LinearInterpolator mLinearInterpolator;
    private DecelerateInterpolator mDecelerateInterpolator;
    private AccelerateInterpolator mAaccelerateInterpolator;
    private AccelerateDecelerateInterpolator mAccelerateDecelerateInterpolator;
    private List<ValueAnimator> mValueAnimatorList;
    private ValueAnimator mRippleAlphaAnimator;
    private ValueAnimator mRippleScaleAnimator;

    private AudioMatchCallingController mCallingController;

    /**
     * 依次执行倒计时动画
     */
    private Runnable mCountTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if(mCountTime > 0){
                mCountTimeView.setVisibility(View.VISIBLE);
                mCountTimeView.setText(String.valueOf(mCountTime));
                scaleAndAlphaAnima(mCountTimeView, 2f, 1f, mAccelerateDecelerateInterpolator, 0f, 1f, mLinearInterpolator, 320);
                handler.postDelayed(this, 1000);
            } else {
                stopAnimaAndHide();
                mCallingController.startCallingAnima();
            }
            mCountTime--;
        }
    };

    public AudioMatchConnectController(View rootView, Handler handler){
        mRootView = rootView;
        this.handler = handler;
        mLinearInterpolator = new LinearInterpolator();
        mAaccelerateInterpolator = new AccelerateInterpolator();
        mDecelerateInterpolator = new DecelerateInterpolator();
        mAccelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        mValueAnimatorList = new ArrayList<>();
        mCallingController = new AudioMatchCallingController(mRootView, handler);
        initView();
    }

    private void initView(){
        mSatelliteContainer = mRootView.findViewById(R.id.colorful_satellite_container);
        mSatellite1 = mSatelliteContainer.findViewById(R.id.satellite_1);
        mSatellite2 = mSatelliteContainer.findViewById(R.id.satellite_2);
        mSatellite3 = mSatelliteContainer.findViewById(R.id.satellite_3);
        mOtherAvatar = mRootView.findViewById(R.id.audio_match_other_avatar);
        mCountTimeView = mRootView.findViewById(R.id.audio_match_connecting_count_time);
        mConnectingTipsView = mRootView.findViewById(R.id.audio_match_connecting_tips);
        mAvatarRippleView = mRootView.findViewById(R.id.audio_match_avatar_ripple);
        mBackgroundLightView = mRootView.findViewById(R.id.audio_match_bg_light);
    }

    /**
     * 启动通话连接过程动画
     */
    public void startConnectingAnima(){
        mConnectingTipsView.setVisibility(View.VISIBLE);
        startAvatarAnima();
        startAvatarRippleAnima();
        startCountTimeAnima();
        startShowSatelliteAnima();
    }

    /**
     * 倒计时动画
     */
    private void startCountTimeAnima(){
        handler.postDelayed(mCountTimeRunnable, 400);
    }

    /**
     * 头像波纹动画
     */
    private void startAvatarRippleAnima(){
        mAvatarRippleView.setVisibility(View.VISIBLE);
        TypeEvaluator rippleAlphaEvaluator = new RippleTypeEvaluator();
        mRippleAlphaAnimator = ValueAnimator.ofObject(rippleAlphaEvaluator, 0f);
        mRippleAlphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float alpha = (float)animation.getAnimatedValue();
                mAvatarRippleView.setAlpha(alpha);
                mAvatarRippleView.invalidate();
            }
        });
        mRippleAlphaAnimator.setDuration(1440);
        mRippleAlphaAnimator.setRepeatCount(-1);
        mRippleAlphaAnimator.setInterpolator(mLinearInterpolator);
        mRippleAlphaAnimator.start();

        mRippleScaleAnimator = ValueAnimator.ofFloat(0f ,1f);
        mRippleScaleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float scale = animation.getAnimatedFraction();
                mAvatarRippleView.setScaleX(scale);
                mAvatarRippleView.setScaleY(scale);
            }
        });
        mRippleScaleAnimator.setDuration(1440);
        mRippleScaleAnimator.setRepeatCount(-1);
        mRippleScaleAnimator.setInterpolator(mAccelerateDecelerateInterpolator);
        mRippleScaleAnimator.start();
    }

    /**
     * 头像透明度变化动画
     */
    private void startAvatarAnima(){
        mOtherAvatar.setVisibility(View.VISIBLE);
        scaleAndAlphaAnima(mOtherAvatar, 0f, 1f, mDecelerateInterpolator, 1f, 1f, mLinearInterpolator, 600);
    }

    /**
     * 初始三个星球的缩放和透明度变化动画
     */
    private void startShowSatelliteAnima(){
        mSatelliteContainer.setVisibility(View.VISIBLE);
        scaleAndAlphaAnima(mSatelliteContainer, 1.9f, 1f, mDecelerateInterpolator, 1f, 1f, mLinearInterpolator, 680);
        scaleAndAlphaAnima(mSatellite1, 1f, 1f, mDecelerateInterpolator, 0f, 1f, mLinearInterpolator, 680);
        scaleAndAlphaAnima(mSatellite2, 1f, 1f, mDecelerateInterpolator, 0f, 1f, mLinearInterpolator, 680);
        scaleAndAlphaAnima(mSatellite3, 1f, 1f, mDecelerateInterpolator, 0f, 1f, mLinearInterpolator, 680);

        Animator.AnimatorListener animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                startAllSatelliteAnima();
            }
        };
        startSatelliteAnima(false, animatorListener, mSatellite1, dp2px(104.0f), 680, 163, 33);
        startSatelliteAnima(false, null, mSatellite2, dp2px(107.5f), 680, -118, 33);
        startSatelliteAnima(false, null, mSatellite3, dp2px(109.5f), 680, 57, 33);
    }

    /**
     * 执行单个view的scale和alpha动画
     * @param view  执行动画的view
     * @param startScale    起始大小
     * @param endScale      结束大小
     * @param scaleInter    大小变化插值器
     * @param startAlpha    起始透明度
     * @param endAlpha      结束透明度
     * @param alphaInter    透明度插值器
     * @param duration      动画时长
     */
    private void scaleAndAlphaAnima(View view, float startScale, float endScale, Interpolator scaleInter, float startAlpha, float endAlpha, Interpolator alphaInter, int duration){
        if(startScale != endScale){
            view.setScaleX(startScale);
            view.setScaleY(startScale);
            view.animate().scaleX(endScale).scaleY(endScale).setDuration(duration).setInterpolator(scaleInter).start();
        }
        if(startAlpha != endAlpha){
            view.setAlpha(startAlpha);
            view.animate().alpha(endAlpha).setDuration(duration).setInterpolator(alphaInter).start();
        }
    }

    /**
     * 启动三个星球旋转的动画
     */
    private void startAllSatelliteAnima(){
        startSatelliteAnima(true, null, mSatellite1, dp2px(104.0f), 26000, -164, 360);
        startSatelliteAnima(true, null, mSatellite2, dp2px(107.5f), 19000, -85, 360);
        startSatelliteAnima(true, null, mSatellite3, dp2px(109.5f), 25000, 90, 360);
    }

    /**
     * 单个星球圆形轨迹旋转动画
     * @param repeat    动画是否重复
     * @param animatorListener  动画监听器
     * @param view  执行动画的view
     * @param radius    圆的半径
     * @param duration  动画时长
     * @param startAngle    起始角度
     * @param totalAngle    总运动角度
     */
    private void startSatelliteAnima(boolean repeat, Animator.AnimatorListener animatorListener, final View view, float radius, long duration, float startAngle, float totalAngle){
        TypeEvaluator circleTypeEvaluator = new CircleTypeEvaluator(radius, startAngle, totalAngle);
        ValueAnimator valueAnimator = ValueAnimator.ofObject(circleTypeEvaluator, new PointF());
        final float relativeDotx = mSatelliteContainer.getWidth() / 2;
        final float relativeDoty = mSatelliteContainer.getHeight() / 2;
        final float offsetX = view.getWidth() / 2;
        final float offsetY = view.getHeight() / 2;
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF roundPoint = (PointF) animation.getAnimatedValue();
                float originX = roundPoint.x;
                float originY = roundPoint.y;
                view.setX(originX + relativeDotx - offsetX);
                view.setY(originY + relativeDoty - offsetY);
                view.invalidate();
            }
        });
        if(animatorListener != null){
            valueAnimator.addListener(animatorListener);
        }
        valueAnimator.setDuration(duration);
        valueAnimator.setInterpolator(mLinearInterpolator);
        valueAnimator.setRepeatCount(repeat ? -1 : 0);
        valueAnimator.start();
        mValueAnimatorList.add(valueAnimator);
    }

    /**
     * 停止通话连接过程中的所有动画，并且隐藏
     */
    public void stopAnimaAndHide(){
        mRippleAlphaAnimator.cancel();
        mRippleScaleAnimator.cancel();
        mBackgroundLightView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mCountTimeView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mConnectingTipsView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        mAvatarRippleView.animate().alpha(0f).setDuration(280).setInterpolator(mLinearInterpolator).start();
        scaleAndAlphaAnima(mSatelliteContainer, 1f, 1.6f, mAccelerateDecelerateInterpolator, 1f, 0f ,mLinearInterpolator, 200);
        for(ValueAnimator valueAnimator : mValueAnimatorList){
            if(valueAnimator != null && valueAnimator.isRunning()){
                valueAnimator.cancel();
            }
        }
    }

    /**
     * 计算通话连接过程，星球运动的圆形轨迹
     */
    private class CircleTypeEvaluator implements TypeEvaluator<PointF>{

        private float radius;
        private float startAngle;
        private float totalAngle;

        /** 圆标准公式(x^2 + y^2) / r^2 = 1，其中 x = r * cos(θ), y = r * sin(θ)
         * @param radius    圆的半径
         * @param startAngle    计算起始角度
         * @param totalAngle    总运动角度值
         */
        private CircleTypeEvaluator(float radius, float startAngle, float totalAngle){
            this.radius = radius;
            this.startAngle = startAngle;
            this.totalAngle = totalAngle;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            //圆标准公式(x^2+y^2)/r^2=1
            fraction = fraction * totalAngle / 360f;
            PointF p = new PointF();
            p.x = (float)(radius * Math.cos(fraction * 2 * Math.PI + Math.PI / 2 + startAngle * Math.PI / 180));
            p.y = (float)(-radius * Math.sin(fraction * 2 * Math.PI + Math.PI / 2 + startAngle * Math.PI / 180));
            return p;
        }
    }

    /**
     * 计算通话连接过程波纹alpha变化值
     */
    private class RippleTypeEvaluator implements TypeEvaluator<Float>{
        /**
         *  波纹alpha变化值曲线：
         *  y = 0。6               x <= 480
         *  y = - 0.6 * x / 960 + 0.9      480 < x <= 1440
         */
        @Override
        public Float evaluate(float fraction, Float startValue, Float endValue) {
            fraction = fraction * 1440;
            if(fraction <= 480){
                return 0.6f;
            } else {
                return -0.6f / 960f * fraction + 0.9f;
            }
        }
    }

    private float dp2px(float dpValue){
        float scale = mRootView.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
