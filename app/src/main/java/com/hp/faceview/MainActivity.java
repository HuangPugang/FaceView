package com.hp.faceview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FaceView faceV = (FaceView) findViewById(R.id.progress);


        findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceV.reset();
            }
        });

        findViewById(R.id.failed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceV.setStatus(FaceView.FAILED);

            }
        });

        findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceV.setStatus(FaceView.SUCCESS);
            }
        });
    }
}
