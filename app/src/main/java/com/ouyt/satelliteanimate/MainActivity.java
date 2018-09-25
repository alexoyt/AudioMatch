package com.ouyt.satelliteanimate;

import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private AudioMatchSatelliteController mSatelliteController;
    private View mSatelliteContainer;
    private View mRootView;
    private AudioMatchLightController mLightController;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = findViewById(R.id.audio_match_container);
        mSatelliteContainer = findViewById(R.id.satellite_container);
        mSatelliteController = new AudioMatchSatelliteController(mSatelliteContainer);
        mLightController = new AudioMatchLightController(mRootView, handler);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mSatelliteContainer.post(new Runnable() {
            @Override
            public void run() {
                mSatelliteController.startAllTrackAnim();
                //ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) mSatelliteContainer.getLayoutParams();
                //mSatelliteController.startTrackAnim(mSatelliteContainer.getX(), mSatelliteContainer.getY(), 274, 134);

            }
        });
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mLightController.startAnima();
            }
        });
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLightController.startRippleAnima();
            }
        }, 3000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fileList();
    }
}
