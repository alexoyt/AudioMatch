package com.ouyt.satelliteanimate;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import android.widget.ImageView;
import android.widget.TextView;

public class AudioMatchCallingController {

    private View mRootView;
    private LinearInterpolator mLinearInterpolator;

    private ImageView mMyAvatarView;
    private ImageView mOtherAvatarView;
    private TextView mMyNameView;
    private TextView mMyCountryView;
    private TextView mOtherNameView;
    private TextView mOtherCountryView;
    private TextView mCountTime;
    private TextView mTvLikeStatus;
    private ImageView mIvLikeStatus;
    private ImageView mBigPlanetView;
    private ImageView mScatteredPlanetView;
    private ImageView mCloseView;

    public AudioMatchCallingController(View rootView){
        mRootView = rootView;
        mLinearInterpolator = new LinearInterpolator();
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

    public void startCallingAnima(){
        initVisible();
        alphaAnima(mCloseView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mMyAvatarView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mOtherAvatarView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mMyNameView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mMyCountryView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mOtherNameView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mOtherCountryView, 0f, 1f, mLinearInterpolator, 280, 320);
        alphaAnima(mCountTime, 0f, 1f, mLinearInterpolator, 280, 320);

        alphaAnima(mScatteredPlanetView, 0f, 1f, mLinearInterpolator, 600, 0);
        scaleAnima(mIvLikeStatus, 0f ,1f, new DecelerateInterpolator(), 600, 0);
        scaleAnima(mTvLikeStatus, 0f ,1f, new DecelerateInterpolator(), 600, 0);

        avatartTranslateAnima();

        Animation animation = AnimationUtils.loadAnimation(mBigPlanetView.getContext(), R.anim.audio_match_big_planet);
        mBigPlanetView.startAnimation(animation);
    }

    private void avatartTranslateAnima(){
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

    private void alphaAnima(View view, float startAlpha, float endAlpha, Interpolator interpolator, int duration, int delayTime){
        if(startAlpha != endAlpha){
            view.setAlpha(startAlpha);
            view.animate().alpha(endAlpha).setDuration(duration).setInterpolator(interpolator).setStartDelay(delayTime);
        }
    }

    private void scaleAnima(View view, float startScale, float endScale, Interpolator interpolator, int duration, int delayTime){
        if(startScale != endScale){
            view.setScaleX(startScale);
            view.setScaleY(startScale);
            view.animate().scaleX(endScale).scaleY(endScale).setDuration(duration).setInterpolator(interpolator).setStartDelay(delayTime);
        }
    }

    private float dp2px(float dpValue){
        float scale = mRootView.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
