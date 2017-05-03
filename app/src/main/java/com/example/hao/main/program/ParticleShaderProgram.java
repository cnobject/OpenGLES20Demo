package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.apidemo.R;

/**
 * Created by hao on 17-4-28.
 * 粒子顶点着色器
 */

public class ParticleShaderProgram extends ShaderProgram {

    //uniform
    private final int uMatrixLocation;
    private final int uTimeLocation;
    private final int uTextureUnitLocation;

    //attribute
    private final int aPositionLocation;
    private final int aColorLocation;
    private final int aDirectionVectorLocation;
    private final int aParticleStartTimeLocation;

    public ParticleShaderProgram(Context context) {
        super(context, R.raw.particle_vectex_shader, R.raw.particles_fragment_shader);
        uMatrixLocation = GLES20.glGetUniformLocation(program, U_MATRIX);
        uTimeLocation = GLES20.glGetUniformLocation(program, U_TIME);
        uTextureUnitLocation = GLES20.glGetUniformLocation(program, U_TEXTURE_UNIT);

        aColorLocation = GLES20.glGetAttribLocation(program, A_COLOR);
        aPositionLocation = GLES20.glGetAttribLocation(program, A_POSITION);
        aDirectionVectorLocation = GLES20.glGetAttribLocation(program, A_DIRECTION__VECTOR);
        aParticleStartTimeLocation = GLES20.glGetAttribLocation(program, A_PARTICLE_START_TIME);
    }

    /**
     * 给uniform设置值
     * @param matrix 矩阵
     */
    public void setUniforms(float[] matrix, float elapsedTime, int textureId) {
        //pass the matrix into the shader program
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, matrix, 0);
        GLES20.glUniform1f(uTimeLocation, elapsedTime);

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
     * 获取aColorLocation
     * @return aColorLocation
     */
    public int getColorAttributeLocation() {
        return aColorLocation;
    }

    public int getDirectionVectorLocation() {
        return aDirectionVectorLocation;
    }

    public int getParticleStartTimeLocation() {
        return aParticleStartTimeLocation;
    }

}
