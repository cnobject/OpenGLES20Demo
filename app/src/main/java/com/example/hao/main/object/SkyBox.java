package com.example.hao.main.object;

import android.opengl.GLES20;

import com.example.hao.main.data.VertexArray;
import com.example.hao.main.program.SkyBoxShaderProgram;

import java.nio.ByteBuffer;

/**
 * Created by hao on 17-5-2.
 *
 */

public class SkyBox {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private final VertexArray vertexArray;
    private final ByteBuffer indexBuffer;

    public SkyBox() {
        this.vertexArray = new VertexArray(new float[]{
                -1,  1,  1,
                 1,  1,  1,
                -1, -1,  1,
                 1, -1,  1,
                -1,  1, -1,
                 1,  1, -1,
                -1, -1, -1,
                 1, -1, -1
        });
        this.indexBuffer = ByteBuffer.allocate(6 * 6)
                            .put(new byte[]{
                                    //front
                                    1, 3, 0,
                                    0, 3, 2,

                                    //back
                                    4, 6, 5,
                                    5, 6, 7,

                                    //left
                                    0, 2, 4,
                                    4, 2, 6,

                                    //right
                                    5, 7, 1,
                                    1, 7, 3,

                                    //top
                                    5, 1, 4,
                                    4, 1, 0,

                                    //bottom
                                    6, 2, 7,
                                    7, 2, 3
                            });
        indexBuffer.position(0);
    }

    public void bindData(SkyBoxShaderProgram skyBoxProgram) {
        vertexArray.setVertexAttribPointer(0,
                skyBoxProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 36, GLES20.GL_UNSIGNED_BYTE, indexBuffer);
    }
}
