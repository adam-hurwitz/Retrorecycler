package io.github.adamshurwitz.retrorecycler.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.adamshurwitz.retrorecycler.R;
import io.github.adamshurwitz.retrorecycler.Model;

import java.util.ArrayList;

import rx.Observable;
import rx.subjects.ReplaySubject;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Model.Course> data = new ArrayList<>();
    private MainViewHolder mainViewHolder;
    private HeaderViewHolder headerViewHolder;
    private ReplaySubject<Pair<String, Integer>> courseClickedSubscriber = ReplaySubject.create();
    private ReplaySubject<String> indexClickedSubscriber = ReplaySubject.create();

    public Adapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_headercell,
                    parent, false);
            headerViewHolder = new HeaderViewHolder(view, this);
            return headerViewHolder;
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cell,
                    parent, false);
            mainViewHolder = new MainViewHolder(view, this);
            return mainViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos) {
        if (getItemViewType(pos) == 0) {
            ((HeaderViewHolder) holder).bind();
        } else {
            ((MainViewHolder) holder).bind(context, data.get(pos));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addItems(ArrayList<Model.Course> list) {
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<Model.Course> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public Model.Course getItem(int position) {
        return data.get(position);
    }

    public Observable<Pair<String, Integer>> courseClickedEvent(){
        return courseClickedSubscriber.asObservable();
    }

    public Observable<String> indexClickedEvent(){
        return indexClickedSubscriber.asObservable();
    }

    @Override
    public void onClick(View v) {
        
        switch (v.getId()) {

            case R.id.course_index:
                indexClickedSubscriber.onNext("");
                break;

            case R.id.recycler_cell:
                String courseUrl = (String) v.getTag(mainViewHolder.getBinding().recyclerCellImage.getId());
                int position = (Integer) v.getTag(mainViewHolder.getBinding().recyclerCellImage.getId()+1);
                courseClickedSubscriber.onNext(new Pair<>(courseUrl, position));
                notifyItemChanged(position);
                break;
        }

    }
}
