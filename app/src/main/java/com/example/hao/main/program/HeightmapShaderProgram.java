package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.apidemo.R;

/**
 * Created by hao on 17-4-28.
 * 粒子顶点着色器
 */

public class HeightmapShaderProgram extends ShaderProgram {

    //uniform
    private final int uVectorToLightLocation;
    private final int uMVMatrixLocation;
    private final int uITMatrixLocation;
    private final int uMVPMatrixLocation;
    private final int uPointLightPositionsLocation;
    private final int uPointLightColorsLocation;

    //attribute
    private final int aPositionLocation;
    private final int aNormalAttributeLocation;

    public HeightmapShaderProgram(Context context) {
        super(context, R.raw.heightmap_vertex_shader, R.raw.heightmap_fragment_shader);
        uVectorToLightLocation = GLES20.glGetUniformLocation(program, U_VECTOR_TO_LIGHT);
        uMVMatrixLocation = GLES20.glGetUniformLocation(program, U_MV_MATRIX);
        uITMatrixLocation = GLES20.glGetUniformLocation(program, U_IT_MV_MATRIX);
        uMVPMatrixLocation = GLES20.glGetUniformLocation(program, U_MVP_MATRIX);
        uPointLightPositionsLocation = GLES20.glGetUniformLocation(program, U_POINT_LIGHT_POSITIONS);
        uPointLightColorsLocation = GLES20.glGetUniformLocation(program, U_POINT_LIGHT_COLORS);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aNormalAttributeLocation = GLES20.glGetAttribLocation(program, A_NORMAL);
    }

    public void setUniforms(float[] mvMatrix, float[] it_mvMatrix, float[] mvpMatrix,
                            float[] vectorToDerectionLight, float[] pointLightPositions,
                            float[] pointLightColors) {
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMVMatrixLocation, 1, false, mvMatrix, 0);
        GLES20.glUniformMatrix4fv(uITMatrixLocation, 1, false, it_mvMatrix, 0);
        GLES20.glUniformMatrix4fv(uMVPMatrixLocation, 1, false, mvpMatrix, 0);

        GLES20.glUniform3fv(uVectorToLightLocation, 1, vectorToDerectionLight, 0);//方向光
        GLES20.glUniform4fv(uPointLightPositionsLocation, 3, pointLightPositions, 0);//点光位置
        GLES20.glUniform3fv(uPointLightColorsLocation, 3, pointLightColors, 0);//颜色
    }

    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }


    public int getNormalAttributeLocation() {
        return aNormalAttributeLocation;
    }


}
