package com.adamhurwitz.retrorecycler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.adamhurwitz.retrorecycler.RecyclerView.Adapter;

import java.util.ArrayList;

public class MainActivity
        extends AppCompatActivity
        implements Adapter.AdapterListener, MainViewModel.MainView {


    private Adapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<Model.Course> courses = new ArrayList<>();

    private MainViewModel mainViewModel;

    private static final String NO_HOMEPAGE = "No Homepage Provided";

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

        adapter = new Adapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
    }

    @Override
    public void onResume(){
        super.onResume();

            adapter.addItems(courses);
            adapter.swapItems(courses);

    }

    @Override
    public void onCellClicked(String imageUrl, String homepageUrl){
        if( homepageUrl != null && !homepageUrl.isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(homepageUrl));
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        } else {
            Toast.makeText(this, NO_HOMEPAGE, Toast.LENGTH_SHORT).show();
        }
    }

    public void getData(ArrayList<Model.Course> data){
        courses = data;
        adapter.swapItems(data);
    }

}
