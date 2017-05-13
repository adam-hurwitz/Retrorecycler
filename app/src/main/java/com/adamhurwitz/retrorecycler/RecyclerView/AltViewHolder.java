package com.adamhurwitz.retrorecycler.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.adamhurwitz.retrorecycler.R;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class AltViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;

    public AltViewHolder(View view){
        super(view);
        imageView = (ImageView) view.findViewById(R.id.recycler_header_image);
    }

    public void bind(){
    }

}
