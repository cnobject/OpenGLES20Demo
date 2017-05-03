package com.example.hao.main.program;

import android.content.Context;
import android.opengl.GLES20;

import com.example.hao.main.util.ShaderHelper;
import com.example.hao.main.util.TextResourceReader;

/**
 * Created by hao on 17-4-28.
 * program基类
 */

public class ShaderProgram {

    //uniform
    protected static final String U_MATRIX = "u_Matrix";
    protected static final String U_TEXTURE_UNIT = "u_TextureUnit";
    protected static final String U_COLOR = "u_Color";
    protected static final String U_VECTOR_TO_LIGHT = "u_VectorToLight";
    protected static final String U_MV_MATRIX = "u_MVMatrix";
    protected static final String U_IT_MV_MATRIX = "u_IT_MVMatrix";
    protected static final String U_MVP_MATRIX = "u_MVPMatrix";
    protected static final String U_POINT_LIGHT_POSITIONS = "u_PointLightPositions";
    protected static final String U_POINT_LIGHT_COLORS= "u_PointLightColors";

    protected static final String U_TIME = "u_Time";

    //attribute
    protected static final String A_POSITION = "a_Position";
    protected static final String A_COLOR = "a_Color";
    protected static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";
    protected static final String A_NORMAL = "a_Normal";

    protected static final String A_DIRECTION__VECTOR = "a_DirectionVector";
    protected static final String A_PARTICLE_START_TIME = "a_ParticleStartTime";

    //shader program
    protected final int program;
    protected ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        program = ShaderHelper.bindProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    /**
     * 使用program
     */
    public void useProgram() {
        GLES20.glUseProgram(program);
    }
}
