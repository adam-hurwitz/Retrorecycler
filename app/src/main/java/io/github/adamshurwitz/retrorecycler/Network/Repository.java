package io.github.adamshurwitz.retrorecycler.Network;

import io.github.adamshurwitz.retrorecycler.Model;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by ahurwitz on 1/24/17.
 */

public class Repository {

    private Service service;

    @Inject
    public Repository(Service service) {
        this.service = service;
    }

    public Observable<Model> getNetworkItems(){
        return service.getData();
    }

}
