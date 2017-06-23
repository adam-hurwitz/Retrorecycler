package com.github.adamshurwitz.materialsearchtoolbar;

import android.content.Context;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.subjects.ReplaySubject;

public class SearchTermsAdapter extends BaseAdapter implements Filterable, View.OnClickListener {

    Context context;
    private ArrayList<String> data;
    private String[] suggestions;
    private LayoutInflater inflater;
    private ReplaySubject<Pair<View, Integer>> subscriber = ReplaySubject.create();
    private SearchTermsViewHolder viewHolder;

    public SearchTermsAdapter(Context context,
                              String[] suggestions) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<String> searchData = new ArrayList<>();

                    for (String string : suggestions) {

                        searchData.add(string);

                        // Filters adapter - Auto Complete
                        /*if (string.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(string);
                        }*/

                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (ArrayList<String>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.search_term_item, parent, false);
            viewHolder = new SearchTermsViewHolder(convertView, this);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchTermsViewHolder) convertView.getTag();
        }

        viewHolder.bind(position,(String) getItem(position));
        return convertView;
    }


    public void notifyDataSetChanged(ArrayList<String> suggestions) {
        data = suggestions;
        notifyDataSetChanged();
    }

    public Observable<Pair<View, Integer>> removeSearchTermEvent() {
        return subscriber.asObservable();
    }

    @Override
    public void onClick(View v) {
        subscriber.onNext(
                new Pair<>(
                        (LinearLayout) v.getTag(viewHolder.binding.searchTermItem.getId()),
                        (Integer) v.getTag(viewHolder.binding.recentSearchExit.getId())))
        ;
    }
}