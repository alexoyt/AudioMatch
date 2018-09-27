package com.ouyt.satelliteanimate;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AudioMatchSatelliteController mSatelliteController;
    private View mSatelliteContainer;
    private View mRootView;
    private AudioMatchLightController mLightController;
    private AudioMatchConnectingController mConnectingController;
    private AudioMatchCallingController mCallingController;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = findViewById(R.id.audio_match_container);
        mSatelliteContainer = findViewById(R.id.satellite_container);
        mSatelliteController = new AudioMatchSatelliteController(mSatelliteContainer);
        mLightController = new AudioMatchLightController(mRootView, handler);
        mConnectingController = new AudioMatchConnectingController(mRootView, handler);
        mCallingController = new AudioMatchCallingController(mRootView, handler);

    }

    @Override
    protected void onStart() {
        super.onStart();
/*        mSatelliteContainer.post(new Runnable() {
            @Override
            public void run() {
                mSatelliteController.startAllTrackAnim();
                mLightController.startBeforMatchAnima();
                //ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSatelliteContainer.getLayoutParams();
                //mSatelliteController.startTrackAnim(mSatelliteContainer.getX(), mSatelliteContainer.getY(), 274, 134);

            }
        });
        mSatelliteContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLightController.startMatchingAnima();
            }
        }, 5000);
        mSatelliteContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLightController.stopAllAnimaAndHide();
                mSatelliteController.stopAllTrackAnimAndHide();
            }
        }, 15000);*/

/*        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mConnectingController.startConnectingAnima();
            }
        });*/

        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mCallingController.startCallingAnima();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fileList();
    }
}
