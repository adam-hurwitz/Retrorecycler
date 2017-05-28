package io.github.adamshurwitz.retrorecycler.RecyclerView;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView indexBtn;

    public HeaderViewHolder(View view, View.OnClickListener onClickListener) {
        super(view);
        imageView = (ImageView) view.findViewById(io.github.adamshurwitz.retrorecycler.R.id.recycler_header_image);
        indexBtn = (TextView) view.findViewById(io.github.adamshurwitz.retrorecycler.R.id.course_index);
        indexBtn.setOnClickListener(onClickListener);
    }

    public void bind() {

    }

}
