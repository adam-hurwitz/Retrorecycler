package io.github.adamshurwitz.retrorecycler


import android.util.Log
import io.github.adamshurwitz.retrorecycler.network.Repository
import io.github.adamshurwitz.retrorecycler.utils.RxHelpers
import java.util.*
import javax.inject.Inject

/**
 * Created by ahurwitz on 12/27/16.
 */

class MainViewModel(internal var mainView: MainView) {

    @Inject
    lateinit var repository: Repository

    init {
        RetroRecyclerApplication.getApp().getDataComponent().inject(this)
    }

    interface MainView {
        fun getData(data: ArrayList<Model.Course>)
    }

    fun makeNetworkCall() {
        repository.networkItems
                .compose(RxHelpers.IOAndMainThreadSchedulers())
                .subscribe({ model -> mainView.getData(model.courses) }) { throwable -> Log.e(MainViewModel::class.java.simpleName, throwable.toString()) }
    }

}
