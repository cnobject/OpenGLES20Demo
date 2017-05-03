package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.apidemo.R;

/**
 * Created by hao on 17-4-28.
 * 粒子顶点着色器
 */

public class SkyBoxShaderProgram extends ShaderProgram {

    //uniform
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //attribute
    private final int aPositionLocation;

    public SkyBoxShaderProgram(Context context) {
        super(context, R.raw.skybox_vertex_shader, R.raw.skybox_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
    }

    /**
     * 给uniform设置值
     * @param matrix 矩阵
     */
    public void setUniforms(float[] matrix, int textureId) {
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //CUP使用纹理单元绘制纹理
        //set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        //bind the texture to the unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureId);

        //把纹理单元传递给片段着色器中的location
        GLES20.glUniform1i(uTextureUnitLocation, 0);
    }

    /**
     * 获取aPositionLocation
     * @return aPositionLocation
     */
    public int getPositionAttributeLocation() {
        return aPositionLocation;
    }

}
