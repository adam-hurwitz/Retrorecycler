package com.adamhurwitz.retrorecycler.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adamhurwitz.retrorecycler.Model;
import com.adamhurwitz.retrorecycler.R;

import java.util.ArrayList;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class Adapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements MainViewHolder.ViewHolderListener{

    Context context;

    private ArrayList<Model.Course> data = new ArrayList<>();
    AdapterListener adapterListener;

    public interface AdapterListener{
        void onCellClicked(String imageUrl, String homepageUrl);
    }

    public Adapter(Context context){
        this.context = context;

        if (context instanceof AdapterListener){
            adapterListener = (AdapterListener) context;
        } else {
            new RuntimeException("Activity or Fragment Needs to Implement AdapterListener");
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_headercell,
                    parent, false);
            return new AltViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cell,
                    parent, false);
            return new MainViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int pos){
        if (getItemViewType(pos) == 0){
            ((AltViewHolder) holder).bind();
        }
        else {
            ((MainViewHolder) holder).bind(context, data.get(pos));
        }
    }

    @Override
    public int getItemViewType(int position){
        if (position == 0){
            return 0;
        } else {
            return position;
        }
    }

    @Override
    public int getItemCount(){
      return data.size();
    }

    public void addItems(ArrayList<Model.Course> list){
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void swapItems(ArrayList<Model.Course> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public Model.Course getItem(int position) {
        return data.get(position);
    }

    @Override
    public void onCellClicked(String url, String homepageUrl, int position){
        notifyItemChanged(position);
        adapterListener.onCellClicked(url, homepageUrl);
    }

}
