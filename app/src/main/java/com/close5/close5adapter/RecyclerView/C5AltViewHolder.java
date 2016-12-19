package com.close5.close5adapter.RecyclerView;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.close5.close5adapter.R;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class C5AltViewHolder extends C5ViewHolder {

    TextView textView;

    public C5AltViewHolder(View view, ViewHolderListener viewHolderListener){
        super(view, viewHolderListener);
        textView = (TextView) view.findViewById(R.id.recycler_altcell_text);
    }

    public void bind(Context context, String imageUrl){
        this.imageUrl = imageUrl;

        textView.setText("Enjoy the pictures of pups below!");
    }

}
