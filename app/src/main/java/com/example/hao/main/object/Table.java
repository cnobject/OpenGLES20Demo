package com.example.hao.main.object;

import android.opengl.GLES20;

import com.example.hao.main.data.VertexArray;
import com.example.hao.main.program.TextureShaderProgram;

import static com.example.hao.main.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by hao on 17-4-28.
 * 桌子数据类
 */

public class Table {

    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + TEXTURE_COORDINATES_COMPONENT_COUNT) * BYTES_PER_FLOAT;
    private static final float[] VERTEX_DATA = new float[] {
            //order of coordinates X, Y ,S ,T
            //Triangle Fan; T分量和Y分量方向相反，因为图片的方向右边向上,计算机中图片的朝向是Y轴朝下
            0f,    0f, 0.5f, 0.5f,
         -0.5f, -0.8f,   0f, 0.9f,
          0.5f, -0.8f,   1f, 0.9f,
          0.5f,  0.8f,   1f, 0.1f,
         -0.5f,  0.8f,   0f, 0.1f,
         -0.5f, -0.8f,   0f, 0.9f
    };
    private VertexArray mVertexArray;

    public Table() {
        mVertexArray = new VertexArray(VERTEX_DATA);
    }

    /**
     * 绑定顶点和纹理数据
     * @param textureShaderProgram 纹理程序
     */
    public void bindData(TextureShaderProgram textureShaderProgram) {
        mVertexArray.setVertexAttribPointer(
                0,
                textureShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE
        );
        mVertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureShaderProgram.getTextureUnitAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE
        );
    }

    /**
     * 画桌子
     */
    public void draw() {
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6);
    }
}
