package com.example.hao.main.object;

import com.example.hao.main.data.VertexArray;
import com.example.hao.main.program.ColorShaderProgram;
import com.example.hao.main.util.Geometry;

import java.util.List;

/**
 * Created by hao on 17-4-28.
 * 木槌类
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;
//    private static final int COLOR_COMPONENT_COUNT = 3;
//    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
//    private static final float[] VERTEX_DATA = new float[] {
//            //X, Y, R, G, B
//            0f, -0.4f, 0f, 0f, 1f,
//            0f,  0.4f, 1f, 0f, 0f
//    };
    public final float radius, height;

    private final VertexArray vertexData;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;

        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(
                new Geometry.Point(0f, 0f, 0f), radius, height, numPointsAroundPuck);
        vertexData = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    /**
     * 绑定顶点和纹理数据
     * @param colorShaderProgram 纹理程序
     */
    public void bindData(ColorShaderProgram colorShaderProgram) {
        vertexData.setVertexAttribPointer(
                0,
                colorShaderProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0
        );
//        mVertexArray.setVertexAttribPointer(
//                POSITION_COMPONENT_COUNT,
//                colorShaderProgram.getColorAttributeLocation(),
//                COLOR_COMPONENT_COUNT,
//                STRIDE
//        );
    }

    /**
     * 画木槌
     */
    public void draw() {
        for (ObjectBuilder.DrawCommand command : drawList) {
            command.draw();
        }
    }
}
