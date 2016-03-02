package com.shirwa.simplistic_rss;

import org.joda.time.DateTime;

public interface RssThing {
    public void parserSetPubDate(DateTime date);
    public void parserSetTitle(String s);
    public void parserSetContent(String tag, String s);
    public void parserSetAuthor(String s);
    public void parserSetLink(String s);
    public void parserSetEnclosure(String s);
}
