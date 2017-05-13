package com.adamhurwitz.retrorecycler;

import android.app.Application;
import android.content.Context;

import com.adamhurwitz.retrorecycler.DependencyInjection.DaggerDataComponent;
import com.adamhurwitz.retrorecycler.DependencyInjection.DataComponent;
import com.adamhurwitz.retrorecycler.DependencyInjection.DataModule;

/**
 * Created by ahurwitz on 1/23/17.
 */

public class RetroRecyclerApplication extends Application {

    private static RetroRecyclerApplication app;
    DataComponent dataComponent;

    public static RetroRecyclerApplication getApp() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        app = this;

        initDataComponent();

        dataComponent.inject(this);
    }

    private void initDataComponent(){
        dataComponent = DaggerDataComponent.builder()
                .dataModule(new DataModule(this))
                .build();
    }

    public DataComponent getDataComponent() {
        return dataComponent;
    }
}
