package com.example.hao.main.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by hao on 17-4-27.
 * 获取shader工具类
 */

public class ShaderHelper {
    private static final String TAG = "ShaderHelper";
    /**
     * 获取顶点着色器对象
     * @param sourceCode 源代码
     * @return 顶点着色器对象
     */
    public static int compileVertexShader(String sourceCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, sourceCode);
    }

    /**
     * 获取片段着色器对象
     * @param sourceCode 源代码
     * @return 片段着色器对象
     */
    public static int compileFragmentShader(String sourceCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, sourceCode);
    }

    private static int compileShader(int shader, String sourceCode) {
        //获取opengl shader对象的引用
        final int shaderObjectId = GLES20.glCreateShader(shader);
        if (shaderObjectId == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new shader!");
            }
            return 0;
        }
        //上传代码 让shader对象关联源代码
        GLES20.glShaderSource(shaderObjectId, sourceCode);
        //编译源代码
        GLES20.glCompileShader(shaderObjectId);
        //检查是否编译成功
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);
        if(LoggerConfig.ON) {
            Log.v(TAG, "Result of compile source: " + "\n" + sourceCode + "\nLog:"
                    + GLES20.glGetShaderInfoLog(shaderObjectId));
        }
        if(compileStatus[0] == 0) {
            GLES20.glDeleteShader(shaderObjectId);
            if(LoggerConfig.ON) {
                Log.w(TAG, "Compilation of shader failed!");
            }
            return 0;
        }
        return shaderObjectId;
    }

    /**
     * 把顶点着色器和片段着色器链接成一个program
     * @param vertexShaderId 顶点着色器
     * @param fragmentShaderId 片段着色器
     * @return program对象
     */
    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if(programObjectId == 0) {
            if(LoggerConfig.ON) {
                Log.w(TAG, "Could not create new program");
            }
            return 0;
        }
        //把着色器对象附加到program对象上
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        //把顶点着色器和片段着色器连接起来
        GLES20.glLinkProgram(programObjectId);
        //检查链接是否成功
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if(LoggerConfig.ON) {
            Log.v(TAG, "Result of linking program: " + "\nLog:"
                    + GLES20.glGetProgramInfoLog(programObjectId));
        }
        //检查链接状态
        if(linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programObjectId);
            if(LoggerConfig.ON) {
                Log.w(TAG, "Linking of program failed!");
            }
            return 0;
        }
        return programObjectId;
    }

    /**
     * 验证program状态是否有效
     * @param programObjectId program对象
     * @return 是否有效
     */
    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.v(TAG, "Result of validating program: " + "\n" + validateStatus[0] + "\nLog:"
                + GLES20.glGetProgramInfoLog(programObjectId));
        return validateStatus[0] != 0;
    }

    /**
     * 绑定顶点着色器和片段着色器
     * @param vertexShaderSource 顶点着色器源码
     * @param fragmentShaderSource 片段着色器源码
     * @return 返回program对象ID
     */
    public static int bindProgram(String vertexShaderSource, String fragmentShaderSource) {
        int program;

        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        program = linkProgram(vertexShader, fragmentShader);

        if(LoggerConfig.ON) {
            validateProgram(program);
        }
        return program;
    }
}
