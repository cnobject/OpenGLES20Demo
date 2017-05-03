package com.example.hao.main.airhockey;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import com.example.hao.apidemo.R;
import com.example.hao.main.object.Mallet;
import com.example.hao.main.object.Puck;
import com.example.hao.main.object.Table;
import com.example.hao.main.program.ColorShaderProgram;
import com.example.hao.main.program.TextureShaderProgram;
import com.example.hao.main.util.Geometry;
import com.example.hao.main.util.MatrixHelper;
import com.example.hao.main.util.TextureHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.Matrix.invertM;
import static android.opengl.Matrix.multiplyMM;
import static android.opengl.Matrix.multiplyMV;

/**
 * Created by hao on 17-4-27.
 * 着色
 */

class AirHockeyRenderer implements GLSurfaceView.Renderer {

//    private static final int POSITION_COMPONENT_COUNT = 2;
//    private static final int COLOR_COMPONENT_COUNT = 3;
//    private static final int BYTES_PER_FLOAT = 4;
//    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT;
//    private static final String A_COLOR = "a_Color";
//    private static final String A_POSITION = "a_Position";
//    private static final String U_MATRIX = "u_Matrix";
    private static final String TAG = "AirHockeyRenderer";
    //投影矩阵
    private final float[] projectionMatrix = new float[16];
    //模型矩阵
    private final float[] modelMatrix = new float[16];
    //视图矩阵
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    //反转矩阵，用于讲归一化坐标转化为三维坐标
    private final float[] invertViewProjectionMatrix = new float[16];
    //    private int aColorLocation;
//    private int aPositionLocation; //attribute 变量的位置
//    private int uMatrixLocation;
//    private FloatBuffer vertexData;
    private Context mContext;
//    private int mProgram;

    private Table mTable;
    private Mallet mMallet;
    private Puck mPuck;

    private TextureShaderProgram mTextureShaderProgram;
    private ColorShaderProgram mColorShaderProgram;

    private int textureID;
    private boolean malletPressed;
    private Geometry.Point malletPosition;

    private final float leftBound = -0.5f;
    private final float rightBound = 0.5f;
    private final float nearBound = 0.8f;
    private final float farBound = -0.8f;

    private Geometry.Point previousMalletPosition;
    private Geometry.Point puckPosition;
    private Geometry.Vector puckVector;

    public AirHockeyRenderer(Context context) {
        mContext = context;
//        float[] tableVertices = {
//            //triangle fan
//                0f, 0f,
//            -0.5f, -0.5f,
//             0.5f,  -0.5f,
//            0.5f,  0.5f,
//            -0.5f, 0.5f,
//             -0.5f, -0.5f,
//            //line
//            -0.5f,  0f,
//             0.5f,   0f,
//            //mallets
//             0f,  -0.25f,
//             0f,   0.25f,
//        };
        //
        // Vertex data is stored in the following manner:
        //
        // The first two numbers are part of the position: X, Y
        // The next three numbers are part of the color: R, G, B
        //
//        float[] tableVertices = {
//                // Order of coordinates: X, Y, R, G, B
//
//                // Triangle Fan
//                0f,    0f,   1f,   1f,   1f,
//                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//                0f, -0.8f, 0.7f, 0.7f, 0.7f,
//                0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//                0.5f, 0f, 0.7f, 0.7f, 0.7f,
//                0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
//                0f,  0.8f, 0.7f, 0.7f, 0.7f,
//                -0.5f,  0.8f, 0.7f, 0.7f, 0.7f,
//                -0.5f,  0f, 0.7f, 0.7f, 0.7f,
//                -0.5f, -0.8f, 0.7f, 0.7f, 0.7f,
//
//                // Line 1
//                -0.5f, 0f, 1f, 0f, 0f,
//                0.5f, 0f, 0f, 0f, 1f,
//
//                // Mallets
//                0f, -0.4f, 0f, 0f, 1f,
//                0f,  0.4f, 1f, 0f, 0f
//        };
//        vertexData = ByteBuffer.allocateDirect(tableVertices.length * BYTES_PER_FLOAT)  //分配本地内存，不是虚拟机
//                                .order(ByteOrder.nativeOrder())  //保证字节序跟底层一致
//                                .asFloatBuffer();
//        vertexData.put(tableVertices); //放入本地内存
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0, 0, 0, 0);
//        String vertexShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_vertex_shader);
//        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(mContext, R.raw.simple_fragment_shader);
//        //创建顶点着色器对象
//        int vertexShader = ShaderHelper.compileVertexShader(vertexShaderSource);
//        //创建片段着色器对象
//        int fragmentShader = ShaderHelper.compileFragmentShader(fragmentShaderSource);
//        //创建program对象
//        mProgram = ShaderHelper.linkProgram(vertexShader, fragmentShader);
//        if(LoggerConfig.ON) {
//            ShaderHelper.validateProgram(mProgram);
//        }
//        //使用program
//        GLES20.glUseProgram(mProgram);
//        //获取uniform的在OpenGL程序中的位置，确定不变
//        aColorLocation = GLES20.glGetAttribLocation(mProgram, A_COLOR);
//        //获取attribute在OpenGL程序中的位置，可以自动分配，也可以手动分配，这里是自动由程序分配
//        aPositionLocation = GLES20.glGetAttribLocation(mProgram, A_POSITION);
//        uMatrixLocation = GLES20.glGetUniformLocation(mProgram, U_MATRIX);
//
//        // Bind our data, specified by the variable vertexData, to the vertex
//        // attribute at location A_POSITION_LOCATION.
//        vertexData.position(0);
//        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT,
//                false, STRIDE, vertexData);
//        GLES20.glEnableVertexAttribArray(aPositionLocation);
//
//        //告诉OpenGL到哪里去找a_Position对应的数据
//        vertexData.position(POSITION_COMPONENT_COUNT);
//        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT,
//                false, STRIDE, vertexData);
//        GLES20.glEnableVertexAttribArray(aColorLocation);

        mTable = new Table();
        mMallet = new Mallet(0.08f, 0.15f, 32);
        mPuck = new Puck(0.06f, 0.02f, 32);
        malletPosition = new Geometry.Point(0, mMallet.height / 2, 0.4f);
        puckPosition = new Geometry.Point(0, mPuck.height / 2f, 0);
        puckVector = new Geometry.Vector(0, 0, 0);

        mTextureShaderProgram = new TextureShaderProgram(mContext);
        mColorShaderProgram = new ColorShaderProgram(mContext);

        textureID = TextureHelper.loadTexture(mContext, R.drawable.air_hockey_surface);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
//        final float aspectRatio = width > height ? (float) width / (float) height
//                                                 : (float) height / (float) width;
//        if(width > height) {
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }
        //生成投影矩阵
        MatrixHelper.perspectiveM(projectionMatrix, 45, (float) width / (float) height, 1f, 10f);
//        //模型矩阵
//        Matrix.setIdentityM(modelMatrix, 0);
//        Matrix.translateM(modelMatrix, 0, 0f, 0f, -3f);
//        //旋转矩阵
//        Matrix.rotateM(modelMatrix, 0, -60f, 1f, 0, 0);
//        final float[] temp = new float[16];
//        //最后的归一化坐标应该是 PM * MM * V,所以这里先把两个矩阵=相乘，然后在drawFrame的时候透视除法顶点位置得到归一化坐标
//        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);
        //设置view矩阵 眼睛所在的位置，眼睛看的位置，头的方向
        Matrix.setLookAtM(viewMatrix, 0, 0f, 1.2f, 2.5f, 0, 0, 0, 0, 1f, 0);

        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    private float angle;
    private void rotateView() {
//        angle++;
//        float x = 2.5f * (float) Math.sin(angle / 50);
//        float z = 2.5f * (float) Math.cos(angle / 50);
//
//        Matrix.setLookAtM(viewMatrix, 0, x, 1.2f, z, 0, 0, 0, 0, 1f, 0);
//
//        Matrix.multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);//清空屏幕
        rotateView();
        puckPosition = puckPosition.translate(puckVector);
        if(puckPosition.x < leftBound + mPuck.radius || puckPosition.x > rightBound - mPuck.radius) {
            puckVector = new Geometry.Vector(-puckVector.x, puckVector.y, puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }
        if(puckPosition.z < farBound + mPuck.radius || puckPosition.z > nearBound - mPuck.radius) {
            puckVector = new Geometry.Vector(puckVector.x, puckVector.y, -puckVector.z);
            puckVector = puckVector.scale(0.9f);
        }
        puckPosition = new Geometry.Point(
                clamp(puckPosition.x, leftBound + mPuck.radius, rightBound - mPuck.radius),
                mPuck.height / 2f,
                clamp(puckPosition.z, farBound + mPuck.radius, nearBound - mPuck.radius)
        );
        //用viewProjectionMatrix缓存后面两个矩阵相乘的结果
//
//        //给着色器传递投影矩阵
//        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0);
//        //绘制桌子
//        //uniform变量没有默认值，必须指定默认值 vec4需要4个分量的值
////        GLES20.glUniform4f(aColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
//        //告诉OpenGL开始画三角形，第二个参数，是从顶点的第0个开始，第三个是告诉一共几个顶点
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 10);
//
//        //绘制分割线
////        GLES20.glUniform4f(aColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        //从第六个顶点开始一共两个顶点画一条线
//        GLES20.glDrawArrays(GLES20.GL_LINES, 10, 2);
//
//        //画两个点
////        GLES20.glUniform4f(aColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
//        //画点
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 12, 1);
//
////        GLES20.glUniform4f(aColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
//        //画点
//        GLES20.glDrawArrays(GLES20.GL_POINTS, 13, 1);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        invertM(invertViewProjectionMatrix, 0, viewProjectionMatrix, 0);
        //画桌子
        positionTableInScene();
        mTextureShaderProgram.useProgram();
        mTextureShaderProgram.setUniforms(modelViewProjectionMatrix, textureID);
        mTable.bindData(mTextureShaderProgram);
        mTable.draw();

        //画木槌
        positionObjectInScene(0f, mMallet.height / 2f, -0.4f);
        mColorShaderProgram.useProgram();
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 1f, 0, 0);
        mMallet.bindData(mColorShaderProgram);
        mMallet.draw();

        positionObjectInScene(malletPosition.x, malletPosition.y, malletPosition.z);
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 0, 0, 1f);
        mMallet.draw();

        positionObjectInScene(puckPosition.x, puckPosition.y, puckPosition.z);
        mColorShaderProgram.setUniforms(modelViewProjectionMatrix, 0.8f, 0.8f, 1f);
        mPuck.bindData(mColorShaderProgram);
        mPuck.draw();
        puckVector = puckVector.scale(0.99f);
    }

    private void positionTableInScene() {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.rotateM(modelMatrix, 0, -90f, 1f, 0, 0);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    private void positionObjectInScene(float x, float y, float z) {
        Matrix.setIdentityM(modelMatrix, 0);
        Matrix.translateM(modelMatrix, 0, x, y, z);
        Matrix.multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix, 0, modelMatrix, 0);
    }

    public void handleTouchPress(float x, float y) {
        Geometry.Ray ray = convert2DPoint2Ray(x, y);//创建一条射线
        Geometry.Sphere malletBoundingSphere = new Geometry.Sphere(new Geometry.Point(
                malletPosition.x,
                malletPosition.y,
                malletPosition.z
        ), mMallet.height / 2);
        //射线和木槌的园是否相交
        malletPressed = Geometry.intersects(malletBoundingSphere, ray);
        Log.i(TAG, "malletPressed = " + malletPressed);
    }

    private Geometry.Ray convert2DPoint2Ray(float x, float y) {
        final float[] nearPointNdc = new float[]{x, y, -1, 1};
        final float[] farPointNdc = new float[]{x, y, 1, 1};

        final float[] nearPointWorld = new float[4];
        final float[] farPointWorld = new float[4];

        //和反转矩阵相乘 得到三维世界里的XYZ值
        multiplyMV(nearPointWorld, 0, invertViewProjectionMatrix, 0, nearPointNdc, 0);
        multiplyMV(farPointWorld, 0, invertViewProjectionMatrix, 0, farPointNdc, 0);

        //得到真正的M值，乘以反转矩阵 得到反转的W 然后x, y, z除以W就消除了透视相除的影响
        divideByW(nearPointWorld);
        divideByW(farPointWorld);

        Geometry.Point nearPoint = new Geometry.Point(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2]);
        Geometry.Point farPoint = new Geometry.Point(farPointWorld[0], farPointWorld[1], farPointWorld[2]);

        return new Geometry.Ray(nearPoint, Geometry.vectorBetween(nearPoint, farPoint));
    }

    private void divideByW(float[] vector) {
        vector[0] /= vector[3];
        vector[1] /= vector[3];
        vector[2] /= vector[3];
    }

    public void handleTouchDrag(float x, float y) {
        if(malletPressed) {
            Geometry.Ray ray = convert2DPoint2Ray(x, y);
            Geometry.Plane plane = new Geometry.Plane(new Geometry.Point(0, 0, 0), new Geometry.Vector(0, 1, 0));
            //桌子上的触摸点
            Geometry.Point touchedPoint = Geometry.intersectionPoint(ray, plane);
//            malletPosition = new Geometry.Point(touchedPoint.x, mMallet.height / 2, touchedPoint.z);
            previousMalletPosition = malletPosition;//保存上一个位置
            malletPosition = new Geometry.Point(
                    clamp(touchedPoint.x, leftBound + mMallet.radius, rightBound - mMallet.radius),
                    mMallet.height / 2,
                    clamp(touchedPoint.z, 0 + mMallet.radius, nearBound - mMallet.radius)
            );

            //检测碰撞
            float distance = Geometry.vectorBetween(malletPosition, puckPosition).length();
            if(distance < mPuck.radius + mMallet.radius) {
                puckVector = Geometry.vectorBetween(previousMalletPosition, malletPosition);
            }
        }
    }

    private float clamp(float value, float min, float max) {
        return Math.min(max, Math.max(value, min));
    }
}
