package com.github.adamshurwitz.materialsearchtoolbar;

import android.app.Application;

/**
 * Created by ahurwitz on 6/19/17.
 */

public class MaterialSearchToolbarApplication extends Application {

    private static MaterialSearchToolbarApplication app;

    public static MaterialSearchToolbarApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

    }

}
