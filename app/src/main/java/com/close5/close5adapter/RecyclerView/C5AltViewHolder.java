package com.close5.close5adapter.RecyclerView;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.close5.close5adapter.R;

/**
 * Created by ahurwitz on 12/18/16.
 */

public class C5AltViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public C5AltViewHolder(View view){
        super(view);
        textView = (TextView) view.findViewById(R.id.recycler_altcell_text);
    }

    public void bind(){
        textView.setText("Udacity Courses");
    }

}
