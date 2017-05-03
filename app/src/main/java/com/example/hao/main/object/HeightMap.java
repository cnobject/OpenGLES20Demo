package com.example.hao.main.object;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.example.hao.main.data.IndexBuffer;
import com.example.hao.main.data.VertexBuffer;
import com.example.hao.main.program.HeightmapShaderProgram;
import com.example.hao.main.util.Constants;
import com.example.hao.main.util.Geometry;

import static android.opengl.GLES20.GL_ELEMENT_ARRAY_BUFFER;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_UNSIGNED_SHORT;
import static android.opengl.GLES20.glBindBuffer;
import static android.opengl.GLES20.glDrawElements;

/**
 * Created by hao on 17-5-2.
 *
 */

public class HeightMap {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int NORMAL_COMPONENT_COUNT = 3;
    private static final int TOTAL_COMPONENT_COUNT = POSITION_COMPONENT_COUNT + NORMAL_COMPONENT_COUNT;
    private static final int STRIDE = TOTAL_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT;
    private int width;
    private int height;
    private int numElements;
    private final VertexBuffer mVertexBuffer;
    private final IndexBuffer mIndexBuffer;

    public HeightMap(Bitmap bitmap) {
        width = bitmap.getWidth();
        height = bitmap.getHeight();
        if(width * height > 65536) {
            throw new RuntimeException("HeightMap is too large for the index buffer");
        }
        numElements = calculateNumElements();
        mVertexBuffer = new VertexBuffer(loadBitmapData(bitmap));
        mIndexBuffer = new IndexBuffer(createIndexData());
    }


    /**
     * Create an index buffer object for the vertices to wrap them together into
     * triangles, creating indices based on the width and height of the
     * heightmap.
     */
    private short[] createIndexData() {
        final short[] indexData = new short[numElements];
        int offset = 0;

        for (int row = 0; row < height - 1; row++) {
            for (int col = 0; col < width - 1; col++) {
                // Note: The (short) cast will end up underflowing the number
                // into the negative range if it doesn't fit, which gives us the
                // right unsigned number for OpenGL due to two's complement.
                // This will work so long as the heightmap contains 65536 pixels
                // or less.
                short topLeftIndexNum = (short) (row * width + col);
                short topRightIndexNum = (short) (row * width + col + 1);
                short bottomLeftIndexNum = (short) ((row + 1) * width + col);
                short bottomRightIndexNum = (short) ((row + 1) * width + col + 1);

                // Write out two triangles.
                indexData[offset++] = topLeftIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = topRightIndexNum;

                indexData[offset++] = topRightIndexNum;
                indexData[offset++] = bottomLeftIndexNum;
                indexData[offset++] = bottomRightIndexNum;
            }
        }

        return indexData;
    }

    private float[] loadBitmapData(Bitmap bitmap) {
        final int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        bitmap.recycle();
        final float[] vertex = new float[width * height * TOTAL_COMPONENT_COUNT];
        int offset = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                final Geometry.Point point = getPoint(pixels, row, col);
                vertex[offset++] = point.x;
                vertex[offset++] = point.y;
                vertex[offset++] = point.z;

                final Geometry.Point top = getPoint(pixels, row - 1, col);
                final Geometry.Point left = getPoint(pixels, row, col - 1);
                final Geometry.Point right = getPoint(pixels, row, col + 1);
                final Geometry.Point bottom = getPoint(pixels, row + 1, col);

                final Geometry.Vector rightToLeft = Geometry.vectorBetween(right, left);
                final Geometry.Vector topToBottom = Geometry.vectorBetween(top, bottom);
                final Geometry.Vector normal = rightToLeft.crossProduct(topToBottom).normalize();

                vertex[offset++] = normal.x;
                vertex[offset++] = normal.y;
                vertex[offset++] = normal.z;
//                final float xPosition = ((col) / (float)(width - 1)) - 0.5f;
//                final float yPosition = (float) Color.red(pixels[row * height + col]) / (float)255;
//                final float zPosition = ((float)row / (float)(height - 1)) - 0.5f;
//
//                vertex[offset++] = xPosition;
//                vertex[offset++] = yPosition;
//                vertex[offset++] = zPosition;
            }
        }
        return vertex;
    }

    private Geometry.Point getPoint(int[] pixels, int row, int col) {
        final float x = ((col) / (float)(width - 1)) - 0.5f;
        final float z = ((float)row / (float)(height - 1)) - 0.5f;
        row = clamp(row, 0, width -  1);
        col = clamp(col, 0, height -  1);
        final float y = (float) Color.red(pixels[row * height + col]) / (float)255;
        return new Geometry.Point(x, y, z);
    }

    private int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }

    private int calculateNumElements() {
        return (width - 1) * (height - 1) * 2 * 3;
    }

    public void bindData(HeightmapShaderProgram heightmapProgram) {
        mVertexBuffer.setVertexAttribPointer(0,
                heightmapProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, STRIDE);

        mVertexBuffer.setVertexAttribPointer(POSITION_COMPONENT_COUNT * Constants.BYTES_PER_FLOAT,
                heightmapProgram.getNormalAttributeLocation(),
                NORMAL_COMPONENT_COUNT, STRIDE);
    }

    public void draw() {
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, mIndexBuffer.getBufferId());
        glDrawElements(GL_TRIANGLES, numElements, GL_UNSIGNED_SHORT, 0);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, 0);
    }
}
