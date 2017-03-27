package com.shengliedu.parent.util;

import android.content.Context;


public class Config {


    public static int screenwidth = 480;
    public static int screenheight = 480;
    public static int statusbarheight = 20;
    public static float density = 0f;


    public static void init(Context context) {
        screenwidth = context.getResources().getDisplayMetrics().widthPixels < context.getResources().getDisplayMetrics().heightPixels ?
                context.getResources().getDisplayMetrics().widthPixels : context.getResources().getDisplayMetrics().heightPixels;
        screenheight = context.getResources().getDisplayMetrics().heightPixels;
        density = context.getResources().getDisplayMetrics().density;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        statusbarheight = result;
        return result;
    }
}
