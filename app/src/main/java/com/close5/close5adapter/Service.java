package com.close5.close5adapter;

import retrofit2.Call;
import retrofit2.http.GET;

public final class Service {

    public final static String BASE_URL = "https://www.udacity.com";

    public interface NetworkCall {

        // unsafe=true ensures unsafe response. Prevents HTML escape characters
        @GET("public-api/v0/courses")

        //append static query parameters

        //append dynamic query paramters
        //@GET("?sort_order=item_id.desc")
        //@GET("{version}/search?page=1&order=desc&sort=activity&unsafe=true")

        Call<Model> getItems(

                //append path
                //@Path("sort_order") String version,

                //append query parameter
                /*
                @Query("intitle") String intitle,
                @Query("site") String site);*//*
                @Query("sort_order") String sort_order)
                */

        );
    }
}
