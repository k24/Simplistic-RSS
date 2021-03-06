package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

import java.util.List;

public class RssFeed implements RssThing {

    final List<RssItem> rssItems;
    String title;
    String description;
    String link;
    DateTime pubDate;

    public RssFeed(List<RssItem> items) {
        this.rssItems = items;
    }

    public List<RssItem> getRssItems() {
        return rssItems;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void parserSetLink(final String link) {
        this.link = link;
    }

    @Override
    public void parserSetEnclosure(String s) {
        // Not a thing for feeds
    }

    public DateTime getPubDate() {
        return pubDate;
    }

    public void parserSetPubDate(final DateTime pubDate) {
        this.pubDate = pubDate;
    }

    public void parserSetContent(String tag, String description) {
        if (this.description == null)
            this.description = "";
        this.description += description;
    }

    @Override
    public void parserSetAuthor(String s) {
        // Not a thing for feeds
    }

    public void parserSetTitle(String title) {
        if (this.title == null)
            this.title = "";
        this.title += title;
    }
}
