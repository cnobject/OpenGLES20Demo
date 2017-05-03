package com.example.hao.main.data;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import static com.example.hao.main.util.Constants.BYTES_PER_SHORT;

/**
 * Created by hao on 17-5-2.
 *
 */

public class IndexBuffer {
    private final int bufferId;

    public IndexBuffer(short[] vertexData) {
        final int[] buffers = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);
        if(buffers[0] == 0) {
            throw new RuntimeException("could not create a new vertex buffer object");
        }
        bufferId = buffers[0];

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, buffers[0]);

        ShortBuffer vertexArray = ByteBuffer
                .allocateDirect(vertexData.length * BYTES_PER_SHORT)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer()
                .put(vertexData);
        vertexArray.position(0);

        //从本地缓存到GPU缓存
        GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, vertexArray.capacity() * BYTES_PER_SHORT,
                vertexArray, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    public int getBufferId() {
        return bufferId;
    }
}
