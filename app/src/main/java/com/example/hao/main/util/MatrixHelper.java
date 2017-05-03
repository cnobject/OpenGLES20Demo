package com.example.hao.main.util;

/**
 * Created by hao on 17-4-27.
 */

public class MatrixHelper {
    /**
     * Defines a projection matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes.
     *
     * @param m the float array that holds the perspective matrix
     * @param offset the offset into float array m where the perspective
     *        matrix data is written
     * @param fovy field of view in y direction, in degrees
     * @param aspect width to height aspect ratio of the viewport
     * @param zNear 近
     * @param zFar 远
     */
    public static void perspectiveM(float[] m, float fovy, float aspect, float zNear, float zFar) {
        final float angleInRadians = (float) (fovy * Math.PI / 180.0);
        final float a = (float) (1.0 / Math.tan(angleInRadians / 2.0));

        m[0] = a / aspect;
        m[1] = 0.0f;
        m[2] = 0.0f;
        m[3] = 0.0f;

        m[4] = 0.0f;
        m[5] = a;
        m[6] = 0.0f;
        m[7] = 0.0f;

        m[8] = 0.0f;
        m[9] = 0.0f;
        m[10] = -((zFar + zNear) / (zFar - zNear));
        m[11] = -1.0f;

        m[12] = 0.0f;
        m[13] = 0.0f;
        m[14] = -((2.0f * zFar * zNear) / (zFar - zNear));
        m[15] = 0.0f;
    }
}
