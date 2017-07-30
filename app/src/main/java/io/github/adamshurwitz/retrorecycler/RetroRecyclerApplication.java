package io.github.adamshurwitz.retrorecycler;

import android.app.Application;

import io.github.adamshurwitz.retrorecycler.dependencyinjection.DaggerDataComponent;
import io.github.adamshurwitz.retrorecycler.dependencyinjection.DataComponent;
import io.github.adamshurwitz.retrorecycler.dependencyinjection.DataModule;

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
