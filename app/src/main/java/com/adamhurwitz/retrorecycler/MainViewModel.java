package com.adamhurwitz.retrorecycler;

import android.util.Log;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by ahurwitz on 12/27/16.
 */

public class MainViewModel {

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    Retrofit retrofit;
    private Call<Model> call;
    private Model model;

    private ArrayList<Model.Item> items = new ArrayList<>();

    MainView mainView;


    public MainViewModel(MainView mainView) {
        this.mainView = mainView;
    }

    public interface MainView {

        void getData(ArrayList<Model.Item> data);

        void cancelCall(Call call);

    }

    public void makeNetworkCall() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Service.NetworkCall networkCall = retrofit.create(Service.NetworkCall.class);

        call = networkCall.getItems();
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Response<Model> response) {

                try {

                    model = response.body();
                    items = model.getItems();
                    mainView.getData(items);

                } catch (NullPointerException e) {

                    if (response.code() == 401) {
                        Log.v(LOG_TAG, "Unauthenticated");
                    } else if (response.code() >= 400) {
                        Log.v(LOG_TAG, "Client Error " + response.code() + " " + response.message());
                    }

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("getParams threw: ", t.getMessage());
            }
        });
    }

    public void cancelNetworkCall(){
        mainView.cancelCall(call);
    }
}
