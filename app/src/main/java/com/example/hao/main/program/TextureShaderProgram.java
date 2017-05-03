package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.apidemo.R;

/**
 * Created by hao on 17-4-28.
 */

public class TextureShaderProgram extends ShaderProgram {

    //uniform
    private final int uMatrixLocation;
    private final int uTextureUnitLocation;

    //attribute
    private final int aPositionLocation;
    private final int aTextureCoordinatesLocation;

    public TextureShaderProgram(Context context) {
        super(context, R.raw.texture_vertex_shader, R.raw.texture_fragment_shader);

        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);

        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aTextureCoordinatesLocation = GLES20.glGetAttribLocation(program, A_TEXTURE_COORDINATES);
    }

    /**
     * 给uniform设置值
     * @param matrix 矩阵
     * @param textureId 纹理对象ID
     */
    public void setUniforms(float[] matrix, int textureId) {
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);

        //CUP使用纹理单元绘制纹理
        //set the active texture unit to texture unit 0
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

        //bind the texture to the unit
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);

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

    /**
     * 获取aTextureCoordinatesLocation
     * @return aTextureCoordinatesLocation
     */
    public int getTextureUnitAttributeLocation() {
        return aTextureCoordinatesLocation;
    }
}
