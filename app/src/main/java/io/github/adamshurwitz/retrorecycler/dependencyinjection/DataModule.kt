package io.github.adamshurwitz.retrorecycler.dependencyinjection

import android.app.Application
import dagger.Module
import dagger.Provides
import io.github.adamshurwitz.retrorecycler.network.Repository
import io.github.adamshurwitz.retrorecycler.network.Service
import okhttp3.OkHttpClient
import retrofit2.GsonConverterFactory
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory
import javax.inject.Singleton

/**
 * Created by ahurwitz on 1/24/17.
 */

@Module
class DataModule(internal var application: Application) {

    companion object {
        val BASE_URL = "https://www.udacity.com"
    }

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
        return retrofit
    }

    @Provides
    fun provideItemService(retrofit: Retrofit): Service {
        return retrofit.create(Service::class.java)
    }

    @Provides
    fun provideRepository(service: Service): Repository {
        return Repository(service)
    }


}