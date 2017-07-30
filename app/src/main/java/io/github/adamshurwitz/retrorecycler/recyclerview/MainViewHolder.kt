package io.github.adamshurwitz.retrorecycler.recyclerview

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import io.github.adamshurwitz.retrorecycler.Model
import io.github.adamshurwitz.retrorecycler.databinding.RecyclerCellBinding

/**
 * Created by ahurwitz on 12/18/16.
 */

class MainViewHolder(view: View, onClickListener: View.OnClickListener) : RecyclerView.ViewHolder(view) {

    var binding: RecyclerCellBinding

    var imageUrl: String? = null
    var homepageUrl: String? = null

    private var HOMEPAG_TAG_ID = 0
    private var LAYOUT_POS_ID = 0

    init {

        binding = RecyclerCellBinding.bind(view)
        HOMEPAG_TAG_ID = binding.recyclerCellImage.id
        LAYOUT_POS_ID = binding.recyclerCellImage.id + 1

        binding.recyclerCell.setOnClickListener(onClickListener)

    }

    fun bind(context: Context, course: Model.Course) {

        this.imageUrl = course.imageUrl

        binding.recyclerCellTitle.text = course.title

        if (imageUrl != null && !imageUrl!!.isEmpty()) {
            Glide.with(context)
                    .load(imageUrl)
                    .into(binding.recyclerCellImage)
        } else {
            Glide.with(context)
                    .load(io.github.adamshurwitz.retrorecycler.R.drawable.udacity_anim)
                    .asGif()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(binding.recyclerCellImage)
        }

        this.homepageUrl = course.homepage

        binding.recyclerCell.setTag(HOMEPAG_TAG_ID, homepageUrl)
        binding.recyclerCell.setTag(LAYOUT_POS_ID, layoutPosition)

    }

}
