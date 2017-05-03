package com.example.hao.main.airhockey;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class AirHockeyActivity extends AppCompatActivity implements View.OnTouchListener {

    GLSurfaceView surfaceView;
    AirHockeyRenderer mRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        surfaceView = new GLSurfaceView(this);
        surfaceView.setEGLContextClientVersion(2);
        surfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        mRenderer = new AirHockeyRenderer(this);
        surfaceView.setRenderer(mRenderer);
        surfaceView.setOnTouchListener(this);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(event != null) {
            //转化为归一化坐标
            final float x = (event.getX() / ((float) v.getWidth())) * 2 - 1;
            final float y = -((event.getY()) / ((float) v.getHeight()) * 2 - 1);
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                surfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mRenderer.handleTouchPress(x, y);
                    }
                });
            } else if(event.getAction() == MotionEvent.ACTION_MOVE) {
                surfaceView.queueEvent(new Runnable() {
                    @Override
                    public void run() {
                        mRenderer.handleTouchDrag(x, y);
                    }
                });
            }
            return true;
        }
        return false;
    }
}
