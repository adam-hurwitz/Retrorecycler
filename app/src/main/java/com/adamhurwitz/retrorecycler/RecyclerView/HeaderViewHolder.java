package com.adamhurwitz.retrorecycler.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.adamhurwitz.retrorecycler.R;
import com.adamhurwitz.retrorecycler.Urls;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class HeaderViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView indexBtn;

    public HeaderViewHolderListener headerViewHolderListener;

    public interface HeaderViewHolderListener {
        void onCourseIndexClick();
    }

    public HeaderViewHolder(View view, HeaderViewHolderListener headerViewHolderListener) {
        super(view);
        this.headerViewHolderListener = headerViewHolderListener;
        imageView = (ImageView) view.findViewById(R.id.recycler_header_image);
        indexBtn = (TextView) view.findViewById(R.id.course_index);

        indexBtn.setOnClickListener(v -> {
            headerViewHolderListener.onCourseIndexClick();

        });
    }

    public void bind() {
    }

}
