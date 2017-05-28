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

    public LinearLayout recyclerCell;
    public TextView title;
    public ImageView imageView;

    public String imageUrl;
    public String homepageUrl;

    private int HOMEPAG_TAG_ID = 0;
    private int LAYOUT_POS_ID = 0;

    public MainViewHolder(View view, View.OnClickListener onClickListener) {
        super(view);

        recyclerCell = (LinearLayout) view.findViewById(R.id.recycler_cell);
        title = (TextView) view.findViewById(R.id.recycler_cell_title);
        imageView = (ImageView) view.findViewById(R.id.recycler_cell_image);
        HOMEPAG_TAG_ID = imageView.getId();
        LAYOUT_POS_ID = imageView.getId()+1;

        recyclerCell.setOnClickListener(onClickListener);

    }

    public void bind(Context context, Model.Course course) {

        this.imageUrl = course.getImageUrl();

        title.setText(course.getTitle());

        if (imageUrl != null && !imageUrl.isEmpty()) {
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

        recyclerCell.setTag(HOMEPAG_TAG_ID, homepageUrl);
        recyclerCell.setTag(LAYOUT_POS_ID, getLayoutPosition());
    }

}
