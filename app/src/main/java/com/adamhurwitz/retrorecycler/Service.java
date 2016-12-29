package com.adamhurwitz.retrorecycler;

import retrofit2.Call;
import retrofit2.http.GET;

public final class Service {

    public final static String BASE_URL = "https://www.udacity.com";

    public interface NetworkCall {

        @GET("public-api/v0/courses")

        Call<Model> getItems();
    }
}
