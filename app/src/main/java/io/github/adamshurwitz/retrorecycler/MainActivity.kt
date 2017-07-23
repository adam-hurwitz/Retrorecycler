package io.github.adamshurwitz.retrorecycler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import io.github.adamshurwitz.retrorecycler.RecyclerView.Adapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import rx.subscriptions.CompositeSubscription

import java.util.ArrayList


class MainActivity : AppCompatActivity(), MainViewModel.MainView {

    private var adapter: Adapter? = null

    private var courses = ArrayList<Model.Course>()

    private var mainViewModel: MainViewModel? = null

    private var compositeSubscription: CompositeSubscription? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        compositeSubscription = CompositeSubscription()

        initToolbar()

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

    private fun initToolbar() {
        setSupportActionBar(this.toolbar)
    }

    private fun initRecyclerView() {

        adapter = Adapter(this)
        this.recyclerList.adapter = adapter;
        this.recyclerList.layoutManager = LinearLayoutManager(this)
        this.recyclerList.setHasFixedSize(true)
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
