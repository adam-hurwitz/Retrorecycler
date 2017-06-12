package io.github.adamshurwitz.retrorecycler.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import io.github.adamshurwitz.retrorecycler.Model;
import io.github.adamshurwitz.retrorecycler.databinding.RecyclerCellBinding;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class MainViewHolder extends RecyclerView.ViewHolder {

    public RecyclerCellBinding binding;

    public String imageUrl;
    public String homepageUrl;

    private int HOMEPAG_TAG_ID = 0;
    private int LAYOUT_POS_ID = 0;

    public MainViewHolder(View view, View.OnClickListener onClickListener) {
        super(view);

        binding = RecyclerCellBinding.bind(view);
        HOMEPAG_TAG_ID = binding.recyclerCellImage.getId();
        LAYOUT_POS_ID = binding.recyclerCellImage.getId()+1;

        binding.recyclerCell.setOnClickListener(onClickListener);

    }

    public void bind(Context context, Model.Course course) {

        this.imageUrl = course.getImageUrl();

        binding.recyclerCellTitle.setText(course.getTitle());

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.recyclerCellImage);
        } else {
            Glide.with(context)
                    .load(io.github.adamshurwitz.retrorecycler.R.drawable.udacity_anim)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(binding.recyclerCellImage);
        }

        this.homepageUrl = course.getHomepage();

        binding.recyclerCell.setTag(HOMEPAG_TAG_ID, homepageUrl);
        binding.recyclerCell.setTag(LAYOUT_POS_ID, getLayoutPosition());

    }

}
