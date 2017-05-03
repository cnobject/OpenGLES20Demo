package com.example.hao.main.object;

import com.example.hao.main.data.VertexArray;
import com.example.hao.main.program.ColorShaderProgram;
import com.example.hao.main.util.Geometry;

import java.util.List;

/**
 * Created by hao on 17-4-28.
 * 冰球
 */

public class Puck {

    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexData;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        this.radius = radius;
        this.height = height;

        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Geometry.Cylinder(new Geometry.Point(0f, 0f, 0f), radius, height)
                , numPointsAroundPuck);
        vertexData = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram program) {
        vertexData.setVertexAttribPointer(0, program.getPositionAttributeLocation()
                                         , POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for (ObjectBuilder.DrawCommand command : drawList) {
            command.draw();
        }
    }
}
