package io.github.adamshurwitz.retrorecycler.utils;

/**
 * Created by ahurwitz on 7/29/17.
 */

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxHelpers {

    public static <T> Observable.Transformer<T, T> IOAndMainThreadSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static <T> Observable.Transformer<T, T> IOAndIOSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }

    public static <T> Observable.Transformer<T, T> currentThreadSchedulers() {
        return tObservable -> tObservable.subscribeOn(Schedulers.immediate())
                .observeOn(Schedulers.immediate());
    }

    public static <T> Observable.Transformer<T, T> MainAndMainSchedulers() {
        return tObservable -> tObservable.subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
