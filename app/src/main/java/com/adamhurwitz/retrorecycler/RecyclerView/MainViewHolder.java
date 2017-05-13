package com.adamhurwitz.retrorecycler.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.adamhurwitz.retrorecycler.Model;
import com.adamhurwitz.retrorecycler.R;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class MainViewHolder extends RecyclerView.ViewHolder {

    LinearLayout recyclerCell;
    TextView title;
    ImageView imageView;

    String imageUrl;
    String homepageUrl;

    ViewHolderListener viewHolderListener;

    public interface ViewHolderListener {
        void onCellClicked(String imageUrl, String homepageUrl, int adapterPosition);
    }

    public MainViewHolder(View view, ViewHolderListener viewHolderListener) {
        super(view);
        recyclerCell = (LinearLayout) view.findViewById(R.id.recycler_cell);
        title = (TextView) view.findViewById(R.id.recycler_cell_title);
        imageView = (ImageView) view.findViewById(R.id.recycler_cell_image);

        this.viewHolderListener = viewHolderListener;
        recyclerCell.setOnClickListener(this::onClick);
    }

    public void bind(Context context, Model.Course course) {
        this.imageUrl = course.getImageUrl();

        title.setText(course.getTitle());

        if (imageUrl != null && !imageUrl.isEmpty()){
            Glide.with(context)
                    .load(imageUrl)
                    .into(imageView);
        } else {
            Glide.with(context)
                    .load(R.drawable.udacity_anim)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }

        this.homepageUrl = course.getHomepage();
    }

    private void onClick(View view) {
        viewHolderListener.onCellClicked(this.imageUrl, this.homepageUrl, getLayoutPosition());
    }
}
