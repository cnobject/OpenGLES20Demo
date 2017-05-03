package com.example.hao.main.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.hao.main.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by hao on 17-4-28.
 * vertex数据类
 */

public class VertexArray {

    private final FloatBuffer mFloatBuffer;

    public VertexArray(float[] vertexData) {
        mFloatBuffer = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
    }

    /**
     * 把着色器的属性和顶点数据关联起来
     * @param dataOffset offset
     * @param attributeLocation 属性在OpenGL中的位置
     * @param componentCount 每个顶点占用数据
     * @param stride 顶点之间相隔
     */
    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        mFloatBuffer.position(dataOffset);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, mFloatBuffer);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        mFloatBuffer.position(0);
    }

    public void updateBuffer(float[] vertexData, int start, int count) {
        mFloatBuffer.position(start);
        mFloatBuffer.put(vertexData, start, count);
        mFloatBuffer.position(0);
    }
}
