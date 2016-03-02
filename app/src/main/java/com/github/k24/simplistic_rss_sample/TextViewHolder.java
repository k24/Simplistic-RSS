package com.github.k24.simplistic_rss_sample;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;

/**
 * Created by k24 on 2016/03/01.
 */
public class TextViewHolder extends EasyViewHolder<String> {
    public TextViewHolder(Context context, ViewGroup parent) {
        super(context, parent, android.R.layout.simple_list_item_1);
    }

    @Override
    public void bindTo(String string) {
        TextView textView = (TextView) itemView.findViewById(android.R.id.text1);
        textView.setText(string);
    }
}
