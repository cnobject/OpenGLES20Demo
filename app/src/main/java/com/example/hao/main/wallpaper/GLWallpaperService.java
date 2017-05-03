package com.example.hao.main.wallpaper;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;

import com.example.hao.main.particles.ParticlesRenderer;

/**
 * Created by hao on 17-5-3.
 *
 */

public class GLWallpaperService extends WallpaperService {

    @Override
    public Engine onCreateEngine() {
        return new GLEngine();
    }

    class GLEngine extends Engine {

        private WallpaperGLSurfaceView mGLSurfaceView;
        private boolean rendererSet;
        private ParticlesRenderer mRenderer;

        @Override
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);

            mGLSurfaceView = new WallpaperGLSurfaceView(GLWallpaperService.this);
            mGLSurfaceView.setPreserveEGLContextOnPause(true);
            mGLSurfaceView.setEGLContextClientVersion(2);
            mGLSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
            mRenderer = new ParticlesRenderer(GLWallpaperService.this);
            mGLSurfaceView.setRenderer(mRenderer);
            rendererSet = true;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            mGLSurfaceView.onWallPaperDestory();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);
            if(rendererSet) {
                if(visible) {
                    mGLSurfaceView.onResume();
                } else {
                    mGLSurfaceView.onPause();
                }
            }
        }

        @Override
        public void onOffsetsChanged(final float xOffset, final float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
            mGLSurfaceView.queueEvent(new Runnable() {
                @Override
                public void run() {
                    mRenderer.handleOffsetsChanged(xOffset, yOffset);
                }
            });
        }

        public class WallpaperGLSurfaceView extends GLSurfaceView {

            WallpaperGLSurfaceView(Context context) {
                super(context);
            }

            @Override
            public SurfaceHolder getHolder() {
                return getSurfaceHolder();
            }

            public void onWallPaperDestory() {
                super.onDetachedFromWindow();
            }
        }
    }

}
