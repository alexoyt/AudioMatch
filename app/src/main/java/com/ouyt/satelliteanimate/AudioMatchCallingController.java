package com.ouyt.satelliteanimate;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class AudioMatchCallingController {

    private static final String TAG = "CallingController";

    private LinearInterpolator mLinearInterpolator;
    private DecelerateInterpolator mDecelerateInterpolator;

    private View mRootView;
    private ImageView mMyAvatarView;
    private ImageView mOtherAvatarView;
    private ImageView mIvLikeStatus;
    private ImageView mBigPlanetView;
    private ImageView mScatteredPlanetView;
    private ImageView mCloseView;
    private TextView mMyNameView;
    private TextView mMyCountryView;
    private TextView mOtherNameView;
    private TextView mOtherCountryView;
    private TextView mCountTime;
    private TextView mTvLikeStatus;

    private Handler handler;
    private boolean isShowMeteor = true;
    private boolean isLikeStatusAnimating;
    private int mCurrentMeteor;
    private int mLikeStatusPhase;
    private int mMatchSuccessHeartStatus;

    private ValueAnimator mHeartSendAnimator;

    /**
     * 循环显示匹配成功后，背景的流星动画
     */
    private Runnable mMeteorShowRunnable = new Runnable() {
        @Override
        public void run() {
            if(isShowMeteor){
                mCurrentMeteor = mCurrentMeteor % 4;
                if(mCurrentMeteor == 0){
                    createMeteorAndAnima(320, 580,158, 464, 1.0f, 2080, 520, 1440);
                    handler.postDelayed(this, 2220);
                } else if(mCurrentMeteor == 1){
                    createMeteorAndAnima(158, 630,27, 521, 0.66f, 2400, 800, 1720);
                    handler.postDelayed(this, 1760);
                } else if(mCurrentMeteor == 2){
                    createMeteorAndAnima(365, 492,299, 393, 0.52f, 2400, 800, 1720);
                    handler.postDelayed(this, 2960);
                } else {
                    createMeteorAndAnima(279, 645,148, 535, 0.66f, 2400, 800, 1720);
                    handler.postDelayed(this, 2680);
                }
                mCurrentMeteor++;
            }
        }
    };

    /**
     * 连续点击like按钮执行的动画
     */
    private Runnable mLikeContinuousClickRunnable = new Runnable() {
        @Override
        public void run() {
            if(mLikeStatusPhase == 0){
                mIvLikeStatus.animate().scaleX(0.93f).scaleY(0.93f).setInterpolator(mLinearInterpolator).setDuration(120).withEndAction(this).start();
            }else if(mLikeStatusPhase == 1){
                mIvLikeStatus.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(mLinearInterpolator).setDuration(120).withEndAction(this).start();
            } else {
                isLikeStatusAnimating = false;
            }
            mLikeStatusPhase++;
        }
    };

    /**
     * 初次点击like按钮执行的动画
     */
    private Runnable mLikeFirstClickRunnable = new Runnable() {
        @Override
        public void run() {
            if(mLikeStatusPhase == 0){
                mIvLikeStatus.animate().scaleX(0.93f).scaleY(0.93f).setInterpolator(mLinearInterpolator).setDuration(120).withEndAction(this).start();
            }else if(mLikeStatusPhase == 1){
                mIvLikeStatus.animate().scaleX(1.04f).scaleY(1.04f).setInterpolator(mLinearInterpolator).setDuration(120).withEndAction(this).start();
            } else if(mLikeStatusPhase == 2) {
                mIvLikeStatus.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(mLinearInterpolator).setDuration(120).withEndAction(this).start();
            } else {
                isLikeStatusAnimating = false;
            }
            mLikeStatusPhase++;
        }
    };


    /**
     * 循环显示匹配成功后爱心view
     */
    private Runnable mShowSuccessHeartRunnable = new Runnable() {
        @Override
        public void run() {
            mMatchSuccessHeartStatus = mMatchSuccessHeartStatus % 2;
            if(mMatchSuccessHeartStatus == 0){
                showMatchSuccessHeartView(true);
                handler.postDelayed(this, 300);
            } else {
                showMatchSuccessHeartView(false);
                handler.postDelayed(this, 2040);
            }
            mMatchSuccessHeartStatus++;
        }
    };

    public AudioMatchCallingController(View rootView, Handler handler){
        mRootView = rootView;
        this.handler = handler;
        mLinearInterpolator = new LinearInterpolator();
        mDecelerateInterpolator = new DecelerateInterpolator();
        initView();
    }

    private void initView(){
        mMyAvatarView = mRootView.findViewById(R.id.audio_match_my_avatar);
        mOtherAvatarView = mRootView.findViewById(R.id.audio_match_other_avatar);
        mMyNameView = mRootView.findViewById(R.id.audio_match_my_name);
        mMyCountryView = mRootView.findViewById(R.id.audio_match_my_country);
        mOtherNameView = mRootView.findViewById(R.id.audio_match_other_name);
        mOtherCountryView = mRootView.findViewById(R.id.audio_match_other_country);
        mCountTime = mRootView.findViewById(R.id.audio_match_calling_time);
        mTvLikeStatus = mRootView.findViewById(R.id.tv_audio_random_status);
        mIvLikeStatus = mRootView.findViewById(R.id.im_audio_random_status);
        mBigPlanetView = mRootView.findViewById(R.id.audio_match_big_planet);
        mScatteredPlanetView = mRootView.findViewById(R.id.audio_match_scattered_planet);
        mCloseView = mRootView.findViewById(R.id.audio_match_close);
    }

    public void stopCallingAnima(){
        mHeartSendAnimator.cancel();
    }

    /**
     * 匹配成功后启动动画
     */
    public void startCallingAnima(){

        initVisible();
        alphaAnima(mCloseView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mMyAvatarView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mOtherAvatarView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mMyNameView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mMyCountryView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mOtherNameView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mOtherCountryView, 0f, 1f, mLinearInterpolator, 280, 320, null);
        alphaAnima(mCountTime, 0f, 1f, mLinearInterpolator, 280, 320, null);

        alphaAnima(mScatteredPlanetView, 0f, 1f, mLinearInterpolator, 600, 0, null);
        scaleAnima(mIvLikeStatus, 0f ,1f, new DecelerateInterpolator(), 600, 0, null);
        scaleAnima(mTvLikeStatus, 0f ,1f, new DecelerateInterpolator(), 600, 0, null);

        avatarTranslateAnima();

        Animation animation = AnimationUtils.loadAnimation(mBigPlanetView.getContext(), R.anim.audio_match_big_planet);
        mBigPlanetView.startAnimation(animation);   //加载大星球旋转缩放变换动画

        handler.post(mMeteorShowRunnable);

        initTouchMeteorCreated();
        initLikeStatusAnima();
        //handler.post(mShowSuccessHeartRunnable);
    }

    /**
     * 创建配对成功后的爱心浮现动画
     * @param firstHeart    是否是第一个爱心
     */
    private void showMatchSuccessHeartView(boolean firstHeart){
        final ImageView heartView = new ImageView(mRootView.getContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        heartView.setLayoutParams(layoutParams);
        heartView.setImageResource(R.drawable.icon_audio_match_like_heart);
        final ViewGroup parentView = (ViewGroup) mMyAvatarView.getParent();
        parentView.addView(heartView);
        float startX = mMyAvatarView.getX() + mMyAvatarView.getWidth() / 2 + (mOtherAvatarView.getX() - mMyAvatarView.getX()) / 2 - dp2px(20);
        float startY = mMyAvatarView.getY() + mMyAvatarView.getHeight() / 2 - dp2px(32);
        heartView.setX(startX);
        heartView.setY(startY);
        float offsetX = firstHeart ? dp2px(-7) : dp2px(7);
        float offsetY = -dp2px(100);
        heartView.animate().translationXBy(offsetX).translationYBy(offsetY).setInterpolator(mLinearInterpolator).setDuration(1040).withEndAction(new Runnable() {
            @Override
            public void run() {
                parentView.removeView(heartView);
            }
        }).start();

        final float targetScale = firstHeart ? 1.0f : 0.85f;
        scaleAnima(heartView, 0f, targetScale, mLinearInterpolator, 360, 0, new Runnable() {
            @Override
            public void run() {
                scaleAnima(heartView, targetScale, 0f, mLinearInterpolator, 680, 0, null);
            }
        });
        alphaAnima(heartView, 0f, 1.0f, mLinearInterpolator, 160, 0, new Runnable() {
            @Override
            public void run() {
                scaleAnima(heartView, 1.0f, 0f, mLinearInterpolator, 880, 0, null);
            }
        });

    }

    /**
     * 初始化点击like的动画和事件
     */
    private void initLikeStatusAnima(){
        mIvLikeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLikeStatusAnimating){
                    mLikeStatusPhase = 0;
                    handler.post(mLikeContinuousClickRunnable);
                } else {
                    isLikeStatusAnimating = true;
                    mLikeStatusPhase = 0;
                    handler.post(mLikeFirstClickRunnable);
                }
                createHeartAndSend();
            }
        });
    }

    /**
     * 点击like创建爱心并且执行发送动画
     */
    private void createHeartAndSend(){
        final ImageView heartView = new ImageView(mRootView.getContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        heartView.setLayoutParams(layoutParams);
        heartView.setImageResource(R.drawable.icon_audio_match_like_heart);
        final ViewGroup parentView = (ViewGroup) mMyAvatarView.getParent();
        parentView.addView(heartView);
        final float startX = mMyAvatarView.getX() + mMyAvatarView.getWidth() / 2 - dp2px(20);
        final float startY = mMyAvatarView.getY() + mMyAvatarView.getHeight() / 2 - dp2px(16);
        final float endX = mOtherAvatarView.getX() + mOtherAvatarView.getWidth() / 2 - dp2px(20);
        final float endY = mOtherAvatarView.getY() + mOtherAvatarView.getHeight() / 2 - dp2px(16);
        final float middleX = startX + (endX - startX) / 2;
        final float middleY = endY - dp2px(56);

        mHeartSendAnimator = ValueAnimator.ofFloat(0, 1f);
        mHeartSendAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                //二阶贝塞尔曲线
                float x = (1 - fraction) * (1 - fraction) * startX + 2 * fraction * (1 - fraction) * middleX + fraction * fraction * endX;
                float y = (1 - fraction) * (1 - fraction) * startY + 2 * fraction * (1 - fraction) * middleY + fraction * fraction * endY;
                heartView.setX(x);
                heartView.setY(y);
                heartView.invalidate();
            }
        });
        mHeartSendAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                parentView.removeView(heartView);
            }
        });
        mHeartSendAnimator.setInterpolator(mLinearInterpolator);
        mHeartSendAnimator.setDuration(1440);
        mHeartSendAnimator.start();

        scaleAnima(heartView, 0.3f, 1.0f, mLinearInterpolator, 440, 0, new Runnable() {
            @Override
            public void run() {
                scaleAnima(heartView, 1.0f, 0.3f, mLinearInterpolator, 1000, 0, null);
            }
        });
        alphaAnima(heartView, 0f, 0.85f, mLinearInterpolator, 640, 0, new Runnable() {
            @Override
            public void run() {
                alphaAnima(heartView, 0.85f, 0f, mLinearInterpolator, 800, 0, null);
            }
        });
    }

    /**
     * 点击背景创建流星
     */
    private void initTouchMeteorCreated(){
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int meteorAmount = random.nextInt(2) + 2;
                for(int i = 0; i < meteorAmount; i++){
                    final float startX = random.nextInt(350) + 65;
                    final float startY = random.nextInt(265) + 487;
                    final float scale = random.nextFloat() * 0.5f + 0.5f;
                    final float delay = random.nextInt(200) + 80;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            createMeteorAndAnima(startX, startY, startX - 131, startY - 109, scale, 2400, 800, 1720);
                        }
                    }, (int)(i * 2400 + delay));
                }
            }
        });
    }


    /**
     * 连接建立后，头像位移动画
     */
    private void avatarTranslateAnima(){
        mMyAvatarView.animate()
                .translationX(-mMyAvatarView.getX() + mMyNameView.getX() - mMyAvatarView.getWidth() / 2 + mMyNameView.getWidth() / 2)
                .translationY(mMyAvatarView.getY() - mMyNameView.getY() - mMyAvatarView.getHeight() - dp2px(20))
                .setDuration(600).setInterpolator(new DecelerateInterpolator()).start();
        mOtherAvatarView.animate()
                .translationX(-mOtherAvatarView.getX() + mOtherNameView.getX() - mOtherAvatarView.getWidth() / 2 + mOtherNameView.getWidth() / 2)
                .translationY(mOtherAvatarView.getY() - mOtherNameView.getY() - mOtherAvatarView.getHeight() - dp2px(20))
                .setDuration(600).setInterpolator(new DecelerateInterpolator()).start();
    }

    private void initVisible(){
        mCloseView.setVisibility(View.VISIBLE);
        mMyAvatarView.setVisibility(View.VISIBLE);
        mOtherAvatarView.setVisibility(View.VISIBLE);
        mMyNameView.setVisibility(View.VISIBLE);
        mMyCountryView.setVisibility(View.VISIBLE);
        mOtherNameView.setVisibility(View.VISIBLE);
        mOtherCountryView.setVisibility(View.VISIBLE);
        mCountTime.setVisibility(View.VISIBLE);
        mScatteredPlanetView.setVisibility(View.VISIBLE);
        mIvLikeStatus.setVisibility(View.VISIBLE);
        mTvLikeStatus.setVisibility(View.VISIBLE);
        mBigPlanetView.setVisibility(View.VISIBLE);

    }

    /**
     * 辅助透明度变化view
     * @param view          执行的view
     * @param startAlpha    起始透明度
     * @param endAlpha      结束透明度
     * @param interpolator  插值器
     * @param duration      动画时长
     * @param delayTime     动画开始延时时长
     * @param endAction     动画结束回调
     */
    private void alphaAnima(View view, float startAlpha, float endAlpha, Interpolator interpolator, int duration, int delayTime, Runnable endAction){
        if(startAlpha != endAlpha){
            view.setAlpha(startAlpha);
            view.animate().alpha(endAlpha).setDuration(duration).setInterpolator(interpolator).withEndAction(endAction).setStartDelay(delayTime);
        }
    }

    /**
     * 辅助缩放动画
     * @param view          执行的view
     * @param startScale    起始大小
     * @param endScale      结束大小
     * @param interpolator  插值器
     * @param duration      动画时长
     * @param delayTime     动画开始延时时长
     * @param endAction     动画结束回调
     */
    private void scaleAnima(View view, float startScale, float endScale, Interpolator interpolator, int duration, int delayTime, Runnable endAction){
        if(startScale != endScale){
            view.setScaleX(startScale);
            view.setScaleY(startScale);
            view.animate().scaleX(endScale).scaleY(endScale).setDuration(duration).setInterpolator(interpolator).withEndAction(endAction).setStartDelay(delayTime);
        }
    }

    /**
     * 创建单个流星view并执行动画
     * @param startX        流星起始x位置
     * @param startY        流星起始y位置
     * @param endX          流星结束x位置
     * @param endY          流星结束y位置
     * @param scale         流星缩放比例
     * @param totalTime     流星运动总时长
     * @param firstOpacity  第一次透明度变化结束时间
     * @param secondOpacity 第二次透明度变化开始时间
     */
    private void createMeteorAndAnima(float startX, float startY, float endX, float endY, float scale, final int totalTime, final int firstOpacity, final int secondOpacity){
        startX = dp2px(startX);
        startY = getScreenHeight() - dp2px(startY);
        endX = dp2px(endX);
        endY = getScreenHeight() - dp2px(endY);
        final ImageView meteorView = new ImageView(mRootView.getContext());
        ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        meteorView.setLayoutParams(layoutParams);
        meteorView.setImageResource(R.drawable.icon_audio_match_meteor);
        final ViewGroup parentView = (ViewGroup) mScatteredPlanetView.getParent();
        parentView.addView(meteorView, parentView.indexOfChild(mScatteredPlanetView));
        meteorView.setX(startX);
        meteorView.setY(startY);
        meteorView.setScaleX(scale);
        meteorView.setScaleY(scale);
        meteorView.setAlpha(0f);
        meteorView.animate().x(endX).y(endY).setDuration(totalTime).setInterpolator(mLinearInterpolator).start();
        meteorView.animate().alpha(1f).setDuration(firstOpacity).setInterpolator(mLinearInterpolator).withEndAction(new Runnable() {
            @Override
            public void run() {
                meteorView.animate().alpha(0f).setDuration(totalTime - secondOpacity).setInterpolator(mLinearInterpolator).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        parentView.removeView(meteorView);
                    }
                }).setStartDelay(secondOpacity - firstOpacity);
            }
        }).start();
    }

    private float dp2px(float dpValue){
        float scale = mRootView.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private int getScreenHeight() {
        return mRootView.getResources().getDisplayMetrics().heightPixels;
    }

}
