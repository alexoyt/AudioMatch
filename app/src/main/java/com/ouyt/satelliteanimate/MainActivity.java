package com.ouyt.satelliteanimate;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private View mRootView;
    private AudioMatchLightController mLightController;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRootView = findViewById(R.id.audio_match_container);
        mLightController = new AudioMatchLightController(mRootView, handler);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRootView.post(new Runnable() {
            @Override
            public void run() {
                mLightController.startBeforMatchAnima();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
