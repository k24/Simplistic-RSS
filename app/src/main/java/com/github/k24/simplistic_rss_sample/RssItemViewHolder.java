package com.github.k24.simplistic_rss_sample;

import android.content.Context;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.EasyViewHolder;
import com.shirwa.simplistic_rss.RssItem;

/**
 * Created by k24 on 2016/03/01.
 */
public class RssItemViewHolder extends EasyViewHolder<RssItem> {
    public RssItemViewHolder(Context context, ViewGroup parent) {
        super(context, parent, R.layout.item_rss);
    }

    @Override
    public void bindTo(RssItem rssItem) {
        TextView titleView = (TextView) itemView.findViewById(R.id.text);
        titleView.setText(rssItem.getTitle());

        TextView descriptionView = (TextView) itemView.findViewById(R.id.text2);
        descriptionView.setText(Html.fromHtml(rssItem.getDescription()));
    }
}
