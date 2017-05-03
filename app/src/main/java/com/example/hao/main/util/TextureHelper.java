package com.example.hao.main.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.util.Log;

/**
 * Created by hao on 17-4-28.
 * 纹理工具类
 */

public class TextureHelper {

    private static final String TAG = "TextureHelper";

    /**
     * 生成纹理对象ID
     * @param context context
     * @param resourceId 资源ID
     * @return 纹理ID
     */
    public static int loadTexture(Context context, int resourceId) {
        final int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);

        if(textureObjectId[0] == 0) {
            if(LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object");
            }
            return 0;
        }
        //OpenGL需要非压缩式的原始数据
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        if(bitmap == null) {
            if(LoggerConfig.ON) {
                Log.w(TAG, "ResourceId: " + resourceId + " could not be decoded");
            }
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        //绑定纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId[0]);
        //选择纹理过滤缩小时用三线性过滤，放大时用双线过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载位图数据
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        //生成MIP贴图
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除与纹理的绑定,防止意外改变这个纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectId[0];//纹理的引用
    }

    /**
     * 加载立方体贴图
     * @param context context
     * @param cubeResources 贴图资源数组
     * @return id
     */
    public static int loadCubeMap(Context context, int[] cubeResources) {
        final int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);
        if(textureObjectId[0] == 0) {
            if(LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object");
            }
            return 0;
        }

        //OpenGL需要非压缩式的原始数据
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;

        final Bitmap[] cubeBitmaps = new Bitmap[6];
        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] = BitmapFactory.decodeResource(context.getResources(), cubeResources[i], options);
            if(cubeBitmaps[i] == null) {
                if(LoggerConfig.ON) {
                    Log.w(TAG, "ResourceId: " + cubeBitmaps[i] + " could not be decoded");
                }
                GLES20.glDeleteTextures(1, textureObjectId, 0);
                return 0;
            }
        }
        //绑定纹理对象
        GLES20.glBindTexture(GLES20.GL_TEXTURE_CUBE_MAP, textureObjectId[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_CUBE_MAP, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载位图数据
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        for (Bitmap bitmap : cubeBitmaps) {
            bitmap.recycle();
        }
        return textureObjectId[0];
    }
}
