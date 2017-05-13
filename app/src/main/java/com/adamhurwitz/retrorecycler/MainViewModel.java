package com.adamhurwitz.retrorecycler;

import android.util.Log;

import com.adamhurwitz.retrorecycler.Network.Repository;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by ahurwitz on 12/27/16.
 */

public class MainViewModel {

    @Inject
    protected Repository repository;

    MainView mainView;

    public MainViewModel(MainView mainView) {

        this.mainView = mainView;

        RetroRecyclerApplication.getApp().getDataComponent().inject(this);

    }

    public interface MainView {

        void getData(ArrayList<Model.Course> data);

    }

    public void makeNetworkCall() {

        repository.getNetworkItems()
                //where process is going to run
                .subscribeOn(Schedulers.io())
                //where results are delivered
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.e(MainViewModel.class.getSimpleName(), "Error with Udacity network call");
                })
                .subscribe(model -> {
                    mainView.getData(model.getCourses());
                });
    }

}
