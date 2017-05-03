package com.example.hao.main.particles;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ParticlesActivity extends AppCompatActivity {

    GLSurfaceView surfaceView;
    ParticlesRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        surfaceView.setOnTouchListener(new View.OnTouchListener() {
            float previousX, previousY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event != null) {
                    if(event.getAction() == MotionEvent.ACTION_DOWN) {
                        previousX = event.getX();
                        previousY = event.getY();
                    } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                        float x = event.getX();
                        float y = event.getY();
                        final float deltaX = x - previousX;
                        final float deltaY = y - previousY;
                        previousX = x;
                        previousY = y;
                        surfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                mRenderer.handleTouchDrag(deltaX, deltaY);
                            }
                        });
                    }
                    return true;
                }
                return false;
            }
        });
        mRenderer = new ParticlesRenderer(this);
        surfaceView.setRenderer(mRenderer);
        setContentView(surfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surfaceView.onResume();
    }
}
