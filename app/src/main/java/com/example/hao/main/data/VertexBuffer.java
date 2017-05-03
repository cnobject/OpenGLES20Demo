package com.example.hao.main.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static com.example.hao.main.util.Constants.BYTES_PER_FLOAT;

/**
 * Created by hao on 17-5-2.
 *
 */

public class VertexBuffer {
    private final int bufferId;

    public VertexBuffer(float[] vertexData) {
        final int[] buffers = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);
        if(buffers[0] == 0) {
            throw new RuntimeException("could not create a new vertex buffer object");
        }
        bufferId = buffers[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);

        FloatBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        vertexArray.position(0);

        //从本地缓存到GPU缓存
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_FLOAT,
                vertexArray, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }

    public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferId);
        GLES20.glVertexAttribPointer(attributeLocation, componentCount, GLES20.GL_FLOAT,
                false, stride, dataOffset);
        GLES20.glEnableVertexAttribArray(attributeLocation);
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
}
