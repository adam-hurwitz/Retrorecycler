package io.github.adamshurwitz.retrorecycler.RecyclerView


import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import io.github.adamshurwitz.retrorecycler.databinding.RecyclerHeadercellBinding

/**
 * Created by ahurwitz on 12/18/16.
 */

class HeaderViewHolder(view: View, onClickListener: View.OnClickListener) : RecyclerView.ViewHolder(view) {

    internal var binding: RecyclerHeadercellBinding

    init {
        binding = RecyclerHeadercellBinding.bind(view)
        binding.courseIndex.setOnClickListener(onClickListener)
    }

    fun bind() {

    }

}
