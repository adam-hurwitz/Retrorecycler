package com.github.adamshurwitz.materialsearchtoolbar;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;

import static com.github.adamshurwitz.materialsearchtoolbar.MaterialSearchToolbarApplication.getApp;

/**
 * Created by ahurwitz on 6/19/17.
 */

public class Utils {

    public static boolean isLollipopOrAbove () {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int getScreenWidth(Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

}
