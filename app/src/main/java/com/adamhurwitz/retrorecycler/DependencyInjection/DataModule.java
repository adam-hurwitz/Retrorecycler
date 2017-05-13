package com.adamhurwitz.retrorecycler.DependencyInjection;

import android.app.Application;

import com.adamhurwitz.retrorecycler.Network.Repository;
import com.adamhurwitz.retrorecycler.Network.Service;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * Created by ahurwitz on 1/24/17.
 */

@Module
public class DataModule {

    public final static String BASE_URL = "https://www.udacity.com";

    Application application;

    public DataModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    public OkHttpClient provideHttpClient() {
        return new OkHttpClient().newBuilder().build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                //converts Retrofit response into Observable
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    @Provides
    public Service provideItemService(Retrofit retrofit) {
        return retrofit.create(Service.class);
    }

    @Provides
    public Repository provideRepository(Service service) {
        return new Repository(service);
    }


}