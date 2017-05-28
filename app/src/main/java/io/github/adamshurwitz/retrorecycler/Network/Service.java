package io.github.adamshurwitz.retrorecycler.Network;


import io.github.adamshurwitz.retrorecycler.Model;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by ahurwitz on 1/24/17.
 */

public interface Service {

    @GET("public-api/v0/courses")
    Observable<Model> getData();

}
