package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.apidemo.R;

/**
 * Created by hao on 17-4-28.
 */

public class ColorShaderProgram extends ShaderProgram {

    //uniform
    private final int uMatrixLocation;
    private final int uColorLocation;

    //attribute
    private final int aPositionLocation;
//    private final int aColorLocation;

    public ColorShaderProgram(Context context) {
        super(context, R.raw.simple_vertex_shader, R.raw.simple_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uColorLocation = GLES20.glGetUniformLocation(program, U_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
//        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
    }

    /**
     * 给uniform设置值
     * @param matrix 矩阵
     */
    public void setUniforms(float[] matrix, float r, float g, float b) {
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glUniform4f(uColorLocation, r, g, b, 1f);
    }

    /**
     * 获取aPositionLocation
     * @return aPositionLocation
     */
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

//    /**
//     * 获取aColorLocation
//     * @return aColorLocation
//     */
//    public int getColorAttributeLocation() {
//        return aColorLocation;
////    }

}
