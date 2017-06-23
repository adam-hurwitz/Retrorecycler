package com.github.adamshurwitz.materialsearchtoolbar;

import android.view.View;
import android.widget.TextView;

import com.github.adamshurwitz.materialsearchtoolbar.databinding.SearchTermItemBinding;


/**
 * Created by ahurwitz on 5/2/17.
 */

public class SearchTermsViewHolder {

    public SearchTermItemBinding binding;
    public TextView textView;


    public SearchTermsViewHolder(View convertView, View.OnClickListener onClickListener) {

        binding = SearchTermItemBinding.bind(convertView);
        binding.recentSearchExit.setOnClickListener(onClickListener);
    }

    public void bind(int position, String searchTerm) {
        binding.recentSearchExit.setTag(binding.searchTermItem.getId(), binding.searchTermItem);
        binding.recentSearchExit.setTag(binding.recentSearchExit.getId(), position);
        binding.recentSearchTerm.setText(searchTerm);
    }

}
