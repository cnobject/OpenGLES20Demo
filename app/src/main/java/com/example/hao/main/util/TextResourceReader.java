package com.example.hao.main.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by hao on 17-4-27.
 *
 */

public class TextResourceReader {
    public static String readTextFileFromResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream in = context.getResources().openRawResource(resId);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String nextLine;
            while ((nextLine = reader.readLine()) != null) {
                body.append(nextLine);
                body.append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return body.toString();
    }
}
