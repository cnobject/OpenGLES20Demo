package com.example.hao.main.particles;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.opengl.GLSurfaceView;
import android.os.SystemClock;
import android.util.Log;

import com.example.hao.apidemo.R;
import com.example.hao.main.object.HeightMap;
import com.example.hao.main.object.ParticleShooter;
import com.example.hao.main.object.ParticleSystem;
import com.example.hao.main.object.SkyBox;
import com.example.hao.main.program.HeightmapShaderProgram;
import com.example.hao.main.program.ParticleShaderProgram;
import com.example.hao.main.program.SkyBoxShaderProgram;
import com.example.hao.main.util.Geometry;
import com.example.hao.main.util.LoggerConfig;
import com.example.hao.main.util.MatrixHelper;
import com.example.hao.main.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_BLEND;
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_TEST;
import static android.opengl.GLES20.GL_LEQUAL;
import static android.opengl.GLES20.GL_LESS;
import static android.opengl.GLES20.GL_ONE;
import static android.opengl.GLES20.glBlendFunc;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDepthFunc;
import static android.opengl.GLES20.glDepthMask;
import static android.opengl.GLES20.glDisable;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.glViewport;
import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;
import static android.opengl.Matrix.rotateM;
import static android.opengl.Matrix.scaleM;
import static android.opengl.Matrix.setIdentityM;
import static android.opengl.Matrix.translateM;
import static android.opengl.Matrix.transposeM;

/**
 * Created by hao on 17-4-27.
 * 着色
 */

public class ParticlesRenderer implements GLSurfaceView.Renderer {
    private final Context context;
    private static final String TAG = "ParticlesRenderer";
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewMatrixForSkybox = new float[16];
    private final float[] projectionMatrix = new float[16];

    private final float[] modelViewMatrix = new float[16];
    private final float[] it_modelViewMatrix = new float[16];

    private final float[] tempMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private HeightmapShaderProgram heightmapProgram;
    private HeightMap heightmap;

    private SkyBoxShaderProgram skyboxProgram;
    private SkyBox skybox;

    private ParticleShaderProgram particleProgram;
    private ParticleSystem particleSystem;
    private ParticleShooter redParticleShooter;
    private ParticleShooter greenParticleShooter;
    private ParticleShooter blueParticleShooter;

    private long globalStartTime;
    private int particleTexture;
    private int skyboxTexture;

    private long frameStartTimeMs;
    private long startTimeMs;
    private int frameCount;

    private float xRotation, yRotation;

    private float[] vectorToLight = new float[]{0.30f, 0.35f, -0.89f, 0};

    private final float[] pointLightPositions = new float[]{
            -1f,  1f,  0f,  1f,
             0f,  1f,  0f,  1f,
             1f,  1f,  0f,  1f
    };

    private final float[] pointLightColors = new float[]{
            1.00f, 0.20f, 0.02f,
            0.02f, 0.25f, 0.02f,
            0.02f, 0.20f, 1.00f
    };

    public ParticlesRenderer(Context context) {
        this.context = context;
    }

    public void handleTouchDrag(float deltaX, float deltaY) {
        xRotation += deltaX / 16f;
        yRotation += deltaY / 16f;

        if (yRotation < -90) {
            yRotation = -90;
        } else if (yRotation > 90) {
            yRotation = 90;
        }

        // Setup view matrix
        updateViewMatrices();
    }

    private void updateViewMatrices() {
        setIdentityM(viewMatrix, 0);
        rotateM(viewMatrix, 0, -yRotation, 1f, 0f, 0f);
        rotateM(viewMatrix, 0, -xRotation, 0f, 1f, 0f);
        System.arraycopy(viewMatrix, 0, viewMatrixForSkybox, 0, viewMatrix.length);

        // We want the translation to apply to the regular view matrix, and not
        // the skybox.
        translateM(viewMatrix, 0, 0 - xOffset, -1.5f - yOffset, -5f);
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);

        heightmapProgram = new HeightmapShaderProgram(context);
        heightmap = new HeightMap(((BitmapDrawable)context.getResources()
                .getDrawable(R.drawable.heightmap)).getBitmap());

        skyboxProgram = new SkyBoxShaderProgram(context);
        skybox = new SkyBox();

        particleProgram = new ParticleShaderProgram(context);
        particleSystem = new ParticleSystem(10000);
        globalStartTime = System.nanoTime();

        final Geometry.Vector particleDirection = new Geometry.Vector(0f, 0.5f, 0f);
        final float angleVarianceInDegrees = 5f;
        final float speedVariance = 1f;

        redParticleShooter = new ParticleShooter(
                new Geometry.Point(-1f, 0f, 0f),
                particleDirection,
                Color.rgb(255, 50, 5),
                angleVarianceInDegrees,
                speedVariance);

        greenParticleShooter = new ParticleShooter(
                new Geometry.Point(0f, 0f, 0f),
                particleDirection,
                Color.rgb(25, 255, 25),
                angleVarianceInDegrees,
                speedVariance);

        blueParticleShooter = new ParticleShooter(
                new Geometry.Point(1f, 0f, 0f),
                particleDirection,
                Color.rgb(5, 50, 255),
                angleVarianceInDegrees,
                speedVariance);

        particleTexture = TextureHelper.loadTexture(context, R.drawable.particle_texture);

        skyboxTexture = TextureHelper.loadCubeMap(context,
                new int[] { R.drawable.night_left, R.drawable.night_right,
                        R.drawable.night_bottom, R.drawable.night_top,
                        R.drawable.night_front, R.drawable.night_back});
    }

    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        glViewport(0, 0, width, height);
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 100f);
        /*
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width
            / (float) height, 1f, 10f);
        */
        updateViewMatrices();
    }

    @Override
    public void onDrawFrame(GL10 glUnused) {
        limitFrameRate(24);
//        logFrameRate();
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        drawHeightmap();
        drawSkybox();
        drawParticles();
    }

    private void limitFrameRate(int framesPerSecond) {
        long elapsedFrameTimeMs = SystemClock.elapsedRealtime() - frameStartTimeMs;
        long expectedFrameTimeMs = 1000 / framesPerSecond;
        long timeToSleepMs = expectedFrameTimeMs - elapsedFrameTimeMs;

        if (timeToSleepMs > 0) {
            SystemClock.sleep(timeToSleepMs);
        }
        frameStartTimeMs = SystemClock.elapsedRealtime();
    }

    private void logFrameRate() {
        if (LoggerConfig.ON) {
            long elapsedRealtimeMs = SystemClock.elapsedRealtime();
            double elapsedSeconds = (elapsedRealtimeMs - startTimeMs) / 1000.0;

            if (elapsedSeconds >= 1.0) {
                Log.v(TAG, frameCount / elapsedSeconds + "fps");
                startTimeMs = SystemClock.elapsedRealtime();
                frameCount = 0;
            }
            frameCount++;
        }
    }

    private void drawHeightmap() {
        setIdentityM(modelMatrix, 0);
        // Expand the heightmap's dimensions, but don't expand the height as
        // much so that we don't get insanely tall mountains.
        scaleM(modelMatrix, 0, 100f, 10f, 100f);
        updateMvpMatrix();
        heightmapProgram.useProgram();
        final float[] vectorToLightInEyeSpace = new float[4];
        final float[] pointPositionsInEysSpace = new float[12];
        multiplyMV(vectorToLightInEyeSpace, 0, viewMatrix, 0, vectorToLight, 0);
        multiplyMV(pointPositionsInEysSpace, 0, viewMatrix, 0, pointLightPositions, 0);
        multiplyMV(pointPositionsInEysSpace, 4, viewMatrix, 0, pointLightPositions, 4);
        multiplyMV(pointPositionsInEysSpace, 8, viewMatrix, 0, pointLightPositions, 8);
        heightmapProgram.setUniforms(modelViewMatrix, it_modelViewMatrix, modelViewProjectionMatrix,
                vectorToLightInEyeSpace, pointPositionsInEysSpace, pointLightColors);
        heightmap.bindData(heightmapProgram);
        heightmap.draw();
    }

    private void drawSkybox() {
        setIdentityM(modelMatrix, 0);
        updateMvpMatrixForSkybox();

        glDepthFunc(GL_LEQUAL); // This avoids problems with the skybox itself getting clipped.
        skyboxProgram.useProgram();
        skyboxProgram.setUniforms(modelViewProjectionMatrix, skyboxTexture);
        skybox.bindData(skyboxProgram);
        skybox.draw();
        glDepthFunc(GL_LESS);
    }

    private void drawParticles() {
        float currentTime = (System.nanoTime() - globalStartTime) / 1000000000f;

        redParticleShooter.addParticles(particleSystem, currentTime, 1);
        greenParticleShooter.addParticles(particleSystem, currentTime, 1);
        blueParticleShooter.addParticles(particleSystem, currentTime, 1);

        setIdentityM(modelMatrix, 0);
        updateMvpMatrix();

        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE);

        particleProgram.useProgram();
        particleProgram.setUniforms(modelViewProjectionMatrix, currentTime, particleTexture);
        particleSystem.bindData(particleProgram);
        particleSystem.draw();

        glDisable(GL_BLEND);
        glDepthMask(true);
    }

    private void updateMvpMatrix() {
        multiplyMM(modelViewMatrix, 0, viewMatrix, 0, modelMatrix, 0);
        invertM(tempMatrix, 0, modelViewMatrix, 0);
        transposeM(it_modelViewMatrix, 0, tempMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, modelViewMatrix, 0);
    }
    private void updateMvpMatrixForSkybox() {
        multiplyMM(tempMatrix, 0, viewMatrixForSkybox, 0, modelMatrix, 0);
        multiplyMM(modelViewProjectionMatrix, 0, projectionMatrix, 0, tempMatrix, 0);
    }

    private float xOffset, yOffset;
    public void handleOffsetsChanged(float xOffset, float yOffset) {
        this.xOffset = (xOffset - 0.5f) * 2.5f;
        this.yOffset = (yOffset - 0.5f) * 2.5f;

        updateViewMatrices();
    }
}