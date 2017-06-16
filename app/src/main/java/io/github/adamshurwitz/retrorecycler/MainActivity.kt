package io.github.adamshurwitz.retrorecycler

import android.content.Intent
import android.databinding.DataBindingUtil
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast


import io.github.adamshurwitz.retrorecycler.RecyclerView.Adapter
import io.github.adamshurwitz.retrorecycler.databinding.ActivityMainBinding
import rx.subscriptions.CompositeSubscription

import java.util.ArrayList


class MainActivity : AppCompatActivity(), MainViewModel.MainView {

    private var binding: ActivityMainBinding? = null

    private var adapter: Adapter? = null

    private var courses = ArrayList<Model.Course>()

    private var mainViewModel: MainViewModel? = null

    private var compositeSubscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeSubscription = CompositeSubscription()

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        initRecyclerView()

        mainViewModel = MainViewModel(this)

        mainViewModel!!.makeNetworkCall()

    }

    public override fun onResume() {
        super.onResume()
        adapter!!.addItems(courses)
        adapter!!.swapItems(courses)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeSubscription!!.clear()
    }

    private fun initRecyclerView() {

        adapter = Adapter(this)
        binding!!.recyclerList.adapter = adapter
        binding!!.recyclerList.layoutManager = LinearLayoutManager(this)
        binding!!.recyclerList.setHasFixedSize(true)
        onIndexClickedEvent()
        onCourseClickedEvent()

    }

    private fun onIndexClickedEvent() {
        compositeSubscription!!.add(
                adapter!!.indexClickedEvent().subscribe({ pair ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Urls.courseCatalog))
                    if (intent.resolveActivity(packageManager) != null) {
                        startActivity(intent)
                    }
                }) { throwable -> Log.e(MainActivity::class.java.simpleName, throwable.toString()) })
    }

    private fun onCourseClickedEvent() {
        compositeSubscription!!.add(
                adapter!!.courseClickedEvent().subscribe({ pair ->
                    if (pair.first != null && !pair.first.isEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(pair.first))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        }
                    } else {
                        Toast.makeText(this, NO_HOMEPAGE, Toast.LENGTH_SHORT).show()
                    }
                }) { throwable -> Log.e(MainActivity::class.java.simpleName, throwable.toString()) })
    }

    override fun getData(data: ArrayList<Model.Course>) {
        courses = data
        adapter!!.swapItems(data)
    }

    companion object {

        private val NO_HOMEPAGE = "No Homepage Provided"
    }

}
