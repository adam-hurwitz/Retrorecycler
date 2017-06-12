package io.github.adamshurwitz.retrorecycler.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.adamshurwitz.retrorecycler.databinding.RecyclerHeadercellBinding;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {

    RecyclerHeadercellBinding binding;

    public HeaderViewHolder(View view, View.OnClickListener onClickListener) {
        super(view);
        binding = RecyclerHeadercellBinding.bind(view);
        binding.courseIndex.setOnClickListener(onClickListener);
    }

    public void bind() {

    }

}
