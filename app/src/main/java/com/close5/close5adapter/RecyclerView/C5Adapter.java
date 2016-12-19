package com.close5.close5adapter.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.close5.close5adapter.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class C5Adapter
        extends RecyclerView.Adapter<C5ViewHolder>
        implements C5ViewHolder.ViewHolderListener{

    Context context;
    private ArrayList<String> data = new ArrayList<>();
    AdapterListener adapterListener;

    public interface AdapterListener{
        void onCellClicked(String url);
    }

    public C5Adapter(Context context, AdapterListener adapterListener){
        this.context = context;
        this.adapterListener = adapterListener;
    }

    @Override
    public C5ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (viewType == 0){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_altcell,
                    parent, false);
            return new C5AltViewHolder(view, this);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cell,
                    parent, false);
            return new C5ViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(C5ViewHolder holder, int pos){
        holder.bind(context, data.get(pos));
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

    public void addItems(List<String> list){
        data.addAll(list);
        notifyDataSetChanged();
    }

    public void swapItems(List<String> list){
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public String geturl(int position) {
        return data.get(position);
    }

    @Override
    public void onCellClicked(String url, int position){
        notifyItemChanged(position);
        Log.v(C5Adapter.class.getSimpleName(), "getUrl - " + url);
        adapterListener.onCellClicked(url);
    }

}
