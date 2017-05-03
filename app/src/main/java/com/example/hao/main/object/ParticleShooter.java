package com.example.hao.main.object;

import android.opengl.Matrix;

import com.example.hao.main.util.Geometry;

import java.util.Random;

/**
 * Created by hao on 17-5-2.
 * 粒子发射器
 */

public class ParticleShooter {
    private final Geometry.Point position;
    private final int color;

    private final float angleVariance;
    private final float speedVariance;

    private final Random mRandom = new Random();

    private float[] rotateMatrix = new float[16];
    private float[] directionVector = new float[4];
    private float[] resultVector = new float[4];

    public ParticleShooter(Geometry.Point position, Geometry.Vector direction, int color,
                           float angleVarianceInDegree, float speedVariance) {
        this.position = position;
        this.color = color;
        this.angleVariance = angleVarianceInDegree;
        this.speedVariance = speedVariance;

        directionVector[0] = direction.x;
        directionVector[1] = direction.y;
        directionVector[2] = direction.z;
    }

    public void addParticles(ParticleSystem particleSystem, float currentTime, int count) {
        for (int i = 0; i < count; i++) {
            //设置旋转矩阵
            Matrix.setRotateEulerM(rotateMatrix, 0, (mRandom.nextFloat() - 0.5f) * angleVariance,
                    (mRandom.nextFloat() - 0.5f) * angleVariance,
                    (mRandom.nextFloat() - 0.5f) * angleVariance);
            //矩阵与向量相乘得到一个稍小的旋转向量
            Matrix.multiplyMV(resultVector, 0, rotateMatrix, 0, directionVector, 0);
            float speedAdjustment = 1f + mRandom.nextFloat() * speedVariance;
            Geometry.Vector direction = new Geometry.Vector(
                    resultVector[0] * speedAdjustment,
                    resultVector[1] * speedAdjustment,
                    resultVector[2] * speedAdjustment
            );
            particleSystem.addParticle(position, color, direction, currentTime);
        }
    }
}
