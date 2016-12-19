package com.close5.close5adapter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.close5.close5adapter.RecyclerView.C5Adapter;

import java.util.Arrays;
import java.util.List;

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

    private List<String> dogs;

    private C5Adapter c5Adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createList();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_list);
        initRecyclerView();
    }

    private void createList() {
        dogs = Arrays.asList(
                "http://cdn2-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-8.jpg",
                "http://cdn3-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-15.jpg",
                "https://i.ytimg.com/vi/mRf3-JkwqfU/hqdefault.jpg",
                "http://cdn1-www.dogtime.com/assets/uploads/gallery/30-impossibly-cute-puppies/impossibly-cute-puppy-2.jpg",
                "http://blogs-images.forbes.com/kristintablang/files/2016/02/Uber-Puppies.jpg",
                "http://theprojectheal.org/wp-content/uploads/2016/01/Aaaaaawwwwwwwwww-Sweet-puppies-9415255-1600-1200.jpg",
                "http://cdn.skim.gs/image/upload/v1456344012/msi/Puppy_2_kbhb4a.jpg",
                "https://i.ytimg.com/vi/oGoPUw0YBAg/maxresdefault.jpg",
                "http://media.mnn.com/assets/images/2015/04/puppies-expression.jpg",
                "http://images.r.cruisecritic.com/news/2016/princess-puppies.jpg"
        );
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

        c5Adapter.addItems(dogs);
        c5Adapter.swapItems(dogs);
    }

    @Override
    public void onCellClicked(String url){
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
    }
}
