package com.close5.close5adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.close5.close5adapter.RecyclerView.C5Adapter;
import com.close5.close5adapter.Service.NetworkCall;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

    /*
    Close5 C5Adapter Exercise - Build a working C5Adapter with the provided image urls with at
    least two different C5Adapter cell view types with unique layouts. For example, you may have a
    main C5Adapter cell with one header or footer or you may have two types of main C5Adapter cells.
    Any variation is fine. The second view type cannot be empty, it must contain sort of content.
    */

    /*
    Design Specs:
     1) Working vertical C5Adapter displaying images that take up the full cell. A grid or list is
     fine.
     2) At least 2 unique view types that show at the same time.
     3) Images must be the same size. You can use the library of your choosing for displaying images
     */

    /*
    Images:
    1) http://cdn2-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-8.jpg
    2) http://cdn3-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-15.jpg
    3) https://i.ytimg.com/vi/mRf3-JkwqfU/hqdefault.jpg
    4) http://cdn1-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-2.jpg
    5) http://blogs-images.forbes.com/kristintablang/files/2016/02/Uber-Puppies.jpg
    6) http://theprojectheal.org/wp-content/uploads/2016/01/Aaaaaawwwwwwwwww-Sweet-puppies-9415255-1600-1200.jpg
    7) http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg
    8) https://i.ytimg.com/vi/oGoPUw0YBAg/maxresdefault.jpg
    9) http://media.mnn.com/assets/images/2015/04/puppies-expression.jpg
    10) http://images.r.cruisecritic.com/news/2016/princess-puppies.jpg
    */

public class MainActivity
        extends AppCompatActivity
        implements C5Adapter.AdapterListener
{

    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    private C5Adapter c5Adapter;
    private RecyclerView recyclerView;

    Retrofit retrofit;
    private Call<Model> call;
    private Model model;
    private ArrayList<Model.Item> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        initRecyclerView();

        //todo: move to ViewModel
        makeNetworkCall();

    }

    private void makeNetworkCall() {

        retrofit = new Retrofit.Builder()
                .baseUrl(Service.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NetworkCall networkCall = retrofit.create(NetworkCall.class);

        call = networkCall.getItems();
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Response<Model> response) {

                try {

                    model = response.body();
                    items = model.getItems();
                    c5Adapter.swapItems(items);

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

    private void initRecyclerView() {

        c5Adapter = new C5Adapter(this, this);
        recyclerView.setAdapter(c5Adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume(){
        super.onResume();

            Log.v(MainActivity.class.getSimpleName(), "getLength - " + items.size());

            c5Adapter.addItems(items);
            c5Adapter.swapItems(items);

    }

    @Override
    public void onCellClicked(String url){
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        call.cancel();
    }
}
