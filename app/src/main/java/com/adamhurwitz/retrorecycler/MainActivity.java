package com.adamhurwitz.retrorecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.adamhurwitz.retrorecycler.RecyclerView.Adapter;

import java.util.ArrayList;

import retrofit2.Call;

public class MainActivity
        extends AppCompatActivity
        implements Adapter.AdapterListener, MainViewModel.MainView
{

    private Adapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<Model.Item> items = new ArrayList<>();

    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);

        initRecyclerView();

        mainViewModel = new MainViewModel(this);

        mainViewModel.makeNetworkCall();

    }

    private void initRecyclerView() {

        adapter = new Adapter(this, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume(){
        super.onResume();

            adapter.addItems(items);
            adapter.swapItems(items);

    }

    @Override
    public void onCellClicked(String url){
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainViewModel.cancelNetworkCall();
    }

    public void getData(ArrayList<Model.Item> data){
        items = data;
        adapter.swapItems(data);
    }

    public void cancelCall(Call call){
        call.cancel();
    }
}
